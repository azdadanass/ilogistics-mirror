package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.CompanyType;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestComment;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestFile;
import ma.azdad.model.DeliveryRequestHistory;
import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.model.DeliveryRequestState;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.InboundType;
import ma.azdad.model.Location;
import ma.azdad.model.PackingDetail;
import ma.azdad.model.PartNumber;
import ma.azdad.model.Po;
import ma.azdad.model.PoTypes;
import ma.azdad.model.ProjectCross;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.Site;
import ma.azdad.model.StockRow;
import ma.azdad.model.StockRowStatus;
import ma.azdad.model.ToNotify;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.model.User;
import ma.azdad.repos.DeliveryRequestRepos;
import ma.azdad.service.AppLinkService;
import ma.azdad.service.BoqService;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CurrencyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.DeliveryRequestDetailService;
import ma.azdad.service.DeliveryRequestFileService;
import ma.azdad.service.DeliveryRequestSerialNumberService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.JobRequestDeliveryDetailService;
import ma.azdad.service.OldEmailService;
import ma.azdad.service.PackingDetailService;
import ma.azdad.service.PackingService;
import ma.azdad.service.PartNumberEquivalenceService;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.PoService;
import ma.azdad.service.ProjectAssignmentService;
import ma.azdad.service.ProjectCrossService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.SiteService;
import ma.azdad.service.SmsService;
import ma.azdad.service.StockRowService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.ToNotifyService;
import ma.azdad.service.TransportationRequestService;
import ma.azdad.service.TransporterService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.service.WarehouseService;
import ma.azdad.utils.DeliveryRequestExcelFileException;
import ma.azdad.utils.FacesContextMessages;
import ma.azdad.utils.LabelValue;

@ManagedBean
@Component
@Scope("view")
public class DeliveryRequestView extends GenericView<Integer, DeliveryRequest, DeliveryRequestRepos, DeliveryRequestService> implements Serializable {
	private static final long serialVersionUID = -2791229372979711793L;

	@Autowired
	private SessionView sessionView;

	@Autowired
	protected DeliveryRequestService service;

	@Autowired
	protected DeliveryRequestDetailService deliveryRequestDetailService;

	@Autowired
	protected DeliveryRequestFileService deliveryRequestFileService;

	@Autowired
	protected PartNumberService partNumberService;

	@Autowired
	protected CustomerService customerService;

	@Autowired
	protected SupplierService supplierService;

	@Autowired
	protected ProjectService projectService;

	@Autowired
	protected WarehouseService warehouseService;

	@Autowired
	protected SiteService siteService;

	@Autowired
	protected OldEmailService emailService;

	@Autowired
	protected TransporterService transporterService;

	@Autowired
	protected SmsService smsService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected FileUploadView fileUploadView;

	@Autowired
	protected StockRowService stockRowService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected CompanyService companyService;

	@Autowired
	protected ProjectCrossService projectCrossService;

	@Autowired
	protected CurrencyService currencyService;

	@Autowired
	protected TransportationRequestService transportationRequestService;

	@Autowired
	protected PoService poService;

	@Autowired
	protected ToNotifyService toNotifyService;

	@Autowired
	protected PartNumberEquivalenceService partNumberEquivalenceService;

	@Autowired
	protected BoqService boqService;

	@Autowired
	protected AppLinkService appLinkService;

	@Autowired
	protected AppLinkView appLinkView;

	@Autowired
	protected PackingService packingService;

	@Autowired
	protected DeliveryRequestSerialNumberService deliveryRequestSerialNumberService;

	@Autowired
	JobRequestDeliveryDetailService jobRequestDeliveryDetailService;

	@Autowired
	PackingDetailService packingDetailService;

	@Autowired
	IndexView indexView;

	@Autowired
	MenuView menuView;

	@Autowired
	ProjectAssignmentService projectAssignmentService;

	private DeliveryRequest deliveryRequest = new DeliveryRequest();
	private DeliveryRequestFile deliveryRequestFile;
	private DeliveryRequestComment deliveryRequestComment = new DeliveryRequestComment();
	private DeliveryRequestType type = null;
	private DeliveryRequestState state;

	private List<Integer> toDeleteDetailList = new ArrayList<>();

	private String[] datesTab;
	private DeliveryRequestComment[][] commentsTab;

	private String storagePage = "storeDeliveryRequest.xhtml";
	private Boolean isStoragePage = false;

	private String preparationPage = "prepareOutboundDeliveryRequest.xhtml";
	private Boolean isPreparationPage = false;

	private String lightViewPage = "viewDeliveryRequestLight.xhtml";
	private Boolean isLightViewPage = false;

	private String printPage = "printDeliveryRequest.xhtml";
	private Boolean isPrintPage = false;

	private int step = 0;

	private Boolean addOriginSite = true;

	private List<DeliveryRequestDetail> deliveryRequestDetailList1;
	private List<DeliveryRequestDetail> deliveryRequestDetailList2;
	private List<DeliveryRequestDetail> deliveryRequestDetailSelectionList = new ArrayList<>();
	private String searchDetail;

	private Integer templateId;

	private String toNotifyUserUsername;

	private String downloadPath;

	private DeliveryRequestDetail deliveryRequestDetail;
	private ProjectCross projectCross;

	private Integer companyId;

	private Boolean selectOrigin;

	private Integer outboundDeliveryRequestId;
	private InboundType inboundType;

	private String warningMessage;

	@Override
	@PostConstruct
	public void init() {
		companyId = menuView.getCompanyId();

		super.init();
		isStoragePage = ("/" + storagePage).equals(currentPath);
		isPreparationPage = ("/" + preparationPage).equals(currentPath);
		isLightViewPage = ("/" + lightViewPage).equals(currentPath);
		isPrintPage = ("/" + printPage).equals(currentPath);
		initParameters();

		if (isListPage)
			type = null;
		refreshList();
		if (isAddPage) {
			deliveryRequest = new DeliveryRequest(type, sessionView.getUser());
			if (templateId != null)
				deliveryRequest.copyFromTemplate(service.findOne(templateId));
			else if (outboundDeliveryRequestId != null && inboundType != null) {
				deliveryRequest.setType(DeliveryRequestType.INBOUND);
				deliveryRequest.setInboundType(inboundType);
				switch (inboundType) {
				case RETURN:
					deliveryRequest.setOutboundDeliveryRequestReturnId(outboundDeliveryRequestId);
					changeOutboundDeliveryRequestReturnListener();
					break;
				case TRANSFER:
					deliveryRequest.setOutboundDeliveryRequestTransferId(outboundDeliveryRequestId);
					changeOutboundDeliveryRequestTransferListener();
					break;
				default:
					break;
				}
				initOwnerType();
			}
			saveDeliveryRequestNextStep();
		} else if (isEditPage) {
			deliveryRequest = service.findOne(id);
			deliveryRequest.init();
			Set<Integer> boqListToUpdate = boqService.getAssociatedBoqIdListWithDeliveryRequest(deliveryRequest.getId());
			deliveryRequest.clearBoqMappingList();
			boqService.updateTotalUsedQuantity(boqListToUpdate);
			deliveryRequest = service.findOne(id);
			deliveryRequest.init();
			deliveryRequest.initDetailList();
			saveDeliveryRequestNextStep();
			if (deliveryRequest.getPoId() != null && deliveryRequest.getIsInbound())
				findRemainingDetailListByPo();

		} else if (isViewPage) {
			initViewPage();

//			initCommentsVariables();

		} else if (isLightViewPage || isPrintPage) {
			deliveryRequest = service.findOne(id);
		} else if (isStoragePage) {
			deliveryRequest = service.findOne(id);
			storageNextStep();
		} else if (isPreparationPage) {
			deliveryRequest = service.findOne(id);
			deliveryRequest.init();
			preparationNextStep();
		}
	}

	private void initViewPage() {
		deliveryRequest = service.findOne(id);
		deliveryRequest.init();
		if (deliveryRequest.getIsInbound() && DeliveryRequestStatus.PARTIALLY_DELIVRED.equals(deliveryRequest.getStatus()))
			deliveryRequest.getDetailList().forEach(i -> i.setTmpDeliveredQuantity(stockRowService.findQuantityByDeliveryRequestDetail(i.getId())));

		projectCross = projectCrossService.findByDeliveryRequest(id);

		if (deliveryRequest.getIsOutbound() && DeliveryRequestStatus.APPROVED2.equals(deliveryRequest.getStatus()))
			generateStockRowFromOutboundDeliveryRequest();
	}

	public Boolean canDuplicate() {
		return sessionView.getIsUser() && sessionView.isTheConnectedUser(deliveryRequest.getRequester());
	}

	public void message() {
		System.out.println(deliveryRequestDetailSelectionList);
	}

	public Boolean getIsInbound() {
		return DeliveryRequestType.INBOUND.equals(type);
	}

	public Boolean getIsOutbound() {
		return DeliveryRequestType.OUTBOUND.equals(type);
	}

	public Boolean getIsXbound() {
		return DeliveryRequestType.XBOUND.equals(type);
	}

	@Override
	public void initParameters() {
		super.initParameters();
		try {
			type = DeliveryRequestType.values()[Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("type"))];
		} catch (Exception e) {
			type = null;
		}

