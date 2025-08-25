package ma.azdad.service;

import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.mobile.model.Vehicule;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.Path;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobItinerary;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestFile;
import ma.azdad.model.TransportationRequestImage;
import ma.azdad.model.TransportationRequestPaymentStatus;
import ma.azdad.model.TransportationRequestState;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.model.User;
import ma.azdad.repos.TransportationJobItineraryRepos;
import ma.azdad.repos.TransportationRequestFileRepos;
import ma.azdad.repos.TransportationRequestImageRepos;
import ma.azdad.repos.TransportationRequestRepos;
import ma.azdad.utils.Public;

@Component
public class TransportationRequestService extends GenericService<Integer, TransportationRequest, TransportationRequestRepos> {

	@Autowired
	TransportationRequestRepos repos;

	@Autowired
	DeliveryRequestService deliveryRequestService;

	@Autowired
	AcceptanceService acceptanceService;

	@Autowired
	ExpensepaymentService expensepaymentService;
	@Autowired
	TransportationJobItineraryRepos transportationJobItineraryRepos;

	@Autowired
	TransportationRequestService transportationRequestService;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	TransportationRequestImageRepos transportationRequestImageRepos;

	@Autowired
	TransportationRequestFileRepos transportationRequestFileRepos;

	@Autowired
	TransportationRequestHistoryService transportationRequestHistoryService;

	@Autowired
	FileReaderService fileReaderService;

	@Override
	public TransportationRequest save(TransportationRequest a) {
		evictCache("deliveryRequestService");
		return super.save(a);
	}

	@Override
	public TransportationRequest findOne(Integer id) {
		TransportationRequest transportationRequest = super.findOne(id);
		if (transportationRequest == null)
			return null;
		Hibernate.initialize(transportationRequest.getFileList());
		Hibernate.initialize(transportationRequest.getHistoryList());
		Hibernate.initialize(transportationRequest.getDeliveryRequest());
		Hibernate.initialize(transportationRequest.getDeliveryRequest().getProject());
		Hibernate.initialize(transportationRequest.getDeliveryRequest().getProject().getManager());
		Hibernate.initialize(transportationRequest.getDeliveryRequest().getOrigin());
		Hibernate.initialize(transportationRequest.getDeliveryRequest().getDestination());
		Hibernate.initialize(transportationRequest.getDeliveryRequest().getDetailList());
		transportationRequest.getDeliveryRequest().getDetailList().forEach(i -> Hibernate.initialize(i.getPacking()));
		transportationRequest.getDeliveryRequest().getDetailList().forEach(i -> i.getPacking().getDetailList().forEach(j -> Hibernate.initialize(j)));
		Hibernate.initialize(transportationRequest.getContact1());
		Hibernate.initialize(transportationRequest.getContact2());
		Hibernate.initialize(transportationRequest.getTransportationJob());
		if (transportationRequest.getTransportationJob() != null) {
			Hibernate.initialize(transportationRequest.getTransportationJob().getVehicle());
			Hibernate.initialize(transportationRequest.getTransportationJob().getDriver());
			Hibernate.initialize(transportationRequest.getTransportationJob().getTransporter());
			if (transportationRequest.getTransportationJob().getTransporter() != null) {
				Hibernate.initialize(transportationRequest.getTransportationJob().getTransporter().getSupplier());
				Hibernate.initialize(transportationRequest.getTransportationJob().getTransporter().getCompany());
			}
		}
		return transportationRequest;
	}

	public List<TransportationRequest> findByNotHavingTransportationJob(TransportationRequestStatus status, List<DeliveryRequestStatus> deliveryRequestStatus) {
		return repos.findByNotHavingTransportationJob(status, deliveryRequestStatus);
	}

	public List<TransportationRequest> findByNotHavingTransportationJob(List<TransportationRequestStatus> status) {
		return repos.findByNotHavingTransportationJob(status);
	}

	public List<TransportationRequest> findLight(TransportationRequestStatus status, Boolean isTM) {
		if (!isTM)
			return null;
		return repos.findLight(status);
	}

	public List<TransportationRequest> find(TransportationRequestState state) {
		if (state == null)
			return repos.findLight();
		return repos.findLight(state.getStatusList());
	}

