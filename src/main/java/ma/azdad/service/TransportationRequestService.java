package ma.azdad.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.Path;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestPaymentStatus;
import ma.azdad.model.TransportationRequestState;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.model.User;
import ma.azdad.repos.TransportationRequestRepos;

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
	TransportationRequestService transportationRequestService;

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

	@Cacheable("transportationRequestService.count")
	public Long count(TransportationRequestStatus status, Boolean isTM) {
		if (!isTM)
			return 0l;
		return repos.count(status);
	}

	public List<TransportationRequest> findByPaymentStatus(TransportationRequestPaymentStatus paymentStatus, Boolean isTm, String username) {
		if (paymentStatus == null)
			return isTm ? repos.findByPaymentStatus(TransportationJobStatus.CLOSED) : repos.findByPaymentStatus(TransportationJobStatus.CLOSED, username);
		else
			return isTm ? repos.findByPaymentStatus(TransportationJobStatus.CLOSED, paymentStatus)
					: repos.findByPaymentStatus(TransportationJobStatus.CLOSED, paymentStatus, username);

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
		List<Integer> idList = repos.findIdList(TransportationJobStatus.CLOSED);
		for (Integer id : idList)
			updatePaymentStatus(id);
	}

	public void updatePaymentStatus(Integer transportationRequestId) {
		Double totalAppLinkCost = repos.getTotalAppLinkCost(transportationRequestId);
		if (TransportationRequestPaymentStatus.PAYMENT_CONFIRMED.equals(repos.getPaymentStatus(transportationRequestId)))
			return;
		TransportationRequestPaymentStatus paymentStatus = null;
		if (TransportationJobStatus.CLOSED.equals(repos.getTransportationJobStatus(transportationRequestId))) {
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
}