		try {
			state = DeliveryRequestState.values()[Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("state"))];
		} catch (Exception e) {
			state = null;
		}

		try {
			templateId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("templateId"));
		} catch (Exception e) {
			templateId = null;
		}

		outboundDeliveryRequestId = UtilsFunctions.getIntegerParameter("outboundDeliveryRequestId");

		try {
			inboundType = InboundType.values()[UtilsFunctions.getIntegerParameter("inboundType")];
		} catch (Exception e) {
			inboundType = null;
		}

	}

	@Override
	public void refreshList() {
		if (isListPage)
			switch (pageIndex) {
			case 1:
				if (sessionView.getInternal() || sessionView.getIsCustomerUser())
					list2 = list1 = service.findLight(sessionView.getUsername(), type, state, cacheView.getWarehouseList(),
							Stream.concat(cacheView.getUserProjectList().stream(), cacheView.getHmProjectList().stream()).distinct().collect(Collectors.toList()));
				else if (sessionView.getIsSupplierUser())
					list2 = list1 = service.findLightBySupplierUser(type, state, sessionView.getUser().getSupplierId(), cacheView.getAssignedProjectList(),
							cacheView.getWarehouseList());

				if (DeliveryRequestState.WAITING.equals(state))
					Collections.sort(list1);
				else if (DeliveryRequestState.DELIVRED.equals(state))
					Collections.sort(list1, new Comparator<DeliveryRequest>() {
						public int compare(DeliveryRequest o1, DeliveryRequest o2) {
							return o2.getDate4().compareTo(o1.getDate4());
						}
					});

				break;
			case 2:
				if (sessionView.getInternal())
					initLists(service.findToAcknowledgeInternal(sessionView.getUsername(), cacheView.getWarehouseList()));
				else if (sessionView.getIsExternalPm()) {
					if (sessionView.getIsSupplierUser())
						initLists(service.findToAcknowledgeExternalSupplierUser(sessionView.getUsername(), sessionView.getUser().getSupplierId(), cacheView.getUserProjectList()));
					if (sessionView.getIsCustomerUser())
						initLists(service.findToAcknowledgeExternalCustomerUser(sessionView.getUsername(), sessionView.getUser().getCustomerId(), cacheView.getUserProjectList()));
				}

				break;
			case 3:
				if (sessionView.getInternal())
					list2 = list1 = service.findLightToApprove(sessionView.getUsername(), cacheView.getDelegatedProjectList());
				break;
			case 4:
				list2 = list1 = service.findLightByWarehouseList(cacheView.getWarehouseList());
				Collections.sort(list2, new Comparator<DeliveryRequest>() {
					@Override
					public int compare(DeliveryRequest o1, DeliveryRequest o2) {
						return o1.getNeededDeliveryDate().compareTo(o2.getNeededDeliveryDate());
					}
				});
				break;
			case 5:
				list2 = list1 = service.findByMissingPo(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), DeliveryRequestType.OUTBOUND);
				break;
			case 6:
				list2 = list1 = service.findByMissingPo(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), DeliveryRequestType.INBOUND);
				break;
			case 7:
				if (sessionView.getInternal())
					list2 = list1 = service.findLightByRequester(sessionView.getUsername(), DeliveryRequestType.XBOUND, DeliveryRequestStatus.APPROVED2);
				break;
			case 8:
				if (sessionView.getInternal())
					list2 = list1 = service.findLightByIsForTransferAndDestinationProjectAndNotTransferred(sessionView.getUsername(), cacheView.getAssignedProjectList());
				break;
			case 9:
				if (sessionView.getInternal())
					list2 = list1 = service.findLightByIsForReturnAndNotFullyReturned(sessionView.getUsername());
				break;
			case 10:
				if (sessionView.getInternal())
					list2 = list1 = service.findLightByPendingTransportation(sessionView.getUsername());
				break;
			case 11:
				list2 = list1 = service.findLightByMissingSerialNumber(cacheView.getWarehouseList());
				break;
			case 12:
				list2 = list1 = service.findLightByMissingExpiry(cacheView.getWarehouseList());
				break;
			case 13:
				list2 = list1 = service.findByMissingBoqMapping(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
						DeliveryRequestType.OUTBOUND);
				break;
			case 14:
				list2 = list1 = service.findByMissingBoqMapping(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(),
						DeliveryRequestType.INBOUND);
				break;
			case 15:
				if (sessionView.getInternal())
					initLists(service.findByMissingOutboundDeliveryNoteFile(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAllProjectList()));
				else if (sessionView.getIsExternalPm()) {
					if (sessionView.getIsSupplierUser())
						initLists(service.findByMissingOutboundDeliveryNoteFileAndDeliverToSupplier(sessionView.getUser().getSupplierId(), cacheView.getUserProjectList()));
					else if (sessionView.getIsCustomerUser())
						initLists(service.findByMissingOutboundDeliveryNoteFileAndDeliverToCustomer(sessionView.getUser().getCustomerId(), cacheView.getUserProjectList()));
				}
				break;
			case 16:
				initLists(service.findPendingJrMapping(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.OUTBOUND, true, false));
				break;
			case 17:
				initLists(service.findPendingJrMapping(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.INBOUND, true, false));
				break;
			case 18:
				initLists(service.findPendingJrMapping(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.OUTBOUND, false, true));
				break;
			case 19:
				initLists(service.findPendingJrMapping(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.INBOUND, false, true));
				break;
			case 20:
				initLists(service.findHavingRunningStock(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.OUTBOUND, true, false));
				break;
			case 21:
				initLists(service.findHavingRunningStock(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.INBOUND, true, false));
				break;
			case 22:
				initLists(service.findHavingRunningStock(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.OUTBOUND, false, true));
				break;
			case 23:
				initLists(service.findHavingRunningStock(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.INBOUND, false, true));
				break;
			default:
				break;
			}
		else if ("/deliveryRequestfinancialReporting.xhtml".equals(currentPath))
			getFinancialLists(true);
	}

	public void getFinancialLists(Boolean outbound) {
		if (outbound)
			list2 = list1 = service.findOutboundFinancialByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
		else
			list2 = list1 = service.findInboundFinancialByCompanyOwner(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), companyId);
	}

	public void refreshDeliveryRequest() {
		service.flush();
		deliveryRequest = service.findOne(deliveryRequest.getId());
	}

	private void filterDetail(String query) {
		Set<DeliveryRequestDetail> list = new HashSet<>();
		query = query.toLowerCase().trim();
		for (DeliveryRequestDetail detail : deliveryRequestDetailList1) {
			if (detail.filter(query))
				list.add(detail);
		}

		list.addAll(deliveryRequestDetailSelectionList);
		deliveryRequestDetailList2 = new ArrayList<>();
		deliveryRequestDetailList2.addAll(list);
	}

	/*
	 * Redirection
	 */
	public Boolean canAccess() {
		if (isViewPage) {
			if (deliveryRequest.getWarehouse() != null && cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId()))
				return true;
			if (sessionView.isTM())
				return true;
			if (sessionView.getInternal())
				return sessionView.isTheConnectedUser(deliveryRequest.getRequester()) //
						|| cacheView.getAssignedProjectList().contains(deliveryRequest.getProject().getId()) //
						|| cacheView.getDelegatedProjectList().contains(deliveryRequest.getProject().getId())//
						|| sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())//
						|| sessionView.isTheConnectedUser(deliveryRequest.getProject().getCostcenter().getLob().getManager().getUsername())//
						|| sessionView.isTheConnectedUser(deliveryRequest.getProject().getCostcenter().getLob().getBu().getDirector().getUsername());
			else if (sessionView.getIsExternalPm()) {
				if (sessionView.getIsSupplierUser())
					return sessionView.getUser().getSupplierId().equals(deliveryRequest.getDeliverToSupplierId()) //
							&& cacheView.getAssignedProjectList().contains(deliveryRequest.getDestinationProject().getId());
				else if (sessionView.getIsCustomerUser())
					return sessionView.getUser().getCustomerId().equals(deliveryRequest.getDeliverToCustomerId()) //
							&& cacheView.getAssignedProjectList().contains(deliveryRequest.getDestinationProject().getId());
			}
		} else if (isStoragePage)
			return canStoreDeliveryRequest();
		else if (isPreparationPage)
			return canPrepareOutboundDeliveryRequest();
		return false;
	}

	@Override
	public void redirect() {
		if (!canAccess())
			cacheView.accessDenied();
	}

	/*
	 * Add Transport
	 */

	public Boolean canAddTrasnport() {
		return service.canAddTrasnport(deliveryRequest, sessionView.getUsername());
	}

	public String addTransport() {
		indexView.setSelectedMenu(3);
		return addParameters("addEditTransportationRequest.xhtml", "faces-redirect=true", "deliveryRequestId=" + deliveryRequest.getId());
	}

	/*
	 * WM WIZARD OUTBOUND
	 */
	public Boolean canPrepareOutboundDeliveryRequest() {
		return DeliveryRequestType.OUTBOUND.equals(deliveryRequest.getType()) && cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId())
				&& Arrays.asList(DeliveryRequestStatus.APPROVED2).contains(deliveryRequest.getStatus());
	}

	public String preparationNextStep() {
		switch (step) {
		case 0:
			System.out.println("step0");
			step++;
			preStep1();
			break;
		case 1:
			System.out.println("step1");
			step++;
			break;
		case 2:
			System.out.println("step2");
			deliveryRequest.setToUser(userService.findOne(deliveryRequest.getToUserUsername()));
			step++;
			break;
		case 3:
			System.out.println("step3");
			step++;
			break;
		case 4:
			System.out.println("step4");
			if (checkDatabaseStatus(deliveryRequest.getId(), DeliveryRequestStatus.DELIVRED)) {
				FacesContextMessages.ErrorMessages("DN already Delivered !");
				return null;
			}

			deliveryRequest.setStatus(DeliveryRequestStatus.DELIVRED);
			deliveryRequest.setDate4(new Date());
			deliveryRequest.setUser4(sessionView.getUser());
			deliveryRequest.addHistory(new DeliveryRequestHistory(DeliveryRequestStatus.DELIVRED.getValue(), sessionView.getUser()));
			service.save(deliveryRequest);
			emailService.deliveryRequestNotification(deliveryRequest);
			smsService.sendSms(deliveryRequest);

			projectCrossService.addCrossCharge(deliveryRequest);
			stockRowService.updateOwnerId(deliveryRequest.getId());
			stockRowService.updateInboundOwnerId(deliveryRequest.getId());

//			if (deliveryRequest.getCustomer() != null)
//				customerService.updateIsStockEmpty(deliveryRequest.getCustomer().getId());

			if (deliveryRequest.getPo() != null) {
				// poService.updateIlogisticsStatus(deliveryRequest.getPo().getId());
				poService.updateGoodsDeliveryStatus(deliveryRequest.getPo().getId());
			}

			// update field missing sn
			deliveryRequest = service.findOne(deliveryRequest.getId());
//			if (deliveryRequest.getStockRowList().stream()
//					.filter(i -> deliveryRequestSerialNumberService.countByPartNumberAndInboundDeliveryRequest(i.getPartNumberId(), i.getInboundDeliveryRequest().getId()) > 0)
//					.count() > 0)
//				service.updateMissingSerialNumber(deliveryRequest.getId(), true);
			service.calculateMissingSerialNumber(deliveryRequest.getId());

			// update is missing expiry
			if (deliveryRequest.getStockRowList().stream().filter(i -> i.getPartNumber().getExpirable()).count() > 0)
				service.updateMissingExpiry(deliveryRequest.getId(), true);

			return addParameters("viewDeliveryRequest.xhtml", "faces-redirect=true", "id=" + deliveryRequest.getId());
		default:
			break;
		}
		execJavascript("PF('nextButton').enable()");
		return null;
	}

	private List<StockRow> potentialStockRowlist = new ArrayList<StockRow>();
	private List<DeliveryRequestSerialNumber> potentialSerialNumberList = new ArrayList<DeliveryRequestSerialNumber>();

	public void generateStockRowFromOutboundDeliveryRequest() {
		System.out.println("generateStockRowFromOutboundDeliveryRequest");
		potentialStockRowlist = stockRowService.generateStockRowFromOutboundDeliveryRequest(deliveryRequest);
		potentialStockRowlist.stream().filter(i -> i.getPacking().getHasSerialnumber()).forEach(i -> {
			System.out.println(i.getPartNumberName());
			i.getPacking().getDetailList().stream().filter(j -> j.getHasSerialnumber()).forEach(j -> {
				Integer quantity = -((int) (double) i.getQuantity()) * j.getQuantity() / i.getPacking().getQuantity();
				List<DeliveryRequestSerialNumber> snList = deliveryRequestSerialNumberService.findHavingSerialNumberAndNoOutbound(i.getInboundDeliveryRequestDetail().getId(),
						i.getLocationId(), j.getId(), quantity);
				potentialSerialNumberList.addAll(snList);
			});
		});
	}

	private void preStep1() {
		// init stock row list
		deliveryRequest.setStockRowList(stockRowService.generateStockRowFromOutboundDeliveryRequest(deliveryRequest));
	}

	// SAVE BY STEPS
	public void preparationPreviousStep() {
		if (step > 1)
			step--;
	}

	/*
	 * WM WIZARD INBOUND
	 */

	public Boolean canStoreDeliveryRequest() {
		return DeliveryRequestType.INBOUND.equals(deliveryRequest.getType()) && cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId())
				&& Arrays.asList(DeliveryRequestStatus.APPROVED2, DeliveryRequestStatus.PARTIALLY_DELIVRED).contains(deliveryRequest.getStatus());
	}

	public String storageNextStep() {
		if (!canStoreDeliveryRequest())
			return null;
		switch (step) {
		case 0:
			System.out.println("step0");
			deliveryRequestComment = new DeliveryRequestComment("Visual Check", sessionView.getUser());
			step++;
			break;
		case 1:
			System.out.println("step1");
			if (!StringUtils.isBlank(deliveryRequestComment.getContent()))
				deliveryRequest.addComment(deliveryRequestComment);
			step++;
			initTmpQuantites();
			deliveryRequestComment = new DeliveryRequestComment("Counts Validation", sessionView.getUser());
			break;
		case 2:
			System.out.println("step2");
			if (!validateStorageStep2())
				break;
			if (!StringUtils.isBlank(deliveryRequestComment.getContent()))
				deliveryRequest.addComment(deliveryRequestComment);
			deliveryRequest.initIsPartial();
			if (!deliveryRequest.getIsPartial() && !deliveryRequestComment.getIsOk()) {
				FacesContextMessages.ErrorMessages("Count is NOK, please put the correct values for quantites ");
				break;
			}
			step++;
			initStockRowList();
			break;
		case 3:
			System.out.println("step3");
			deliveryRequest.getStockRowList().forEach(i -> i.setState(deliveryRequest.getStockRowState()));
			step++;
			break;
		case 4:
			System.out.println("step4");
			if (!validateStorageStep4())
				break;
			for (StockRow row : deliveryRequest.getStockRowList())
				row.setInitial(true);
			step++;
			break;
		case 5:
			System.out.println("step5");
			if (!validateStorageStep5())
				break;
			step++;
			break;
		case 6:
			step++;
			break;
		case 7:
			if (checkDatabaseStatus(deliveryRequest.getId(), DeliveryRequestStatus.DELIVRED)) {
				FacesContextMessages.ErrorMessages("DN already Delivered !");
				return null;
			}

			if (checkDatabaseStatus(deliveryRequest.getId(), DeliveryRequestStatus.PARTIALLY_DELIVRED)) {
				// Part Number / Qty maps
				Map<Integer, Double> deliveryRequestDetailMap = deliveryRequest.getDetailList().stream()
						.collect(Collectors.groupingBy(DeliveryRequestDetail::getPartNumberId, Collectors.summingDouble(DeliveryRequestDetail::getQuantity)));
				Map<Integer, Double> stockRowExistingMap = stockRowService.findQuantityPartNumberMapByDeliveryRequest(deliveryRequest.getId());
				Map<Integer, Double> stockRowNewMap = deliveryRequest.getStockRowList().stream().filter(i -> i.getId() == null)
						.collect(Collectors.groupingBy(StockRow::getPartNumberId, Collectors.summingDouble(StockRow::getQuantity)));

				for (Integer partNumberId : deliveryRequestDetailMap.keySet()) {
					Double detailQuantity = deliveryRequestDetailMap.get(partNumberId);
					Double existingQuantity = stockRowExistingMap.getOrDefault(partNumberId, 0.0);
					Double newQuantity = stockRowNewMap.getOrDefault(partNumberId, 0.0);
					if (detailQuantity < existingQuantity + newQuantity) {
						FacesContextMessages.ErrorMessages("Remaining quantity error, probably concurrent access, please to reload this page");
						return null;
					}
				}
			}

			deliveryRequest.setStatus(deliveryRequest.getIsPartial() ? DeliveryRequestStatus.PARTIALLY_DELIVRED : DeliveryRequestStatus.DELIVRED);
			deliveryRequest.setDate4(new Date());
			deliveryRequest.setUser4(sessionView.getUser());
			deliveryRequest.addHistory(new DeliveryRequestHistory(deliveryRequest.getStatus().getValue(), sessionView.getUser()));
			service.save(deliveryRequest);

			stockRowService.updateOwnerId(deliveryRequest.getId());
			stockRowService.updateInboundOwnerId(deliveryRequest.getId());

			deliveryRequest = service.findOne(deliveryRequest.getId());

			if (deliveryRequest.getPo() != null) {
				// poService.updateIlogisticsStatus(deliveryRequest.getPo().getId());
				poService.updateGoodsDeliveryStatus(deliveryRequest.getPo().getId());
			}

			if (deliveryRequest.getIsSnRequired())
				generateSerialNumberList();

			if (DeliveryRequestStatus.DELIVRED.equals(deliveryRequest.getStatus()))
				projectCrossService.addCrossChargeForReturnFromOutbound(deliveryRequest);

			if (deliveryRequest.getIsInboundReturn()) {
				service.updateIsFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn().getId(),
						deliveryRequestDetailService.isOutboundDeliveryRequestFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn()));
				service.updateReturnInboundsUnitPrice(deliveryRequest.getOutboundDeliveryRequestReturn().getId());
				DeliveryRequest outboundDeliveryRequestReturn = service.findOne(deliveryRequest.getOutboundDeliveryRequestReturn().getId());
				service.clearBoqMapping(outboundDeliveryRequestReturn);
				jobRequestDeliveryDetailService.deleteByDeliveryRequestAndNotStartedJobRequest(deliveryRequest.getOutboundDeliveryRequestReturn(), deliveryRequest,
						sessionView.getUser());
				service.calculatePendingJrMapping(outboundDeliveryRequestReturn.getId());
				service.calculateHavingRunningStock(outboundDeliveryRequestReturn.getId());
			}

			emailService.deliveryRequestNotification(deliveryRequest);
			smsService.sendSms(deliveryRequest);

