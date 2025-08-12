package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.map.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.ContactType;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.GenericPlace;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestFile;
import ma.azdad.model.TransportationRequestPaymentStatus;
import ma.azdad.model.TransportationRequestState;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.repos.TransportationRequestRepos;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.ExternalResourceService;
import ma.azdad.service.MapService;
import ma.azdad.service.OldEmailService;
import ma.azdad.service.SmsService;
import ma.azdad.service.TransportationRequestFileService;
import ma.azdad.service.TransportationRequestHistoryService;
import ma.azdad.service.TransportationRequestService;
import ma.azdad.service.TransporterService;
import ma.azdad.service.UserService;
import ma.azdad.service.VehicleService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class TransportationRequestView extends GenericView<Integer, TransportationRequest, TransportationRequestRepos, TransportationRequestService> {

	@Autowired
	protected TransportationRequestService transportationRequestService;

	@Autowired
	protected TransportationRequestHistoryService transportationRequestHistoryService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected SessionView sessionView;

	@Autowired
	protected UserService userService;

	@Autowired
	protected ExternalResourceService externalResourceService;

	@Autowired
	protected TransportationRequestFileService transportationRequestFileService;

	@Autowired
	protected DeliveryRequestService deliveryRequestService;

	@Autowired
	protected MapService mapService;

	@Autowired
	protected TransporterService transporterService;

	@Autowired
	protected VehicleService vehicleService;

	@Autowired
	protected FileUploadView fileUploadView;

	@Autowired
	protected OldEmailService emailService;

	@Autowired
	protected SmsService smsService;

	private TransportationRequest transportationRequest = new TransportationRequest();
	private TransportationRequestFile transportationRequestFile;

	private Integer deliveryRequestId;

	private MapModel mapModel;

	private TransportationRequestState state;
	private TransportationRequestPaymentStatus paymentStatus;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		initParameters();

		if (isListPage)
			refreshList();
		else if (isAddPage) {
			if (deliveryRequestId != null) {
				DeliveryRequest dr = deliveryRequestService.findOne(deliveryRequestId);
				if (deliveryRequestService.canAddTrasnport(dr, sessionView.getUsername())) {
					transportationRequest.setDeliveryRequestId(deliveryRequestId);
					transportationRequest.setDeliveryRequest(dr);
				}
			}
		} else if (isEditPage) {
			transportationRequest = transportationRequestService.findOne(id);
			transportationRequest.init();
		}

		else if (isViewPage) {
			transportationRequest = transportationRequestService.findOne(id);
			transportationRequest.init();
			generateMap(transportationRequest);
		}

	}

	@Override
	public void initParameters() {
		super.initParameters();
		try {
			deliveryRequestId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("deliveryRequestId"));
		} catch (Exception e) {
			deliveryRequestId = null;
		}

		try {
			state = TransportationRequestState.values()[Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("state"))];
		} catch (Exception e) {
			state = null;
		}

		try {
			pageIndex = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pageIndex"));
		} catch (Exception e) {
			pageIndex = null;
		}

	}

	@Override
	public void refreshList() {
		if (isListPage)
			switch (pageIndex) {
			case 1:
				if(sessionView.getIsInternalTM())
					initLists(service.find(state));
				else if (sessionView.getInternal() || sessionView.getIsCustomerUser())
					list2 = list1 = transportationRequestService.findLight(sessionView.getUsername(), state, cacheView.getAssignedProjectList(), sessionView.isTM());
				else if (sessionView.getIsSupplierUser())
					list2 = list1 = transportationRequestService.findLightBySupplierUser(state, sessionView.getUser().getSupplierId(), cacheView.getAssignedProjectList());
				break;
			case 2:
				list2 = list1 = transportationRequestService.findLightByRequester(sessionView.getUsername(), TransportationRequestStatus.DELIVERED);
				break;
			case 3:
				list2 = list1 = transportationRequestService.findLightByProjectManager(sessionView.getUsername(), TransportationRequestStatus.REQUESTED);
				break;
			case 4:
				list2 = list1 = transportationRequestService.findLight(TransportationRequestStatus.APPROVED, sessionView.isTM());
				break;
			case 5:
				list2 = list1 = transportationRequestService.findLight(TransportationRequestStatus.ASSIGNED, sessionView.isTM());
				break;
			case 6:
				list2 = list1 = transportationRequestService.findLight(TransportationRequestStatus.PICKEDUP, sessionView.isTM());
				break;
			case 7:
				list2 = list1 = transportationRequestService.findByPaymentStatus(paymentStatus, sessionView.isTM(), sessionView.getUsername());
			default:
				break;
			}
	}

	public void refreshTransportationRequest() {
		transportationRequestService.flush();
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
	}

	public void setDeliveryRequest() {
		transportationRequest.setDeliveryRequest(deliveryRequestService.findOne(transportationRequest.getDeliveryRequestId()));
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (isViewPage) {
			Boolean test = sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getRequester());
			test = test || cacheView.getAssignedProjectList().contains(transportationRequest.getDeliveryRequest().getProject().getId());
			test = test || cacheView.getDelegatedProjectList().contains(transportationRequest.getDeliveryRequest().getProject().getId());
			test = test || sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getProject().getManager().getUsername());
			test = test || (transportationRequest.getDeliveryRequest().getWarehouse() != null
					&& cacheView.getWarehouseList().contains(transportationRequest.getDeliveryRequest().getWarehouse().getId()));
			test = test || sessionView.isTM();
			test = test || sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getProject().getCostcenter().getLob().getManager().getUsername());
			if (!test)
				cacheView.accessDenied();
		}
	}

	// SAVE TRANSPORTATIONREQUEST
	public Boolean canSaveTransportationRequest() {
		if (isListPage || isAddPage)
			return sessionView.isUser() || sessionView.getIsPm();
		else if (isViewPage || isEditPage)
			return ( sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getRequester()) || sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getProject().getManager())  ) 
					&& Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REJECTED, TransportationRequestStatus.CANCELED)
							.contains(transportationRequest.getStatus());
		return false;
	}

	public String saveTransportationRequest() {
		if (!canSaveTransportationRequest())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateTransportationRequest())
			return null;

		transportationRequest.clearTimeLine();
		transportationRequest.setStatus(TransportationRequestStatus.EDITED);
		transportationRequest.setDate1(new Date());

		if (transportationRequest.getContact1Username() != null)
			transportationRequest.setContact1(userService.findOne(transportationRequest.getContact1Username()));

		if (transportationRequest.getContact2Username() != null)
			transportationRequest.setContact2(userService.findOne(transportationRequest.getContact2Username()));

		if (ContactType.EXTERNAL.equals(transportationRequest.getContactType1()))
			transportationRequest.setContact1(null);

		if (ContactType.EXTERNAL.equals(transportationRequest.getContactType2()))
			transportationRequest.setContact2(null);

		transportationRequestService.calculateEstimatedDistanceAndDuration(transportationRequest);

		if (transportationRequest.getDeliveryRequest().getTransporter() != null)
			transportationRequest.setTransporter(transportationRequest.getDeliveryRequest().getTransporter());

		if (transportationRequest.getReference() == null)
			transportationRequest.generateReference();

		transportationRequest = transportationRequestService.save(transportationRequest);

		if (!isEditPage)
			transportationRequestHistoryService.created(transportationRequest,sessionView.getUser());
		else
			transportationRequestHistoryService.edited(transportationRequest,sessionView.getUser());

		return addParameters(viewPage, "faces-redirect=true", "id=" + transportationRequest.getId());
	}

	public Boolean validateTransportationRequest() {
		if (transportationRequest.getNeededPickupDate().compareTo(transportationRequest.getNeededDeliveryDate()) > 0) {
			FacesContextMessages.ErrorMessages("Expected Pickup Time should be lower than Needed Delivery Date");
			return false;
		}

		return true;
	}

	/*
	 * WORKFLOW
	 */
	public Boolean canRequestTransportationRequest() {
		return TransportationRequestStatus.EDITED.equals(transportationRequest.getStatus()) //
				&& (sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getRequester()) || sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getProject().getManager())) //
				&& Arrays.asList(DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2, DeliveryRequestStatus.DELIVRED,
						DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED).contains(transportationRequest.getDeliveryRequest().getStatus());
	}

	public void requestTransportationRequest() {
		if (!canRequestTransportationRequest())
			return;

		transportationRequest.setStatus(TransportationRequestStatus.REQUESTED);
		transportationRequest.setDate2(new Date());
		transportationRequestHistoryService.requestedNew(transportationRequest,sessionView.getUser());
		transportationRequestService.save(transportationRequest);
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());

		// emailService.transportationRequestNotification(transportationRequest);
		// smsService.sendSms(transportationRequest);
	}

	// APPROVE TRANSPORTATION REQUEST
	public Boolean canApproveTransportationRequest() {
		return canApproveTransportationRequest(transportationRequest);
	}

	public Boolean canApproveTransportationRequest(TransportationRequest transportationRequest) {
		return TransportationRequestStatus.REQUESTED.equals(transportationRequest.getStatus()) //
				&& (sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getProject().getManager().getUsername())
						|| cacheView.hasDelegation(transportationRequest.getDeliveryRequest().getProject().getId())) //
				&& Arrays.asList(DeliveryRequestStatus.APPROVED2, DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED)
						.contains(transportationRequest.getDeliveryRequest().getStatus());
	}

	public void approveTransportationRequest(TransportationRequest transportationRequest) {
		if (!canApproveTransportationRequest(transportationRequest))
			return;
		transportationRequest.setStatus(TransportationRequestStatus.APPROVED);
		transportationRequest.setDate3(new Date());
		transportationRequest.setUser3(sessionView.getUser());
		transportationRequestHistoryService.approvedNew(transportationRequest, sessionView.getUser());
		transportationRequestService.save(transportationRequest);
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());

		// emailService.transportationRequestNotification(transportationRequest);
		// smsService.sendSms(transportationRequest);
	}

	public void approveTransportationRequest() {
		if (isViewPage) {
			approveTransportationRequest(transportationRequest);
			// refreshTransportationRequest();
		} else if (pageIndex == 3) {
			for (TransportationRequest transportationRequest : list4)
				approveTransportationRequest(transportationRequestService.findOne(transportationRequest.getId()));
			refreshList();
		}
	}

	// ASSIGN TRANSPORTATION REQUEST
	public Boolean canAssignTransportationRequest() {
		return TransportationRequestStatus.APPROVED.equals(transportationRequest.getStatus()) && sessionView.isTM();
	}

	public void assignTransportationRequest() {
		if (!canAssignTransportationRequest())
			return;

		transportationRequest.setStatus(TransportationRequestStatus.ASSIGNED);
		transportationRequest.setDate4(new Date());
		transportationRequest.setUser4(sessionView.getUser());
		// transportationRequest.setTransporter(transporterService.findOne(transportationRequest.getTransporterId()));
		// transportationRequest.setVehicle(vehicleService.findOne(transportationRequest.getVehicleId()));
		// transportationRequest.setDriver(externalResourceService.findOne(transportationRequest.getDriverId()));
		transportationRequest = transportationRequestService.save(transportationRequest);
		transportationRequestHistoryService.assigned(transportationRequest, sessionView.getUser());

		refreshTransportationRequest();
	}

	// PICKUP TRANSPORTATION REQUEST
	public Boolean canPickupTransportationRequest() {
		return TransportationRequestStatus.ASSIGNED.equals(transportationRequest.getStatus()) && sessionView.isTM();
	}

	public void pickupTransportationRequest() {
		if (!canPickupTransportationRequest())
			return;
		if (!validatePickupTransportationRequest())
			return;

		transportationRequest.setStatus(TransportationRequestStatus.PICKEDUP);
		transportationRequest.setDate5(new Date());
		transportationRequest.setUser5(sessionView.getUser());
		// transportationRequest.setTransporter(transporterService.findOne(transportationRequest.getTransporterId()));
		// transportationRequest.setVehicle(vehicleService.findOne(transportationRequest.getVehicleId()));
		transportationRequest = transportationRequestService.save(transportationRequest);
		transportationRequestHistoryService.pickedup(transportationRequest, sessionView.getUser());
		refreshTransportationRequest();
	}

	public Boolean validatePickupTransportationRequest() {
		if (transportationRequest.getPickupDate().compareTo(transportationRequest.getExpectedDeliveryDate()) > 0) {
			FacesContextMessages.ErrorMessages("Expected Delivery Time should not be lower than Pickup Time");
			return false;
		}

		return true;
	}

	// DELIVER TRANSPORTATION REQUEST
	public Boolean canDeliverTransportationRequest() {
		return TransportationRequestStatus.PICKEDUP.equals(transportationRequest.getStatus()) //
				&& sessionView.isTM() //
				&& Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED)
						.contains(transportationRequest.getDeliveryRequest().getStatus());
	}

	public void deliverTransportationRequest() {
		if (!canDeliverTransportationRequest())
			return;

		transportationRequest.setStatus(TransportationRequestStatus.DELIVERED);
		transportationRequest.setDate6(new Date());
		transportationRequest.setUser6(sessionView.getUser());
		transportationRequest = transportationRequestService.save(transportationRequest);
		transportationRequestHistoryService.delivred(transportationRequest, sessionView.getUser());

		refreshTransportationRequest();
	}

	// ACKNOWLEDGE TRANSPORTATION REQUEST
	public Boolean canAcknowledgeTransportationRequest() {
		return canAcknowledgeTransportationRequest(transportationRequest);
	}

	public Boolean canAcknowledgeTransportationRequest(TransportationRequest transportationRequest) {
		return TransportationRequestStatus.DELIVERED.equals(transportationRequest.getStatus())
				&& sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getRequester());
	}

	public void acknowledgeTransportationRequest(TransportationRequest transportationRequest) {
		if (!canAcknowledgeTransportationRequest(transportationRequest))
			return;

		transportationRequest.setStatus(TransportationRequestStatus.ACKNOWLEDGED);
		transportationRequest.setDate7(new Date());
		transportationRequest.setUser7(sessionView.getUser());
		transportationRequestHistoryService.acknowledgedNew(transportationRequest);
		transportationRequestService.save(transportationRequest);
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
	}

	public void acknowledgeTransportationRequest() {
		if (isViewPage) {
			acknowledgeTransportationRequest(transportationRequest);
			// refreshTransportationRequest();
		} else if (pageIndex == 2) {
			for (TransportationRequest transportationRequest : list4)
				acknowledgeTransportationRequest(transportationRequestService.findOne(transportationRequest.getId()));
			refreshList();
		}
	}

	// REJECT TRANSPORTATION REQUEST
	public Boolean canRejectTransportationRequest() {
		return canRejectTransportationRequest(transportationRequest);
	}

	public Boolean canRejectTransportationRequest(TransportationRequest transportationRequest) {
		return TransportationRequestStatus.REQUESTED.equals(transportationRequest.getStatus())
				&& (sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getProject().getManager().getUsername())
						|| cacheView.hasDelegation(transportationRequest.getDeliveryRequest().getProject().getId()));
	}

	public void rejectTransportationRequest(TransportationRequest transportationRequest) {
		if (!canRejectTransportationRequest(transportationRequest))
			return;
		transportationRequest = transportationRequestService.rejectTransportationRequest(transportationRequest, sessionView.getUser(), transportationRequest.getRejectionReason());
	}

	public void rejectTransportationRequest() {
		if (isViewPage) {
			rejectTransportationRequest(transportationRequest);
			// refreshTransportationRequest();
		} else if (pageIndex == 3) {
			for (TransportationRequest transportationRequest : list4)
				rejectTransportationRequest(transportationRequestService.findOne(transportationRequest.getId()));
			refreshList();
		}
	}

	// CANCEL DELIVERY REQUEST
	public Boolean canCancelTransportationRequest() {
		return Arrays.asList(TransportationRequestStatus.EDITED, TransportationRequestStatus.REQUESTED, TransportationRequestStatus.APPROVED)
				.contains(transportationRequest.getStatus())
				&& (sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getRequester())
						|| sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getProject().getManager().getUsername()));

	}

	public void cancelTransportationRequest() {
		if (!canCancelTransportationRequest())
			return;
		transportationRequest = transportationRequestService.cancelTransportationRequest(transportationRequest.getId(), sessionView.getUser(), null);
		// refreshTransportationRequest();
	}

	// CALCULATE ESTIMATED DISTANCE & DURATION
	// private void calculateEstimatedDistanceAndDuration() {
	// JSONObject json;
	// try {
	// json =
	// JsonReader.readJsonFromUrl("https://maps.googleapis.com/maps/api/distancematrix/json?origins="
	// + transportationRequest.getOriginValue() + "&destinations="
	// + transportationRequest.getDestinationValue() +
	// "&key=AIzaSyCsA6hQ1C8D6IIeB_r2WDEEgPelvpUWIf8");
	// JSONObject firstRow =
	// json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
	// System.out.println(firstRow);
	// System.out.println(firstRow.getJSONObject("duration").get("text"));
	// System.out.println(firstRow.getJSONObject("duration").get("value"));
	// System.out.println(firstRow.getJSONObject("distance").get("text"));
	// System.out.println(firstRow.getJSONObject("distance").get("value"));
	// transportationRequest.setEstimatedDuration(Double.valueOf(firstRow.getJSONObject("duration").get("value").toString()));
	// transportationRequest.setEstimatedDurationText(firstRow.getJSONObject("duration").get("text").toString());
	// transportationRequest.setEstimatedDistance(Double.valueOf(firstRow.getJSONObject("distance").get("value").toString())
	// / 1000.0);
	// transportationRequest.setEstimatedDistanceText(firstRow.getJSONObject("distance").get("text").toString());
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	// DELETE TRANSPORTATIONREQUEST
	public Boolean canDeleteTransportationRequest() {
		return TransportationRequestStatus.EDITED.equals(transportationRequest.getStatus())
				&& sessionView.isTheConnectedUser(transportationRequest.getDeliveryRequest().getRequester());
	}

	public String deleteTransportationRequest() {
		if (canDeleteTransportationRequest())
			try {
				transportationRequestService.delete(transportationRequest);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}
		return addParameters(listPage, "faces-redirect=true");
	}

	// FILES MANAGEMENT
	private String transportationRequestFileType;
	private Integer transportationRequestFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		TransportationRequestFile transportationRequestFile = new TransportationRequestFile(file, transportationRequestFileType, event.getFile().getFileName(),
				sessionView.getUser(), transportationRequest);
		transportationRequestFileService.save(transportationRequestFile);
		synchronized (TransportationRequestView.class) {
			refreshTransportationRequest();
		}
	}

	public void deleteTransportationRequestFile() {
		try {
			transportationRequestFileService.delete(transportationRequestFileId);
			refreshTransportationRequest();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}
	}

	/*
	 * MAP
	 */

	public void generateMap(TransportationRequest transportationRequest) {
		System.out.println("generate from transportationRequest !");

		GenericPlace origin = !transportationRequest.getDeliveryRequest().getIsOutbound() ? transportationRequest.getDeliveryRequest().getOrigin()
				: transportationRequest.getDeliveryRequest().getWarehouse();
		GenericPlace destination = !transportationRequest.getDeliveryRequest().getIsInbound() ? transportationRequest.getDeliveryRequest().getDestination()
				: transportationRequest.getDeliveryRequest().getWarehouse();

		mapModel = mapService.generate(origin, destination);
	}

	// generic

	public Long countToAcknowledgeRequests() {
		return transportationRequestService.countByRequester(sessionView.getUsername(), TransportationRequestStatus.DELIVERED);
	}

	public Long countToApproveRequests() {
		return transportationRequestService.countByProjectManager(sessionView.getUsername(), TransportationRequestStatus.REQUESTED);
	}

	public Long countToAssignRequests() {
		return transportationRequestService.count(TransportationRequestStatus.APPROVED, sessionView.isTM());
	}

	public Long countToPickupRequests() {
		return transportationRequestService.count(TransportationRequestStatus.ASSIGNED, sessionView.isTM());
	}

	public Long countToDeliverRequests() {
		return transportationRequestService.count(TransportationRequestStatus.PICKEDUP, sessionView.isTM());
	}

	@Cacheable("transportationRequestView.countToAdd")
	public Long countToAdd() {
		return deliveryRequestService.countByPendingTransportation(sessionView.getUsername());
	}

	@Cacheable("transportationRequestView.countTotal")
	public Long countTotal() {
		Long total = 0l;
		if (sessionView.getIsUser())
			total += countToAdd() + countToAcknowledgeRequests();
		if (sessionView.getIsPM())
			total += countToApproveRequests();
		if (sessionView.getIsTM())
			total += countToAssignRequests() + countToPickupRequests() + countToDeliverRequests();

		return total;
	}

	// GETTERS & SETTERS

	public TransportationRequestService getTransportationRequestService() {
		return transportationRequestService;
	}

	public void setTransportationRequestService(TransportationRequestService transportationRequestService) {
		this.transportationRequestService = transportationRequestService;
	}

	public TransportationRequest getTransportationRequest() {
		return transportationRequest;
	}

	public void setTransportationRequest(TransportationRequest transportationRequest) {
		this.transportationRequest = transportationRequest;
	}

	public FileUploadView getFileUploadView() {
		return fileUploadView;
	}

	public void setFileUploadView(FileUploadView fileUploadView) {
		this.fileUploadView = fileUploadView;
	}

	public TransportationRequestFileService getTransportationRequestFileService() {
		return transportationRequestFileService;
	}

	public void setTransportationRequestFileService(TransportationRequestFileService transportationRequestFileService) {
		this.transportationRequestFileService = transportationRequestFileService;
	}

	public String getTransportationRequestFileType() {
		return transportationRequestFileType;
	}

	public void setTransportationRequestFileType(String transportationRequestFileType) {
		this.transportationRequestFileType = transportationRequestFileType;
	}

	public Integer getTransportationRequestFileId() {
		return transportationRequestFileId;
	}

	public void setTransportationRequestFileId(Integer transportationRequestFileId) {
		this.transportationRequestFileId = transportationRequestFileId;
	}

	public TransportationRequestFile getTransportationRequestFile() {
		return transportationRequestFile;
	}

	public void setTransportationRequestFile(TransportationRequestFile transportationRequestFile) {
		this.transportationRequestFile = transportationRequestFile;
	}

	public Integer getDeliveryRequestId() {
		return deliveryRequestId;
	}

	public void setDeliveryRequestId(Integer deliveryRequestId) {
		this.deliveryRequestId = deliveryRequestId;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;

	}

	@Override
	public Integer getPageIndex() {
		return pageIndex;
	}

	@Override
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public TransportationRequestState getState() {
		return state;
	}

	public void setState(TransportationRequestState state) {
		this.state = state;
	}

	public TransportationRequestPaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(TransportationRequestPaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

}
