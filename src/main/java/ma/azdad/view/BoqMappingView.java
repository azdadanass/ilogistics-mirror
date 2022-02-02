package ma.azdad.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Boq;
import ma.azdad.model.BoqMapping;
import ma.azdad.model.BoqMappingInverse;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.PartNumber;
import ma.azdad.model.PartNumberEquivalence;
import ma.azdad.model.PartNumberEquivalenceDetail;
import ma.azdad.repos.BoqMappingRepos;
import ma.azdad.service.BoqMappingService;
import ma.azdad.service.BoqService;
import ma.azdad.service.DeliveryRequestDetailService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.PartNumberEquivalenceService;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.PoService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class BoqMappingView extends GenericView<Integer, BoqMapping, BoqMappingRepos, BoqMappingService> implements Serializable {
	private static final long serialVersionUID = 2888107105740674611L;

	@Autowired
	private SessionView sessionView;

	@Autowired
	private DeliveryRequestService deliveryRequestService;

	@Autowired
	private DeliveryRequestDetailService deliveryRequestDetailService;

	@Autowired
	private PartNumberService partNumberService;

	@Autowired
	private BoqService boqService;

	@Autowired
	private PoService poService;

	@Autowired
	private PartNumberEquivalenceService partNumberEquivalenceService;

	private BoqMapping boqMapping = new BoqMapping();

	private DeliveryRequest deliveryRequest;

	private PartNumberEquivalence partNumberEquivalence;

	private List<BoqMappingInverse> bmiList = new ArrayList<>();
	private List<Boq> boqList;

	private BoqMappingInverse boqMappingInverse;

	private Map<PartNumber, Double> partNumberQuantityMap;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if ("/viewDeliveryRequest.xhtml".equals(currentPath)) {
			deliveryRequest = deliveryRequestService.findOne(id);
			if (deliveryRequest.getPo() == null)
				return;

			initBmiList();
		}

	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	public void initBmiList() {
		if (Arrays.asList(DeliveryRequestStatus.CANCELED, DeliveryRequestStatus.REJECTED).contains(deliveryRequest.getStatus()))
			return;

		partNumberQuantityMap = deliveryRequest.getDetailList().stream().collect(Collectors.groupingBy(DeliveryRequestDetail::getPartNumber, Collectors.summingDouble(DeliveryRequestDetail::getQuantity)));

		// remove returned quantities
		Map<PartNumber, Double> returnedQuantityMap = deliveryRequestDetailService.findReturnedQuantityMap(deliveryRequest.getId());
		if (deliveryRequest.getIsOutbound()) {
			partNumberQuantityMap.forEach((k, v) -> partNumberQuantityMap.put(k, v - returnedQuantityMap.getOrDefault(k, 0.0)));
			partNumberQuantityMap.values().removeIf(v -> v.equals(0.0));
		}

		if (deliveryRequest.getBoqMappingList().isEmpty()) {
			partNumberQuantityMap.forEach((x, y) -> bmiList.add(new BoqMappingInverse(x, y, y)));
			autoFillBoq();
		} else {
			for (BoqMapping bm : deliveryRequest.getBoqMappingList()) {
				if (bm.getPartNumberEquivalence() == null)
					bmiList.add(new BoqMappingInverse(bm.getBoq().getPartNumber(), bm.getQuantity(), bm.getBoq(), null, null));
				else {
					if (bm.getDirectEquivalence())
						bm.getPartNumberEquivalence().getDetailList().forEach(i -> bmiList.add(new BoqMappingInverse(i.getPartNumber(), bm.getQuantity() * i.getQuantity(), bm.getBoq(), bm.getPartNumberEquivalence(), bm.getDirectEquivalence())));
					else
						bmiList.add(new BoqMappingInverse(bm.getPartNumberEquivalence().getPartNumber(), bm.getQuantity() / bm.getPartNumberEquivalence().getDetailList().get(0).getQuantity(), bm.getBoq(), bm.getPartNumberEquivalence(), bm.getDirectEquivalence()));
				}
			}

			// test if quantity DN > mapping then add empty boq mapping line
			Map<PartNumber, Double> boqMappingQuantityMap = bmiList.stream().collect(Collectors.groupingBy(BoqMappingInverse::getPartNumber, Collectors.summingDouble(BoqMappingInverse::getQuantity)));
			for (PartNumber pn : partNumberQuantityMap.keySet()) {
				Double dnQuantity = partNumberQuantityMap.get(pn);
				Double mappingQuantity = ObjectUtils.firstNonNull(boqMappingQuantityMap.get(pn), 0.0);
				if (dnQuantity > mappingQuantity)
					bmiList.add(new BoqMappingInverse(pn, dnQuantity - mappingQuantity, null));
			}
		}
	}

	private void autoFillBoq() {
		for (BoqMappingInverse bmi : bmiList) {
			refreshBoqList(bmi);
			if (boqList.size() == 1) {
				Boq boq = boqList.get(0);
				if (bmi.getPartNumber().equals(boq.getPartNumber()))
					bmi.setBoq(boq);
				else {
					List<PartNumberEquivalence> equivalenceList = getPartNumberEquivalenceList(boq, bmi);
					if (equivalenceList.size() != 1)
						continue;
					bmi.setBoq(boq);
					bmi.setPartNumberEquivalence(equivalenceList.get(0));
					bmi.setDirectEquivalence(boq.getDirectEquivalence());
				}

				if (bmi.getBoqQuantity() > bmi.getBoq().getRemainingQuantity()) {
					bmi.setPartNumberEquivalence(null);
					bmi.setBoq(null);
				}
			}
		}
	}

	public void changeQuantityListener(BoqMappingInverse bmi, int index) {
		if (bmi.getQuantity().equals(0.0))
			FacesContextMessages.ErrorMessages("Quantity should be greather than 0");
		else if (bmi.getQuantity() < bmi.getTmpQuantity()) {
			bmiList.add(++index, new BoqMappingInverse(bmi.getPartNumber(), bmi.getTmpQuantity() - bmi.getQuantity(), bmi.getTmpQuantity() - bmi.getQuantity(), null, null, null));
			bmi.setTmpQuantity(bmi.getQuantity());
		}
	}

	public void cancelChangeQuantityListener(BoqMappingInverse bmi) {
		bmi.setQuantity(bmi.getTmpQuantity());
		boqMappingInverse.setPartNumberEquivalence(null);
		bmi.setBoq(null);
	}

	public void refreshBoqList() {
		refreshBoqList(boqMappingInverse);
	}

	public void refreshBoqList(BoqMappingInverse bmi) {
		boqList = new ArrayList<>();

		List<Boq> directList = findDirectBoqList(bmi.getPartNumber().getId());
		List<Boq> nonDirectList = findNonDirectBoqList(bmi.getPartNumber().getId());
		boqList.addAll(directList);
		boqList.addAll(nonDirectList.stream().filter(i -> !directList.contains(i)).collect(Collectors.toList()));

		System.out.println("directList : " + directList);
		System.out.println("nonDirectList : " + nonDirectList);

		// boqList.forEach(i ->
		// i.setUsedQuantity(boqService.getUsedQuantity(i.getId()))); // replaced by
		// prsisted totalUsedQuantity
	}

	private List<Boq> findDirectBoqList(Integer partNumberId) {
		return boqService.findByPoAndPartNumber(deliveryRequest.getPo().getIdpo(), partNumberId);
	}

	private List<Boq> findNonDirectBoqList(Integer partNumberId) {
		PartNumber partNumber = partNumberService.findOne(partNumberId);
		List<Boq> result = boqService.findByPoAndPartNumber(deliveryRequest.getPo().getIdpo(), partNumber.getEquivalenceList().stream().filter(i -> i.getActive() && i.getDetailList().size() == 1).map(i -> i.getDetailList().get(0).getPartNumber().getId()).collect(Collectors.toList()));

		result.forEach(i -> i.setDirectEquivalence(false));
		return result;
	}

	public void map() {
		if (boqMappingInverse.getPartNumber().equals(boqMappingInverse.getBoq().getPartNumber()))
			boqMappingInverse.setPartNumberEquivalence(null);
		else
			boqMappingInverse.setDirectEquivalence(boqMappingInverse.getBoq().getDirectEquivalence());

		if (boqMappingInverse.getBoqQuantity() > boqMappingInverse.getBoq().getRemainingQuantity()) {
			boqMappingInverse.setPartNumberEquivalence(null);
			boqMappingInverse.setBoq(null);
			refreshBoqList();
			FacesContextMessages.ErrorMessages("Quantity should be lower than remaining quantity");
			return;
		}

		execJavascript("PF('mapDlg').hide()");
	}

	public Boolean canSave() {
		return (sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) || sessionView.isTheConnectedUser(deliveryRequest.getRequester())) && deliveryRequest.getBoqMappingList().isEmpty() && !Arrays.asList(DeliveryRequestStatus.CANCELED, DeliveryRequestStatus.REJECTED).contains(deliveryRequest.getStatus());
	}

	public Boolean validate() {

		Set<String> keySet = new HashSet<String>();
		for (BoqMappingInverse bmi : bmiList) {
			if (bmi.getBoq() == null)
				return FacesContextMessages.ErrorMessages("BoQ PN should not be null");
			if (!bmi.getQuantity().equals(bmi.getTmpQuantity()))
				return FacesContextMessages.ErrorMessages("Please finish all actions before save");

			keySet.add(bmi.getKey());
		}

		if (keySet.size() < bmiList.size())
			return FacesContextMessages.ErrorMessages("Duplicate combination (PN+BoQ+BoQ Type)");

		// validate formula

		for (BoqMappingInverse bmi : bmiList) {
			if (bmi.getPartNumberEquivalence() == null || !bmi.getDirectEquivalence())
				continue;
			Double boqQuantity = bmi.getBoqQuantity();

			for (PartNumberEquivalenceDetail pned : bmi.getPartNumberEquivalence().getDetailList())
				if (bmiList.stream().filter(i -> i.getBoq().equals(bmi.getBoq()) && i.getPartNumberEquivalence() != null && i.getPartNumberEquivalence().equals(bmi.getPartNumberEquivalence()) && i.getPartNumber().equals(pned.getPartNumber()) && i.getBoqQuantity().equals(boqQuantity)).count() != 1)
					return FacesContextMessages.ErrorMessages("Formula not respected : " + bmi.getPartNumberEquivalence().getFormula());
		}

		// compare with boq remaining quantity
		keySet = new HashSet<String>();
		Map<Boq, Double> boqUsedQuantityInDn = new HashMap<>();
		for (BoqMappingInverse bmi : bmiList) {
			String key = bmi.getBoq().getId() + ";" + (bmi.getPartNumberEquivalence() == null ? "null" : bmi.getPartNumberEquivalence().getId());
			if (!keySet.add(key))
				continue;

			boqUsedQuantityInDn.putIfAbsent(bmi.getBoq(), 0.0);
			boqUsedQuantityInDn.put(bmi.getBoq(), boqUsedQuantityInDn.get(bmi.getBoq()) + bmi.getBoqQuantity());
		}
		for (Boq boq : boqUsedQuantityInDn.keySet())
			if (boq.getRemainingQuantity() < boqUsedQuantityInDn.get(boq))
				return FacesContextMessages.ErrorMessages("Quantity should be lower than remaining BoQ quantity for BoQ : " + boq.getPartNumber().getName());

		System.out.println("boqUsedQuantityInDn --> " + boqUsedQuantityInDn);

		// Dn.Qty should be == to mapping.qty
		Map<PartNumber, Double> map = bmiList.stream().collect(Collectors.groupingBy(BoqMappingInverse::getPartNumber, Collectors.summingDouble(BoqMappingInverse::getQuantity)));
		for (PartNumber pn : map.keySet()) {
			Double mappingQuantity = map.get(pn);
			Double dnQuantity = partNumberQuantityMap.get(pn);
			if (!mappingQuantity.equals(dnQuantity))
				return FacesContextMessages.ErrorMessages("Mapping Quantity should be equals to DN Quantity for part number : " + pn.getName());
		}

		return true;
	}

	public String save() {
		if (!canSave())
			return null;
		if (!validate())
			return null;
		Set<String> keySet = new HashSet<String>();
		for (BoqMappingInverse bmi : bmiList) {
			String key = bmi.getBoq().getId() + ";" + (bmi.getPartNumberEquivalence() == null ? "null" : bmi.getPartNumberEquivalence().getId());
			if (!keySet.add(key))
				continue;
			deliveryRequest.getBoqMappingList().add(new BoqMapping(bmi.getBoqQuantity(), deliveryRequest, bmi.getBoq(), bmi.getPartNumberEquivalence(), bmi.getDirectEquivalence()));
		}
		deliveryRequestService.save(deliveryRequest);
		boqService.updateTotalUsedQuantity(boqService.getAssociatedBoqIdListWithDeliveryRequest(deliveryRequest.getId()));
		deliveryRequestService.updateDetailListUnitPriceFromBoqMapping(deliveryRequest.getId());
		poService.updateBoqStatus(deliveryRequest.getPo().getIdpo());
		return addParameters("/viewDeliveryRequest.xhtml", "faces-redirect=true", "id=" + deliveryRequest.getId());
	}

	public List<PartNumberEquivalence> getPartNumberEquivalenceList(Boq boq, BoqMappingInverse bmi) {
		if (boq.getDirectEquivalence())
			return partNumberEquivalenceService.findByPartNumberAndContainingPartNumber(boq.getPartNumber().getId(), bmi.getPartNumber().getId());
		else
			return partNumberService.findOne(bmi.getPartNumber().getId()).getEquivalenceList().stream().filter(pne -> pne.getActive()).collect(Collectors.toList());
	}

	public List<PartNumberEquivalence> getPartNumberEquivalenceList() {
		return getPartNumberEquivalenceList(boqMappingInverse.getBoq(), boqMappingInverse);
	}

	// MAP

	// public void initMap() {
	// // refresh BOQ PN
	// boqMapping.getBoq().setPartNumber(partNumberService.findOne(boqMapping.getBoq().getPartNumber().getId()));
	// boqMapping.setQuantity(0.0);
	// }
	//
	// public void map() {
	// if (!validateMap())
	// return;
	//
	// if (!boqMapping.getEquivalence()) {
	//// boqMapping.setPartNumber(boqMapping.getBoq().getPartNumber());
	// boqMapping.setPartNumberEquivalence(null);
	// } else {
	//// boqMapping.setPartNumber(null);
	// boqMapping.setPartNumberEquivalence(partNumberEquivalence);
	// }
	//
	// execJavascript("PF('mapDlg').hide()");
	// }
	//
	// private Boolean validateMap() {
	// if (boqMapping.getQuantity() <= 0.0)
	// return FacesContextMessages.ErrorMessages("Quantity should be greather than
	// 0");
	//
	// if (boqMapping.getRemainingQuantity() < boqMapping.getQuantity())
	// return FacesContextMessages.ErrorMessages("BoQ Quantity should not be
	// greather than Remaining
	// Quantity");
	//
	// if (!boqMapping.getEquivalence()) {
	// Double remainingQuantityInDn =
	// getRemainingDnQuantity(boqMapping.getBoq().getPartNumber().getId());
	// if (remainingQuantityInDn == 0.0)
	// return FacesContextMessages.ErrorMessages("Boq PN " +
	// boqMapping.getBoq().getPartNumber().getName() + " dosen't exist in DN or all
	// DN quantity is
	// used");
	// if (remainingQuantityInDn < boqMapping.getQuantity())
	// return FacesContextMessages.ErrorMessages("BoQ Quantity should not be
	// greather than Remaining DN
	// Quantity");
	// } else {
	// for (PartNumberEquivalenceDetail pned :
	// partNumberEquivalence.getDetailList()) {
	// Double remainingQuantityInDn =
	// getRemainingDnQuantity(pned.getPartNumber().getId());
	// if (remainingQuantityInDn == 0.0)
	// return FacesContextMessages.ErrorMessages("Boq PN " +
	// pned.getPartNumber().getName() + " dosen't
	// exist in DN or all DN quantity is used");
	// if (remainingQuantityInDn < boqMapping.getQuantity())
	// return FacesContextMessages.ErrorMessages("BoQ Quantity should not be
	// greather than Remaining DN
	// Quantity");
	// }
	// }
	// return true;
	// }
	//
	// private Double getRemainingDnQuantity(Integer partNumberId) {
	// Double totalDnQty = deliveryRequest.getDetailList().stream().filter(i ->
	// i.getPartNumber().getId().equals(partNumberId)).mapToDouble(i ->
	// i.getQuantity()).sum();
	//
	// // case same PN
	// Double usedQuantity1 = list4.stream().filter(i ->
	// i.getPartNumberEquivalence() == null &&
	// i.getBoq().getPartNumber().getId().equals(partNumberId)).mapToDouble(i ->
	// i.getQuantity()).sum();
	//
	// // case equivalence
	// Double usedQuantity2 = 0.0;
	// for (BoqMapping bm : list4) {
	// if (bm.getPartNumberEquivalence() != null)
	// for (PartNumberEquivalenceDetail pned :
	// bm.getPartNumberEquivalence().getDetailList())
	// if (pned.getPartNumber().getId().equals(partNumberId))
	// usedQuantity2 += bm.getQuantity() * pned.getQuantity();
	// }
	// System.out.println("--------------------------------------------------");
	// System.out.println("part number : " + partNumberId);
	// System.out.println("totalDnQty : " + totalDnQty);
	// System.out.println("usedQuantity1 : " + usedQuantity1);
	// System.out.println("usedQuantity2 : " + usedQuantity2);
	// System.out.println("--------------------------------------------------");
	//
	// return totalDnQty - (usedQuantity1 + usedQuantity2);
	// }
	//
	// public void refreshPartNumberEquivalenceList() {
	//
	// if (!boqMapping.getEquivalence())
	// return;
	//
	// boqMapping.getBoq().setPartNumber(partNumberService.findOne(boqMapping.getBoq().getPartNumber().getId()));
	//
	// partNumberEquivalenceList = new ArrayList<>();
	// Set<Integer> dnPnIdSet = deliveryRequest.getDetailList().stream().map(i ->
	// i.getPartNumber().getId()).collect(Collectors.toSet());
	// for (PartNumberEquivalence pne :
	// boqMapping.getBoq().getPartNumber().getEquivalenceList())
	// if (pne.getDetailList().stream().map(i -> i.getPartNumber().getId()).filter(i
	// ->
	// !dnPnIdSet.contains(i)).count() == 0)
	// partNumberEquivalenceList.add(pne);
	// }
	//
	// // SAVE MAPPING
	//
	// public Boolean canSave() { // same in deliveryRequestView
	// return deliveryRequest.getDate4() != null && deliveryRequest.getPo() != null
	// &&
	// deliveryRequest.getBoqMappingList().isEmpty();
	// }
	//
	// public String save() {
	// if (!canSave())
	// return null;
	// if (!validate())
	// return null;
	// for (BoqMapping boqMapping : list4) {
	// boqMapping.setDeliveryRequest(deliveryRequest);
	// deliveryRequest.getBoqMappingList().add(boqMapping);
	// }
	// deliveryRequestService.save(deliveryRequest);
	// return addParameters("/viewDeliveryRequest.xhtml", "faces-redirect=true",
	// "id=" +
	// deliveryRequest.getId());
	// }
	//
	// private Boolean validate() {
	// if (list4.isEmpty())
	// return FacesContextMessages.ErrorMessages("List should not be empty");
	//
	// if (list4.stream().filter(i -> i.getQuantity() == null || i.getQuantity() <=
	// 0).count() > 0)
	// return FacesContextMessages.ErrorMessages("Quantity should not be null and
	// greather than 0");
	//
	//// if (list4.stream().filter(i -> i.getPartNumber() == null &&
	// i.getPartNumberEquivalence() ==
	// null).count() > 0)
	//// return FacesContextMessages.ErrorMessages("DN PN should not be null");
	//
	// if (deliveryRequest.getDetailList().stream().map(i ->
	// i.getPartNumber().getId()).distinct().filter(i -> getRemainingDnQuantity(i) >
	// 0).count() > 0)
	// return FacesContextMessages.ErrorMessages("All DN Part number should be
	// mapped");
	//
	// return true;
	// }

	// GETTERS & SETTERS
	public BoqMapping getBoqMapping() {
		return boqMapping;
	}

	public void setBoqMapping(BoqMapping boqMapping) {
		this.boqMapping = boqMapping;
	}

	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	public PartNumberEquivalence getPartNumberEquivalence() {
		return partNumberEquivalence;
	}

	public void setPartNumberEquivalence(PartNumberEquivalence partNumberEquivalence) {
		this.partNumberEquivalence = partNumberEquivalence;
	}

	public List<BoqMappingInverse> getBmiList() {
		return bmiList;
	}

	public void setBmiList(List<BoqMappingInverse> bmiList) {
		this.bmiList = bmiList;
	}

	public BoqMappingInverse getBoqMappingInverse() {
		return boqMappingInverse;
	}

	public void setBoqMappingInverse(BoqMappingInverse boqMappingInverse) {
		this.boqMappingInverse = boqMappingInverse;
	}

	public List<Boq> getBoqList() {
		return boqList;
	}

	public void setBoqList(List<Boq> boqList) {
		this.boqList = boqList;
	}

}