//			if (deliveryRequest.getCustomer() != null)
//				customerService.updateIsStockEmpty(deliveryRequest.getCustomer().getId());

			// update is missing expiry
			if (deliveryRequest.getStockRowList().stream().filter(i -> i.getPartNumber().getExpirable()).count() > 0)
				service.updateMissingExpiry(deliveryRequest.getId(), true);

			if (deliveryRequest.getIsInboundReturnFromOutboundHardwareSwap())
				service.updateHardwareSwapInboundIdAndStatus(deliveryRequest.getOutboundDeliveryRequestReturnId(), deliveryRequest.getId(), deliveryRequest.getStatus());

			return addParameters("viewDeliveryRequest.xhtml", "faces-redirect=true", "id=" + deliveryRequest.getId());
		default:
			break;
		}

		execJavascript("PF('nextButton').enable()");

		return null;
	}

	private void generateSerialNumberList() {
		Map<String, Integer> map = new HashMap<>();

		for (Integer stockRowId : deliveryRequest.getStockRowList().stream().map(i -> i.getId()).collect(Collectors.toList())) {
			StockRow inboundStockRow = stockRowService.findOne(stockRowId);
			for (PackingDetail packingDetail : inboundStockRow.getPacking().getDetailList()) {
				if (!packingDetail.getHasSerialnumber())
					continue;

				map.putIfAbsent(inboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId(), 0);

				int packingQuantity = (int) (inboundStockRow.getQuantity() / packingDetail.getParent().getQuantity());
				int n = packingDetail.getQuantity();
				for (int i = 0; i < packingQuantity; i++) {
					int packingNumero = map.get(inboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId()) + 1;
					map.put(inboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId(), packingNumero);
					for (int j = 0; j < n; j++)
						deliveryRequestSerialNumberService.save(new DeliveryRequestSerialNumber(packingNumero, packingDetail, inboundStockRow));
				}
			}
		}

		if (!map.isEmpty())
			service.updateMissingSerialNumber(deliveryRequest.getId(), true);

		// automatic fill SN from associated Outbound
		if (deliveryRequest.getIsInboundReturn() || deliveryRequest.getIsInboundTransfer()) {
			try {
				final DeliveryRequest outboundDn = deliveryRequest.getOutboundDeliveryRequestReturn() != null ? deliveryRequest.getOutboundDeliveryRequestReturn()
						: deliveryRequest.getOutboundDeliveryRequestTransfer();
				deliveryRequest.getDetailList().stream().filter(i -> i.getPacking().getHasSerialnumber()).forEach(i -> {
//					Double outboundQuantity = outboundDn.getDetailList().stream().filter(j -> j.getPartNumber().equals(i.getPartNumber())).mapToDouble(j -> j.getQuantity()).sum();
					Double outboundQuantity = deliveryRequestDetailService.findQuantityByDeliveryRequestAndPartNumber(outboundDn.getId(), i.getPartNumberId());
					if (i.getQuantity().equals(outboundQuantity)) {
						List<DeliveryRequestSerialNumber> drsnList = deliveryRequestSerialNumberService.findByInboundDeliveryRequestDetail(i.getId());
						List<DeliveryRequestSerialNumber> outboundDrsnList = deliveryRequestSerialNumberService.findByOutboundDeliveryRequestAndPartNumber(outboundDn.getId(),
								i.getPartNumberId());
						drsnList.forEach(drsn -> {
							DeliveryRequestSerialNumber outboundDrsn = outboundDrsnList.stream().filter(j -> j.getPackingDetail().equals(drsn.getPackingDetail())).findFirst()
									.get();
							drsn.setSerialNumber(outboundDrsn.getSerialNumber());
							deliveryRequestSerialNumberService.save(drsn);
							outboundDrsnList.removeIf(j -> j.getId().equals(outboundDrsn.getId()));
							System.out.println("outboundDrsnList : "+outboundDrsnList);
						});
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private Boolean validateStorageStep2() {
		if (deliveryRequest.getDetailList().stream().filter(i -> i.getTmpQuantity() % i.getPacking().getQuantity() != 0).count() > 0)
			return FacesContextMessages.ErrorMessages("Packing Qty should be an integer");

		return true;
	}

	private Boolean validateStorageStep4() {
		HashSet<String> set = new HashSet<>();

		int newItemsListSize = 0;

		for (StockRow row : deliveryRequest.getStockRowList()) {
			if (row.getId() != null)
				continue;

			newItemsListSize++;
			if (UtilsFunctions.compareDoubles(row.getTmpQuantity(), row.getQuantity(), 4) != 0) {
				FacesContextMessages.ErrorMessages("please compelte the from");
				return false;
			}
			set.add(row.getPartNumber().getId() + ";" + row.getStatus().ordinal());
		}

		if (set.size() != newItemsListSize) {
			FacesContextMessages.ErrorMessages("The combination part number and status must be unique");
			return false;
		}

		if (deliveryRequest.getStockRowList().stream().filter(i -> i.getQuantity() % i.getPacking().getQuantity() != 0).count() > 0)
			return FacesContextMessages.ErrorMessages("Packing Qty should be an integer");

		return true;
	}

	private Boolean validateStorageStep5() {
		HashSet<String> set = new HashSet<>();

		int newItemsListSize = 0;
		for (StockRow row : deliveryRequest.getStockRowList()) {
			if (row.getId() != null)
				continue;

			newItemsListSize++;
			if (UtilsFunctions.compareDoubles(row.getTmpQuantity(), row.getQuantity(), 4) != 0) {
				FacesContextMessages.ErrorMessages("please compelte the from");
				return false;
			}
			if (row.getLocation() == null) {
				FacesContextMessages.ErrorMessages("Location should not be null");
				return false;
			}
			set.add(row.getPartNumber().getId() + ";" + row.getStatus().ordinal() + ";" + row.getLocation().getId());
		}

		if (set.size() != newItemsListSize) {
			FacesContextMessages.ErrorMessages("The combination part number,status and location must be unique");
			return false;
		}

		if (deliveryRequest.getStockRowList().stream().filter(i -> i.getQuantity() % i.getPacking().getQuantity() != 0).count() > 0)
			return FacesContextMessages.ErrorMessages("Packing Qty should be an integer");

		return true;
	}

	public void initTmpQuantites() {
		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList())
			detail.setTmpQuantity(detail.getRemainingQuantity());
	}

	public void initTmpQuantitesIfOk() {
		if (deliveryRequestComment.getIsOk())
			initTmpQuantites();
	}

	public void initStockRowList() {
		Date currentDate = new Date();
		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList())
			if (detail.getTmpQuantity() > 0.0) {
				detail.setRemainingQuantity(detail.getRemainingQuantity() - detail.getTmpQuantity());
				DeliveryRequestDetail inboundDeliveryRequestDetail = deliveryRequestDetailService
						.findByDeliveryRequestAndPartNumber(deliveryRequest.getId(), detail.getPartNumber().getId()).get(0);
				deliveryRequest.getStockRowList().add(new StockRow(detail, detail.getTmpQuantity(), detail.getTmpQuantity(), detail.getPartNumber(), deliveryRequest, true,
						deliveryRequest.getOriginNumber(), deliveryRequest, inboundDeliveryRequestDetail, currentDate, detail.getPacking()));
			}
	}

	public void changeStockRowQuantityListener(StockRow row, int index) {
		if (UtilsFunctions.compareDoubles(row.getQuantity(), 0.0, 4) <= 0)
			FacesContextMessages.ErrorMessages("Quantity should be greather than 0");
		else if (UtilsFunctions.compareDoubles(row.getTmpQuantity(), row.getQuantity(), 4) != 0) {
			DeliveryRequestDetail inboundDeliveryRequestDetail = deliveryRequestDetailService
					.findByDeliveryRequestAndPartNumber(deliveryRequest.getId(), row.getPartNumber().getId()).get(0);
			deliveryRequest.getStockRowList().add(++index,
					new StockRow(row.getTmpQuantity() - row.getQuantity(), row.getTmpQuantity() - row.getQuantity(), row.getPartNumber(), deliveryRequest, row.getStatus(),
							deliveryRequest.getOriginNumber(), deliveryRequest, inboundDeliveryRequestDetail, inboundDeliveryRequestDetail, row.getPacking()));
			row.setTmpQuantity(row.getQuantity());
		}
	}

	public void removeStockRow(StockRow row, int index) {
		if (!row.getInitial()) {
			if (deliveryRequest.getStockRowList().stream().filter(i -> UtilsFunctions.compareDoubles(i.getQuantity(), i.getTmpQuantity(), 4) != 0).count() > 0)
				return;
			StockRow previousRow = deliveryRequest.getStockRowList().get(index - 1);
			previousRow.setQuantity(previousRow.getQuantity() + row.getQuantity());
			previousRow.setTmpQuantity(previousRow.getQuantity());
			deliveryRequest.getStockRowList().remove(index);
		}
	}

	public void cancelStockRowQuantityChange(StockRow row) {
		row.setQuantity(row.getTmpQuantity());
	}

	public void setSameLocation() {
		try {
			Location firstLocation = deliveryRequest.getStockRowList().get(0).getLocation();
			if (firstLocation != null)
				for (StockRow sr : deliveryRequest.getStockRowList())
					sr.setLocation(firstLocation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ADD DETAIL FROM EXCEL
	public void uploadExcelFile(FileUploadEvent event) throws IOException {
		if (deliveryRequest.getIsInbound() || deliveryRequest.getIsXbound())
			try {
				List<DeliveryRequestDetail> list = deliveryRequestDetailService.readInboundFile(event.getFile().getInputstream(), deliveryRequest);
				if (list != null)
					deliveryRequest.getDetailList().addAll(list);
			} catch (DeliveryRequestExcelFileException e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return;
			}
		else if (deliveryRequest.getIsOutbound())
			try {
				deliveryRequestDetailSelectionList.clear();
				Map<String, Double> map = deliveryRequestDetailService.readOutboundFile(event.getFile().getInputstream(), deliveryRequest, deliveryRequestDetailList1);
				for (DeliveryRequestDetail detail : deliveryRequestDetailList1) {
					if (map.containsKey(detail.getKey())) {
						detail.setQuantity(map.get(detail.getKey()));
						deliveryRequestDetailSelectionList.add(detail);
					}
				}
			} catch (DeliveryRequestExcelFileException e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return;
			}
	}

	// handover
	public Boolean canHandover() {
		return Arrays.asList(DeliveryRequestStatus.APPROVED2, DeliveryRequestStatus.PARTIALLY_DELIVRED).contains(deliveryRequest.getStatus()) //
				&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())
						|| cacheView.hasDelegation(deliveryRequest.getProject().getId()));
	}

	public void handover() {
		if (!canHandover())
			return;
		String oldRequesterFullName = service.findRequesterFullName(deliveryRequest.getId());
		deliveryRequest.setRequester(userService.findOneLight(deliveryRequest.getRequesterUsername()));
		deliveryRequest.addHistory(
				new DeliveryRequestHistory("Handover", sessionView.getUser(), "DN Handover from " + oldRequesterFullName + " to " + deliveryRequest.getRequesterFullName()));
		service.save(deliveryRequest);
		initViewPage();
	}

	/*
	 * XBOUND WORKFLOW
	 */

	public Boolean canDeliverDeliveryRequest() {
		return canDeliverDeliveryRequest(deliveryRequest);
	}

	public Boolean canDeliverDeliveryRequest(DeliveryRequest deliveryRequest) {
		return DeliveryRequestType.XBOUND.equals(deliveryRequest.getType()) && DeliveryRequestStatus.APPROVED1.equals(deliveryRequest.getStatus())
				&& sessionView.isTheConnectedUser(deliveryRequest.getRequester());
	}

	public void deliverDeliveryRequest() {
		if (isViewPage) {
			deliverDeliveryRequest(deliveryRequest);
			// refreshDeliveryRequest();
		} else if (pageIndex == 7) {
			for (DeliveryRequest deliveryRequest : list4)
				deliverDeliveryRequest(service.findOne(deliveryRequest.getId()));
			refreshList();
		}
	}

	public void deliverDeliveryRequest(DeliveryRequest deliveryRequest) {
		if (!canDeliverDeliveryRequest(deliveryRequest))
			return;

		deliveryRequest.setStatus(DeliveryRequestStatus.DELIVRED);
		deliveryRequest.setDate4(new Date());
		deliveryRequest.setUser4(sessionView.getUser());

		Date currentDate = new Date();

		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			deliveryRequest.getStockRowList()
					.add(new StockRow(detail, detail.getQuantity(), currentDate, deliveryRequest.getOriginNumber(), detail.getPartNumber(), deliveryRequest, detail.getPacking()));
			deliveryRequest.getStockRowList()
					.add(new StockRow(detail, -detail.getQuantity(), currentDate, deliveryRequest.getOriginNumber(), detail.getPartNumber(), deliveryRequest, detail.getPacking()));
		}

		deliveryRequest.addHistory(new DeliveryRequestHistory(deliveryRequest.getStatus().getValue(), sessionView.getUser()));
		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());

		emailService.deliveryRequestNotification(deliveryRequest);
		smsService.sendSms(deliveryRequest);
	}

	// SDM / ISM
	public Boolean showSdm() {
		if (deliveryRequest.getType() == null)
			return false;
		switch (deliveryRequest.getType()) {
		case OUTBOUND:
			return deliveryRequest.getDestinationProjectSdm();
		case INBOUND:
			return Boolean.TRUE.equals(deliveryRequest.getProjectSdm()) && InboundType.DISMANTLE.equals(deliveryRequest.getInboundType());
		default:
			return false;
		}
	}

	public Boolean showIsm() {
		if (deliveryRequest.getType() == null)
			return false;

		switch (deliveryRequest.getType()) {
		case OUTBOUND:
			return deliveryRequest.getDestinationProjectIsm();
		case INBOUND:
			return Boolean.TRUE.equals(deliveryRequest.getProjectIsm())
					&& (InboundType.DISMANTLE.equals(deliveryRequest.getInboundType()) || deliveryRequest.getIsInboundReturnFromOutboundHardwareSwap());
		default:
			return false;
		}
	}

	public Boolean canEditSdm() {
		return (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())
				|| projectService.isHardwareManager(deliveryRequest.getProject().getId(), sessionView.getUsername())
				|| (sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) || cacheView.hasDelegation(deliveryRequest.getProject().getId()))) //
				&& jobRequestDeliveryDetailService.countByDeliveryRequest(deliveryRequest.getId()) == 0;
	}

	public void editSdm() {
		if (!canEditSdm())
			return;
		service.save(deliveryRequest);
	}

	public Boolean canEditIsm() {
		return (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())
				|| projectService.isHardwareManager(deliveryRequest.getProject().getId(), sessionView.getUsername())
				|| (sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) || cacheView.hasDelegation(deliveryRequest.getProject().getId()))) //
				&& jobRequestDeliveryDetailService.countByDeliveryRequest(deliveryRequest.getId()) == 0;
	}

	public void editIsm() {
		if (!canEditIsm())
			return;
		service.save(deliveryRequest);
	}

	public Boolean canEditIsSnRequired() {

		return deliveryRequest.getIsInbound() && //
				(sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())
						|| projectService.isHardwareManager(deliveryRequest.getProject().getId(), sessionView.getUsername())) //
				&& ((deliveryRequest.getIsSnRequired() && deliveryRequestSerialNumberService.countByDeliveryRequest(deliveryRequest.getId()) == 0) //
						|| (!deliveryRequest.getIsSnRequired()) && packingDetailService.countByDeliveryRequestAndHasSerialNumber(deliveryRequest.getId()) > 0);
	}

	public void editIsSnRequired() {
		if (!canEditIsSnRequired())
			return;
		service.updateIsSnRequired(deliveryRequest.getId(), deliveryRequest.getIsSnRequired());
		service.updateMissingSerialNumber(deliveryRequest.getId(),
				deliveryRequest.getIsSnRequired() && Arrays.asList(DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED).contains(deliveryRequest.getStatus())
						? true
						: null);
	}

	/*
	 * INBOUND WORKFLOW
	 */

	// REQUEST DELIVERY REQUEST
	public Boolean canRequestDeliveryRequest() {
		return DeliveryRequestStatus.EDITED.equals(deliveryRequest.getStatus()) && sessionView.isTheConnectedUser(deliveryRequest.getRequester());
	}

	public void requestDeliveryRequest() {
		if (!canRequestDeliveryRequest())
			return;
		deliveryRequest.setStatus(DeliveryRequestStatus.REQUESTED);
		deliveryRequest.setDate2(new Date());
		deliveryRequest.addHistory(new DeliveryRequestHistory(deliveryRequest.getStatus().getValue(), sessionView.getUser()));
		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());
		// emailService.deliveryRequestNotification(deliveryRequest);
		// smsService.sendSms(deliveryRequest);
		// refreshDeliveryRequest();
	}

	// APPROVE DELIVERY REQUEST
	public Boolean canApprove() {
		return canApprove(deliveryRequest);
	}

	public Boolean canApprove(DeliveryRequest deliveryRequest) {
		return canApprovePm(deliveryRequest) || canApproveHm(deliveryRequest);
	}

	public void approve(DeliveryRequest deliveryRequest) {
		if (canApprovePm(deliveryRequest))
			approvePm(deliveryRequest);
		else if (canApproveHm(deliveryRequest))
			approveHm(deliveryRequest);
	}

//	public void approve() {
//		approve(deliveryRequest);
//	}

	public void approve() {
		if (isViewPage) {
			approve(deliveryRequest);
			deliveryRequest = service.findOne(deliveryRequest.getId());
		} else if (isListPage && pageIndex == 3) {
			for (DeliveryRequest deliveryRequest : list4)
				approve(service.findOne(deliveryRequest.getId()));
			refreshList();
		}
	}

	public Boolean canApprovePm() {
		return canApprovePm(deliveryRequest);
	}

	public Boolean canApprovePm(DeliveryRequest deliveryRequest) {
		return DeliveryRequestStatus.REQUESTED.equals(deliveryRequest.getStatus())
				&& (sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) || cacheView.hasDelegation(deliveryRequest.getProject().getId()));
	}

	public void approvePm(DeliveryRequest deliveryRequest) {
		if (!canApprovePm(deliveryRequest))
			return;
		deliveryRequest.setStatus(DeliveryRequestStatus.APPROVED1);
		deliveryRequest.setDate3(new Date());
		deliveryRequest.setUser3(sessionView.getUser());
		deliveryRequest.addHistory(new DeliveryRequestHistory(deliveryRequest.getStatus().getValue(), sessionView.getUser()));
		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());

		// TransportationRequest transportationRequest =
		// transportationRequestService.findByDeliveryRequest(deliveryRequest.getId());
		// if (transportationRequest != null)
		// transportationRequestView.approveTransportationRequest(transportationRequest);

		// smsService.sendSms(deliveryRequest);
	}

//	public void approvePm() {
//		if (isViewPage) {
//			approvePm(deliveryRequest);
//			// refreshDeliveryRequest();
//		} else if (pageIndex == 3) {
//			for (DeliveryRequest deliveryRequest : list4)
//				approvePm(service.findOne(deliveryRequest.getId()));
//			refreshList();
//		}
//	}

	// approve dn hm
	public Boolean canApproveHm() {
		return canApproveHm(deliveryRequest);
	}

	public Boolean canApproveHm(DeliveryRequest deliveryRequest) {
		return (deliveryRequest.getIsOutbound() || deliveryRequest.getIsInbound()) //
				&& DeliveryRequestStatus.APPROVED1.equals(deliveryRequest.getStatus()) //
				&& projectService.isHardwareManager(deliveryRequest.getProject().getId(), sessionView.getUsername());
	}

	public void approveHm(DeliveryRequest deliveryRequest) {
		if (!canApproveHm(deliveryRequest))
			return;
		deliveryRequest.setStatus(DeliveryRequestStatus.APPROVED2);
		deliveryRequest.setDate8(new Date());
		deliveryRequest.setUser8(sessionView.getUser());
		deliveryRequest.addHistory(new DeliveryRequestHistory(deliveryRequest.getStatus().getValue(), sessionView.getUser()));
		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());
		if (deliveryRequest.getIsInboundReturnFromOutboundHardwareSwap())
			service.updateHardwareSwapInboundIdAndStatus(deliveryRequest.getOutboundDeliveryRequestReturnId(), deliveryRequest.getId(), deliveryRequest.getStatus());
		emailService.deliveryRequestNotification(deliveryRequest);

	}