	@Cacheable("transportationRequestService.count")
	public Long count(TransportationRequestStatus status, Boolean isTM) {
		if (!isTM)
			return 0l;
		return repos.count(status);
	}

	public List<TransportationRequest> findByPaymentStatus(TransportationRequestPaymentStatus paymentStatus, Boolean isTm, String username) {
		if (paymentStatus == null)
			return isTm ? repos.findByPaymentStatus(TransportationJobStatus.ACKNOWLEDGED) : repos.findByPaymentStatus(TransportationJobStatus.ACKNOWLEDGED, username);
		else
			return isTm ? repos.findByPaymentStatus(TransportationJobStatus.ACKNOWLEDGED, paymentStatus) : repos.findByPaymentStatus(TransportationJobStatus.ACKNOWLEDGED, paymentStatus, username);

	}

	public List<TransportationRequest> findToAssign() {
		return repos.findToAssign();
	}

	public Long countToAssign() {
		return repos.countToAssign();
	}

	public List<TransportationRequest> findToPickup(String username) {
		return repos.findToPickup(username);
	}

	public Long countToPickup(String username) {
		return repos.countToPickup(username);
	}

	public List<TransportationRequest> findToDeliver(String username) {
		return repos.findToDeliver(username);
	}

	public Long countToDeliver(String username) {
		return repos.countToDeliver(username);
	}

	public List<TransportationRequest> findToAcknowledge(String username) {
		return repos.findToAcknowledge(username);
	}

	public Long countToAcknowledge(String username) {
		return repos.countToAcknowledge(username);
	}

	@Cacheable("transportationRequestService.countByDriverAndStatus")
	public Long countByDriverAndStatus(String username, List<TransportationRequestStatus> statusList) {
		return repos.countByDriverAndStatus(username, statusList);
	}
	
	public Long countPendingByDriver(String username) {
		return countByDriverAndStatus(username, Arrays.asList(TransportationRequestStatus.EDITED,TransportationRequestStatus.REQUESTED,TransportationRequestStatus.APPROVED,TransportationRequestStatus.ASSIGNED,TransportationRequestStatus.PICKEDUP));
	}

	public List<TransportationRequest> findLight(String username, TransportationRequestState state, List<Integer> assignedProjectList, Boolean isTM) {
		if (state == null)
			if (isTM)
				return repos.findLight();
			else
				return repos.findLight(assignedProjectList, username);
		if (isTM)
			return repos.findLight(state.getStatusList());
		else
			return repos.findLight(username, state.getStatusList(), assignedProjectList);

//		if (TransportationRequestState.WAITING.equals(state))
//			if (isTM)
//				return repos.findLight(Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REQUESTED, TransportationRequestStatus.APPROVED));
//			else
//				return repos.findLight(username, Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REQUESTED, TransportationRequestStatus.APPROVED), assignedProjectList);
//		else if (TransportationRequestState.HANDLED.equals(state))
//			if (isTM)
//				return repos.findLight(Arrays.asList(TransportationRequestStatus.ASSIGNED, TransportationRequestStatus.PICKEDUP));
//			else
//				return repos.findLight(username, Arrays.asList(TransportationRequestStatus.ASSIGNED, TransportationRequestStatus.PICKEDUP), assignedProjectList);
//		else if (TransportationRequestState.DELIVRED.equals(state))
//			if (isTM)
//				return repos.findLight(Arrays.asList(TransportationRequestStatus.DELIVERED, TransportationRequestStatus.ACKNOWLEDGED));
//			else
//				return repos.findLight(username, Arrays.asList(TransportationRequestStatus.DELIVERED, TransportationRequestStatus.ACKNOWLEDGED), assignedProjectList);
//		else if (TransportationRequestState.REJECTED.equals(state))
//			if (isTM)
//				return repos.findLight(Arrays.asList(TransportationRequestStatus.REJECTED, TransportationRequestStatus.CANCELED));
//			else
//				return repos.findLight(username, Arrays.asList(TransportationRequestStatus.REJECTED, TransportationRequestStatus.CANCELED), assignedProjectList);
//		return null;
	}

	public List<TransportationRequest> findByDriver(String driverUsername, TransportationRequestState state) {
		if (state == null)
			return repos.findByDriver(driverUsername);
		return repos.findByDriver(driverUsername, state.getStatusList());
	}

