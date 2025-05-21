package ma.azdad.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.PackingDetail;
import ma.azdad.model.StockRow;
import ma.azdad.repos.DeliveryRequestSerialNumberRepos;
import ma.azdad.service.DeliveryRequestSerialNumberService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.StockRowService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class DeliveryRequestSerialNumberView extends GenericView<Integer, DeliveryRequestSerialNumber, DeliveryRequestSerialNumberRepos, DeliveryRequestSerialNumberService> {

	@Autowired
	private DeliveryRequestSerialNumberService deliveryRequestSerialNumberService;

	@Autowired
	private CacheView cacheView;

	@Autowired
	private StockRowService stockRowService;

	@Autowired
	private DeliveryRequestService deliveryRequestService;

	@Autowired
	private DeliveryRequestView deliveryRequestView;

	private DeliveryRequestSerialNumber deliveryRequestSerialNumber = new DeliveryRequestSerialNumber();

	List<SerialNumberSummary> serialNumberSummaryList;

	private List<DeliveryRequestSerialNumber> inboundList1 = new ArrayList<>();
	private List<DeliveryRequestSerialNumber> inboundList2 = new ArrayList<>();
	private List<DeliveryRequestSerialNumber> inboundList3;
	private String inboundSearch;

	private DatatableList<DeliveryRequestSerialNumber> datatable1;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		refreshList();
		if (isEditPage)
			deliveryRequestSerialNumber = deliveryRequestSerialNumberService.findOne(id);
		else if (isViewPage)
			deliveryRequestSerialNumber = deliveryRequestSerialNumberService.findOne(id);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if ("/viewDeliveryRequest.xhtml".equals(currentPath)) {
			list2 = list1 = deliveryRequestSerialNumberService.findByDeliveryRequest(id);
			refreshSerialNumberSummaryList();
			datatable1 = new DatatableList<DeliveryRequestSerialNumber>(list1.stream().filter(i -> i.getOutboundDeliveryRequest() == null).collect(Collectors.toList()));
		}
	}

	public void flushDeliveryRequestSerialNumber() {
		deliveryRequestSerialNumberService.flush();
	}

	public void refreshDeliveryRequestSerialNumber() {
		if (deliveryRequestSerialNumber.getId() != null)
			deliveryRequestSerialNumber = deliveryRequestSerialNumberService.findOne(deliveryRequestSerialNumber.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewDeliveryRequestSerialNumber())
			cacheView.accessDenied();
	}

	public Boolean canViewDeliveryRequestSerialNumber() {
		return true;
	}

	// SAVE DELIVERYREQUESTSERIALNUMBER
	public Boolean canSaveDeliveryRequestSerialNumber() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveDeliveryRequestSerialNumber() {
		if (!canSaveDeliveryRequestSerialNumber())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateDeliveryRequestSerialNumber())
			return null;
		deliveryRequestSerialNumber = deliveryRequestSerialNumberService.save(deliveryRequestSerialNumber);

		return addParameters(viewPage, "faces-redirect=true", "id=" + deliveryRequestSerialNumber.getId());
	}

	public Boolean validateDeliveryRequestSerialNumber() {
		return true;
	}

	// DELETE DELIVERYREQUESTSERIALNUMBER
	public Boolean canDeleteDeliveryRequestSerialNumber() {
		return true;
	}

	public String deleteDeliveryRequestSerialNumber() {
		if (canDeleteDeliveryRequestSerialNumber())
			try {
				deliveryRequestSerialNumberService.delete(deliveryRequestSerialNumber);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}
		return addParameters(listPage, "faces-redirect=true");
	}

	// Fill SN
	private Boolean editMode = false;
	private List<Integer> exculdeList = new ArrayList<Integer>();

	public Boolean canEdit() {
		DeliveryRequest deliveryRequest = deliveryRequestView.getDeliveryRequest();
		if (deliveryRequest.getIsInbound())
			return deliveryRequest.getDate4() != null && cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId()) && !editMode;
		else if (deliveryRequest.getIsOutbound())
			return deliveryRequest.getDate4() != null && cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId()) && Boolean.TRUE.equals(deliveryRequest.getMissingSerialNumber());
		return false;
	}

	public void initEdit(DeliveryRequest deliveryRequest) {
		if (deliveryRequest.getIsOutbound()) {
			// add remaining qty as empty rows
			List<DeliveryRequestSerialNumber> inboundSerialNumberList = deliveryRequestSerialNumberService.findInboundSerialNumberByOutboundDeliveryRequest(deliveryRequest.getId());
			Set<String> inboundSerialNumberKeySet = inboundSerialNumberList.stream().map(i -> i.getKey1()).collect(Collectors.toSet());

			Map<String, Integer> map = new HashMap<>();

			for (Integer stockRowId : deliveryRequest.getStockRowList().stream().map(i -> i.getId()).collect(Collectors.toList())) {
				StockRow outboundStockRow = stockRowService.findOne(stockRowId);
				for (PackingDetail packingDetail : outboundStockRow.getPacking().getDetailList()) {

					map.putIfAbsent(outboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId(), 0);

					String key1 = outboundStockRow.getKey() + ";" + packingDetail.getId();

					if (!inboundSerialNumberKeySet.contains(key1))
						continue;

					int packingQuantity = (int) (-outboundStockRow.getQuantity() / outboundStockRow.getPacking().getQuantity());
					int n = packingDetail.getQuantity();

					for (int i = 0; i < packingQuantity; i++) {
						int packingNumero = map.get(outboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId()) + 1;
						map.put(outboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId(), packingNumero);
						for (int j = 0; j < n; j++)
							list1.add(new DeliveryRequestSerialNumber(packingNumero, packingDetail, deliveryRequest, outboundStockRow));

					}

				}
			}
			list1.sort(DeliveryRequestSerialNumberService.COMPARATOR);
		}
	}

	public Boolean canSave() {
		DeliveryRequest deliveryRequest = deliveryRequestView.getDeliveryRequest();
		if (deliveryRequest.getIsInbound())
			return cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId()) && editMode;
		if (deliveryRequest.getIsOutbound())
			return cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId());
		return false;
	}

	public void save(DeliveryRequest deliveryRequest) {
		if (!validate(deliveryRequest))
			return;

		if (deliveryRequest.getIsOutbound())
			list1.forEach(i -> i.setOutboundDeliveryRequest(deliveryRequest));

		list1.stream().forEach(i -> deliveryRequestSerialNumberService.save(i));

		// update missingSn field
//		if (deliveryRequest.getIsInbound()) {
//			if (deliveryRequestSerialNumberService.countByInboundDeliveryRequestAndEmpty(deliveryRequest.getId()) == 0)
//				deliveryRequestService.updateMissingSerialNumber(deliveryRequest.getId(), false);
//		} else if (deliveryRequest.getIsOutbound())
//			deliveryRequestService.updateMissingSerialNumber(deliveryRequest.getId(), false);
		deliveryRequestService.calculateMissingSerialNumber(deliveryRequest.getId());
		// update associated outbound
		stockRowService.findAssociatedOutboundWithInbound(deliveryRequest.getId()).forEach(deliveryRequestService::calculateMissingSerialNumber);

		editMode = false;
		refreshList();
	}

	private Boolean validate(DeliveryRequest deliveryRequest) {
		list1.stream().filter(drsn -> drsn.getSerialNumber() != null).forEach(drsn -> drsn.setSerialNumber(drsn.getSerialNumber().replace("\\s", "")));

		if (deliveryRequest.getIsInbound()) {
			if (list1.stream().filter(i -> i.getSerialNumber() != null && !i.getSerialNumber().isEmpty()).count() > list1.stream()
					.filter(i -> i.getSerialNumber() != null && !i.getSerialNumber().isEmpty()).map(i -> i.getSerialNumber()).distinct().count())
				return FacesContextMessages.ErrorMessages("Duplicate SN !");
		} else if (deliveryRequest.getIsOutbound()) {
			if (list1.stream().filter(i -> i.getInboundStockRow() == null).count() > 0)
				return FacesContextMessages.ErrorMessages("You should fill all packings");
			if (list1.stream().filter(i -> i.getSerialNumber() == null || i.getSerialNumber().isEmpty()).count() > 0)
				return FacesContextMessages.ErrorMessages("SN should not be null");

		}

		return true;
	}

	public void selectSerialNumberListener(DeliveryRequestSerialNumber drsn, DeliveryRequest deliveryRequest) {

		Integer packingNumero = deliveryRequestSerialNumberService.findPackingNumeroByPartNumberAndInboundDeliveryRequestAndSerialNumber(drsn.getTmpPartNumber().getId(),
				drsn.getTmpInboundDeliveryRequest().getId(), drsn.getSerialNumber());
		list1.removeIf(i -> i.getInboundStockRow() == null && i.getTmpInboundDeliveryRequest().getId().equals(drsn.getTmpInboundDeliveryRequest().getId())
				&& i.getTmpPartNumber().getId().equals(drsn.getTmpPartNumber().getId()) && i.getPackingNumero().equals(drsn.getPackingNumero()));
		list1.addAll(deliveryRequestSerialNumberService.findByPartNumberAndInboundDeliveryRequestAndPackingNumero(drsn.getTmpPartNumber().getId(), drsn.getTmpInboundDeliveryRequest().getId(),
				packingNumero));

		exculdeList = list1.stream().filter(i -> i.getId() != null).map(i -> i.getId()).collect(Collectors.toList());

		// list1.sort(DeliveryRequestSerialNumberService.COMPARATOR);

	}

	public void uploadExcelFile(FileUploadEvent event) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook(event.getFile().getInputstream());
		Sheet sheet = wb.getSheetAt(0);

		if (deliveryRequestView.getDeliveryRequest().getIsInbound()) {
			if (sheet.getRow(0).getLastCellNum() != 5) {
				FacesContextMessages.ErrorMessages("File should have 5 columns : KEY - Reference - Packing Detail - Serial Number - Box Id");
				return;
			}
			Map<String, String> mapSerialNumber = new HashMap<String, String>();
			Map<String, String> mapBox = new HashMap<String, String>();
			Iterator<Row> rowIterator = sheet.iterator();
			// ignore first row
			rowIterator.next();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				try {
					String key = getCellValue(row.getCell(0));
					String serialNumber = getCellValue(row.getCell(3));
					String box = getCellValue(row.getCell(4));

					if (mapSerialNumber.containsKey(key) || mapBox.containsKey(key)) {
						FacesContextMessages.ErrorMessages("Duplicate Key");
						return;
					}

					if (!StringUtils.isBlank(serialNumber)) {
						if (mapSerialNumber.containsValue(serialNumber)) {
							FacesContextMessages.ErrorMessages("Duplicate SN in excel file : " + serialNumber);
							return;
						}
						mapSerialNumber.put(key, serialNumber);
					}

					if (!StringUtils.isBlank(box))
						mapBox.put(key, box);

				} catch (Exception e) {
					FacesContextMessages.ErrorMessages(e.getMessage());
					return;
				}
			}

			for (String key : mapSerialNumber.keySet()) {
				String value = mapSerialNumber.get(key);
				Optional<DeliveryRequestSerialNumber> optional = list1.stream().filter(i -> i.getKey().contentEquals(key)).findFirst();
				if (optional.isPresent() && StringUtils.isBlank(optional.get().getSerialNumber()))
					optional.get().setSerialNumber(value);
			}

			for (String key : mapBox.keySet()) {
				String value = mapBox.get(key);
				Optional<DeliveryRequestSerialNumber> optional = list1.stream().filter(i -> i.getKey().contentEquals(key)).findFirst();
				if (optional.isPresent() && StringUtils.isBlank(optional.get().getBox()))
					optional.get().setBox(value);
			}
		} else if (deliveryRequestView.getDeliveryRequest().getIsOutbound()) {
			if (sheet.getRow(0).getLastCellNum() != 1) {
				FacesContextMessages.ErrorMessages("File should have 1 column : Serial Number");
				return;
			}
			Set<String> serialNumberSet = new HashSet<String>();
			Iterator<Row> rowIterator = sheet.iterator();
			// ignore first row
			rowIterator.next();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				try {
					String serialNumber = getCellValue(row.getCell(0));
					if (StringUtils.isNotEmpty(serialNumber))
						serialNumberSet.add(serialNumber);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			inboundList3 = inboundList1.stream().filter(i -> serialNumberSet.contains(i.getSerialNumber())).collect(Collectors.toList());
			validateInboundSelectinList();
		}

	}

	private String getCellValue(Cell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue().replace("\\s", "");
		case Cell.CELL_TYPE_NUMERIC:
			Double value = cell.getNumericCellValue();
			if (value == Math.floor(value))
				return String.valueOf((int) cell.getNumericCellValue());
			return String.valueOf(cell.getNumericCellValue());
		default:
			return null;
		}

	}

	// outbound

	public void refreshSerialNumberSummaryList() {
		DeliveryRequest deliveryRequest = deliveryRequestView.getDeliveryRequest();
		List<StockRow> list = null;
		serialNumberSummaryList = new ArrayList<DeliveryRequestSerialNumberView.SerialNumberSummary>();
		Map<String, SerialNumberSummary> map = new HashMap<String, SerialNumberSummary>(); // key (inboundDeliveryRequestDetailId;packingDetailId)
		switch (deliveryRequest.getType()) {
		case INBOUND:
			list = deliveryRequest.getStockRowList().stream().filter(i -> i.getPacking().getHasSerialnumber()).collect(Collectors.toList());
			break;
		case OUTBOUND:
			list = deliveryRequest.getStockRowList().stream().filter(i -> i.getInboundDeliveryRequest().getIsSnRequired() && i.getPacking().getHasSerialnumber()).collect(Collectors.toList());
			break;
		}

		for (StockRow stockRow : list) {
			stockRow.getPacking().getDetailList().stream().filter(pd -> pd.getHasSerialnumber()).forEach(pd -> {
				String key = stockRow.getInboundDeliveryRequestDetail().getId() + ";" + pd.getId();
				Integer inboundDeliveryRequestDetailId = stockRow.getInboundDeliveryRequestDetail().getId();
				String partNumberName = stockRow.getPartNumberName();
				Integer inboundDeliveryRequestId = stockRow.getInboundDeliveryRequestId();
				String inboundDeliveryRequestReference = stockRow.getInboundDeliveryRequestReference();
				String packingName = stockRow.getPacking().getName();
				Double usedQuantity = null;
				Double stockRowQuantity = null;
				switch (deliveryRequest.getType()) {
				case INBOUND:
					usedQuantity = (double) list1.stream()
					.filter(i -> i.getInboundStockRow().getDeliveryRequestDetail().getId().equals(inboundDeliveryRequestDetailId) && i.getPackingDetail().getId().equals(pd.getId()) && StringUtils.isNotBlank(i.getSerialNumber())).count();
					stockRowQuantity = stockRow.getQuantity();
					break;
				case OUTBOUND:
					usedQuantity = (double) list1.stream()
							.filter(i -> i.getInboundStockRow().getDeliveryRequestDetail().getId().equals(inboundDeliveryRequestDetailId) && i.getPackingDetail().getId().equals(pd.getId())).count();
					stockRowQuantity = -stockRow.getQuantity();
					break;
				}
				map.putIfAbsent(key, new SerialNumberSummary(inboundDeliveryRequestDetailId, pd.getId(), partNumberName, inboundDeliveryRequestId, inboundDeliveryRequestReference, packingName,
						pd.getType(), usedQuantity));
				map.get(key).setQuantity(map.get(key).getQuantity() + pd.getQuantity() * (stockRowQuantity / stockRow.getPacking().getQuantity()));
			});
		}
		for (String key : map.keySet())
			serialNumberSummaryList.add(map.get(key));

	}

	public void initInboundLists() {
		inboundList1 = new ArrayList<DeliveryRequestSerialNumber>();
		serialNumberSummaryList.stream().filter(i -> i.getRemainingQuantity() > 0.0)
				.forEach(i -> inboundList1.addAll(service.findRemainingOutbound(i.getInboundDeliveryRequestDetailId(), i.getPackingDetailId())));
		inboundList2 = inboundList1;

		validateInboundSelectinList = false;
	}

	private Boolean validateInboundSelectinList = false;

	public void validateInboundSelectinList() {
		if (inboundList3 == null || inboundList3.isEmpty())
			validateInboundSelectinList = false;
		Map<String, Double> inboundQuantityMap = new HashMap<String, Double>();
		Map<String, Double> outboundRemainingQuantityMap = new HashMap<String, Double>();

		for (DeliveryRequestSerialNumber drsn : inboundList3) {
			String key = drsn.getInboundStockRow().getDeliveryRequestDetail().getId() + ";" + drsn.getPackingDetail().getId();
			inboundQuantityMap.putIfAbsent(key, 0.0);
			inboundQuantityMap.put(key, inboundQuantityMap.get(key) + 1);
		}

		for (SerialNumberSummary outboundSerialNumberSummary : serialNumberSummaryList) {
			String key = outboundSerialNumberSummary.getInboundDeliveryRequestDetailId() + ";" + outboundSerialNumberSummary.getPackingDetailId();
			outboundRemainingQuantityMap.put(key, outboundSerialNumberSummary.getRemainingQuantity());
		}

		System.out.println(inboundQuantityMap);
		System.out.println(outboundRemainingQuantityMap);

		for (String key : inboundQuantityMap.keySet())
			if (inboundQuantityMap.get(key) > outboundRemainingQuantityMap.get(key)) {
				FacesContextMessages.ErrorMessages("Selection contains more than remaining quantity");
				validateInboundSelectinList = false;
				return;
			}

		validateInboundSelectinList = true;
	}

	public String outboundSave() {
		if (!canSave())
			return null;
		if (!validateInboundSelectinList)
			return null;
		inboundList3.forEach(i -> {
			i.setOutboundDeliveryRequest(deliveryRequestView.getDeliveryRequest());
			deliveryRequestSerialNumberService.save(i);
		});

		deliveryRequestView.refreshDeliveryRequest();
		refreshList();
//		deliveryRequestService.updateMissingSerialNumber(deliveryRequestView.getDeliveryRequest().getId(),
//				serialNumberSummaryList.stream().filter(i -> i.getRemainingQuantity() > 0).count() > 0);

		deliveryRequestService.calculateMissingSerialNumber(deliveryRequestView.getDeliveryRequest().getId());

		return addParameters("viewDeliveryRequest.xhtml", "faces-redirect=true", "id=" + deliveryRequestView.getId());

	}

	// generic
	public List<DeliveryRequestSerialNumber> findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(DeliveryRequestSerialNumber current) {
		return deliveryRequestSerialNumberService.findRemainingByPartNumberAndInboundDeliveryRequestAndStatusAndLocationAndPackingDetail(current, exculdeList);
	}
	

	// tmp class
	public static class SerialNumberSummary {

		// key
		private Integer inboundDeliveryRequestDetailId;
		private Integer packingDetailId;

		private String partNumberName;
		private Integer inboundDeliveryRequestId;
		private String inboundDeliveryRequestReference;
		private String packingName;
		private String packingDetailType;
		private Double quantity = 0.0;
		private Double usedQuantity = 0.0;

		public SerialNumberSummary(Integer inboundDeliveryRequestDetailId, Integer packingDetailId, String partNumberName, Integer inboundDeliveryRequestId, String inboundDeliveryRequestReference,
				String packingName, String packingDetailType, Double usedQuantity) {
			super();
			this.inboundDeliveryRequestDetailId = inboundDeliveryRequestDetailId;
			this.packingDetailId = packingDetailId;
			this.partNumberName = partNumberName;
			this.inboundDeliveryRequestId = inboundDeliveryRequestId;
			this.inboundDeliveryRequestReference = inboundDeliveryRequestReference;
			this.packingName = packingName;
			this.packingDetailType = packingDetailType;
			this.usedQuantity = usedQuantity;
		}

		public String getPartNumberName() {
			return partNumberName;
		}

		public void setPartNumberName(String partNumberName) {
			this.partNumberName = partNumberName;
		}

		public Integer getInboundDeliveryRequestId() {
			return inboundDeliveryRequestId;
		}

		public void setInboundDeliveryRequestId(Integer inboundDeliveryRequestId) {
			this.inboundDeliveryRequestId = inboundDeliveryRequestId;
		}

		public String getInboundDeliveryRequestReference() {
			return inboundDeliveryRequestReference;
		}

		public void setInboundDeliveryRequestReference(String inboundDeliveryRequestReference) {
			this.inboundDeliveryRequestReference = inboundDeliveryRequestReference;
		}

		public String getPackingName() {
			return packingName;
		}

		public void setPackingName(String packingName) {
			this.packingName = packingName;
		}

		public Integer getInboundDeliveryRequestDetailId() {
			return inboundDeliveryRequestDetailId;
		}

		public void setInboundDeliveryRequestDetailId(Integer inboundDeliveryRequestDetailId) {
			this.inboundDeliveryRequestDetailId = inboundDeliveryRequestDetailId;
		}

		public Integer getPackingDetailId() {
			return packingDetailId;
		}

		public void setPackingDetailId(Integer packingDetailId) {
			this.packingDetailId = packingDetailId;
		}

		public String getPackingDetailType() {
			return packingDetailType;
		}

		public void setPackingDetailType(String packingDetailType) {
			this.packingDetailType = packingDetailType;
		}

		public Double getQuantity() {
			return quantity;
		}

		public void setQuantity(Double quantity) {
			this.quantity = quantity;
		}

		public Double getUsedQuantity() {
			return usedQuantity;
		}

		public void setUsedQuantity(Double usedQuantity) {
			this.usedQuantity = usedQuantity;
		}

		public Double getRemainingQuantity() {
			return quantity - usedQuantity;
		}

		@Override
		public String toString() {
			return "SerialNumberSummary [partNumberName=" + partNumberName + ", inboundDeliveryRequestReference=" + inboundDeliveryRequestReference + ", packingName=" + packingName
					+ ", packingDetailType=" + packingDetailType + ", quantity=" + quantity + ", usedQuantity=" + usedQuantity + "]";
		}

	}

	// GETTERS & SETTERS
	public DeliveryRequestSerialNumber getDeliveryRequestSerialNumber() {
		return deliveryRequestSerialNumber;
	}

	public void setDeliveryRequestSerialNumber(DeliveryRequestSerialNumber deliveryRequestSerialNumber) {
		this.deliveryRequestSerialNumber = deliveryRequestSerialNumber;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

	public List<DeliveryRequestSerialNumber> getInboundList1() {
		return inboundList1;
	}

	public void setInboundList1(List<DeliveryRequestSerialNumber> inboundList1) {
		this.inboundList1 = inboundList1;
	}

	public List<DeliveryRequestSerialNumber> getInboundList2() {
		return inboundList2;
	}

	public void setInboundList2(List<DeliveryRequestSerialNumber> inboundList2) {
		this.inboundList2 = inboundList2;
	}

	public String getInboundSearch() {
		return inboundSearch;
	}

	public void setInboundSearch(String inboundSearch) {
		this.inboundSearch = inboundSearch;
		List<DeliveryRequestSerialNumber> list = new ArrayList<DeliveryRequestSerialNumber>();
		inboundSearch = inboundSearch.toLowerCase().trim();
		for (DeliveryRequestSerialNumber bean : inboundList1) {
			if (bean.filter(inboundSearch))
				list.add(bean);
		}
		inboundList2 = list;
	}

	public List<SerialNumberSummary> getSerialNumberSummaryList() {
		return serialNumberSummaryList;
	}

	public void setSerialNumberSummaryList(List<SerialNumberSummary> serialNumberSummaryList) {
		this.serialNumberSummaryList = serialNumberSummaryList;
	}

	public List<DeliveryRequestSerialNumber> getInboundList3() {
		return inboundList3;
	}

	public void setInboundList3(List<DeliveryRequestSerialNumber> inboundList3) {
		this.inboundList3 = inboundList3;
	}

	public Boolean getValidateInboundSelectinList() {
		return validateInboundSelectinList;
	}

	public void setValidateInboundSelectinList(Boolean validateInboundSelectinList) {
		this.validateInboundSelectinList = validateInboundSelectinList;
	}

	public DatatableList<DeliveryRequestSerialNumber> getDatatable1() {
		return datatable1;
	}

	public void setDatatable1(DatatableList<DeliveryRequestSerialNumber> datatable1) {
		this.datatable1 = datatable1;
	}

}