//	public void approveHm() {
//		if (isViewPage) {
//			approveHm(deliveryRequest);
//		} else if (pageIndex == 3) {
//			for (DeliveryRequest deliveryRequest : list4)
//				approveHm(service.findOne(deliveryRequest.getId()));
//			refreshList();
//		}
//	}

	// REJECT DELIVERY REQUEST
	public Boolean canRejectDeliveryRequest() {
		return canRejectDeliveryRequest(deliveryRequest);
	}

	public Boolean canRejectDeliveryRequest(DeliveryRequest deliveryRequest) {
		return canApprovePm(deliveryRequest) || canApproveHm(deliveryRequest);
	}

	public String rejectDeliveryRequest(DeliveryRequest deliveryRequest) {
		if (!canRejectDeliveryRequest(deliveryRequest))
			return null;

		if (deliveryRequest.getIsInbound() && jobRequestDeliveryDetailService.countByDeliveryRequest(deliveryRequest.getId()) > 0) {
			FacesContextMessages.ErrorMessages(deliveryRequest.getReference() + " could not be cancelled as it’s alredy mapped with JR, Please clear the JR mapping First");
			return null;
		}

		Set<Integer> boqListToUpdate = boqService.getAssociatedBoqIdListWithDeliveryRequest(deliveryRequest.getId());

		deliveryRequest.setStatus(DeliveryRequestStatus.REJECTED);
		deliveryRequest.setDate6(new Date());
		deliveryRequest.setUser6(sessionView.getUser());

		deliveryRequest.addHistory(new DeliveryRequestHistory(deliveryRequest.getStatus().getValue(), sessionView.getUser()));

		deliveryRequest.clearBoqMappingList();

		Integer poId = null;
		if (deliveryRequest.getPo() != null) {
			poId = deliveryRequest.getPo().getId();
			deliveryRequest.setPo(null);
			deliveryRequest.setMissingPo(null);
		}
		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());

		emailService.deliveryRequestNotification(deliveryRequest);
		// smsService.sendSms(deliveryRequest);

		if (deliveryRequest.getTransportationRequest() != null)
			transportationRequestService.rejectTransportationRequest(deliveryRequest.getTransportationRequest(), sessionView.getUser(), "DN Rejected");
		if (projectCross != null && projectCross.getIdprojectcross() != null)
			projectCrossService.delete(projectCross.getIdprojectcross());

		if (deliveryRequest.getIsInboundReturn())
			service.updateIsFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn().getId(),
					deliveryRequestDetailService.isOutboundDeliveryRequestFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn()));

		appLinkService.deleteByDeliveryRequest(deliveryRequest.getId());
		boqService.updateTotalUsedQuantity(boqListToUpdate);
		if (poId != null)
			poService.updateIlogisticsStatus(poId);

		jobRequestDeliveryDetailService.deleteByDeliveryRequest(deliveryRequest.getId());

		return addParameters(viewPage, "faces-redirect=true", "id=" + deliveryRequest.getId());
	}

	public void rejectDeliveryRequest() {
		if (isViewPage) {
			rejectDeliveryRequest(deliveryRequest);
			// refreshDeliveryRequest();
		} else if (pageIndex == 3) {
			for (DeliveryRequest deliveryRequest : list4)
				rejectDeliveryRequest(service.findOne(deliveryRequest.getId()));
			refreshList();
		}
	}

	// CLEAR BOQ MAAPING
	public Boolean canClearBoqMapping() {
		return Boolean.FALSE.equals(deliveryRequest.getAutoBoqMapped()) //
				&& (sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) || sessionView.isTheConnectedUser(deliveryRequest.getRequester())) //
				&& !deliveryRequest.getBoqMappingList().isEmpty();
	}

	public String clearBoqMapping() {
		if (!canClearBoqMapping())
			return null;
		service.clearBoqMapping(deliveryRequest);
		return addParameters(viewPage, "faces-redirect=true", "id=" + deliveryRequest.getId());
	}

	// CANCEL DELIVERY REQUEST
	public Boolean canCancelDeliveryRequest() {
		return (Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED).contains(deliveryRequest.getStatus())
				&& sessionView.isTheConnectedUser(deliveryRequest.getRequester()))
				|| (Arrays.asList(DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2).contains(deliveryRequest.getStatus())
						&& (sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())
								|| cacheView.getDelegatedProjectList().contains(deliveryRequest.getProjectId())))//
						&& deliveryRequest.getAcceptance() == null;
	}

	public String cancelDeliveryRequest() {
		if (!canCancelDeliveryRequest())
			return null;
		if (deliveryRequest.getTransportationRequest() != null && Arrays
				.asList(TransportationRequestStatus.ASSIGNED, TransportationRequestStatus.PICKEDUP, TransportationRequestStatus.DELIVERED, TransportationRequestStatus.ACKNOWLEDGED)
				.contains(deliveryRequest.getTransportationRequest().getStatus())) {
			FacesContextMessages.ErrorMessages("can not cancel DN --> associated TR status is : " + deliveryRequest.getTransportationRequest().getStatus().getValue());
			return null;
		}

		if (deliveryRequest.getIsInbound() && jobRequestDeliveryDetailService.countByDeliveryRequest(deliveryRequest.getId()) > 0) {
			FacesContextMessages.ErrorMessages(deliveryRequest.getReference() + " could not be cancelled as it’s alredy mapped with JR, Please clear the JR mapping First");
			return null;
		}

		Set<Integer> boqListToUpdate = boqService.getAssociatedBoqIdListWithDeliveryRequest(deliveryRequest.getId());

		deliveryRequest.setStatus(DeliveryRequestStatus.CANCELED);
		deliveryRequest.setDate7(new Date());
		deliveryRequest.setUser7(sessionView.getUser());

		deliveryRequest.addHistory(new DeliveryRequestHistory(deliveryRequest.getStatus().getValue(), sessionView.getUser()));
		deliveryRequest.clearBoqMappingList();
		Integer poId = null;
		if (deliveryRequest.getPo() != null) {
			poId = deliveryRequest.getPo().getId();
			deliveryRequest.setPo(null);
			deliveryRequest.setMissingPo(null);
		}
		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());

		if (deliveryRequest.getTransportationRequest() != null)
			transportationRequestService.cancelTransportationRequest(deliveryRequest.getTransportationRequest().getId(), sessionView.getUser(), "DN Canceled");

		if (projectCross != null && projectCross.getIdprojectcross() != null)
			projectCrossService.delete(projectCross.getIdprojectcross());
		if (deliveryRequest.getIsInboundReturn())
			service.updateIsFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn().getId(),
					deliveryRequestDetailService.isOutboundDeliveryRequestFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn()));

		appLinkService.deleteByDeliveryRequest(deliveryRequest.getId());
		boqService.updateTotalUsedQuantity(boqListToUpdate);
		if (poId != null)
			poService.updateIlogisticsStatus(poId);

		jobRequestDeliveryDetailService.deleteByDeliveryRequest(deliveryRequest.getId());

		if (deliveryRequest.getIsInboundReturnFromOutboundHardwareSwap())
			service.updateHardwareSwapInboundIdAndStatus(deliveryRequest.getOutboundDeliveryRequestReturnId(), deliveryRequest.getId(), deliveryRequest.getStatus());

		return addParameters(viewPage, "faces-redirect=true", "id=" + deliveryRequest.getId());
	}

	// ACKNOWLEDGE DELIVERY REQUEST
	public Boolean canAcknowledgeDeliveryRequest(DeliveryRequest deliveryRequest) {
		try {
			return DeliveryRequestType.OUTBOUND.equals(deliveryRequest.getType()) //
					&& DeliveryRequestStatus.DELIVRED.equals(deliveryRequest.getStatus()) //
					&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) //
							|| sessionView.isTheConnectedUser(deliveryRequest.getToUserUsername())//
							|| (sessionView.getIsExternalPm() //
									&& sessionView.getIsSupplierUser() //
									&& sessionView.getUser().getSupplierId().equals(deliveryRequest.getDeliverToSupplierId()) //
									&& projectAssignmentService.isUserHavingActiveAssignmentInProject(deliveryRequest.getDestinationProjectId(), sessionView.getUsername())) //
							|| (sessionView.getIsExternalPm() //
									&& sessionView.getIsCustomerUser() //
									&& sessionView.getUser().getCustomerId().equals(deliveryRequest.getDeliverToCustomerId()) //
									&& projectAssignmentService.isUserHavingActiveAssignmentInProject(deliveryRequest.getDestinationProjectId(), sessionView.getUsername())));
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean canAcknowledgeDeliveryRequest() {
		return canAcknowledgeDeliveryRequest(deliveryRequest);
	}

	public void acknowledgeDeliveryRequest(DeliveryRequest deliveryRequest) {
		if (!canAcknowledgeDeliveryRequest(deliveryRequest))
			return;
		deliveryRequest.setStatus(DeliveryRequestStatus.ACKNOWLEDGED);
		deliveryRequest.setDate5(new Date());
		deliveryRequest.setUser5(sessionView.getUser());
		deliveryRequest.addHistory(new DeliveryRequestHistory(deliveryRequest.getStatus().getValue(), sessionView.getUser()));
		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());
	}

	public void acknowledgeDeliveryRequest() {
		if (isViewPage)
			acknowledgeDeliveryRequest(deliveryRequest);
		else if (pageIndex == 2) {
			for (DeliveryRequest deliveryRequest : list4)
				acknowledgeDeliveryRequest(service.findOne(deliveryRequest.getId()));
			refreshList();
		}
	}

	// COMMENTS MANAGEMENT