	public List<TransportationRequest> findByTransporter(Integer transporterId, TransportationRequestState state) {
		if (state == null)
			return repos.findByTransporter(transporterId);
		return repos.findByTransporter(transporterId, state.getStatusList());
	}

	public List<TransportationRequest> findLightBySupplierUser(TransportationRequestState state, Integer supplierId, List<Integer> assignedProjectList) {
		if (state == null)
			return repos.findLightBySupplierUser(supplierId, assignedProjectList);
		else
			return repos.findLightBySupplierUser(supplierId, assignedProjectList, state.getStatusList());
	}

	public List<TransportationRequest> findLightByRequester(String username, TransportationRequestStatus status) {
		return repos.findLightByRequester(username, status);
	}

	@Cacheable("transportationRequestService.countByRequester")
	public Long countByRequester(String username, TransportationRequestStatus status) {
		return repos.countByRequester(username, status);
	}

	public List<TransportationRequest> findLightByProjectManager(String username, TransportationRequestStatus status) {
		return repos.findLightByProjectManager(username, status);
	}

	@Cacheable("transportationRequestService.countByProjectManager")
	public Long countByProjectManager(String username, TransportationRequestStatus status) {
		return repos.countByProjectManager(username, status);
	}

	public TransportationRequest findByDeliveryRequest(Integer deliveryRequestId) {
		return repos.findByDeliveryRequest(deliveryRequestId);
	}

	public Integer findIdByDeliveryRequest(Integer deliveryRequestId) {
		return repos.findIdByDeliveryRequest(deliveryRequestId);
	}

	public void calculateEstimatedDistanceAndDuration(TransportationRequest transportationRequest) {
		Path path = PathService.getNewPath(transportationRequest.getOriginPlace(), transportationRequest.getDestinationPlace());
		transportationRequest.setEstimatedDuration(path.getEstimatedDuration());
		transportationRequest.setEstimatedDurationText(path.getEstimatedDurationText());
		transportationRequest.setEstimatedDistance(path.getEstimatedDistance());
		transportationRequest.setEstimatedDistanceText(path.getEstimatedDistanceText());
	}

	public void updatePaymentStatus() {
		List<Integer> idList = repos.findIdList(TransportationJobStatus.ACKNOWLEDGED);
		for (Integer id : idList)
			updatePaymentStatus(id);
	}

	public void updatePaymentStatus(Integer transportationRequestId) {
		Double totalAppLinkCost = repos.getTotalAppLinkCost(transportationRequestId);
		if (TransportationRequestPaymentStatus.PAYMENT_CONFIRMED.equals(repos.getPaymentStatus(transportationRequestId)))
			return;
		TransportationRequestPaymentStatus paymentStatus = null;
		if (TransportationJobStatus.ACKNOWLEDGED.equals(repos.getTransportationJobStatus(transportationRequestId))) {
			if (UtilsFunctions.compareDoubles(totalAppLinkCost, 0.0) == 0)
				paymentStatus = TransportationRequestPaymentStatus.PENDING;
			else {
				Double cost = repos.getCost(transportationRequestId);
				if (UtilsFunctions.compareDoubles(totalAppLinkCost, 0.0) > 0 && UtilsFunctions.compareDoubles(totalAppLinkCost, cost) < 0)
					paymentStatus = TransportationRequestPaymentStatus.PAYMENT_IN_PROGRESS;
				else if (UtilsFunctions.compareDoubles(totalAppLinkCost, cost) == 0) {
					Long a = acceptanceService.countPendingAcceptances(transportationRequestId);
					Long e = expensepaymentService.countNotAcknowledgedExpensepayments(transportationRequestId);
					if (a + e > 0)
						paymentStatus = TransportationRequestPaymentStatus.PAYMENT_PENDING;
					else
						paymentStatus = TransportationRequestPaymentStatus.PAYMENT_CONFIRMED;
				}
			}
		}

		System.out.println("update\t" + transportationRequestId + "\tto\t" + paymentStatus);
		repos.updatePaymentStatus(transportationRequestId, paymentStatus);
	}

