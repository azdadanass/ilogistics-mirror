package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.map.MapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.Path;
import ma.azdad.model.Stop;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobFile;
import ma.azdad.model.TransportationJobStatus;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestPaymentStatus;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.MapService;
import ma.azdad.service.OldEmailService;
import ma.azdad.service.PathService;
import ma.azdad.service.SmsService;
import ma.azdad.service.StopService;
import ma.azdad.service.TransportationJobFileService;
import ma.azdad.service.TransportationJobHistoryService;
import ma.azdad.service.TransportationJobService;
import ma.azdad.service.TransportationRequestHistoryService;
import ma.azdad.service.TransportationRequestService;
import ma.azdad.service.TransporterService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.service.VehicleService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class TransportationJobView extends GenericView<TransportationJob> {

	@Autowired
	protected TransportationJobService transportationJobService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected TransportationJobFileService transportationJobFileService;

	@Autowired
	protected TransportationRequestHistoryService transportationRequestHistoryService;

	@Autowired
	private TransportationJobHistoryService transportationJobHistoryService;

	@Autowired
	protected FileView fileView;

	@Autowired
	protected TransportationRequestService transportationRequestService;

	@Autowired
	protected DeliveryRequestService deliveryRequestService;

	@Autowired
	protected TransporterService transporterService;

	@Autowired
	protected VehicleService vehicleService;

	@Autowired
	protected StopService stopService;

	@Autowired
	protected PathService pathService;

	@Autowired
	protected MapService mapService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected OldEmailService emailService;

	@Autowired
	protected SmsService smsService;

	private TransportationJob transportationJob = new TransportationJob();
	private TransportationJobFile transportationJobFile;

	private List<TransportationRequest> transportationRequestList1 = new ArrayList<>();
	private List<TransportationRequest> transportationRequestList2 = new ArrayList<>();
	private List<TransportationRequest> transportationRequestList3; // selection

	private String assignPage = "assignTransportationRequestList.xhtml";
	private Boolean isAssignPage = false;

	private TransportationRequest transportationRequest;

	private MapModel mapModel;
	private Boolean viewPathList = false;
	private Boolean editCosts = false;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		isAssignPage = ("/" + assignPage).equals(currentPath);
		if (isListPage)
			refreshList();
		else if (isEditPage || isAssignPage) {
			transportationJob = transportationJobService.findOne(selectedId);
			transportationJob.init();
			refreshTransportationRequestList();
		} else if (isViewPage) {
			transportationJob = transportationJobService.findOne(selectedId);
			transportationJob.init();
			refreshMapMpdel();
		}

	}

	public void refreshMapMpdel() {
		mapModel = mapService.generate(transportationJob.getStopList(), viewPathList);
	}

	public void refreshList() {
		if (isListPage)
			if (pageIndex == null)
				list2 = list1 = transportationJobService.find();
			else
				switch (pageIndex) {
				case 1:
					list2 = list1 = transportationJobService.find(TransportationJobStatus.NOT_STARTED);
					break;
				case 2:
					list2 = list1 = transportationJobService.find(TransportationJobStatus.IN_PROGRESS);
					break;
				case 3:
					list2 = list1 = transportationJobService.find(Arrays.asList(TransportationJobStatus.COMPLETED, TransportationJobStatus.CLOSED));
					break;
				}
	}

	public void refreshTransportationJob() {
		transportationJobService.flush();
		transportationJob = transportationJobService.findOne(transportationJob.getId());
		transportationJob.init();
	}

	private void refreshTransportationRequestList() {
		transportationRequestList2 = transportationRequestList1 = transportationRequestService.findByNotHavingTransportationJob(TransportationRequestStatus.APPROVED, Arrays.asList(DeliveryRequestStatus.APPROVED, DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED));
	}

	/*
	 * Redirection
	 */
	public void redirect() {
		// if (false)
		// cacheView.accessDenied();
	}

	// SAVE TRANSPORTATIONJOB
	public Boolean canSaveTransportationJob() {
		if (isListPage || isAddPage)
			return sessionView.isTM();
		else if (isViewPage || isEditPage)
			return sessionView.isTM() && !TransportationJobStatus.CLOSED.equals(transportationJob.getStatus());
		return false;
	}

	public String saveTransportationJob() {
		if (!canSaveTransportationJob())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateTransportationJob())
			return null;

		transportationJob.setTransporter(transporterService.findOne(transportationJob.getTransporterId()));
		transportationJob.setVehicle(vehicleService.findOne(transportationJob.getVehicleId()));
		transportationJob.setDriver(userService.findOne(transportationJob.getDriverUsername()));
		if (isAddPage) {
			// tmp validation
			if (transportationJob.getVehicle().getVehicleType() == null || transportationJob.getVehicle().getVehicleType().getPrice() == null) {
				FacesContextMessages.ErrorMessages("VehicleType and VehicleType.price should not be null");
				return null;
			}
			transportationJob.setVehiclePrice(transportationJob.getVehicle().getVehicleType().getPrice());
		}

		if (isEditPage) {
			transportationJob.calculateEstimatedCost();
			transportationJobService.calculateTransportationRequestListCosts(transportationJob, true);
		}

		transportationJob = transportationJobService.save(transportationJob);

		if (!isEditPage)
			transportationJobHistoryService.created(transportationJob, sessionView.getUser());
		else
			transportationJobHistoryService.edited(transportationJob, sessionView.getUser());

		return addParameters(viewPage, "faces-redirect=true", "id=" + transportationJob.getId());
	}

	public Boolean validateTransportationJob() {
		return true;
	}

	public void updateCalculableFields() {
		transportationJobService.updateCalculableFields(transportationJob, true);
		transportationJob = transportationJobService.findOne(transportationJob.getId());
		transportationJob.init();
		// refreshTransportationJob();
	}

	public void updateCalculableFieldsOldddddd() {
		refreshTransportationJob();
		transportationJob.calculateStartDate();
		transportationJob.calculateEndDate();
		transportationJob.calculateStatus();
		transportationJobService.save(transportationJob);
		// clear path List
		for (Path path : transportationJob.getPathList())
			pathService.delete(path);
		transportationJob.getPathList().clear();
		// clear stop List
		for (Stop stop : transportationJob.getStopList())
			stopService.delete(stop);
		transportationJob.getStopList().clear();
		refreshTransportationJob();
		transportationJob.generateStopList();
		transportationJobService.save(transportationJob);
		refreshTransportationJob();
		transportationJob.generatePathList();
		transportationJob = transportationJobService.save(transportationJob);
		refreshTransportationJob();
		transportationJobService.calculateTransportationRequestListCosts(transportationJob, true);
	}

	public void updatePathList() {

	}

	// ASSIGN
	public Boolean canAssignTransportationRequestList() {
		return sessionView.isTM() && !TransportationJobStatus.CLOSED.equals(transportationJob.getStatus());
	}

	public String assignTransportationRequestList() {
		if (!canAssignTransportationRequestList())
			return null;
		if (!validateAssignTransportationRequestList())
			return null;
		Date currentDate = new Date();
		for (TransportationRequest tr : transportationRequestList3) {
			Date expectedPickupDate = tr.getExpectedPickupDate();
			Date expectedDeliveryDate = tr.getExpectedDeliveryDate();
			tr = transportationRequestService.findOne(tr.getId());
			tr.setExpectedPickupDate(expectedPickupDate);
			tr.setExpectedDeliveryDate(expectedDeliveryDate);
			tr.setStatus(TransportationRequestStatus.ASSIGNED);
			tr.setDate4(currentDate);
			tr.setUser4(sessionView.getUser());
			tr.setTransportationJob(transportationJob);
			transportationRequestHistoryService.assignedNew(tr, sessionView.getUser());
			transportationRequestService.save(tr);
			tr = transportationRequestService.findOne(tr.getId());
			emailService.transportationRequestNotification(tr);
			// smsService.sendSms(tr);
		}

		refreshTransportationJob();
		updateCalculableFields();

		return addParameters(viewPage, "faces-redirect=true", "id=" + transportationJob.getId());
	}

	public Boolean validateAssignTransportationRequestList() {
		for (TransportationRequest tr : transportationRequestList3) {
			if (tr.getExpectedPickupDate() == null || tr.getExpectedDeliveryDate() == null) {
				FacesContextMessages.ErrorMessages("Expected Pickup/Delivery Time should not be null");
				return false;
			}
			if (tr.getExpectedPickupDate().compareTo(tr.getExpectedDeliveryDate()) >= 0) {
				FacesContextMessages.ErrorMessages("Expected Pickup Time should be lower than Expected Delivery Time");
				return false;
			}
		}
		if (!transportationJobService.validateTransportationRequestListDates(transportationJob, transportationRequestList3)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		return true;
	}

	// REMOVE FROM TR JOB
	public Boolean canRemoveTransportationRequest(TransportationRequest transportationRequest) {
		return sessionView.isTM() && TransportationRequestStatus.ASSIGNED.equals(transportationRequest.getStatus());
	}

	public void removeTransportationRequest() {
		if (!canRemoveTransportationRequest(transportationRequest))
			return;
		transportationRequest.setStatus(TransportationRequestStatus.APPROVED);
		transportationRequest.setUser4(null);
		transportationRequest.setDate4(null);
		transportationRequest.setTransportationJob(null);
		transportationRequestService.save(transportationRequest);
		refreshTransportationJob();
		updateCalculableFields();
	}

	// UPDATE Expected Times
	public Boolean canUpdateExpectedPickupDate(TransportationRequest transportationRequest) {
		return sessionView.isTM() && transportationRequest.getPickupDate() == null;
	}

	public void updateExpectedPickupDate(TransportationRequest transportationRequest) {
		if (!canUpdateExpectedPickupDate(transportationRequest))
			return;
		if (!validateUpdateExpectedPickupDate(transportationRequest))
			return;

		Date expectedPickupDate = transportationRequest.getExpectedPickupDate();
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		transportationRequest.setExpectedPickupDate(expectedPickupDate);
		transportationRequest = transportationRequestService.save(transportationRequest);
		updateCalculableFields();
	}

	public Boolean validateUpdateExpectedPickupDate(TransportationRequest transportationRequest) {
		if (transportationRequest.getExpectedPickupDate().compareTo(transportationRequest.getExpectedDeliveryDate()) >= 0) {
			FacesContextMessages.ErrorMessages("Expected Pickup Time should be lower than Expected Delivery Time");
			return false;
		}
		if (!transportationJobService.validateTransportationRequestListDates(transportationJob)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		return true;
	}

	public Boolean canUpdateExpectedDeliveryDate(TransportationRequest transportationRequest) {
		return sessionView.isTM() && transportationRequest.getDeliveryDate() == null;
	}

	public void updateExpectedDeliveryDate(TransportationRequest transportationRequest) {
		if (!canUpdateExpectedDeliveryDate(transportationRequest))
			return;
		if (!validateUpdateExpectedDeliveryDate(transportationRequest))
			return;

		Date expectedDeliveryDate = transportationRequest.getExpectedDeliveryDate();
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		transportationRequest.setExpectedDeliveryDate(expectedDeliveryDate);
		transportationRequest = transportationRequestService.save(transportationRequest);

		updateCalculableFields();
	}

	public Boolean validateUpdateExpectedDeliveryDate(TransportationRequest transportationRequest) {
		if (transportationRequest.getPickupDate() == null) {
			if (transportationRequest.getExpectedPickupDate().compareTo(transportationRequest.getExpectedDeliveryDate()) >= 0) {
				FacesContextMessages.ErrorMessages("Expected Pick Up Time should be lower than Expected Delivery Time");
				return false;
			}
		} else if (transportationRequest.getPickupDate().compareTo(transportationRequest.getExpectedDeliveryDate()) >= 0) {
			FacesContextMessages.ErrorMessages("Pick Up Time should be lower than Expected Delivery Time");
			return false;
		}

		if (!transportationJobService.validateTransportationRequestListDates(transportationJob)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		return true;
	}

	// CLOSE TJ
	public Boolean canCloseTransportationJob() {
		return canCloseTransportationJob(transportationJob);
	}

	public Boolean canCloseTransportationJob(TransportationJob transportationJob) {
		return sessionView.isTM() && TransportationJobStatus.COMPLETED.equals(transportationJob.getStatus());
	}

	public void closeTransportationJob(TransportationJob transportationJob) {
		if (!canCloseTransportationJob(transportationJob))
			return;
		if (!validateCloseTransportationJob(transportationJob))
			return;
		transportationJob.setStatus(TransportationJobStatus.CLOSED);
		transportationJob = transportationJobService.save(transportationJob);
		transportationJob = transportationJobService.findOne(transportationJob.getId());
		transportationJobHistoryService.closed(transportationJob, sessionView.getUser());
	}

	public Boolean validateCloseTransportationJob(TransportationJob transportationJob) {
		Double sum = 0.0;

		for (TransportationRequest tr : transportationJob.getTransportationRequestList())
			sum += tr.getCost();

		if (UtilsFunctions.compareDoubles(transportationJob.getRealCost(), sum, 2) != 0)
			return FacesContextMessages.ErrorMessages("Total TR COsts should be equal to Real Cost : " + UtilsFunctions.formatDouble(transportationJob.getRealCost()));

		return true;
	}

	// OPEN TJ
	public Boolean canOpenTransportationJob() {
		return canOpenTransportationJob(transportationJob);
	}

	public Boolean canOpenTransportationJob(TransportationJob transportationJob) {
		for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
			transportationRequestService.updatePaymentStatus(tr.getId());
			tr = transportationRequestService.findOne(tr.getId());
			if (tr.getPaymentStatus() != null && !TransportationRequestPaymentStatus.PENDING.equals(tr.getPaymentStatus()))
				return false;
		}

		return sessionView.isTM() && TransportationJobStatus.CLOSED.equals(transportationJob.getStatus());
	}

	public void openTransportationJob(TransportationJob transportationJob) {
		if (!canOpenTransportationJob(transportationJob))
			return;
		transportationJob.setStatus(TransportationJobStatus.COMPLETED);
		transportationJob = transportationJobService.save(transportationJob);
		transportationJob = transportationJobService.findOne(transportationJob.getId());
		transportationJobHistoryService.opened(transportationJob, sessionView.getUser());
	}

	public void openTransportationJob() {
		openTransportationJob(transportationJob);
	}

	// private void calculateTransportationRequestListCosts(TransportationJob
	// transportationJob) {
	// Double total1 = 0.0, total2 = 0.0;
	// Boolean test = true;
	// for (TransportationRequest tr :
	// transportationJob.getTransportationRequestList()) {
	// Double grossWeight =
	// deliveryRequestService.getGrossWeight(tr.getDeliveryRequest().getId());
	// Double estimatedDistance = tr.getEstimatedDistance();
	// total1 += grossWeight * estimatedDistance;
	// total2 += estimatedDistance;
	// if (grossWeight.equals(0.0))
	// test = false;
	// }
	// for (TransportationRequest tr :
	// transportationJob.getTransportationRequestList()) {
	// Double grossWeight =
	// deliveryRequestService.getGrossWeight(tr.getDeliveryRequest().getId());
	// Double estimatedDistance = tr.getEstimatedDistance();
	// tr.setEstimatedCost(transportationJob.getEstimatedCost() * (test ?
	// estimatedDistance * grossWeight / total1 : estimatedDistance / total2));
	// tr.setCost(transportationJob.getRealCost() * (test ? estimatedDistance *
	// grossWeight / total1 : estimatedDistance / total2));
	// transportationRequestService.save(tr);
	// }
	// }

	public void closeTransportationJob() {
		if (isViewPage) {
			closeTransportationJob(transportationJob);
			refreshTransportationJob();

		} else if (pageIndex == 1) {
			for (TransportationJob transportationJob : list4)
				closeTransportationJob(transportationJobService.findOne(transportationJob.getId()));
			refreshList();
		}
	}

	// RETURN BACK
	public Boolean canReturnBack(TransportationRequestStatus status) {
		return !TransportationJobStatus.CLOSED.equals(transportationJob.getStatus()) && sessionView.isTM() && Arrays.asList(TransportationRequestStatus.PICKEDUP, TransportationRequestStatus.DELIVERED).contains(status);
	}

	public Boolean canReturnBack() {
		return canReturnBack(transportationRequest.getStatus());
	}

	public void returnBack() {
		if (!canReturnBack())
			return;
		if (TransportationRequestStatus.PICKEDUP.equals(transportationRequest.getStatus())) {
			transportationRequest.setStatus(TransportationRequestStatus.ASSIGNED);
			transportationRequest.setDate5(null);
			transportationRequest.setUser5(null);
			transportationRequest.setPickupDate(null);
		} else if (TransportationRequestStatus.DELIVERED.equals(transportationRequest.getStatus())) {
			transportationRequest.setStatus(TransportationRequestStatus.PICKEDUP);
			transportationRequest.setDate6(null);
			transportationRequest.setUser6(null);
			transportationRequest.setDeliveryDate(null);
		}
		transportationRequest = transportationRequestService.save(transportationRequest);
		updateCalculableFields();
	}

	// PICK UP
	public Boolean canPickup(TransportationRequestStatus status) {
		return sessionView.isTM() && TransportationRequestStatus.ASSIGNED.equals(status);
	}

	public Boolean canPickup() {
		return canPickup(transportationRequest.getStatus());
	}

	public void pickupTransportationRequest() {
		if (!canPickup())
			return;
		if (!validatePickupTransportationRequest())
			return;
		transportationRequest.setStatus(TransportationRequestStatus.PICKEDUP);
		transportationRequest.setDate5(new Date());
		transportationRequest.setUser5(sessionView.getUser());
		transportationRequestHistoryService.pickedupNew(transportationRequest, sessionView.getUser());
		transportationRequestService.save(transportationRequest);
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		emailService.transportationRequestNotification(transportationRequest);
		// smsService.sendSms(transportationRequest);

		updateCalculableFields();
	}

	public Boolean validatePickupTransportationRequest() {
		if (transportationRequest.getPickupDate().compareTo(new Date()) > 0) {
			FacesContextMessages.ErrorMessages("Pickup Time should be lower than Current Time");
			return false;
		}
		if (transportationRequest.getPickupDate().compareTo(transportationRequest.getExpectedDeliveryDate()) > 0) {
			FacesContextMessages.ErrorMessages("Expected Delivery Time should be greather than Pickup Time");
			return false;
		}
		if (!transportationJobService.validateTransportationRequestListDates(transportationJob)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		return true;
	}

	// DELIVER
	public Boolean canDeliver(TransportationRequestStatus status) {
		return sessionView.isTM() && TransportationRequestStatus.PICKEDUP.equals(status);
	}

	public Boolean canDeliver() {
		return canDeliver(transportationRequest.getStatus());
	}

	public void deliverTransportationRequest() {
		if (!canDeliver())
			return;
		if (!validateDeliverTransportationRequest())
			return;
		transportationRequest.setStatus(TransportationRequestStatus.DELIVERED);
		transportationRequest.setDate6(new Date());
		transportationRequest.setUser6(sessionView.getUser());
		transportationRequestHistoryService.delivredNew(transportationRequest, sessionView.getUser());
		transportationRequestService.save(transportationRequest);
		transportationRequest = transportationRequestService.findOne(transportationRequest.getId());
		emailService.transportationRequestNotification(transportationRequest);
		smsService.sendSms(transportationRequest);

		updateCalculableFields();
	}

	public Boolean validateDeliverTransportationRequest() {
		if (transportationRequest.getDeliveryDate().compareTo(new Date()) > 0) {
			FacesContextMessages.ErrorMessages("Delivery Time should be lower than Current Time");
			return false;
		}
		if (transportationRequest.getDeliveryDate().compareTo(transportationRequest.getPickupDate()) <= 0) {
			FacesContextMessages.ErrorMessages("Delivery Time should be greather than Pickup Date");
			return false;
		}
		if (!transportationJobService.validateTransportationRequestListDates(transportationJob)) {
			FacesContextMessages.ErrorMessages("At same time, they can not be different sites");
			return false;
		}
		return true;
	}

	// DELETE TRANSPORTATIONJOB
	public Boolean canDeleteTransportationJob() {
		return sessionView.isTM() && transportationJob.getTransportationRequestList().isEmpty();
	}

	public String deleteTransportationJob() {
		if (canDeleteTransportationJob())
			transportationJobService.delete(transportationJob);
		return addParameters(listPage, "faces-redirect=true");
	}

	// FILES MANAGEMENT
	private String transportationJobFileType;
	private Integer transportationJobFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileView.handleFileUpload(event, getClassName2());
		TransportationJobFile transportationJobFile = new TransportationJobFile(getClassName2(), file, transportationJobFileType, event.getFile().getFileName(), transportationJob, sessionView.getUser());
		transportationJobFileService.save(transportationJobFile);
		synchronized (TransportationJobView.class) {
			refreshTransportationJob();
		}
	}

	public void deleteTransportationJobFile() {
		transportationJobFileService.delete(transportationJobFileId);
		refreshTransportationJob();
	}

	// Edit TR List Costs
	public Boolean canEditTransportationRequestListCosts() {
		return sessionView.isTM() && TransportationJobStatus.COMPLETED.equals(transportationJob.getStatus());
	}

	public void editTransportationRequestListCosts() {
		if (!canAssignTransportationRequestList())
			return;
		if (!validateEditTransportationRequestListCosts())
			return;

		for (TransportationRequest tr : transportationJob.getTransportationRequestList())
			transportationRequestService.save(tr);

		editCosts = false;
	}

	public Boolean validateEditTransportationRequestListCosts() {
		Double sum = 0.0;
		for (TransportationRequest tr : transportationJob.getTransportationRequestList()) {
			if (tr.getCost() == null)
				return FacesContextMessages.ErrorMessages("TR Cost should not be null");
			sum += tr.getCost();
		}

		if (UtilsFunctions.compareDoubles(transportationJob.getRealCost(), sum, 2) != 0)
			return FacesContextMessages.ErrorMessages("Total Costs should be equal to Real Cost : " + UtilsFunctions.formatDouble(transportationJob.getRealCost()));

		return true;
	}

	// GETTERS & SETTERS
	@Override
	public SessionView getSessionView() {
		return sessionView;
	}

	@Override
	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public TransportationJobService getTransportationJobService() {
		return transportationJobService;
	}

	public void setTransportationJobService(TransportationJobService transportationJobService) {
		this.transportationJobService = transportationJobService;
	}

	public TransportationJob getTransportationJob() {
		return transportationJob;
	}

	public void setTransportationJob(TransportationJob transportationJob) {
		this.transportationJob = transportationJob;
	}

	public FileView getFileView() {
		return fileView;
	}

	public void setFileView(FileView fileView) {
		this.fileView = fileView;
	}

	public TransportationJobFileService getTransportationJobFileService() {
		return transportationJobFileService;
	}

	public void setTransportationJobFileService(TransportationJobFileService transportationJobFileService) {
		this.transportationJobFileService = transportationJobFileService;
	}

	public String getTransportationJobFileType() {
		return transportationJobFileType;
	}

	public void setTransportationJobFileType(String transportationJobFileType) {
		this.transportationJobFileType = transportationJobFileType;
	}

	public Integer getTransportationJobFileId() {
		return transportationJobFileId;
	}

	public void setTransportationJobFileId(Integer transportationJobFileId) {
		this.transportationJobFileId = transportationJobFileId;
	}

	public TransportationJobFile getTransportationJobFile() {
		return transportationJobFile;
	}

	public void setTransportationJobFile(TransportationJobFile transportationJobFile) {
		this.transportationJobFile = transportationJobFile;
	}

	public List<TransportationRequest> getTransportationRequestList1() {
		return transportationRequestList1;
	}

	public void setTransportationRequestList1(List<TransportationRequest> transportationRequestList1) {
		this.transportationRequestList1 = transportationRequestList1;
	}

	public List<TransportationRequest> getTransportationRequestList2() {
		return transportationRequestList2;
	}

	public void setTransportationRequestList2(List<TransportationRequest> transportationRequestList2) {
		this.transportationRequestList2 = transportationRequestList2;
	}

	public List<TransportationRequest> getTransportationRequestList3() {
		return transportationRequestList3;
	}

	public void setTransportationRequestList3(List<TransportationRequest> transportationRequestList3) {
		this.transportationRequestList3 = transportationRequestList3;
	}

	public String getAssignPage() {
		return assignPage;
	}

	public Boolean getIsAssignPage() {
		return isAssignPage;
	}

	public TransportationRequest getTransportationRequest() {
		return transportationRequest;
	}

	public void setTransportationRequest(TransportationRequest transportationRequest) {
		this.transportationRequest = transportationRequest;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public Boolean getViewPathList() {
		return viewPathList;
	}

	public void setViewPathList(Boolean viewPathList) {
		this.viewPathList = viewPathList;
	}

	public Boolean getEditCosts() {
		return editCosts;
	}

	public void setEditCosts(Boolean editCosts) {
		this.editCosts = editCosts;
	}

}