//	public void initComment() {
//		System.out.println("initComment !");
//		deliveryRequestComment = new DeliveryRequestComment();
//	}
//
//	public Boolean canAddComment() {
//		return sessionView.isTheConnectedUser(deliveryRequest.getRequester()) //
//				|| sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())//
//				|| cacheView.hasDelegation(deliveryRequest.getProject().getId())//
//				|| sessionView.isTheConnectedUser(userService.findLobManagerByDeliveryRequest(deliveryRequest.getId()))//
//				|| (deliveryRequest.getWarehouse() != null && cacheView.getWarehouseList().contains(deliveryRequest.getWarehouse().getId()));
//	}
//
//	public void addComment() {
//		if (!canAddComment())
//			return;
//		deliveryRequestComment.setDate(new Date());
//		deliveryRequestComment.setParent(deliveryRequest);
//		deliveryRequestComment.setUser(sessionView.getUser());
//		deliveryRequest.getCommentList().add(deliveryRequestComment);
//		service.save(deliveryRequest);
//		deliveryRequest = service.findOne(deliveryRequest.getId());
//		deliveryRequest.init();
//		initCommentsVariables();
//	}
//
//	public void initCommentsVariables() {
//		Map<String, List<DeliveryRequestComment>> map = new HashMap<>();
//		for (DeliveryRequestComment comment : deliveryRequest.getCommentList()) {
//			String date = UtilsFunctions.getFormattedDate(comment.getDate());
//			if (!map.containsKey(date))
//				map.put(date, new ArrayList<DeliveryRequestComment>());
//			map.get(date).add(comment);
//		}
//
//		datesTab = map.keySet().toArray(new String[map.keySet().size()]);
//		commentsTab = new DeliveryRequestComment[datesTab.length][];
//		for (int i = 0; i < datesTab.length; i++)
//			commentsTab[i] = map.get(datesTab[i]).toArray(new DeliveryRequestComment[map.get(datesTab[i]).size()]);
//	}

	// calculate unit cost
	public Boolean canCalculateUnitCost() {
		Double totalAppLink = appLinkView.getTotalAmount1();
		return deliveryRequest.getIsInboundNew() //
				&& DeliveryRequestStatus.DELIVRED.equals(deliveryRequest.getStatus()) //
						& !deliveryRequest.getBoqMappingList().isEmpty() //
				&& totalAppLink > 0 && Math.abs(totalAppLink - deliveryRequest.getTotalCost()) / totalAppLink >= 0.001;
	}

	public void calculateUnitCost() {
		if (!canCalculateUnitCost())
			return;
		service.updateDetailListUnitCost(deliveryRequest.getId());
		deliveryRequest = service.findOne(deliveryRequest.getId());
	}

	// comments
	private DeliveryRequestComment comment = new DeliveryRequestComment();

	public Boolean canAddComment() {
		return true;
	}

	public void addComment() {
		if (!canAddComment())
			return;
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		deliveryRequest.addComment(comment);
		deliveryRequest = service.saveAndRefresh(deliveryRequest);
	}

	public Boolean canDeleteComment(DeliveryRequestComment comment) {
		return sessionView.isTheConnectedUser(comment.getUser());
	}

	public void deleteComment() {
		if (!canDeleteComment(comment))
			return;
		deliveryRequest.removeComment(comment);
		deliveryRequest = service.saveAndRefresh(deliveryRequest);
	}

	// SAVE DELIVERYREQUEST
	public Boolean canSaveDeliveryRequest() {
		if (isAddPage || isListPage)
			return sessionView.isUser() || sessionView.getIsPm() || cacheView.getDelegatedProjectList().size() > 1;
		if (isEditPage || isViewPage)
			return (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) && !DeliveryRequestType.OUTBOUND.equals(deliveryRequest.getType())
					&& Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED).contains(deliveryRequest.getStatus()))
					|| (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) && DeliveryRequestType.OUTBOUND.equals(deliveryRequest.getType())
							&& Arrays.asList(DeliveryRequestStatus.EDITED).contains(deliveryRequest.getStatus()));
		return false;
	}

	// SAVE BY STEPS
	public void saveDeliveryRequestPreviousStep() {
		Boolean isProjectStock = ProjectTypes.STOCK.getValue().equals(projectService.getType(deliveryRequest.getProjectId()));
		if (step == 5 && !(isProjectStock && ((deliveryRequest.getIsInbound() && !deliveryRequest.getIsInboundReturn()) || deliveryRequest.getIsXbound())))
			step--;
		if (step > 1)
			step--;
	}

	public String saveDeliveryRequestNextStep() {
		if (!canSaveDeliveryRequest())
			return addParameters(listPage, "faces-redirect=true");
		switch (step) {
		case 0:
			System.err.println("step0");
			step++;
			break;
		case 1:
			System.err.println("step1");
			if (!validateStep1())
				return null;

			if (DeliveryRequestType.OUTBOUND.equals(deliveryRequest.getType())) {
				findRemainingDetailListByProjectAndWarehouse();
				fillDetailSelectionList();
			}

			step++;
			break;
		case 2:
			System.err.println("step2");
			if (!validateStep2())
				return null;
			step++;
//			if (DeliveryRequestType.OUTBOUND.equals(deliveryRequest.getType())) {
//				findRemainingDetailListByProjectAndWarehouse();
//				fillDetailSelectionList();
//			}
			if (deliveryRequest.getIsInboundReturn() || deliveryRequest.getIsInboundTransfer()) {
				findRemainingDetailListByOutboundDeliveryRequest();
				deliveryRequestDetailList2 = deliveryRequestDetailList1 = deliveryRequestDetailList1.stream(). //
						filter(i -> deliveryRequest.getOwnerType().equals(i.getTmpOwnerType()) && deliveryRequest.getOwnerId().equals(i.getTmpOwnerId()))
						.collect(Collectors.toList());
				fillDetailSelectionList();

				if (isAddPage && deliveryRequest.getIsInboundReturnFromOutboundHardwareSwap())
					deliveryRequestDetailList1.stream().forEach(i -> {
						i.setQuantity(i.getRemainingQuantity());
						deliveryRequestDetailSelectionList.add(i);
					});

			}
			break;
		case 3:
			System.err.println("step3");
			if (deliveryRequest.getIsOutbound() //
					|| deliveryRequest.getIsInboundReturn() //
					|| deliveryRequest.getIsInboundTransfer() //
					|| (deliveryRequest.getIsInboundNew() && getIsPoNeeded() && deliveryRequest.getPoId() != null))
				fillDetailListFromSelection();
			if (!validateStep3())
				return null;
			step++;
			if (deliveryRequest.getIsInbound() || deliveryRequest.getIsXbound())
				initPackingData();
			else
				step++;
			if (isAddPage)
				initToNotifyList();
			break;
		case 4:
			System.err.println("step5");
			if (!validateStep4())
				return null;
			step++;
			break;
		case 5:
			System.err.println("step6");
			step++;
			break;
		case 6:
			System.err.println("step7");
			if (!StringUtils.isBlank(deliveryRequestComment.getContent())) {
				deliveryRequestComment.setDate(new Date());
				deliveryRequestComment.setTitle(isAddPage ? "Delivery Request Creation" : "Delivery Request Edition");
				deliveryRequestComment.setParent(deliveryRequest);
				deliveryRequestComment.setUser(sessionView.getUser());
				deliveryRequest.addComment(deliveryRequestComment);
			}
			step++;
			preSaveDeliveryRequest();
			break;
		case 7:
			return saveDeliveryRequest();
		default:
			break;
		}
		return null;
	}

	private Boolean validateStep1() {
//		if (deliveryRequest.getIsOutbound() && service.countPendingAcknowledgementIdList(sessionView.getUsername()) > 0)
//			return FacesContextMessages.ErrorMessages(
//					"Please note that you can not create a new outbond DN unless you acknowledge the previously delivered DN(s). The maximum deadline to acknowledge the delivered DN is 5 working days");

		return true;
	}

	public List<String> getFilterList() {
		System.out.println("getFilterList");
		if (deliveryRequest.getFilterType() != null)
			switch (deliveryRequest.getFilterType()) {
			case "Brand":
				return deliveryRequestDetailList1.stream().filter(i -> StringUtils.isNotBlank(i.getPartNumberBrandName())).map(i -> i.getPartNumberBrandName()).distinct()
						.collect(Collectors.toList());
			case "Category":
				return deliveryRequestDetailList1.stream().filter(i -> StringUtils.isNotBlank(i.getPartNumberCategoryName())).map(i -> i.getPartNumberCategoryName()).distinct()
						.collect(Collectors.toList());
			case "Type":
				return deliveryRequestDetailList1.stream().filter(i -> StringUtils.isNotBlank(i.getPartNumberTypeName())).map(i -> i.getPartNumberTypeName()).distinct()
						.collect(Collectors.toList());
			case "Part Number":
				return deliveryRequestDetailList1.stream().filter(i -> StringUtils.isNotBlank(i.getPartNumberName())).map(i -> i.getPartNumberName()).distinct()
						.collect(Collectors.toList());
			case "Purchase PO":
				return deliveryRequestDetailList1.stream().filter(i -> StringUtils.isNotBlank(i.getInboundPoNumero()))
						.map(i -> i.getInboundPoNumero() + "-" + i.getInboundPoSupplierName()).distinct().collect(Collectors.toList());
			case "Inbound DN":
				return deliveryRequestDetailList1.stream().filter(i -> StringUtils.isNotBlank(i.getInboundDeliveryRequestReference()))
						.map(i -> i.getInboundDeliveryRequestReference()).distinct().collect(Collectors.toList());
			case "Origin DN Number":
				return deliveryRequestDetailList1.stream().filter(i -> StringUtils.isNotBlank(i.getOriginNumber())).map(i -> i.getOriginNumber()).distinct()
						.collect(Collectors.toList());
			default:
				break;
			}

		return new ArrayList<String>();
	}

	public void changeFilterListener() {
		if (!deliveryRequest.getFilter())
			deliveryRequestDetailList2 = deliveryRequestDetailList1;
	}

	public void filter() {
		if (deliveryRequest.getFilterType() != null)
			switch (deliveryRequest.getFilterType()) {
			case "Brand":
				deliveryRequestDetailList2 = deliveryRequestDetailList1.stream().filter(i -> deliveryRequest.getFilterValue().equals(i.getPartNumberBrandName()))
						.collect(Collectors.toList());
				break;
			case "Category":
				deliveryRequestDetailList2 = deliveryRequestDetailList1.stream().filter(i -> deliveryRequest.getFilterValue().equals(i.getPartNumberCategoryName()))
						.collect(Collectors.toList());
				break;
			case "Type":
				deliveryRequestDetailList2 = deliveryRequestDetailList1.stream().filter(i -> deliveryRequest.getFilterValue().equals(i.getPartNumberTypeName()))
						.collect(Collectors.toList());
				break;
			case "Part Number":
				deliveryRequestDetailList2 = deliveryRequestDetailList1.stream().filter(i -> deliveryRequest.getFilterValue().equals(i.getPartNumberName()))
						.collect(Collectors.toList());
				break;
			case "Purchase PO":
				deliveryRequestDetailList2 = deliveryRequestDetailList1.stream()
						.filter(i -> deliveryRequest.getFilterValue().equals(i.getInboundPoNumero() + "-" + i.getInboundPoSupplierName())).collect(Collectors.toList());
				break;
			case "Inbound DN":
				deliveryRequestDetailList2 = deliveryRequestDetailList1.stream().filter(i -> deliveryRequest.getFilterValue().equals(i.getInboundDeliveryRequestReference()))
						.collect(Collectors.toList());
				break;
			case "Origin DN Number":
				deliveryRequestDetailList2 = deliveryRequestDetailList1.stream().filter(i -> deliveryRequest.getFilterValue().equals(i.getOriginNumber()))
						.collect(Collectors.toList());
				break;
			}

	}

	private void initPackingData() {
		deliveryRequest.getDetailList().forEach(i -> i.getPartNumber().setTmpPackingList(packingService.findByPartNumberAndActive(i.getPartNumber().getId())));
		// if tmp packing list size == 1 then select this item as packing
		deliveryRequest.getDetailList().stream().filter(i -> i.getPartNumber().getTmpPackingList().size() == 1)
				.forEach(i -> i.setPacking(i.getPartNumber().getTmpPackingList().get(0)));
//		changePackingListener();
	}

	private Boolean validateStep4() {
		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			if (detail.getPacking() == null)
				return FacesContextMessages.ErrorMessages("Packing Field should not be null");
			if (detail.getQuantity() % detail.getPacking().getQuantity() != 0)
				return FacesContextMessages.ErrorMessages("Packing Qty should be an integer");
		}

		return true;
	}

	private Boolean validateStep2() {
		if (deliveryRequest.getIsOutbound() && deliveryRequest.getIsForTransfer() && deliveryRequest.getDestination() == null)
			return FacesContextMessages.ErrorMessages("Destination Warehouse should not be null in case of Transfer");
		return true;
	}

	public void initToNotifyList() {
		deliveryRequest.getToNotifyList().clear();
		deliveryRequest.setProject(projectService.findOne2(deliveryRequest.getProjectId()));
		if (deliveryRequest.getIsOutbound()) {
			deliveryRequest.setDestinationProject(projectService.findOne2(deliveryRequest.getDestinationProjectId()));
			deliveryRequest.setOriginNumber("");
		}
		Set<User> toNotifyUserSet = new HashSet<User>();
		toNotifyUserSet.add(deliveryRequest.getProject().getCostcenter().getLob().getManager());
		toNotifyUserSet.add(deliveryRequest.getProject().getManager());
		toNotifyUserSet.addAll(deliveryRequest.getProject().getManagerList().stream().map(i -> i.getUser()).collect(Collectors.toSet()));
		// toNotifyUserSet.addAll(userService.findByProjectAssignment(deliveryRequest.getProject().getId(),
		// true));
		toNotifyUserSet.addAll(userService.findByProjectDelegation(deliveryRequest.getProject().getId(), true));
		if (deliveryRequest.getIsOutbound()) {
			toNotifyUserSet.add(deliveryRequest.getDestinationProject().getCostcenter().getLob().getManager());
			toNotifyUserSet.add(deliveryRequest.getDestinationProject().getManager());
			toNotifyUserSet.addAll(deliveryRequest.getDestinationProject().getManagerList().stream().map(i -> i.getUser()).collect(Collectors.toSet()));
			toNotifyUserSet.addAll(userService.findByProjectAssignment(deliveryRequest.getDestinationProject().getId(), true));
			toNotifyUserSet.addAll(userService.findByProjectDelegation(deliveryRequest.getDestinationProject().getId(), true));
			toNotifyUserSet.addAll(userService.findByCustomerOrSupplierAndHavingAssignement(deliveryRequest.getDeliverToCustomerId(), deliveryRequest.getDeliverToSupplierId(),
					deliveryRequest.getDestinationProjectId()));
			if (deliveryRequest.getToUserUsername() != null) {
				deliveryRequest.setToUser(userService.findOne(deliveryRequest.getToUserUsername()));
				toNotifyUserSet.add(deliveryRequest.getToUser());
			}
		}
		if (getIsCustomerRequesterDataNeeded())
			toNotifyUserSet.add(userService.findOneLight(deliveryRequest.getTmpExternalRequesterUsername()));

		toNotifyUserSet.stream().filter(user -> Boolean.TRUE.equals(user.getActive())).forEach(i -> deliveryRequest.addToNotify(new ToNotify(i)));
	}

	public void addToNotifyItem() {
		User toNotifyUser = userService.findOne(toNotifyUserUsername);
		if (deliveryRequest.getToNotifyList().stream().filter(i -> i.getInternalResource().getUsername().equals(toNotifyUser.getUsername())).count() == 0)
			deliveryRequest.getToNotifyList().add(new ToNotify(toNotifyUser, deliveryRequest));
	}

	public void removeToNotifyItem(int index) {
		deliveryRequest.getToNotifyList().get(index).setDeliveryRequest(null);
		deliveryRequest.getToNotifyList().remove(index);
	}

	private void preSaveDeliveryRequest() {
		try {
			deliveryRequest.clearTimeLine();
			deliveryRequest.setStatus(DeliveryRequestStatus.EDITED);
			deliveryRequest.setDate1(new Date());

			if (getIsPoNeeded() && deliveryRequest.getPoId() != null) {
				deliveryRequest.setPo(poService.findOne(deliveryRequest.getPoId()));
				if (deliveryRequest.getContainsBoqMapping() == null)
					deliveryRequest.setContainsBoqMapping(false);
			}

			if (getIsCustomerRequesterDataNeeded())
				if (!StringUtils.isBlank(deliveryRequest.getTmpExternalRequesterUsername()))
					deliveryRequest.setExternalRequester(userService.findOne(deliveryRequest.getTmpExternalRequesterUsername()));

			if (deliveryRequest.getIsOutbound() || deliveryRequest.getIsXbound()) {
				deliveryRequest.setEndCustomer(customerService.findOne(deliveryRequest.getEndCustomerId()));
				if (deliveryRequest.getToUserUsername() != null)
					deliveryRequest.setToUser(userService.findOne(deliveryRequest.getToUserUsername()));
				else
					deliveryRequest.setToUser(null);
			}

			if ((deliveryRequest.getIsInbound() && deliveryRequest.getOrigin() != null) || (deliveryRequest.getIsOutbound() && deliveryRequest.getDestination() != null)
					|| (deliveryRequest.getIsXbound() && deliveryRequest.getOrigin() != null && deliveryRequest.getDestination() != null))
				if (deliveryRequest.getTransportationNeeded() && deliveryRequest.getTransporterId() != null)
					deliveryRequest.setTransporter(transporterService.findOne(deliveryRequest.getTransporterId()));
				else
					deliveryRequest.setTransporter(null);
			else
				deliveryRequest.setTransportationNeeded(false);

			if (deliveryRequest.getIsInbound() || deliveryRequest.getIsXbound()) {
				if (deliveryRequest.getOwnerType() != null)
					switch (deliveryRequest.getOwnerType()) {
					case COMPANY:
						deliveryRequest.setCompany(companyService.findOne(deliveryRequest.getCompanyId()));
						deliveryRequest.setCustomer(null);
						deliveryRequest.setSupplier(null);
						break;
					case CUSTOMER:
						deliveryRequest.setCompany(null);
						deliveryRequest.setSupplier(null);
						break;
					case SUPPLIER:
						deliveryRequest.setCompany(null);
						deliveryRequest.setCustomer(null);
						deliveryRequest.setSupplier(supplierService.findOne(deliveryRequest.getSupplierId()));
						break;

					default:
						break;
					}
			} else if (deliveryRequest.getIsOutbound()) {

				deliveryRequest.setCompany(null);
				deliveryRequest.setCustomer(null);
				deliveryRequest.setSupplier(null);

				Set<CompanyType> inboundOwnerTypeSet = deliveryRequest.getDetailList().stream().map(i -> i.getInboundDeliveryRequest().getOwnerType()).collect(Collectors.toSet());
				if (inboundOwnerTypeSet.size() == 1) {
					CompanyType ownerType = inboundOwnerTypeSet.iterator().next();
					switch (ownerType) {
					case COMPANY:
						Set<Integer> companyIdSet = deliveryRequest.getDetailList().stream().map(i -> i.getInboundDeliveryRequest().getCompanyId()).collect(Collectors.toSet());
						if (companyIdSet.size() == 1) {
							deliveryRequest.setOwnerType(ownerType);
							deliveryRequest.setCompany(companyService.findOne(companyIdSet.iterator().next()));
						}
						break;
					case CUSTOMER:
						Set<Integer> customerIdSet = deliveryRequest.getDetailList().stream().map(i -> i.getInboundDeliveryRequest().getCustomerId()).collect(Collectors.toSet());
						if (customerIdSet.size() == 1) {
							deliveryRequest.setOwnerType(ownerType);
							deliveryRequest.setCustomer(customerService.findOne(customerIdSet.iterator().next()));
						}
						break;
					case SUPPLIER:
						Set<Integer> supplierIdSet = deliveryRequest.getDetailList().stream().map(i -> i.getInboundDeliveryRequest().getSupplierId()).collect(Collectors.toSet());
						if (supplierIdSet.size() == 1) {
							deliveryRequest.setOwnerType(ownerType);
							deliveryRequest.setSupplier(supplierService.findOne(supplierIdSet.iterator().next()));
						}
						break;
					default:
						break;
					}
				}

			}

			if (deliveryRequest.getDeliverToCompanyType() != null)
				switch (deliveryRequest.getDeliverToCompanyType()) {
				case COMPANY:
					deliveryRequest.setDeliverToCompany(companyService.findOne(deliveryRequest.getDeliverToCompanyId()));
					deliveryRequest.setDeliverToCustomer(null);
					deliveryRequest.setDeliverToSupplier(null);
					deliveryRequest.setDeliverToOther(null);
					break;
				case CUSTOMER:
					deliveryRequest.setDeliverToCompany(null);
					deliveryRequest.setDeliverToCustomer(customerService.findOne(deliveryRequest.getDeliverToCustomerId()));
					deliveryRequest.setDeliverToSupplier(null);
					deliveryRequest.setDeliverToOther(null);
					break;
				case SUPPLIER:
					deliveryRequest.setDeliverToCompany(null);
					deliveryRequest.setDeliverToCustomer(null);
					deliveryRequest.setDeliverToSupplier(supplierService.findOne(deliveryRequest.getDeliverToSupplierId()));
					deliveryRequest.setDeliverToOther(null);
					break;
				default:
					break;
				}

			if (deliveryRequest.getIsInboundReturn() && deliveryRequest.getOutboundDeliveryRequestReturn().getInboundPo() != null)
				deliveryRequest.setPo(deliveryRequest.getOutboundDeliveryRequestReturn().getInboundPo());

			for (Integer detailId : toDeleteDetailList)
				deliveryRequestDetailService.delete(detailId);

			for (DeliveryRequestDetail detail : deliveryRequest.getDetailList())
				detail.setPartNumber(partNumberService.findOne(detail.getPartNumber().getId()));

			if (DeliveryRequestType.INBOUND.equals(deliveryRequest.getType()))
				for (DeliveryRequestDetail detail : deliveryRequest.getDetailList())
					detail.setRemainingQuantity(detail.getQuantity());

			if (isAddPage) {
				deliveryRequest.setReferenceNumber(service.getMaxReferenceNumber(deliveryRequest.getType()) + 1);
				deliveryRequest.setQrKey(UtilsFunctions.generateQrKey());
			}

			if (!deliveryRequest.getIsInboundReturn())
				deliveryRequest.setOutboundDeliveryRequestReturn(null);
			if (!deliveryRequest.getIsInboundTransfer())
				deliveryRequest.setOutboundDeliveryRequestTransfer(null);

			if (CompanyType.COMPANY.equals(deliveryRequest.getOwnerType()) && //
					(deliveryRequest.getIsInboundNew() //
							|| (deliveryRequest.getIsOutbound() && !deliveryRequest.getIsForReturn() && !deliveryRequest.getIsForTransfer())))
				deliveryRequest.setMissingPo(deliveryRequest.getPo() == null);

			// fill details currencies
			if (deliveryRequest.getPo() != null)
				if (deliveryRequest.getIsInbound())
					deliveryRequest.getDetailList().forEach(d -> d.setPurchaseCurrency(deliveryRequest.getPo().getCurrency()));
				else if (deliveryRequest.getIsOutbound())
					deliveryRequest.getDetailList().forEach(d -> d.setPriceCurrency(deliveryRequest.getPo().getCurrency()));

			if (deliveryRequest.getIsInboundNew())
				deliveryRequest.getDetailList().forEach(d -> {
					d.setCostCurrency(companyService.findAccountingCurrency(companyService.findIdByProject(deliveryRequest.getProject().getId())));
				});

		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
			e.printStackTrace();
		}
	}

	private void fillDetailSelectionList() {
		deliveryRequestDetailSelectionList.clear();
		Set<String> keys = new HashSet<>();
		if (deliveryRequest.getId() != null)
			for (DeliveryRequestDetail detail : deliveryRequest.getDetailList())
				if (keys.add(detail.getKey()))
					deliveryRequestDetailSelectionList.add(detail);
	}

	private void fillDetailListFromSelection() {
		try {
			if (deliveryRequest.getId() != null)
				for (DeliveryRequestDetail detail : deliveryRequest.getDetailList())
					deliveryRequestDetailService.delete(detail.getId());
			deliveryRequest.getDetailList().clear();

			for (DeliveryRequestDetail detail : deliveryRequestDetailSelectionList) {
				DeliveryRequestDetail inboundDeliveryRequestDetail = null;
				if (deliveryRequest.getIsOutbound())
					inboundDeliveryRequestDetail = deliveryRequestDetailService
							.findByDeliveryRequestAndPartNumber(detail.getInboundDeliveryRequest().getId(), detail.getPartNumber().getId()).get(0);
				deliveryRequest.getDetailList()
						.add(new DeliveryRequestDetail(detail.getQuantity(), detail.getPartNumber(), deliveryRequest, detail.getStatus(), detail.getOriginNumber(),
								detail.getInboundDeliveryRequest(), inboundDeliveryRequestDetail, detail.getUnitCost(),
								deliveryRequest.getIsForTransfer() ? detail.getUnitCost() : 0.0, detail.getPacking()));

			}

		} catch (Exception e) {
			e.printStackTrace();
			deliveryRequest.setDetailList(new ArrayList<DeliveryRequestDetail>());
		}

	}

	public void setQuantitiesToZero() {
		for (DeliveryRequestDetail detail : deliveryRequestDetailSelectionList)
			detail.setQuantity(0.0);
	}

	public void setQuantitiesToRemainingQuantities() {
		for (DeliveryRequestDetail detail : deliveryRequestDetailSelectionList)
			detail.setQuantity(detail.getRemainingQuantity());
	}

	public void findRemainingDetailListByProjectAndWarehouse() {
		if (DeliveryRequestType.OUTBOUND.equals(deliveryRequest.getType()) && deliveryRequest.getProjectId() != null && deliveryRequest.getWarehouse() != null) {

			deliveryRequestDetailList2 = deliveryRequestDetailList1 = deliveryRequestDetailService.findRemainingByProjectAndWarehouse(deliveryRequest.getProjectId(),
					deliveryRequest.getWarehouse().getId(), deliveryRequest);

			// second filter
			if (deliveryRequest.getFilter())
				switch (deliveryRequest.getFilterType()) {
				case "Inbound DN":
					deliveryRequestDetailList2 = deliveryRequestDetailList1 = deliveryRequestDetailList1.stream()
							.filter(i -> i.getInboundDeliveryRequestReference().equals(deliveryRequest.getFilterValue())).collect(Collectors.toList());
					break;
				case "Origin DN Number":
					deliveryRequestDetailList2 = deliveryRequestDetailList1 = deliveryRequestDetailList1.stream()
							.filter(i -> i.getOriginNumber().equals(deliveryRequest.getFilterValue())).collect(Collectors.toList());
					break;
				default:
					break;
				}

			// third filter by Po.boqlist (dont use in edit case)
			if (getIsPoNeeded() && deliveryRequest.getPoId() != null) {
				Set<Integer> boqSet = boqService.findPartNumberIdListByPoAndHavingRemainingQuantity(deliveryRequest.getPoId());
				Set<Integer> equivalenceSet = partNumberEquivalenceService.findPartNumberIdListByEquivalence(boqSet);
				deliveryRequestDetailList2 = deliveryRequestDetailList1 = deliveryRequestDetailList1.stream()
						.filter(item -> item.getQuantity() > 0 || boqSet.contains(item.getPartNumber().getId()) || equivalenceSet.contains(item.getPartNumber().getId()))
						.collect(Collectors.toList());
				// item.getQuantity()>0 for edit case
			}
		}

	}

	public void findRemainingDetailListByPo() {
		if (deliveryRequest.getIsInbound() && getIsPoNeeded() && deliveryRequest.getPoId() != null)
			deliveryRequestDetailList2 = deliveryRequestDetailList1 = deliveryRequestDetailService.findRemainingByPo(deliveryRequest.getPoId());
	}

	public void findRemainingDetailListByOutboundDeliveryRequest() {
		if (deliveryRequest.getIsInboundReturn() && deliveryRequest.getOutboundDeliveryRequestReturn() != null)
			deliveryRequestDetailList2 = deliveryRequestDetailList1 = deliveryRequestDetailService.findRemainingByOutboundDeliveryRequestReturn(deliveryRequest,
					deliveryRequest.getOutboundDeliveryRequestReturn());
		else if (deliveryRequest.getIsInboundTransfer() && deliveryRequest.getOutboundDeliveryRequestTransfer() != null)
			deliveryRequestDetailList2 = deliveryRequestDetailList1 = deliveryRequestDetailService.findRemainingByOutboundDeliveryRequestTransfer(deliveryRequest,
					deliveryRequest.getOutboundDeliveryRequestTransfer());
	}

	private Boolean ignoreMultipleStatusWarning = false;

	private Boolean validateStep3() {
		if (deliveryRequest.getDetailList().isEmpty()) {
			FacesContextMessages.ErrorMessages("Detail List should not be empty");
			return false;
		}
		HashSet<Integer> tmp = new HashSet<>();
		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			if (detail.getPartNumber() == null) {
				FacesContextMessages.ErrorMessages("Part Number should not be null");
				return false;
			}
			if (detail.getQuantity() == null || detail.getQuantity().compareTo(0.0) == 0) {
				FacesContextMessages.ErrorMessages("Quantity should be greather than 0");
				return false;
			}
			tmp.add(detail.getPartNumber().getId());
		}

		if (deliveryRequest.getIsInbound()) {
			if (tmp.size() != deliveryRequest.getDetailList().size())
				return FacesContextMessages.ErrorMessages("same part number can not have multiple rows");
			if (deliveryRequest.getDetailList().stream().filter(i -> !partNumberService.getHasPacking(i.getPartNumber().getId())).count() > 0) {
				deliveryRequest.getDetailList().stream().filter(i -> !partNumberService.getHasPacking(i.getPartNumber().getId()))
						.forEach(i -> FacesContextMessages.ErrorMessages(i.getPartNumber().getName() + " dosent have packing informations, please correct part number and retry"));
				return false;
			}

		}

		if (deliveryRequest.getIsOutbound()) {
			if (deliveryRequest.getDetailList().stream().filter(i -> i.getQuantity() % i.getPacking().getQuantity() != 0).count() > 0)
				return FacesContextMessages.ErrorMessages("Packing Quantity should be an integer");
			if (deliveryRequest.getIsForTransfer()) {
				Map<Integer, Integer> mapPartNumberPacking = new HashMap<Integer, Integer>();
				for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
					mapPartNumberPacking.putIfAbsent(detail.getPartNumber().getId(), detail.getPacking().getId());
					if (!mapPartNumberPacking.get(detail.getPartNumber().getId()).equals(detail.getPacking().getId()))
						return FacesContextMessages.ErrorMessages("In Case of Planned Transfer, PN should have same packing ");
				}
			}
			if (deliveryRequest.getDetailList().stream().map(i -> i.getInboundDeliveryRequest().getOwnerType() + ";" + i.getInboundDeliveryRequest().getOwnerId()).distinct()
					.count() > 1)
				return FacesContextMessages.ErrorMessages("All the selected items should belong to the same Owner");

			Set<StockRowStatus> stockRowStatusList = deliveryRequest.getDetailList().stream().map(i -> i.getStatus()).distinct().collect(Collectors.toSet());
			if (!ignoreMultipleStatusWarning && stockRowStatusList.contains(StockRowStatus.BRAND_NEW) && stockRowStatusList.size() > 1) {
				ignoreMultipleStatusWarning = true;
				warningMessage = "Part Number with different status are included in the DN, are you sure ?";
				execJavascript("PF('warningDlg').show();");
				return FacesContextMessages.WarningMessages(warningMessage);
			}

		}

		return true;
	}

	public Boolean noPackingDetailHasSerialNumber() {
		return deliveryRequest.getDetailList().stream()
				.filter(d -> d.getPacking() != null && d.getPacking().getDetailList().stream().filter(packingDetail -> packingDetail.getHasSerialnumber()).count() > 0)
				.count() == 0;
	}