	public TransportationRequest rejectTransportationRequest(TransportationRequest transportationRequest, User connectedUser, String reason) {
		transportationRequest.setStatus(TransportationRequestStatus.REJECTED);
		transportationRequest.setDate8(new Date());
		transportationRequest.setUser8(connectedUser);

		transportationRequestHistoryService.rejectedNew(transportationRequest, connectedUser, reason);
		transportationRequestService.save(transportationRequest);
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		return transportationRequest;
	}

	public TransportationRequest cancelTransportationRequest(Integer id, User connectedUser, String reason) {
		TransportationRequest transportationRequest = findOne(id);
		transportationRequest.setStatus(TransportationRequestStatus.CANCELED);
		transportationRequest.setDate9(new Date());
		transportationRequest.setUser9(connectedUser);
		transportationRequestHistoryService.canceledNew(transportationRequest, connectedUser, reason);
		transportationRequestService.save(transportationRequest);
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		return transportationRequest;
	}

	public String generateEmailNotification(TransportationRequest transportationRequest, String dearFullName) {
		Map<String, String> params = new HashMap<>();
		params.put("reference", transportationRequest.getReference());
		params.put("status", transportationRequest.getStatus().getValue());
		params.put("priority", transportationRequest.getPriority().getValue());
		params.put("important", transportationRequest.getImportant() ? "Yes" : "No");
		params.put("origin", transportationRequest.getOriginName());
		params.put("destination", transportationRequest.getDestinationName());
		params.put("neededPickupDate", UtilsFunctions.getFormattedDateTime(transportationRequest.getNeededPickupDate()));
		params.put("neededDeliveryDate", UtilsFunctions.getFormattedDateTime(transportationRequest.getNeededDeliveryDate()));
		params.put("deliveryRequestReference", transportationRequest.getDeliveryRequestReference());
		params.put("deliveryRequestType", transportationRequest.getDeliveryRequestType().getValue());
		params.put("project", transportationRequest.getProjectName());
		params.put("smsRef", transportationRequest.getSmsRef());
		params.put("grossWeight", UtilsFunctions.formatDouble(transportationRequest.getGrossWeight()) + " Kg");
		params.put("netWeight", UtilsFunctions.formatDouble(transportationRequest.getNetWeight()) + " Kg");
		params.put("volume", UtilsFunctions.formatDouble(transportationRequest.getVolume()) + " m3");
		params.put("numberOfItems", "" + transportationRequest.getNumberOfItems());
		params.put("estimatedDistance", transportationRequest.getEstimatedDistanceText());
		params.put("estimatedDuration", transportationRequest.getEstimatedDurationText());
		params.put("transporter", transportationRequest.getTransporterName());
		params.put("driverAndVehicle", transportationRequest.getDriverName() + ", " + transportationRequest.getDriverPhone() + " / " + transportationRequest.getVehicleMatricule());
		params.put("expectedPickupTime", UtilsFunctions.getFormattedDateTime(transportationRequest.getExpectedPickupDate()));
		params.put("pickupTime", UtilsFunctions.getFormattedDateTime(transportationRequest.getPickupDate()));
		params.put("expectedDeliveryTime", UtilsFunctions.getFormattedDateTime(transportationRequest.getExpectedDeliveryDate()));
		params.put("deliveryTime", UtilsFunctions.getFormattedDateTime(transportationRequest.getDeliveryDate()));
		params.put("transportationJob", transportationRequest.getTransportationJobNumero());
		params.put("transportationJobStatus", transportationRequest.getTransportationJobStatus() != null ? transportationRequest.getTransportationJobStatus().getValue() : "");

		params.put("statusColorCode", transportationRequest.getStatus().getColorCode());
		params.put("typeColorCode", transportationRequest.getDeliveryRequestType().getColorCode());
		params.put("priorityColorCode", transportationRequest.getPriority().getColorCode());
		params.put("timeLineImage", getTimeLineImage(transportationRequest));
		params.put("dearFullName", dearFullName);

		// TransportationRequestStatus.REQUESTED.equals(transportationRequest.getStatus())
		// ?
		// transportationRequest.getDeliveryRequest().getProject().getManager().getFullname()
		// : transportationRequest.getDeliveryRequest().getRequester().getFullName()

		String mailBody = fileReaderService.readFile("classpath:mail/tr.html", params);
		// System.out.println(mailBody);
		return mailBody;
	}