//	public void changePackingListener() {
//		deliveryRequest.setIsSnRequired(!noPackingDetailHasSerialNumber());
//	}

	// check database status
	public Boolean checkDatabaseStatus(Integer id, DeliveryRequestStatus status) {
		return status.equals(service.findStatusById(id));
	}

	public Boolean checkDatabaseStatus(Integer id, DeliveryRequestStatus... status) {
		for (int i = 0; i < status.length; i++)
			if (checkDatabaseStatus(id, status[i]))
				return true;
		return false;
	}

	public String saveDeliveryRequest() {

		// to be sure
		if (isAddPage) {
			deliveryRequest.setReferenceNumber(service.getMaxReferenceNumber(deliveryRequest.getType()) + 1);
			deliveryRequest.setQrKey(UtilsFunctions.generateQrKey());
		}

		if (DeliveryRequestType.XBOUND.equals(deliveryRequest.getType()))
			deliveryRequest.setWarehouse(null);

		if (deliveryRequest.getReference() == null)
			deliveryRequest.generateReference();

		deliveryRequest.addHistory(new DeliveryRequestHistory(isAddPage ? "Created" : "Edited", sessionView.getUser()));

		deliveryRequest = service.save(deliveryRequest);

		if (deliveryRequest.getIsInboundReturn()) {
			service.updateIsFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn().getId(),
					deliveryRequestDetailService.isOutboundDeliveryRequestFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn()));
			service.updateReturnInboundsUnitPrice(deliveryRequest.getOutboundDeliveryRequestReturn().getId());
		}

		if (deliveryRequest.getIsInboundTransfer())
			service.updateIsForTransfer(deliveryRequest.getOutboundDeliveryRequestTransfer().getId(), true);

		if (deliveryRequest.getIsOutbound())
			service.updateOutboundInboundPo(deliveryRequest.getId());

		return addParameters(viewPage, "faces-redirect=true", "id=" + deliveryRequest.getId());
	}

	public Boolean validateDeliveryRequest() {
		if (deliveryRequest.getDetailList().isEmpty()) {
			FacesContextMessages.ErrorMessages("Detail List should not be empty");
			return false;
		}

		HashSet<Integer> tmp = new HashSet<>();
		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			if (detail.getPartNumber() == null) {
				FacesContextMessages.ErrorMessages("Part Number should not be null");
				return false;
			}
			if (detail.getQuantity().compareTo(0.0) == 0) {
				FacesContextMessages.ErrorMessages("Quantity should be greather than 0");
				return false;
			}
			tmp.add(detail.getPartNumber().getId());
		}

		if (tmp.size() != deliveryRequest.getDetailList().size()) {
			FacesContextMessages.ErrorMessages("same part number can not have multiple rows");
			return false;
		}

		return true;
	}

	public List<CompanyType> getOwnerTypeList() {
		switch (deliveryRequest.getInboundType()) {
		case NEW:
			List<CompanyType> result = new ArrayList<CompanyType>();
			if (deliveryRequest.getProject().getCompanyWarehousing())
				result.add(CompanyType.COMPANY);
			if (deliveryRequest.getProject().getCustomerWarehousing())
				result.add(CompanyType.CUSTOMER);
			if (deliveryRequest.getProject().getSupplierWarehousing())
				result.add(CompanyType.SUPPLIER);
			return result;
		case RETURN:
			return stockRowService.findOwnerTypeListByDeliveryRequest(outboundDeliveryRequestId);
		case TRANSFER:
			return stockRowService.findOwnerTypeListByDeliveryRequest(outboundDeliveryRequestId);
		}
		return null;
	}

	// DELETE DELIVERYREQUEST
	public Boolean canDeleteDeliveryRequest() {
		return Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REJECTED).contains(deliveryRequest.getStatus())
				&& sessionView.isTheConnectedUser(deliveryRequest.getRequester());
	}

	public String deleteDeliveryRequest() {
		if (!canDeleteDeliveryRequest())
			return null;
		try {
			Set<Integer> boqListToUpdate = boqService.getAssociatedBoqIdListWithDeliveryRequest(deliveryRequest.getId());
			Integer poId = deliveryRequest.getPo() != null ? deliveryRequest.getPo().getId() : null;
			service.delete(deliveryRequest);
			boqService.updateTotalUsedQuantity(boqListToUpdate);
			if (poId != null)
				poService.updateIlogisticsStatus(poId);
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
			return null;
		}

		return addParameters(listPage, "state=0", "faces-redirect=true");
	}

	// DETAILS MANAGEMENT
	public Boolean canAddDetail() {
		return true;
	}

	public void addDetail() {
		if (canAddDetail())
			deliveryRequest.getDetailList().add(new DeliveryRequestDetail(deliveryRequest));
	}

	public Boolean canRemoveDetail() {
		return true;
	}

	public void removeDetail(int index) {
		if (canRemoveDetail()) {
			Integer id = deliveryRequest.getDetailList().get(index).getId();
			if (id != null)
				toDeleteDetailList.add(id);
			deliveryRequest.getDetailList().remove(index);
		}

	}

	public void initAddDetailForInboundTransfer() {
		deliveryRequestDetail = new DeliveryRequestDetail(deliveryRequest);
	}

	public void addDetailForInboundTransfer() {
		if (deliveryRequestDetailList2.stream().map(item -> item.getPartNumber().getId()).collect(Collectors.toList()).contains(deliveryRequestDetail.getPartNumber().getId()))
			return;
		deliveryRequestDetail.setRemainingQuantity(deliveryRequestDetail.getQuantity());
		deliveryRequestDetailList2.add(deliveryRequestDetail);
	}

	// GENERATE EMAIL NOTIFICATION
	public String generateEmailNotification() {
		return service.generateEmailNotification(deliveryRequest, null, false);
	}

	/*
	 * Generate PDF
	 */

	public void generatePdf() {
		downloadPath = service.generatePdf(deliveryRequest);
	}

	public void generatePdf2() {
		downloadPath = service.generatePdfLink(deliveryRequest);
	}

	public void generateStamp() {
		downloadPath = service.generateStamp(deliveryRequest);
	}

	// smsRef
	public Boolean canUpdateSmsRef() {
		return Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2)
				.contains(deliveryRequest.getStatus())
				&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()));
	}

	public void updateSmsRef() {
		if (!canUpdateSmsRef())
			return;
		service.updateSmsRef(deliveryRequest.getId(), deliveryRequest.getSmsRef());
	}

	// approximativeStoragePeriod
	public Boolean canUpdateApproximativeStoragePeriod() {
		return sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername());
	}

	public void updateApproximativeStoragePeriod() {
		if (!canUpdateApproximativeStoragePeriod())
			return;
		service.updateApproximativeStoragePeriod(deliveryRequest.getId(), deliveryRequest.getApproximativeStoragePeriod());
	}

	// requestDate
	public Boolean canUpdateRequestDate() {
		return sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername());
	}

	public void updateRequestDate() {
		if (!canUpdateRequestDate())
			return;
		service.updateRequestDate(deliveryRequest.getId(), deliveryRequest.getRequestDate());
	}

	// requestFrom
	public Boolean canUpdateRequestFrom() {
		return sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername());
	}

	public void updateRequestFrom() {
		if (!canUpdateRequestFrom())
			return;
		service.updateRequestFrom(deliveryRequest.getId(), deliveryRequest.getRequestFrom());
	}

	// external requester
	public Boolean canUpdateExternalRequester() {
		return sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername());
	}

	public void updateExternalRequester() {
		if (!canUpdateExternalRequester())
			return;
		service.updateExternalRequester(deliveryRequest.getId(), userService.findOne(deliveryRequest.getTmpExternalRequesterUsername()));
	}

	// needed delivery date
	public Boolean canUpdateNeededDeliveryDate() {
		return Arrays
				.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2,
						DeliveryRequestStatus.PARTIALLY_DELIVRED)
				.contains(deliveryRequest.getStatus())
				&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())
						|| cacheView.hasDelegation(deliveryRequest.getProject().getId()));
	}

	public void updateNeededDeliveryDate() {
		if (!canUpdateNeededDeliveryDate())
			return;

		service.updateNeededDeliveryDate(deliveryRequest.getId(), deliveryRequest.getNeededDeliveryDate());
	}

	// transportation needed
	public Boolean canUpdateTransportationNeeded() {
		if (deliveryRequest.getTransportationNeeded() == null || !deliveryRequest.getTransportationNeeded())
			return Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2)
					.contains(deliveryRequest.getStatus())
					&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()))
					&& (deliveryRequest.getIsInbound() ? deliveryRequest.getOrigin() != null
							: deliveryRequest.getIsOutbound() ? deliveryRequest.getDestination() != null
									: deliveryRequest.getOrigin() != null && deliveryRequest.getDestination() != null);
		else
			return deliveryRequest.getTransportationRequest() == null || (deliveryRequest.getTransportationRequest() != null
					&& Arrays.asList(TransportationRequestStatus.REJECTED, TransportationRequestStatus.CANCELED).contains(deliveryRequest.getTransportationRequest().getStatus()));
	}

	public void updateTransportationNeeded() {
		service.updateTransportationNeeded(deliveryRequest.getId(), deliveryRequest.getTransportationNeeded());
	}

	// destination project
	public void updateDestinationProject() {
		Boolean isProjectStock = ProjectTypes.STOCK.getValue().equals(projectService.getType(deliveryRequest.getProjectId()));
		if (!isProjectStock) {
			deliveryRequest.setDestinationProjectId(deliveryRequest.getProjectId());
			changeDestinationProjectListener();
		}

		if (deliveryRequest.getId() != null) {
			if (projectCrossService.countByDeliveryRequest(deliveryRequest.getId()) > 0) {
				projectCrossService.delete(projectCrossService.findByDeliveryRequest(deliveryRequest.getId()).getIdprojectcross());
				projectCrossService.addCrossCharge(deliveryRequest);
			}
		}
	}

	public void changeDestinationProjectListener() {
		deliveryRequest.setDestinationProject(projectService.findOne2(deliveryRequest.getDestinationProjectId()));
//		Integer destinationCustomerId = projectService.getCustomerId(deliveryRequest.getDestinationProjectId());
		Integer destinationCustomerId = deliveryRequest.getDestinationProject().getCustomer().getId();
		deliveryRequest.setEndCustomerId(destinationCustomerId);

		Boolean isProjectStock = ProjectTypes.STOCK.getValue().equals(projectService.getType(deliveryRequest.getProjectId()));
		if (!isProjectStock && deliveryRequest.getProjectId() != null && !deliveryRequest.getProjectId().equals(deliveryRequest.getDestinationProjectId())) {
			Integer customerId = projectService.getCustomerId(deliveryRequest.getProjectId());
			String str = customerId.equals(destinationCustomerId) ? "the same" : "different";
			FacesContextMessages.WarningMessages("You are trying to deliver Goods to different project under " + str + " customer, are you sure?");
		}

		Boolean sdm = deliveryRequest.getDestinationProject().getSdm();
		deliveryRequest.setSdm(Boolean.TRUE.equals(sdm));

	}

	public Boolean canEditDestinationProject() {
		return sessionView.isTheConnectedUser(deliveryRequest.getRequester());
	}

	public void editDestinationProject() {
		if (!canEditDestinationProject())
			return;
		deliveryRequest.setDestinationProject(projectService.findOne(deliveryRequest.getDestinationProjectId()));
		if (deliveryRequest.getIsOutbound() && projectService.isDstrProject(deliveryRequest.getDestinationProjectId()))
			deliveryRequest.setMissingPo(deliveryRequest.getPo() == null);
		service.save(deliveryRequest);
		refreshDeliveryRequest();
		projectCrossService.deleteAndReCreateCrossCharge(deliveryRequest);
	}

	// files
	private DeliveryRequestFile file;
	private String fileType;

	public Boolean canAddFile() {
		return true;
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		DeliveryRequestFile modelFile = new DeliveryRequestFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		deliveryRequest.addFile(modelFile);
		synchronized (DeliveryRequestView.class) {
			deliveryRequest.calculateMissingOutboundDeliveryNote();
			deliveryRequest.calculateCountFiles();
			deliveryRequest = service.saveAndRefresh(deliveryRequest);
		}
	}

	public void handleFileUpload2(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		DeliveryRequestFile deliveryRequestFile = new DeliveryRequestFile(file, fileType, event.getFile().getFileName(), sessionView.getUser(), deliveryRequest);
		deliveryRequest.getFileList().add(deliveryRequestFile);
		deliveryRequest.calculateMissingOutboundDeliveryNote();
	}

	public Boolean canDeleteFile(DeliveryRequestFile file) {
		return sessionView.getInternal() && sessionView.isTheConnectedUser(file.getUser());
	}

	public void deleteFile() {
		if (!canDeleteFile(file))
			return;
		model.removeFile(file);
		model.calculateMissingOutboundDeliveryNote();
		model = service.saveAndRefresh(model);
	}

	// UNIT COST
	public Boolean showCostInformations() {
		return sessionView.getInternal() //
				&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) //
						|| sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) //
						|| cacheView.getAssignedProjectList().contains(deliveryRequest.getProject().getId()));
	}

	public Boolean canUpdateUnitCost() {
		return showCostInformations() && ((deliveryRequest.getIsInbound() && !deliveryRequest.getIsInboundReturn()) || deliveryRequest.getIsXbound());
	}

	public void updateUnitCost(DeliveryRequestDetail detail) {
		if (!canUpdateUnitCost())
			return;
		deliveryRequestDetailService.updateUnitCost(detail.getId(), detail.getUnitCost());
	}

	// UNIT PRICE
	public Boolean showPriceInformations() {
		return (sessionView.getInternal() || sessionView.getIsCustomerUser())//
				&& deliveryRequest.getIsOutbound() //
				&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) //
						|| sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())//
						|| cacheView.getAssignedProjectList().contains(deliveryRequest.getProject().getId()));
	}

	public Boolean canUpdateUnitPrice() {
		return showPriceInformations();
	}

	public void updateUnitPrice(DeliveryRequestDetail detail) {
		if (!canUpdateUnitPrice())
			return;
		deliveryRequestDetailService.updateUnitPrice(detail.getId(), detail.getUnitPrice());
		service.updateReturnInboundsUnitPrice(deliveryRequest.getId());
	}

	public void changeTypeListener() {
		findRemainingDetailListByProjectAndWarehouse();
	}

	public void changeProjectListener() {
		deliveryRequest.setProject(projectService.findOne2(deliveryRequest.getProjectId()));
		findRemainingDetailListByProjectAndWarehouse();
		updateDestinationProject();
		initOwnerType();

		Boolean ism = deliveryRequest.getProjectIsm();
		deliveryRequest.setIsm(Boolean.TRUE.equals(ism));
	}

	public Boolean canFillOwnerType() {
		return !projectService.isStockProject(deliveryRequest.getProjectId()) //
				&& (deliveryRequest.getOutboundDeliveryRequestReturn() == null || deliveryRequest.getOutboundDeliveryRequestReturn().getOwnerType() == null) //
				&& (deliveryRequest.getOutboundDeliveryRequestTransfer() == null || deliveryRequest.getOutboundDeliveryRequestTransfer().getOwnerType() == null);

	}

	public void initOwnerType() {
		if (deliveryRequest.getIsOutbound())
			return;
		if (deliveryRequest.getOwnerType() != null)
			return;

		deliveryRequest.setOwnerType(CompanyType.COMPANY);
		changeOwnerTypeListener();

		if (deliveryRequest.getIsInboundReturn() && deliveryRequest.getOutboundDeliveryRequestReturn().getOwnerType() != null) {
			deliveryRequest.setOwnerType(deliveryRequest.getOutboundDeliveryRequestReturn().getOwnerType());
			deliveryRequest.setCompany(deliveryRequest.getOutboundDeliveryRequestReturn().getCompany());
			deliveryRequest.setCustomer(deliveryRequest.getOutboundDeliveryRequestReturn().getCustomer());
			deliveryRequest.setSupplier(deliveryRequest.getOutboundDeliveryRequestReturn().getSupplier());
		}

		if (deliveryRequest.getIsInboundTransfer() && deliveryRequest.getOutboundDeliveryRequestTransfer().getOwnerType() != null) {
			deliveryRequest.setOwnerType(deliveryRequest.getOutboundDeliveryRequestTransfer().getOwnerType());
			deliveryRequest.setCompany(deliveryRequest.getOutboundDeliveryRequestTransfer().getCompany());
			deliveryRequest.setCustomer(deliveryRequest.getOutboundDeliveryRequestTransfer().getCustomer());
			deliveryRequest.setSupplier(deliveryRequest.getOutboundDeliveryRequestTransfer().getSupplier());
		}

	}

	public void changeDeliverToCompanyTypeListener() {
		if (deliveryRequest.getDeliverToCompanyType() != null)
			switch (deliveryRequest.getDeliverToCompanyType()) {
			case CUSTOMER:
				if (deliveryRequest.getDestinationProject() != null)
					deliveryRequest.setDeliverToCustomerId(deliveryRequest.getDestinationProject().getCustomer().getId());
				break;

			default:
				break;
			}
	}

	public void changeToUserListener() {
		if (deliveryRequest.getToUserUsername() == null)
			deliveryRequest.setToUser(null);
		else
			deliveryRequest.setToUser(userService.findOneLight(deliveryRequest.getToUserUsername()));
	}

	public void changeOwnerTypeListener() {
		if (deliveryRequest.getOwnerType() != null)
			switch (deliveryRequest.getOwnerType()) {
			case COMPANY:
				deliveryRequest.setCompanyId(projectService.findCompanyId(deliveryRequest.getProjectId()));
				deliveryRequest.setCompany(companyService.findOne(deliveryRequest.getCompanyId()));
				break;
			case CUSTOMER:
				deliveryRequest.setCustomer(customerService.findOne(projectService.findCustomerId(deliveryRequest.getProjectId())));
				break;

			default:
				break;
			}

	}

	public Integer getApproximativeStoragePeriodMaxValue() {
		String projectType = projectService.getType(deliveryRequest.getProjectId());
		return ProjectTypes.DELIVERY.getValue().equals(projectType) ? 90 : ProjectTypes.STOCK.getValue().equals(projectType) ? 180 : 0;
	}

	// EXTERNAL COMPANY

	public List<User> findByDeliverToEntity() {
		if (deliveryRequest.getDeliverToCompanyType() == null)
			return userService.find(false);

		switch (deliveryRequest.getDeliverToCompanyType()) {
		case CUSTOMER:
			return userService.findByCustomer(deliveryRequest.getDeliverToCustomerId());
		case SUPPLIER:
			return userService.findBySupplier(deliveryRequest.getDeliverToSupplierId());
		default:
			return null;
		}
	}

	// ORIGIN / DESTINATION MANAGEMENT
	public void setOriginSite(Site site) {
		if (site.getId() != null)
			deliveryRequest.setOrigin(siteService.findOne(site.getId()));
	}

	public void setDestinationSite(Site site) {
		if (site.getId() != null)
			deliveryRequest.setDestination(siteService.findOne(site.getId()));
	}

	// RETURN FROM OUTBOUND
	public void changeOutboundDeliveryRequestReturnListener() {
		if (deliveryRequest.getOutboundDeliveryRequestReturnId() == null)
			return;
		deliveryRequest.setOutboundDeliveryRequestReturn(service.findOne(deliveryRequest.getOutboundDeliveryRequestReturnId()));
		deliveryRequest.setOriginNumber(deliveryRequest.getOutboundDeliveryRequestReturn().getReference());
		findRemainingDetailListByOutboundDeliveryRequest();
		deliveryRequest.setProject(deliveryRequest.getOutboundDeliveryRequestReturn().getProject());
		deliveryRequest.setProjectId(deliveryRequest.getProject().getId());
		deliveryRequest.setWarehouse(deliveryRequest.getOutboundDeliveryRequestReturn().getWarehouse());
		if (deliveryRequest.getOutboundDeliveryRequestReturn().getDestination() != null)
			deliveryRequest.setOrigin(deliveryRequest.getOutboundDeliveryRequestReturn().getDestination());
//		if (deliveryRequest.getIsInboundReturnFromOutboundHardwareSwap()) {
//			deliveryRequest.setSdm(deliveryRequest.getOutboundDeliveryRequestReturn().getSdm());
//			deliveryRequest.setIsm(deliveryRequest.getOutboundDeliveryRequestReturn().getIsm());
//		}
//		if(deliveryRequest.getIsInboundReturn()) {
//			deliveryRequest.setSdm(deliveryRequest.getOutboundDeliveryRequestReturn().getSdm());
//			deliveryRequest.setIsm(deliveryRequest.getOutboundDeliveryRequestReturn().getIsm());
//		}

		// set owner
		List<LabelValue> ownerList = deliveryRequestDetailService.findOwnerList(deliveryRequest.getOutboundDeliveryRequestReturn().getId());
		if (ownerList.size() == 1)
			deliveryRequest.setOwner(ownerList.get(0));
	}

	public void changeOutboundDeliveryRequestTransferListener() {
		if (deliveryRequest.getOutboundDeliveryRequestTransferId() == null)
			return;
		deliveryRequest.setOutboundDeliveryRequestTransfer(service.findOne(deliveryRequest.getOutboundDeliveryRequestTransferId()));
		deliveryRequest.setOriginNumber(deliveryRequest.getOutboundDeliveryRequestTransfer().getReference());
		findRemainingDetailListByOutboundDeliveryRequest();
		deliveryRequest.setProjectId(deliveryRequest.getOutboundDeliveryRequestTransfer().getDestinationProject().getId());
		deliveryRequest.setWarehouse(warehouseService.findOne(deliveryRequest.getOutboundDeliveryRequestTransfer().getDestination().getWarehouse().getId()));
		deliveryRequest.setOrigin(deliveryRequest.getOutboundDeliveryRequestTransfer().getDestination());
		deliveryRequest.setTransportationNeeded(false);
	}

	// LINK PO
	public Boolean getIsPoNeeded() {
		if (deliveryRequest.getIsInboundNew())
			return CompanyType.COMPANY.equals(deliveryRequest.getOwnerType());
		else if (deliveryRequest.getIsOutbound())
			if (deliveryRequest.getIsForReturn() || deliveryRequest.getIsForTransfer())
				return false;
			else if (isAddPage && deliveryRequest.getProject() != null)
				return !deliveryRequest.getProject().getCustomerWarehousing() && !deliveryRequest.getProject().getSupplierWarehousing();
			else
				return CompanyType.COMPANY.equals(deliveryRequest.getOwnerType());
		return false;
	}

	public List<Po> getPoList() {
		if (deliveryRequest.getIsInbound())
			return poService.findByTypeAndProjectAndNotMapped(PoTypes.SUPPLIER.getValue(), deliveryRequest.getProjectId());
		else
			return poService.findByTypeAndProjectAndNotMapped(PoTypes.Customer.getValue(), deliveryRequest.getDestinationProjectId());
	}

	// CUSTOMER REQUSTER DATA
	public Boolean getIsCustomerRequesterDataNeeded() {
		return deliveryRequest.getIsOutbound() && projectService.isDeliveryProject(deliveryRequest.getProjectId());
	}

	// boq mapping
	public Boolean canMapBoq() { // same in BoqMappingView
		return deliveryRequest.getDate4() != null && deliveryRequest.getPo() != null && deliveryRequest.getBoqMappingList().isEmpty();
	}

	// EDIT PO
	public Boolean canEditPo() {
		return sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) //
				&& (deliveryRequest.getPo() == null || deliveryRequest.getBoqMappingList().isEmpty());
	}

	public void editPo() {
		if (!canEditPo())
			return;
		deliveryRequest.setPo(poService.findOne(deliveryRequest.getPo().getId()));
		if (deliveryRequest.getContainsBoqMapping() == null)
			deliveryRequest.setContainsBoqMapping(false);
		deliveryRequest.setMissingPo(false);
		// fil currencies
		if (deliveryRequest.getIsInbound())
			deliveryRequest.getDetailList().forEach(d -> d.setPurchaseCurrency(deliveryRequest.getPo().getCurrency()));
		else if (deliveryRequest.getIsOutbound())
			deliveryRequest.getDetailList().forEach(d -> d.setPriceCurrency(deliveryRequest.getPo().getCurrency()));
		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());
		deliveryRequest.init();
	}

	// edit deliver to
	public Boolean canEditDeliverTo() {
		return Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2)
				.contains(deliveryRequest.getStatus()) //
				&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) //
						|| sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) //
						|| cacheView.hasDelegation(deliveryRequest.getProject().getId())) //
		;
	}

	public void editDeliverTo() {
		if (!canEditDeliverTo())
			return;

		switch (deliveryRequest.getDeliverToCompanyType()) {
		case COMPANY:
			deliveryRequest.setDeliverToCompany(companyService.findOne(deliveryRequest.getDeliverToCompanyId()));
			deliveryRequest.setDeliverToCustomer(null);
			deliveryRequest.setDeliverToSupplier(null);
			break;
		case CUSTOMER:
			deliveryRequest.setDeliverToCompany(null);
			deliveryRequest.setDeliverToCustomer(customerService.findOne(deliveryRequest.getDeliverToCustomerId()));
			deliveryRequest.setDeliverToSupplier(null);
			break;
		case SUPPLIER:
			deliveryRequest.setDeliverToCompany(null);
			deliveryRequest.setDeliverToCustomer(null);
			deliveryRequest.setDeliverToSupplier(supplierService.findOne(deliveryRequest.getDeliverToSupplierId()));
			break;
		default:
			break;
		}

		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());
		deliveryRequest.init();

	}

	// edit sub type
	public Boolean canEditSubType() {
		return deliveryRequest.getIsOutbound() //
				&& !Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED).contains(deliveryRequest.getStatus()) //
				&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) //
						|| sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) //
						|| cacheView.hasDelegation(deliveryRequest.getProject().getId()));
	}

	public Boolean canEditIsForTransfer() {
		return Boolean.FALSE.equals(deliveryRequest.getIsForTransfer()) || service.countByOutboundDeliveryRequestTransfer(deliveryRequest.getId()) == 0;
	}

	public Boolean canEditIsForReturn() {
		return !deliveryRequest.getIsForReturn() || service.countByOutboundDeliveryRequestReturn(deliveryRequest.getId()) == 0;
	}

	public void editSubType() {
		if (!canEditSubType())
			return;

		if (!deliveryRequest.getIsForReturn())
			deliveryRequest.setReturnReason(null);

		service.save(deliveryRequest);
		deliveryRequest = service.findOne(deliveryRequest.getId());
		deliveryRequest.init();

	}

	// Return / Transfer from outbound
	public Boolean canReturnFromOutbound() {
		return sessionView.getInternal() //
				&& deliveryRequest.getIsOutbound() //
				&& Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED).contains(deliveryRequest.getStatus()) //
				&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) //
						|| sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername()) //
						|| cacheView.getAssignedProjectList().contains(deliveryRequest.getProject().getId())) //
				&& !deliveryRequestDetailService.isOutboundDeliveryRequestFullyReturned(deliveryRequest);

	}

	public Boolean canTransferFromOutbound() {
		return sessionView.getInternal() //
				&& deliveryRequest.getIsOutbound() && Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED).contains(deliveryRequest.getStatus())
				&& (sessionView.isTheConnectedUser(deliveryRequest.getRequester()) || sessionView.isTheConnectedUser(deliveryRequest.getProject().getManager().getUsername())
						|| cacheView.getAssignedProjectList().contains(deliveryRequest.getProject().getId()))
				&& deliveryRequest.getIsForTransfer() && service.countByOutboundDeliveryRequestTransfer(deliveryRequest.getId()) == 0;
	}

	public String getTransferStatus() {
		return service.getTransferStatus(deliveryRequest.getId());
	}

	public List<DeliveryRequestDetail> getTransferOrReturnSituation(List<StockRow> deliveredList, List<DeliveryRequestDetail> inProgressList) {
		List<DeliveryRequestDetail> result = new ArrayList<DeliveryRequestDetail>();

		Map<PartNumber, Double> sum = deliveryRequest.getDetailList().stream()
				.collect(Collectors.groupingBy(DeliveryRequestDetail::getPartNumber, Collectors.summingDouble(DeliveryRequestDetail::getQuantity)));

		sum.keySet().forEach(pn -> {
			DeliveryRequestDetail deliveryRequestDetail = new DeliveryRequestDetail();
			deliveryRequestDetail.setPartNumber(pn);
			deliveryRequestDetail.setQuantity(sum.get(pn));
			deliveryRequestDetail.setTmpQuantity1(deliveredList.stream().filter(drd -> drd.getPartNumber().equals(pn)).mapToDouble(drd -> drd.getQuantity()).sum());
			deliveryRequestDetail.setTmpQuantity2(inProgressList.stream().filter(drd -> drd.getPartNumber().equals(pn)).mapToDouble(drd -> drd.getQuantity()).sum());
			result.add(deliveryRequestDetail);
		});

		return result;
	}

//	public Boolean getHasReturnedStockRowList() {
//		return stockRowService.countReturnedStockRowList(deliveryRequest.getId()) > 0;
//	}

//	public List<StockRow> getReturnedStockRowList() {
//		return stockRowService.findReturnedStockRowList(deliveryRequest.getId());
//	}

//	public Boolean getHasTransferredStockRowList() {
//		return stockRowService.countTransferredStockRowList(deliveryRequest.getId()) > 0;
//	}
//
//	public List<StockRow> getTransferredStockRowList() {
//		return stockRowService.findTransferredStockRowList(deliveryRequest.getId());
//	}

	// adjust quantity
	public Boolean canAdjustQuantity() {
		return service.canAdjustQuantity(deliveryRequest, sessionView.getUser());
//		return DeliveryRequestStatus.PARTIALLY_DELIVRED.equals(deliveryRequest.getStatus()) //
//				&& sessionView.isTheConnectedUser(deliveryRequest.getRequester()) //
//				&& deliveryRequest.getBoqMappingList().isEmpty() //
//				&& jobRequestDeliveryDetailService.countByDeliveryRequest(deliveryRequest.getId()) == 0;

	}

	public String adjustQuantity() {
		if (!canAdjustQuantity())
			return null;
		service.adjustQuantity(deliveryRequest, sessionView.getUser());
//		Iterator<DeliveryRequestDetail> it = deliveryRequest.getDetailList().iterator();
//		while (it.hasNext()) {
//			DeliveryRequestDetail detail = it.next();
//			Double srQuantity = deliveryRequest.getStockRowList().stream().filter(i -> i.getDeliveryRequestDetail().getId().equals(detail.getId())).mapToDouble(i -> i.getQuantity()).sum();
//			detail.setQuantity(srQuantity);
//			detail.setRemainingQuantity(0.0);
//			if(srQuantity.equals(0.0)) {
//				detail.setDeliveryRequest(null);
//				it.remove();
//			}
//		}
//		deliveryRequest.setIsPartial(false);
//		deliveryRequest.setStatus(DeliveryRequestStatus.DELIVRED);
//		deliveryRequest.addHistory(new DeliveryRequestHistory("Adjust Quantity", sessionView.getUser()));
//		service.save(deliveryRequest);

		return addParameters(viewPage, "faces-redirect=true", "id=" + deliveryRequest.getId());
	}

	// serial number
	public Boolean showSerialNumber() {
		if (deliveryRequest.getIsInbound())
			return deliveryRequest.getIsSnRequired();
		else if (deliveryRequest.getIsOutbound())
			return deliveryRequest.getMissingSerialNumber() != null;
		return false;
	}

	// generic
	public Boolean getisHardwareSwapInbound() {
		return service.countByHardwareSwapInboundId(id) > 0;
	}

	public List<DeliveryRequest> findByCanBeTransported() {
		return service.findByCanBeTransported(sessionView.getUsername());
	}

	public List<SelectItem> getCompanyAndSupplierAndCustomerList() {
		List<SelectItem> result = new ArrayList<>();
		SelectItemGroup g1, g2, g3;
		String projectType = projectService.getType(deliveryRequest.getProjectId());
		g1 = new SelectItemGroup("Compagnies");
		List<LabelValue> companyList = companyService.findLabelValueList();
		SelectItem[] tab1 = new SelectItem[companyList.size()];
		for (int i = 0; i < tab1.length; i++)
			tab1[i] = new SelectItem(companyList.get(i), companyList.get(i).getLabel());
		g1.setSelectItems(tab1);
		result.add(g1);
		if (!ProjectTypes.STOCK.getValue().equals(projectType)) {
			g2 = new SelectItemGroup("Customers");
			List<LabelValue> customerList = customerService.findLabelValueList();
			SelectItem[] tab2 = new SelectItem[customerList.size()];
			for (int i = 0; i < tab2.length; i++)
				tab2[i] = new SelectItem(customerList.get(i), customerList.get(i).getLabel());
			g2.setSelectItems(tab2);
			result.add(g2);
			g3 = new SelectItemGroup("Suppliers");
			List<LabelValue> supplierList = supplierService.findLabelValueList();
			SelectItem[] tab3 = new SelectItem[supplierList.size()];
			for (int i = 0; i < tab3.length; i++)
				tab3[i] = new SelectItem(supplierList.get(i), supplierList.get(i).getLabel());
			g3.setSelectItems(tab3);

			result.add(g3);
		}

		return result;
	}

	public Long countPendingJrMapping1() {
		return service.countPendingJrMapping(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.OUTBOUND, true, false);
	}

	public Long countPendingJrMapping2() {
		return service.countPendingJrMapping(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.INBOUND, true, false);
	}

	public Long countPendingJrMapping3() {
		return service.countPendingJrMapping(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.OUTBOUND, false, true);
	}

	public Long countPendingJrMapping4() {
		return service.countPendingJrMapping(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.INBOUND, false, true);
	}

	public Long countHavingRunningStock1() {
		return service.countHavingRunningStock(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.OUTBOUND, true, false);
	}

	public Long countHavingRunningStock2() {
		return service.countHavingRunningStock(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.INBOUND, true, false);
	}

	public Long countHavingRunningStock3() {
		return service.countHavingRunningStock(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.OUTBOUND, false, true);
	}

	public Long countHavingRunningStock4() {
		return service.countHavingRunningStock(sessionView.getUsername(), cacheView.getAllProjectList(), DeliveryRequestType.INBOUND, false, true);
	}

	public Long countToTransfer() {
		if (sessionView.getIsInternalPM())
			return service.countByIsForTransferAndDestinationProjectAndNotTransferred(sessionView.getUsername(), cacheView.getAssignedProjectList());
		return 0l;
	}

	public Long countToReturn() {
		if (sessionView.getIsUser())
			return service.countByIsForReturnAndNotFullyReturned(sessionView.getUsername());
		return 0l;
	}

	public Long countToDeliverXboundRequests() {
		if (sessionView.getIsUser())
			return service.countByRequester(sessionView.getUsername(), DeliveryRequestType.XBOUND, DeliveryRequestStatus.APPROVED2);
		return 0l;
	}

	public Long countToAcknowledgeRequests() {
		if (sessionView.getInternal())
			return service.countToAcknowledgeInternal(sessionView.getUsername(), cacheView.getWarehouseList());
		else if (sessionView.getIsExternalPm()) {
			if (sessionView.getIsSupplierUser())
				return service.countToAcknowledgeExternalSupplierUser(sessionView.getUsername(), sessionView.getUser().getSupplierId(), cacheView.getUserProjectList());
			if (sessionView.getIsCustomerUser())
				return service.countToAcknowledgeExternalCustomerUser(sessionView.getUsername(), sessionView.getUser().getCustomerId(), cacheView.getUserProjectList());
		}
		return 0l;
	}

	public Long countToApproveRequests() {
		if (sessionView.getIsInternalPM())
			return service.countToApprove(sessionView.getUsername(), cacheView.getDelegatedProjectList());
		return 0l;
	}

	public Long countToDeliverRequests() {
		if (sessionView.getIsWM())
			return service.countByWarehouseList(cacheView.getWarehouseList(), Arrays.asList(DeliveryRequestStatus.APPROVED2, DeliveryRequestStatus.PARTIALLY_DELIVRED));
		return 0l;
	}

	public List<DeliveryRequest> findLightByRequester() {
		return service.findLightByRequester(sessionView.getUsername());
	}

	public Long countByMissingSupplierPo() {
		return service.countByMissingPo(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), DeliveryRequestType.INBOUND);
	}

	public Long countByMissingCustomerPo() {
		return service.countByMissingPo(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), DeliveryRequestType.OUTBOUND);
	}

	public Long countByMissingCustomerBoqMapping() {
		return service.countByMissingBoqMapping(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), DeliveryRequestType.OUTBOUND);
	}

	public Long countByMissingSupplierBoqMapping() {
		return service.countByMissingBoqMapping(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAssignedProjectList(), DeliveryRequestType.INBOUND);
	}

	public Long countToAddTransport() {
		return service.countByPendingTransportation(sessionView.getUsername());
	}

	public Long countMissingSerialNumber() {
		if (sessionView.getIsWM())
			return service.countByMissingSerialNumber(cacheView.getWarehouseList());
		return 0l;
	}

	public Long countMissingExpiry() {
		if (sessionView.getIsWM())
			return service.countByMissingExpiry(cacheView.getWarehouseList());
		return 0l;
	}

	public Long countByMissingOutboundDeliveryNoteFile() {
		if (sessionView.getInternal())
			return service.countByMissingOutboundDeliveryNoteFile(sessionView.getUsername(), cacheView.getWarehouseList(), cacheView.getAllProjectList());
		else if (sessionView.getIsExternalPm()) {
			if (sessionView.getIsSupplierUser())
				return service.countByMissingOutboundDeliveryNoteFileAndDeliverToSupplier(sessionView.getUser().getSupplierId(), cacheView.getUserProjectList());
			else if (sessionView.getIsCustomerUser())
				return service.countByMissingOutboundDeliveryNoteFileAndDeliverToCustomer(sessionView.getUser().getCustomerId(), cacheView.getUserProjectList());
		}
		return 0l;
	}

	public Long countTotal() {
		Long total = 0l;
		total += countToApproveRequests() + countToTransfer();
		total += countToDeliverRequests() + countMissingSerialNumber() + countMissingExpiry();
		total += countToReturn() + countToDeliverXboundRequests();
		total += countToAcknowledgeRequests();
		return total;
	}

	// GETTERS & SETTERS
	public DeliveryRequestService getDeliveryRequestService() {
		return service;
	}

	public void setDeliveryRequestService(DeliveryRequestService service) {
		this.service = service;
	}

	public DeliveryRequest getDeliveryRequest() {
		return deliveryRequest;
	}

	public void setDeliveryRequest(DeliveryRequest deliveryRequest) {
		this.deliveryRequest = deliveryRequest;
	}

	public FileUploadView getFileUploadView() {
		return fileUploadView;
	}

	public void setFileUploadView(FileUploadView fileUploadView) {
		this.fileUploadView = fileUploadView;
	}

	public DeliveryRequestFileService getDeliveryRequestFileService() {
		return deliveryRequestFileService;
	}

	public void setDeliveryRequestFileService(DeliveryRequestFileService deliveryRequestFileService) {
		this.deliveryRequestFileService = deliveryRequestFileService;
	}

	public String getDeliveryRequestFileType() {
		return fileType;
	}

	public void setDeliveryRequestFileType(String fileType) {
		this.fileType = fileType;
	}

	public DeliveryRequestFile getDeliveryRequestFile() {
		return deliveryRequestFile;
	}

	public void setDeliveryRequestFile(DeliveryRequestFile deliveryRequestFile) {
		this.deliveryRequestFile = deliveryRequestFile;
	}

	public DeliveryRequestType getType() {
		return type;
	}

	public void setType(DeliveryRequestType type) {
		this.type = type;
	}

	public DeliveryRequestComment getDeliveryRequestComment() {
		return deliveryRequestComment;
	}

	public void setDeliveryRequestComment(DeliveryRequestComment deliveryRequestComment) {
		this.deliveryRequestComment = deliveryRequestComment;
	}

	public String[] getDatesTab() {
		return datesTab;
	}

	public void setDatesTab(String[] datesTab) {
		this.datesTab = datesTab;
	}

	public DeliveryRequestComment[][] getCommentsTab() {
		return commentsTab;
	}

	public void setCommentsTab(DeliveryRequestComment[][] commentsTab) {
		this.commentsTab = commentsTab;
	}

	@Override
	public Integer getPageIndex() {
		return pageIndex;
	}

	@Override
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public DeliveryRequestState getState() {
		return state;
	}

	public void setState(DeliveryRequestState state) {
		this.state = state;
	}

	public String getStoragePage() {
		return storagePage;
	}

	public void setStoragePage(String storagePage) {
		this.storagePage = storagePage;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public Boolean getAddOriginSite() {
		return addOriginSite;
	}

	public void setAddOriginSite(Boolean addOriginSite) {
		this.addOriginSite = addOriginSite;
	}

	public List<DeliveryRequestDetail> getDeliveryRequestDetailList1() {
		return deliveryRequestDetailList1;
	}

	public void setDeliveryRequestDetailList1(List<DeliveryRequestDetail> deliveryRequestDetailList1) {
		this.deliveryRequestDetailList1 = deliveryRequestDetailList1;
	}

	public List<DeliveryRequestDetail> getDeliveryRequestDetailList2() {
		return deliveryRequestDetailList2;
	}

	public void setDeliveryRequestDetailList2(List<DeliveryRequestDetail> deliveryRequestDetailList2) {
		this.deliveryRequestDetailList2 = deliveryRequestDetailList2;
	}

	public List<DeliveryRequestDetail> getDeliveryRequestDetailSelectionList() {
		return deliveryRequestDetailSelectionList;
	}

	public void setDeliveryRequestDetailSelectionList(List<DeliveryRequestDetail> deliveryRequestDetailSelectionList) {
		this.deliveryRequestDetailSelectionList = deliveryRequestDetailSelectionList;
	}

	public Boolean getIsStoragePage() {
		return isStoragePage;
	}

	public void setIsStoragePage(Boolean isStoragePage) {
		this.isStoragePage = isStoragePage;
	}

	public String getPreparationPage() {
		return preparationPage;
	}

	public void setPreparationPage(String preparationPage) {
		this.preparationPage = preparationPage;
	}

	public Boolean getIsPreparationPage() {
		return isPreparationPage;
	}

	public void setIsPreparationPage(Boolean isPreparationPage) {
		this.isPreparationPage = isPreparationPage;
	}

	public String getSearchDetail() {
		return searchDetail;
	}

	public void setSearchDetail(String searchDetail) {
		this.searchDetail = searchDetail;
		filterDetail(searchDetail);
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	// public static class ToNotify {
	// private String name;
	// private Boolean internal;
	// private String email;
	// private String phone;
	//
	// private Boolean notifyByEmail = true;
	// private Boolean notifyBySms = true;
	//
	// public ToNotify(String name, Boolean internal, String email, String phone) {
	// super();
	// this.name = name;
	// this.internal = internal;
	// this.email = email;
	// this.phone = phone;
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (!(obj instanceof ToNotify))
	// return false;
	// ToNotify cast = (ToNotify) obj;
	// return this.name.equals(cast.getName());
	// }
	//
	// public String getName() {
	// return name;
	// }
	//
	// public void setName(String name) {
	// this.name = name;
	// }
	//
	// public Boolean getInternal() {
	// return internal;
	// }
	//
	// public void setInternal(Boolean internal) {
	// this.internal = internal;
	// }
	//
	// public String getEmail() {
	// return email;
	// }
	//
	// public void setEmail(String email) {
	// this.email = email;
	// }
	//
	// public String getPhone() {
	// return phone;
	// }
	//
	// public void setPhone(String phone) {
	// this.phone = phone;
	// }
	//
	// public Boolean getNotifyByEmail() {
	// return notifyByEmail;
	// }
	//
	// public void setNotifyByEmail(Boolean notifyByEmail) {
	// this.notifyByEmail = notifyByEmail;
	// }
	//
	// public Boolean getNotifyBySms() {
	// return notifyBySms;
	// }
	//
	// public void setNotifyBySms(Boolean notifyBySms) {
	// this.notifyBySms = notifyBySms;
	// }
	//
	// }

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getToNotifyUserUsername() {
		return toNotifyUserUsername;
	}

	public void setToNotifyUserUsername(String toNotifyUserUsername) {
		this.toNotifyUserUsername = toNotifyUserUsername;
	}

	public DeliveryRequestDetail getDeliveryRequestDetail() {
		return deliveryRequestDetail;
	}

	public void setDeliveryRequestDetail(DeliveryRequestDetail deliveryRequestDetail) {
		this.deliveryRequestDetail = deliveryRequestDetail;
	}

	public DeliveryRequestDetailService getDeliveryRequestDetailService() {
		return deliveryRequestDetailService;
	}

	public PartNumberService getPartNumberService() {
		return partNumberService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public SupplierService getSupplierService() {
		return supplierService;
	}

	public ProjectService getProjectService() {
		return projectService;
	}

	public WarehouseService getWarehouseService() {
		return warehouseService;
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public OldEmailService getEmailService() {
		return emailService;
	}

	public TransporterService getTransporterService() {
		return transporterService;
	}

	public SmsService getSmsService() {
		return smsService;
	}

	public CacheView getCacheView() {
		return cacheView;
	}

	public StockRowService getStockRowService() {
		return stockRowService;
	}

	public UserService getUserService() {
		return userService;
	}

	public List<Integer> getToDeleteDetailList() {
		return toDeleteDetailList;
	}

	public Boolean getIsLightViewPage() {
		return isLightViewPage;
	}

	public String getLightViewPage() {
		return lightViewPage;
	}

	public ProjectCross getProjectCross() {
		return projectCross;
	}

	public void setProjectCross(ProjectCross projectCross) {
		this.projectCross = projectCross;
	}

	public String getPrintPage() {
		return printPage;
	}

	public void setPrintPage(String printPage) {
		this.printPage = printPage;
	}

	public Boolean getSelectOrigin() {
		return selectOrigin;
	}

	public void setSelectOrigin(Boolean selectOrigin) {
		this.selectOrigin = selectOrigin;
	}

	public Integer getOutboundDeliveryRequestId() {
		return outboundDeliveryRequestId;
	}

	public void setOutboundDeliveryRequestId(Integer outboundDeliveryRequestId) {
		this.outboundDeliveryRequestId = outboundDeliveryRequestId;
	}

	public DeliveryRequestComment getComment() {
		return comment;
	}

	public void setComment(DeliveryRequestComment comment) {
		this.comment = comment;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	public DeliveryRequestFile getFile() {
		return file;
	}

	public void setFile(DeliveryRequestFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public List<StockRow> getPotentialStockRowlist() {
		return potentialStockRowlist;
	}

	public void setPotentialStockRowlist(List<StockRow> potentialStockRowlist) {
		this.potentialStockRowlist = potentialStockRowlist;
	}

	public List<DeliveryRequestSerialNumber> getPotentialSerialNumberList() {
		return potentialSerialNumberList;
	}

	public void setPotentialSerialNumberList(List<DeliveryRequestSerialNumber> potentialSerialNumberList) {
		this.potentialSerialNumberList = potentialSerialNumberList;
	}

}