	private String getTimeLineImage(TransportationRequest transportationRequest) {
		String path = "http://www.3gcom.ma/erp/ilogistics/tr/";
		switch (transportationRequest.getStatus()) {
		case EDITED:
		case REQUESTED:
		case APPROVED:
		case ASSIGNED:
		case PICKEDUP:
		case DELIVERED:
		case ACKNOWLEDGED:
			return path + (transportationRequest.getStatus().ordinal() + 1) + ".png";
		default:
			break;
		}
		return null;
	}

	// mobile
	public List<ma.azdad.mobile.model.TransportationRequest> findByTmMobile() {
		List<TransportationRequest> list = repos.findLight();
		List<ma.azdad.mobile.model.TransportationRequest> mbList = new ArrayList<>();
		for (TransportationRequest tj : list) {

			mbList.add(new ma.azdad.mobile.model.TransportationRequest(tj.getId(), tj.getReference(), tj.getStatus(), tj.getNeededPickupDate(), tj.getNeededDeliveryDate(), tj.getExpectedPickupDate(),
					tj.getPickupDate(), tj.getExpectedDeliveryDate(), tj.getDeliveryDate(), tj.getOriginName(), tj.getDestinationName()));

		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationRequest> findByTmMobile(List<TransportationRequestStatus> status) {
		List<TransportationRequest> list = repos.findLight(status);
		List<ma.azdad.mobile.model.TransportationRequest> mbList = new ArrayList<>();
		for (TransportationRequest tj : list) {

			mbList.add(new ma.azdad.mobile.model.TransportationRequest(tj.getId(), tj.getReference(), tj.getStatus(), tj.getNeededPickupDate(), tj.getNeededDeliveryDate(), tj.getExpectedPickupDate(),
					tj.getPickupDate(), tj.getExpectedDeliveryDate(), tj.getDeliveryDate(), tj.getOriginName(), tj.getDestinationName()));

		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationRequest> findByDriverMobile(List<TransportationRequestStatus> status, String username) {
		List<TransportationRequest> list = repos.findLightByDriver(status, username);
		List<ma.azdad.mobile.model.TransportationRequest> mbList = new ArrayList<>();
		for (TransportationRequest tj : list) {

			mbList.add(new ma.azdad.mobile.model.TransportationRequest(tj.getId(), tj.getReference(), tj.getStatus(), tj.getNeededPickupDate(), tj.getNeededDeliveryDate(), tj.getExpectedPickupDate(),
					tj.getPickupDate(), tj.getExpectedDeliveryDate(), tj.getDeliveryDate(), tj.getOriginName(), tj.getDestinationName()));

		}

		return mbList;
	}

	public List<ma.azdad.mobile.model.TransportationRequest> findByTmMobileByStatus(Integer state) {
		switch (state) {
		case 0:
			return findByTmMobile(Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.APPROVED));
		case 1:
			return findByTmMobile(Arrays.asList(TransportationRequestStatus.ASSIGNED));
		case 2:
			return findByTmMobile(Arrays.asList(TransportationRequestStatus.PICKEDUP));
		case 3:
			return findByTmMobile(Arrays.asList(TransportationRequestStatus.DELIVERED, TransportationRequestStatus.ACKNOWLEDGED));
		case 4:
			return findByTmMobile(Arrays.asList(TransportationRequestStatus.CANCELED));
		case 5:
			return findByTmMobile();

		default:
			return new ArrayList<>();
		}
	}

	public List<ma.azdad.mobile.model.TransportationRequest> findByDriverMobileByStatus(Integer state, String username) {
		switch (state) {
		case 0:
			return findByDriverMobile(Arrays.asList(TransportationRequestStatus.ASSIGNED), username);
		case 2:
			return findByDriverMobile(Arrays.asList(TransportationRequestStatus.PICKEDUP), username);
		case 3:
			return findByDriverMobile(Arrays.asList(TransportationRequestStatus.DELIVERED, TransportationRequestStatus.ACKNOWLEDGED), username);

		default:
			return new ArrayList<>();
		}
	}

	public ma.azdad.mobile.model.TransportationRequest findOneMobile(Integer id) {
		TransportationRequest tr = findOne(id);
		ma.azdad.mobile.model.TransportationRequest trMobile = new ma.azdad.mobile.model.TransportationRequest(tr.getId(), tr.getReference(), tr.getStatus(), tr.getPriority(),
				tr.getDeliveryRequest().getNumberOfItems(), tr.getDeliveryRequest().getGrossWeight(), tr.getDeliveryRequest().getVolume(), tr.getNeededPickupDate(), tr.getNeededDeliveryDate(),
				tr.getExpectedPickupDate(), tr.getPickupDate(), tr.getExpectedDeliveryDate(), tr.getDeliveryDate());

		if (tr.getEstimatedDistance() != null) {
			trMobile.setEstimatedDistanceText(tr.getEstimatedDistanceText());
		}
		if (tr.getTransportationJob().getVehicle() != null) {
			trMobile.setVehicule(new Vehicule(tr.getTransportationJob().getVehicleId(), tr.getTransportationJob().getVehicleTypeName(), tr.getTransportationJob().getVehicleMatricule()));
		}
		if (tr.getTransportationJob().getDriver() != null) {
			trMobile.setDriver(toMobileUser2(tr.getTransportationJob().getDriver()));
		}
		if (tr.getDeliveryRequest() != null) {
			trMobile.setDnRef(tr.getDeliveryRequestReference());
			trMobile.setDnType(tr.getDeliveryRequestType().getValue());
			trMobile.setDnProject(tr.getDeliveryRequest().getProjectName());
		}

		if (tr.getDeliveryRequest().getOrigin() != null) {
			trMobile.setOriginName(tr.getDeliveryRequest().getOriginName());
			trMobile.setOriginAddress(tr.getDeliveryRequest().getOrigin().getGoogleAddress());
			trMobile.setOriginOwner(tr.getDeliveryRequest().getOrigin().getOwnerName());
			trMobile.setOriginPhone(tr.getDeliveryRequest().getOrigin().getPhone());
			trMobile.setOriginPhoto(Public.getPublicUrl(tr.getDeliveryRequest().getOrigin().getUser().getPhoto()));
			trMobile.setOriginLongitude(tr.getDeliveryRequest().getOrigin().getLongitude());
			trMobile.setOriginLatitude(tr.getDeliveryRequest().getOrigin().getLatitude());
		}
		if (tr.getDeliveryRequest().getDestination() != null) {
			trMobile.setDestinationName(tr.getDeliveryRequest().getDestinationName());
			trMobile.setDestinationAddress(tr.getDeliveryRequest().getDestination().getGoogleAddress());
			trMobile.setDestinationOwner(tr.getDeliveryRequest().getDestination().getOwnerName());
			trMobile.setDestinationPhone(tr.getDeliveryRequest().getDestination().getPhone());
			trMobile.setDestinationPhoto(Public.getPublicUrl(tr.getDeliveryRequest().getDestination().getUser().getPhoto()));
			trMobile.setDestinationLongitude(tr.getDeliveryRequest().getDestination().getLongitude());
			trMobile.setDestinationLatitude(tr.getDeliveryRequest().getDestination().getLatitude());
		}
		if (tr.getUser1() != null) {
			trMobile.setUser1(toMobileUser(tr.getUser1()));
			trMobile.setDate1(tr.getDate1());
		}
		if (tr.getUser2() != null) {
			trMobile.setUser2(toMobileUser(tr.getUser2()));
			trMobile.setDate2(tr.getDate2());
		}
		if (tr.getUser3() != null) {
			trMobile.setUser3(toMobileUser(tr.getUser3()));
			trMobile.setDate3(tr.getDate3());
		}
		if (tr.getUser4() != null) {
			trMobile.setUser4(toMobileUser(tr.getUser4()));
			trMobile.setDate4(tr.getDate4());
		}
		if (tr.getUser5() != null) {
			trMobile.setUser5(toMobileUser(tr.getUser5()));
			trMobile.setDate5(tr.getDate5());
		}
		if (tr.getUser6() != null) {
			trMobile.setUser6(toMobileUser(tr.getUser6()));
			trMobile.setDate6(tr.getDate6());
		}
		if (tr.getUser7() != null) {
			trMobile.setUser7(toMobileUser(tr.getUser7()));
			trMobile.setDate7(tr.getDate7());
		}
		if (tr.getUser8() != null) {
			trMobile.setUser8(toMobileUser(tr.getUser8()));
			trMobile.setDate8(tr.getDate8());
		}
		if (tr.getUser9() != null) {
			trMobile.setUser9(toMobileUser(tr.getUser9()));
			trMobile.setDate9(tr.getDate9());
		}

		trMobile.setHistoryList(repos.findHistoryListMobile(id));

		return trMobile;
	}

	private ma.azdad.mobile.model.User toMobileUser(User user) {
		return new ma.azdad.mobile.model.User(user.getUsername(), user.getFirstName(), user.getLastName(), user.getLogin(), user.getPhoto(), user.getEmail(), user.getJob());
	}

	private ma.azdad.mobile.model.User toMobileUser2(User user) {
		return new ma.azdad.mobile.model.User(user.getUsername(), user.getFirstName(), user.getLastName(), user.getLogin(), user.getPhoto(), user.getEmail(), user.getCin(), user.getPhone());
	}

	public void handleFileUpload(FileUploadEvent event, User user, Integer id, String fileType) throws IOException {
		TransportationRequest job = findOne(id);

		File file = fileUploadService.handleFileUploadMobile(event, "transportationRequest");
		TransportationRequestFile modelFile = new TransportationRequestFile(file, fileType, event.getFile().getFileName(), user);

		modelFile.setParent(job);
		transportationRequestFileRepos.save(modelFile);
		job = saveAndRefresh(job);

	}

	public void deleteFile(Integer idDn, Integer idFile) {
		TransportationRequest job = findOne(idDn);
		TransportationRequestFile file = transportationRequestFileRepos.findById(idFile).get();

		try {
			transportationRequestFileRepos.delete(file);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Iterator<TransportationRequestFile> i = job.getFileList().iterator();
		while (i.hasNext()) {
			TransportationRequestFile current = i.next();
			if (current.getId().equals(file.getId())) {
				job.getFileList().remove(current);
				break;
			}
		}
		job = saveAndRefresh(job);
	}

	public List<ma.azdad.mobile.model.TransportationRequestFile> findTrAttachments(Integer id) {
		TransportationRequest job = findOne(id);
		List<ma.azdad.mobile.model.TransportationRequestFile> list = new ArrayList<>();
		for (TransportationRequestFile dnFile : job.getFileList()) {
			list.add(new ma.azdad.mobile.model.TransportationRequestFile(dnFile.getId(), dnFile.getDate(), dnFile.getLink(), dnFile.getExtension(), dnFile.getType(), dnFile.getSize(),
					dnFile.getName()));

		}
		return list;
	}

	public String getContentTypeFromUrl(String url) {
		// You can enhance this with more comprehensive detection
		url = url.toLowerCase();
		if (url.contains(".png"))
			return "image/png";
		if (url.contains(".gif"))
			return "image/gif";
		if (url.contains(".webp"))
			return "image/webp";
		return "image/jpeg"; // default
	}

	public String uploadTaskDetailPhoto(TransportationRequest tr, String type, InputStream inputStream, String fileName, Long fileSize, Boolean showFacesMessage) throws IOException {

		File file = fileUploadService.handlePhotoUpload(inputStream, fileName, fileSize, "transportationRequestImage", 400 * 1024, showFacesMessage);
		TransportationRequestImage trm = new TransportationRequestImage(type, "files/transportationRequestImage/" + file.getName(), tr);

		TransportationRequestImage newTrm = transportationRequestImageRepos.save(trm);
		return newTrm.getValue();
	}

	public List<ma.azdad.mobile.model.TransportationRequestImage> findTrImages(String type, Integer id) {
		List<TransportationRequestImage> list = transportationRequestImageRepos.findByTypeAndTransportationRequestId(type, id);
		List<ma.azdad.mobile.model.TransportationRequestImage> mbList = new ArrayList<>();
		for (TransportationRequestImage trm : list) {
			mbList.add(new ma.azdad.mobile.model.TransportationRequestImage(trm.getId(), trm.getType(), trm.getValue(), trm.getLatitude(), trm.getLongitude(), trm.getGoogleAddress(),
					trm.getTakenDate(), trm.getPhoneModel()));
		}
		return mbList;
	}
}
