package ma.azdad.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.Color;
import ma.azdad.utils.LabelValue;

@Entity

public class DeliveryRequest extends GenericModel<Integer> implements Comparable<DeliveryRequest> {

	private String reference;
	private String description;
	private Priority priority;
	private Boolean important = false;
	private Date neededDeliveryDate;
	private Date deliveryDate;
	private String originNumber = "";
	private Boolean isForReturn = false;
	private Boolean isForTransfer = false;
	private Boolean isSnRequired = false;
	private Boolean isPackingListRequired = false;
	private Integer approximativeStoragePeriod = 0;
	private Integer referenceNumber;
	private String rejectionReason;
	private Boolean transportationNeeded = false;
	private String smsRef;
	private String qrKey;
	private String returnReason;
	private Boolean isFullyReturned = false;
	private Boolean sdm = false;
	private Boolean ism = false;
	private Boolean containsBoqMapping = null;
	private Boolean missingPo = null;
	private Boolean missingOutboundDeliveryNote = false;
	private Boolean autoBoqMapped = false;
	private Integer countFiles = 0;

	private DeliveryRequestType type;
	private InboundType inboundType = InboundType.NEW;
	private DeliveryRequestStatus status = DeliveryRequestStatus.EDITED;

	private User requester;
	private Project project;
	private Project destinationProject;

	private Warehouse warehouse;
	
	private Acceptance acceptance;

	private CompanyType ownerType;
	private Company company;// Owner
	private Customer customer; // Owner
	private Supplier supplier; // Owner

	private Transporter transporter;

	private Customer endCustomer;
	// private Supplier toSupplier;
	// private Transporter toTransporter;
	private User toUser;
	private Site origin;
	private Site destination;
	private Boolean isPartial = null;
	private Location preferedLocation;

	private TransportationRequest transportationRequest;

	private CompanyType deliverToCompanyType;
	private Company deliverToCompany;
	private Customer deliverToCustomer;
	private Supplier deliverToSupplier;
	private String deliverToOther;

	private DeliveryRequest outboundDeliveryRequestReturn;
	private DeliveryRequest outboundDeliveryRequestTransfer;

	private Integer hardwareSwapInboundId;
	private DeliveryRequestStatus hardwareSwapInboundStatus;

	private Po po;

	private Date requestDate;
	private String requestFrom;
	private User externalRequester;
	private Boolean missingSerialNumber = null;
	private Boolean missingExpiry = null;

	private List<StockRow> stockRowList = new ArrayList<>();
	private List<DeliveryRequestComment> commentList = new ArrayList<>();
	private List<DeliveryRequestDetail> detailList = new ArrayList<>();
	private List<DeliveryRequestFile> fileList = new ArrayList<>();
	private List<DeliveryRequestHistory> historyList = new ArrayList<>();
	private List<ToNotify> toNotifyList = new ArrayList<>();
	private List<BoqMapping> boqMappingList = new ArrayList<>();
	private List<Issue> issueList = new ArrayList<>();

	// TIMELINE
	private Date date1; // Edited
	private Date date2; // Requested
	private Date date3; // Approved PM
	private Date date8; // Approved HM
	private Date date4; // Delivered
	private Date date5; // Acknowledged
	private Date date6; // Rejected
	private Date date7; // Canceled

	private User user3;
	private User user4;
	private User user5;
	private User user6;
	private User user7;
	private User user8;

	// count issues

	private Long countIssues1 = 0l; // (Raised, confirmed, assigned, Not resolved) & blocking = true
	private Long countIssues2 = 0l; // (Raised, confirmed, assigned, Not resolved) & blocking = false
	private Long countIssues3 = 0l; // (resolved)

	// TMP
	private LabelValue owner;
	private Integer projectId;
	private Integer destinationProjectId;
	private Integer originId;
	private Integer destinationId;
	private Integer transporterId;
	private Integer outboundDeliveryRequestTransferId;
	private Integer poId;

	private StockRowState stockRowState;

	private String tmpExternalRequesterUsername;

	private Double totalCost;
	private Double totalPrice;
	
	private Boolean filter = false;
	private String filterType;
	private String filterValue;

	// PERFORMANCE
	private String ownerName;
	private String transporterName;
	private Boolean hasTransportationRequest;

	// Queries var
	private Double qTotalCost = 0.0;
	private Double qTotalRevenue = 0.0;
	private Double qTotalCrossCharge = 0.0;
	private Double qAssociatedCost = 0.0;
	private Integer crossChargeId;

	private List<CommentGroup<DeliveryRequestComment>> commentGroupList;

	public DeliveryRequest(Integer id, Double qTotalCost, Double qTotalCrossCharge, Integer crossChargeId) {
		super(id);
		this.qTotalCost = qTotalCost;
		this.qTotalCrossCharge = qTotalCrossCharge;
		this.crossChargeId = crossChargeId;
	}

	public DeliveryRequest(Integer id, String reference, Integer referenceNumber, DeliveryRequestType type, DeliveryRequestStatus status, Project project, Date date4,
			Double qTotalCost, Double qAssociatedCostIbuy, Double qAssociatedCostIexpense, InboundType inboundType) {
		super(id);
		this.reference = reference;
		this.referenceNumber = referenceNumber;
		this.type = type;
		this.status = status;
		this.project = project;
		this.date4 = date4;
		this.qTotalCost = qTotalCost;
		this.qAssociatedCost += qAssociatedCostIbuy != null ? qAssociatedCostIbuy : 0.0;
		this.qAssociatedCost += qAssociatedCostIexpense != null ? qAssociatedCostIexpense : 0.0;
		this.inboundType = inboundType;
	}

	public DeliveryRequest(Integer id, String reference, Integer referenceNumber, DeliveryRequestType type, DeliveryRequestStatus status, Project project,
			Project destinationProject, String destinationProjectCustomerName, Date date4, Double qTotalCost, Double qTotalRevenue, Double qTotalCrossCharge, String poNumero) {
		super(id);
		this.reference = reference;
		this.referenceNumber = referenceNumber;
		this.type = type;
		this.status = status;
		this.project = project;
		this.destinationProject = destinationProject;
		this.date4 = date4;
		this.qTotalCost = qTotalCost;
		this.qTotalRevenue = qTotalRevenue;
		this.qTotalCrossCharge = qTotalCrossCharge;
		this.setDestinationProjectCustomerName(destinationProjectCustomerName);
		this.setPoNumero(poNumero);
	}

	public DeliveryRequest() {
		super();
	}

	// c1
	public DeliveryRequest(Integer id, String description, Integer referenceNumber, String reference, Priority priority, User requester, Project project, DeliveryRequestType type, //
			InboundType inboundType, Boolean isForReturn, Boolean isForTransfer, Boolean sdm, DeliveryRequestStatus status, String originNumber, Date date4, //
			Date neededDeliveryDate, String originName, String destinationName, CompanyType ownerType, String customerName, String supplierName, String companyName,
			Warehouse warehouse, //
			String destinationProjectName, String transporterName1, String transporterName2, Long transportationRequestNumber, Boolean transportationNeeded, String smsRef, //
			Boolean containsBoqMapping, Boolean missingPo, Boolean missingOutboundDeliveryNote, String poNumero, CompanyType deliverToCompanyType, String deliverToCompanyName, //
			String deliverToCustomerName, String deliverToSupplierName, String toUserFullName, String endCustomerName, String projectCustomerName,
			String destinationProjectCustomerName) {
		super(id);
		this.description = description;
		this.priority = priority;
		this.requester = requester;
		this.project = project;
		this.type = type;
		this.inboundType = inboundType;
		this.isForReturn = isForReturn;
		this.isForTransfer = isForTransfer;
		this.sdm = sdm;
		this.status = status;
		this.referenceNumber = referenceNumber;
		this.reference = reference;
		this.originNumber = originNumber;
		this.date4 = date4;
		this.neededDeliveryDate = neededDeliveryDate;
		this.setOriginName(originName);
		this.setDestinationName(destinationName);
		this.setOwnerType(ownerType);
		this.setCompanyName(companyName);
		this.setCustomerName(customerName);
		this.setSupplierName(supplierName);
		this.warehouse = warehouse;
		this.transporterName = transporterName1 != null ? transporterName1 : transporterName2;
		this.transportationNeeded = transportationNeeded;
		this.hasTransportationRequest = transportationRequestNumber != null && transportationRequestNumber > 0;
		this.smsRef = smsRef;
		this.containsBoqMapping = containsBoqMapping;
		this.missingPo = missingPo;
		this.missingOutboundDeliveryNote = missingOutboundDeliveryNote;
		this.setDestinationProjectName(destinationProjectName);
		this.setPoNumero(poNumero);
		this.deliverToCompanyType = deliverToCompanyType;
		this.setDeliverToCompanyName(deliverToCompanyName);
		this.setDeliverToCustomerName(deliverToCustomerName);
		this.setDeliverToSupplierName(deliverToSupplierName);
		this.setToUserFullName(toUserFullName);
		this.setEndCustomerName(endCustomerName);
		this.setProjectCustomerName(projectCustomerName);
		this.setDestinationProjectCustomerName(destinationProjectCustomerName);
	}

	// c2
	public DeliveryRequest(Integer id, String reference, DeliveryRequestType type, DeliveryRequestStatus status, Date date4, //
			String requesterUsername, String requesterPhoto, String warehouseName, String projectName, String destinationProjectName, //
			CompanyType deliverToCompanyType, String deliverToCompanyName, String deliverToCustomerName, String deliverToSupplierName, String toUserFullName) {
		super(id);
		this.reference = reference;
		this.type = type;
		this.status = status;
		this.date4 = date4;
		this.setRequesterUsername(requesterUsername);
		this.setRequesterPhoto(requesterPhoto);
		this.setWarehouseName(warehouseName);
		this.setProjectName(projectName);
		this.setDestinationProjectName(destinationProjectName);
		this.setDeliverToCompanyType(deliverToCompanyType);
		this.setDeliverToCompanyName(deliverToCompanyName);
		this.setDeliverToCustomerName(deliverToCustomerName);
		this.setDeliverToSupplierName(deliverToSupplierName);
		this.setToUserFullName(toUserFullName);
	}

	public DeliveryRequest(DeliveryRequestType type, User requester) {
		super();
		this.type = type;
		this.requester = requester;
	}

	public DeliveryRequest(Integer id, Integer referenceNumber, DeliveryRequestType type) {
		super(id);
		this.referenceNumber = referenceNumber;
		this.type = type;
	}

	public void init() {
		if (project != null)
			projectId = project.getId();
		if (destinationProject != null)
			destinationProjectId = destinationProject.getId();
		if (customer != null)
			owner = new LabelValue(customer.getName(), customer.getId(), "customer");
		if (supplier != null)
			owner = new LabelValue(supplier.getName(), supplier.getId(), "supplier");
		if (company != null)
			owner = new LabelValue(company.getName(), company.getId(), "company");
		if (origin != null)
			originId = origin.getId();
		if (destination != null)
			destinationId = destination.getId();
		if (transporterId != null)
			transporterId = transporter.getId();
//		if (outboundDeliveryRequestReturn != null)
//			outboundDeliveryRequestReturnId = outboundDeliveryRequestReturn.getId();
		if (outboundDeliveryRequestTransfer != null)
			outboundDeliveryRequestTransferId = outboundDeliveryRequestTransfer.getId();
		if (po != null)
			poId = po.getId();
		if (externalRequester != null)
			tmpExternalRequesterUsername = externalRequester.getUsername();
	}

	public void initDetailList() {
		for (DeliveryRequestDetail detail : detailList)
			detail.init();
	}

	public void clearTimeLine() {
		rejectionReason = null;

		date2 = null;
		date3 = null;
		date4 = null;
		date5 = null;
		date6 = null;
		date7 = null;

		user3 = null;
		user4 = null;
		user5 = null;
		user6 = null;
		user7 = null;
	}

	public void addHistory(DeliveryRequestHistory history) {
		history.setParent(this);
		historyList.add(history);
	}

	public void removeHistory(DeliveryRequestHistory history) {
		history.setParent(null);
		historyList.remove(history);
	}

	public void addToNotify(ToNotify toNotify) {
		toNotify.setDeliveryRequest(this);
		toNotifyList.add(toNotify);
	}

	public void removeToNotify(ToNotify toNotify) {
		toNotify.setDeliveryRequest(null);
		toNotifyList.remove(toNotify);
	}

	public void calculateCountFiles() {
		countFiles = fileList.size();
	}

	@Transient
	public Boolean getHasFiles() {
		return countFiles > 0;
	}

	public void addFile(DeliveryRequestFile file) {
		file.setParent(this);
		fileList.add(file);
		calculateCountFiles();
	}

	public void removeFile(DeliveryRequestFile file) {
		file.setParent(null);
		fileList.remove(file);
		calculateCountFiles();
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getReference(), smsRef, description, originNumber, ownerName, //
				getProjectName(), getDestinationProjectName(), getRequesterFullName(), getSubType(), getWarehouseName(), //
				deliverToCompanyType != null ? deliverToCompanyType.getValue() : null, getDeliverToCompanyName(), getDeliverToSupplierName(), getDeliverToCustomerName(),
				getToUserFullName());
	}

	public void copyFromTemplate(DeliveryRequest template) {
		this.type = template.getType();
		this.isForReturn = template.getIsForReturn();
		this.isForTransfer = template.getIsForTransfer();
		this.inboundType = template.getInboundType();
		this.project = template.getProject();
		this.destinationProject = template.getDestinationProject();
		this.warehouse = template.getWarehouse();
		this.externalRequester = template.externalRequester;
		this.requestDate = template.requestDate;
		this.requestFrom = template.requestFrom;
		this.description = template.description;
		this.smsRef = template.smsRef;
		this.priority = template.getPriority();
//		this.neededDeliveryDate = template.getNeededDeliveryDate();
		this.origin = template.getOrigin();
		this.destination = template.getDestination();
		this.originNumber = template.getOriginNumber();
		this.ownerType = template.getOwnerType();
		this.customer = template.getCustomer();
		this.supplier = template.getSupplier();
		this.company = template.getCompany();
		this.approximativeStoragePeriod = template.getApproximativeStoragePeriod();
		this.endCustomer = template.getEndCustomer();
//		this.toSupplier = template.getToSupplier();
		this.toUser = template.getToUser();
		this.deliverToCompany = template.getDeliverToCompany();
		this.deliverToCompanyType = template.deliverToCompanyType;
		this.deliverToCustomer = template.deliverToCustomer;
		this.deliverToSupplier = template.deliverToSupplier;
		this.deliverToOther = template.deliverToOther;
		this.isSnRequired = template.getIsSnRequired();
		this.isPackingListRequired = template.getIsPackingListRequired();
		this.important = template.getImportant();
		this.transportationNeeded = template.transportationNeeded;
		this.transporter = template.transporter;
		this.preferedLocation = template.preferedLocation;
		this.approximativeStoragePeriod = template.approximativeStoragePeriod;
		this.sdm = template.sdm;
		init();
		if (getIsInbound() || getIsXbound()) {
			for (DeliveryRequestDetail detail : template.getDetailList())
				detailList.add(new DeliveryRequestDetail(detail.getQuantity(), detail.getPartNumber(), this));
			initDetailList();
		}

		if (!template.getToNotifyList().isEmpty())
			for (ToNotify toNotify : template.getToNotifyList())
				toNotifyList.add(new ToNotify(toNotify.getInternalResource(), this));

	}

	public void removeBoqMapping(BoqMapping bm) {
		bm.setDeliveryRequest(null);
		boqMappingList.remove(bm);
	}

	public void clearBoqMappingList() {
		if (boqMappingList.size() > 1)
			containsBoqMapping = false;
		boqMappingList.forEach(i -> i.setDeliveryRequest(null));
		boqMappingList.clear();

	}

	public void addComment(DeliveryRequestComment comment) {
		comment.setParent(this);
		commentList.add(comment);
	}

	public void removeComment(DeliveryRequestComment comment) {
		comment.setParent(null);
		commentList.remove(comment);
	}

	private void generateCommentGroupList() {
		Map<String, List<DeliveryRequestComment>> map = new HashMap<>();
		for (DeliveryRequestComment comment : commentList) {
			String dateStr = UtilsFunctions.getFormattedDate(comment.getDate());
			map.putIfAbsent(dateStr, new ArrayList<DeliveryRequestComment>());
			map.get(dateStr).add(comment);
		}
		commentGroupList = new ArrayList<>();
		for (String dateStr : map.keySet())
			commentGroupList.add(new CommentGroup<>(UtilsFunctions.getDate(dateStr), map.get(dateStr)));
		Collections.sort(commentGroupList);
	}

	@Transient
	public List<PackingDetail> getPackingDetailSummaryList() {
		List<PackingDetail> result = new ArrayList<PackingDetail>();
		Map<PackingDetail, Integer> map = new HashMap<PackingDetail, Integer>();

		detailList.forEach(i -> {
			i.getPacking().getDetailList().forEach(j -> {
				map.putIfAbsent(j, 0);
				map.put(j, map.get(j) + (int) (j.getQuantity() * i.getQuantity() / i.getPacking().getQuantity()));
			});
		});

		map.forEach((x, y) -> {
			x.setQuantity(y);
			result.add(x);
		});

		return result;
	}

	@Transient
	public String getNeededDeliveryDateStr() {
		if (neededDeliveryDate == null)
			return null;
		if (UtilsFunctions.compareDates(new Date(), neededDeliveryDate) > 0)
			return "Overdue";
		else if (UtilsFunctions.compareDates(new Date(), neededDeliveryDate) == 0)
			return "Today";
		else if (UtilsFunctions.compareDates(UtilsFunctions.addDays(new Date(), 1), neededDeliveryDate) == 0)
			return "Tomorrow";
		else if (UtilsFunctions.compareDates(UtilsFunctions.addDays(new Date(), 2), neededDeliveryDate) == 0)
			return "In 2 Days";
		else if (UtilsFunctions.compareDates(UtilsFunctions.addDays(new Date(), 3), neededDeliveryDate) == 0)
			return "In 3 Days";
		else if (UtilsFunctions.isDateInCurrentWeek(neededDeliveryDate))
			return "This week";
		else if (UtilsFunctions.isDateInNextWeek(neededDeliveryDate))
			return "Next week";
		return UtilsFunctions.formatDate(neededDeliveryDate);
	}

	@Transient
	public Color getNeededDeliveryDateColor() {
		if (neededDeliveryDate == null)
			return null;
		if (UtilsFunctions.compareDates(new Date(), neededDeliveryDate) < 0)
			return Color.BLUE;
		else if (UtilsFunctions.compareDates(new Date(), neededDeliveryDate) > 0)
			return Color.RED;
		return Color.GREEN;
	}

	@Transient
	public Boolean showExpiryData() {
		return detailList.stream().filter(i -> i.getPartNumber().getExpirable()).count() > 0;
	}

	@Transient
	public Boolean showSerialNumberData() {
		return detailList.stream().filter(i -> i.getPacking().getDetailList().stream().filter(j -> j.getHasSerialnumber()).count() > 0).count() > 0
				&& (getIsOutbound() || (getIsInbound() && isSnRequired));
	}

	@Transient
	public String getSubType() {
		if (getIsInbound())
			return inboundType.getValue();
		else if (getIsOutbound())
			if (isForTransfer)
				return "Transfer to Inbound";
			else if (isForReturn)
				return "Planned return";

		return null;
	}

	@Transient
	public Integer getWarehouseId() {
		return warehouse != null ? warehouse.getId() : null;
	}

	@Transient
	public void setWarehouseId(Integer warehouseId) {
		if (warehouse == null || !warehouseId.equals(warehouse.getId()))
			warehouse = new Warehouse();
		warehouse.setId(warehouseId);
	}

	@Transient
	public Integer getCompanyId() {
		return company == null ? null : company.getId();
	}

	@Transient
	public void setCompanyId(Integer companyId) {
		if (company == null || !company.getId().equals(companyId))
			company = new Company();
		company.setId(companyId);
	}

	@Transient
	public Integer getCustomerId() {
		return customer == null ? null : customer.getId();
	}

	@Transient
	public void setCustomerId(Integer customerId) {
		if (customer == null || !customer.getId().equals(customerId))
			customer = new Customer();
		customer.setId(customerId);
	}

	@Transient
	public Integer getSupplierId() {
		return supplier == null ? null : supplier.getId();
	}

	@Transient
	public void setSupplierId(Integer supplierId) {
		if (supplier == null || !supplier.getId().equals(supplierId))
			supplier = new Supplier();
		supplier.setId(supplierId);
	}

	@Transient
	public String getSubTypeColor() {
		if (getIsInbound())
			return inboundType.getColor();
		else if (getIsOutbound())
			if (isForTransfer)
				return "purple";
			else if (isForReturn)
				return "orange";

		return null;
	}

	@Transient
	public Double getTotalBoq() {
		return boqMappingList.stream().filter(i -> i.getBoq() != null).mapToDouble(i -> i.getQuantity() * i.getBoq().getUnitPrice()).sum();
	}

	@Transient
	public String getDeliverToEntityName() {
		try {
			switch (deliverToCompanyType) {
			case COMPANY:
				return "Company / " + getDeliverToCompanyName();
			case CUSTOMER:
				return "Customer / " + getDeliverToCustomerName();
			case SUPPLIER:
				return "Supplier / " + getDeliverToSupplierName();
			case OTHER:
				return "Other / " + deliverToOther;
			default:
				return "";
			}
		} catch (Exception e) {
			return "";
		}
	}

	@Transient
	public String getDeliverToCompanyName() {
		return deliverToCompany != null ? deliverToCompany.getName() : null;
	}

	@Transient
	public void setDeliverToCompanyName(String deliverToCompanyName) {
		if (deliverToCompany == null)
			deliverToCompany = new Company();
		deliverToCompany.setName(deliverToCompanyName);
	}

	@Transient
	public String getDeliverToCustomerName() {
		return deliverToCustomer != null ? deliverToCustomer.getName() : null;
	}

	@Transient
	public void setDeliverToCustomerName(String deliverToCustomerName) {
		if (deliverToCustomer == null)
			deliverToCustomer = new Customer();
		deliverToCustomer.setName(deliverToCustomerName);
	}

	@Transient
	public String getDeliverToSupplierName() {
		return deliverToSupplier != null ? deliverToSupplier.getName() : null;
	}

	@Transient
	public void setDeliverToSupplierName(String deliverToSupplierName) {
		if (deliverToSupplier == null)
			deliverToSupplier = new Supplier();
		deliverToSupplier.setName(deliverToSupplierName);
	}

	@Transient
	public Boolean getIsOutboundHardwareSwap() {
		return getIsOutbound() && isForReturn && "Hardware Swap".equals(returnReason);
	}

	@Transient
	public Boolean getIsInboundReturnFromOutboundHardwareSwap() {
		return getIsInboundReturn() && outboundDeliveryRequestReturn.getIsOutboundHardwareSwap();
	}

	@Transient
	public Boolean getIsStockProject() {
		if (project == null)
			return null;
		return ProjectTypes.STOCK.getValue().equals(project.getType());
	}

	@Transient
	public Integer getNumberOfItems() {
		Integer result = 0;
		for (DeliveryRequestDetail deliveryRequestDetail : detailList)
			result += deliveryRequestDetail.getPacking().getDetailList().stream()
					.mapToInt(i -> (int) (i.getQuantity() * deliveryRequestDetail.getQuantity() / deliveryRequestDetail.getPacking().getQuantity())).sum();
		return result;
	}

	@Transient
	public Double getNetWeight() {
		Double result = 0.0;
		if (detailList != null)
			for (DeliveryRequestDetail detail : detailList)
				result += detail.getNetWeight();
		return result;
	}

	@Transient
	public Double getGrossWeight() {
		Double result = 0.0;
		if (detailList != null)
			for (DeliveryRequestDetail detail : detailList)
				result += detail.getGrossWeight();
		return result;
	}

	@Transient
	public Double getVolume() {
		Double result = 0.0;
		if (detailList != null)
			for (DeliveryRequestDetail detail : detailList)
				result += detail.getVolume();
		return result;
	}

	@Transient
	public Double getTotalCost() {
		if (totalCost == null) {
			totalCost = 0.0;
			if (!detailList.isEmpty())
				for (DeliveryRequestDetail detail : detailList)
					totalCost += detail.getTotalCost();
		}
		return totalCost;
	}

	@Transient
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	@Transient
	public Double getTotalPrice() {
		if (totalPrice == null) {
			totalPrice = 0.0;
			if (!detailList.isEmpty())
				for (DeliveryRequestDetail detail : detailList)
					totalPrice += detail.getTotalPrice();
		}
		return totalPrice;
	}

	@Transient
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Transient
	public Boolean getIsInbound() {
		return DeliveryRequestType.INBOUND.equals(type);
	}

	@Transient
	public Boolean getIsOutbound() {
		return DeliveryRequestType.OUTBOUND.equals(type);
	}

	@Transient
	public Boolean getIsXbound() {
		return DeliveryRequestType.XBOUND.equals(type);
	}

	@Transient
	public String getStatusStyleClass() {
		switch (status) {
		case EDITED:
			return "badge badge-warning";
		case REQUESTED:
			return "badge badge-pink";
		case APPROVED1:
			return "badge badge-success";
		case APPROVED2:
			return "badge badge-success";
		case DELIVRED:
			return "badge badge-warning";
		case PARTIALLY_DELIVRED:
			return "badge badge-info";
		case ACKNOWLEDGED:
			return "badge badge-purple";
		case REJECTED:
			return "badge badge-danger";
		case CANCELED:
			return "badge badge-danger";
		default:
			break;
		}
		return null;
	}

	@Transient
	public String getDeliverToName() {
		if (toUser == null)
			return null;
		return toUser.getFullName();
	}

	@Transient
	public Integer getOwnerId() {
		switch (ownerType) {
		case COMPANY:
			return company.getId();
		case CUSTOMER:
			return customer.getId();
		case SUPPLIER:
			return supplier.getId();
		default:
			return null;
		}
	}

	@Transient
	public String getOwnerName() {
		if (ownerType == null)
			return null;
		switch (ownerType) {
		case COMPANY:
			return getCompanyName();
		case CUSTOMER:
			return getCustomerName();
		case SUPPLIER:
			return getSupplierName();
		default:
			return null;
		}
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public DeliveryRequestType getType() {
		return type;
	}

	public void setType(DeliveryRequestType type) {
		this.type = type;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public DeliveryRequestStatus getStatus() {
		return status;
	}

	public void setStatus(DeliveryRequestStatus status) {
		this.status = status;
	}

	@Enumerated(EnumType.STRING)
	public DeliveryRequestStatus getHardwareSwapInboundStatus() {
		return hardwareSwapInboundStatus;
	}

	public void setHardwareSwapInboundStatus(DeliveryRequestStatus hardwareSwapInboundStatus) {
		this.hardwareSwapInboundStatus = hardwareSwapInboundStatus;
	}

	public Boolean getIsSnRequired() {
		return isSnRequired;
	}

	public void setIsSnRequired(Boolean isSnRequired) {
		this.isSnRequired = isSnRequired;
	}

	public Boolean getIsPackingListRequired() {
		return isPackingListRequired;
	}

	public void setIsPackingListRequired(Boolean isPackingListRequired) {
		this.isPackingListRequired = isPackingListRequired;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Project getDestinationProject() {
		return destinationProject;
	}

	public void setDestinationProject(Project destinationProject) {
		this.destinationProject = destinationProject;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Boolean getImportant() {
		return important;
	}

	public void setImportant(Boolean important) {
		this.important = important;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryRequest", cascade = CascadeType.ALL)
	public List<DeliveryRequestDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<DeliveryRequestDetail> detailList) {
		this.detailList = detailList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<DeliveryRequestComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<DeliveryRequestComment> commentList) {
		this.commentList = commentList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DeliveryRequestFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<DeliveryRequestFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DeliveryRequestHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<DeliveryRequestHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ToNotify> getToNotifyList() {
		return toNotifyList;
	}

	public void setToNotifyList(List<ToNotify> toNotifyList) {
		this.toNotifyList = toNotifyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryRequest", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<BoqMapping> getBoqMappingList() {
		return boqMappingList;
	}

	public void setBoqMappingList(List<BoqMapping> boqMappingList) {
		this.boqMappingList = boqMappingList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryRequest", cascade = CascadeType.ALL)
	public List<StockRow> getStockRowList() {
		return stockRowList;
	}

	public void setStockRowList(List<StockRow> stockRowList) {
		this.stockRowList = stockRowList;
	}

	@Transient
	public LabelValue getOwner() {
		return owner;
	}

	@Transient
	public void setOwner(LabelValue owner) {
		this.owner = owner;
	}

	public String getOriginNumber() {
		return originNumber;
	}

	public void setOriginNumber(String originNumber) {
		this.originNumber = originNumber;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	@Enumerated(EnumType.STRING)
	public CompanyType getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(CompanyType ownerType) {
		this.ownerType = ownerType;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getNeededDeliveryDate() {
		return neededDeliveryDate;
	}

	public void setNeededDeliveryDate(Date neededDeliveryDate) {
		this.neededDeliveryDate = neededDeliveryDate;
	}

	@Temporal(TemporalType.DATE)
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Site getOrigin() {
		return origin;
	}

	public void setOrigin(Site origin) {
		this.origin = origin;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Site getDestination() {
		return destination;
	}

	public void setDestination(Site destination) {
		this.destination = destination;
	}

//	@ManyToOne(fetch = FetchType.LAZY, optional = true)
//	public Supplier getToSupplier() {
//		return toSupplier;
//	}
//
//	public void setToSupplier(Supplier toSupplier) {
//		this.toSupplier = toSupplier;
//	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Customer getEndCustomer() {
		return endCustomer;
	}

	public void setEndCustomer(Customer endCustomer) {
		this.endCustomer = endCustomer;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getToUser() {
		return toUser;
	}

	public void setToUser(User toUser) {
		this.toUser = toUser;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@Transient
	public Integer getProjectId() {
		return projectId;
	}

	@Transient
	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	@Transient
	public String getRequesterUsername() {
		return requester != null ? requester.getUsername() : null;
	}

	@Transient
	public void setRequesterUsername(String requesterUsername) {
		if (requester == null || !requesterUsername.equals(requester.getUsername()))
			requester = new User();
		requester.setUsername(requesterUsername);
	}

	@Transient
	@JsonIgnore
	public String getRequesterFullName() {
		return requester == null ? null : requester.getFullName();
	}

	@Transient
	@JsonIgnore
	public String getProjectName() {
		if (project != null)
			return project.getName();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setProjectName(String name) {
		if (project == null)
			project = new Project();
		project.setName(name);
	}

	@Transient
	@JsonIgnore
	public String getProjectCustomerName() {
		if (project != null)
			return project.getCustomerName();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setProjectCustomerName(String name) {
		if (project == null)
			project = new Project();
		project.setCustomerName(name);
	}

	@Transient
	@JsonIgnore
	public String getProjectSubType() {
		if (project != null)
			return project.getSubType();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setProjectSubType(String name) {
		if (project == null)
			project = new Project();
		project.setSubType(name);
	}

	@Transient
	@JsonIgnore
	public String getWarehouseName() {
		if (warehouse != null)
			return warehouse.getName();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setWarehouseName(String name) {
		if (warehouse == null)
			warehouse = new Warehouse();
		warehouse.setName(name);
	}

	@Transient
	@JsonIgnore
	public String getRequesterPhoto() {
		if (requester != null)
			return requester.getPhoto();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setRequesterPhoto(String name) {
		if (requester == null)
			requester = new User();
		requester.setPhoto(name);
	}

	@Transient
	@JsonIgnore
	public String getToUserFullName() {
		if (toUser != null)
			return toUser.getFullName();
		return null;
	}

	@Transient
	@JsonIgnore
	public void setToUserFullName(String name) {
		if (toUser == null)
			toUser = new User();
		toUser.setFullName(name);
	}

	@Transient
	public Integer getOriginId() {
		return originId;
	}

	@Transient
	public void setOriginId(Integer originId) {
		System.out.println("setOriginId ! " + originId);
		this.originId = originId;
	}

	@Transient
	public Integer getDestinationId() {
		return destinationId;
	}

	@Transient
	public void setDestinationId(Integer destinationId) {
		this.destinationId = destinationId;
	}

	@Transient
	public Integer getTransporterId() {
		return transporterId;
	}

	@Transient
	public void setTransporterId(Integer transporterId) {
		this.transporterId = transporterId;
	}

	public Integer getApproximativeStoragePeriod() {
		return approximativeStoragePeriod;
	}

	public void setApproximativeStoragePeriod(Integer approximativeStoragePeriod) {
		this.approximativeStoragePeriod = approximativeStoragePeriod;
	}

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	public Date getDate3() {
		return date3;
	}

	public void setDate3(Date date3) {
		this.date3 = date3;
	}

	public Date getDate8() {
		return date8;
	}

	public void setDate8(Date date8) {
		this.date8 = date8;
	}

	public Date getDate4() {
		return date4;
	}

	public void setDate4(Date date4) {
		this.date4 = date4;
	}

	public Date getDate5() {
		return date5;
	}

	public void setDate5(Date date5) {
		this.date5 = date5;
	}

	public Date getDate6() {
		return date6;
	}

	public void setDate6(Date date6) {
		this.date6 = date6;
	}

	public Date getDate7() {
		return date7;
	}

	public void setDate7(Date date7) {
		this.date7 = date7;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser3() {
		return user3;
	}

	public void setUser3(User user3) {
		this.user3 = user3;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser8() {
		return user8;
	}

	public void setUser8(User user8) {
		this.user8 = user8;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser4() {
		return user4;
	}

	public void setUser4(User user4) {
		this.user4 = user4;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser5() {
		return user5;
	}

	public void setUser5(User user5) {
		this.user5 = user5;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser6() {
		return user6;
	}

	public void setUser6(User user6) {
		this.user6 = user6;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getUser7() {
		return user7;
	}

	public void setUser7(User user7) {
		this.user7 = user7;
	}

	public void initIsPartial() {
		isPartial = false;
		try {
			for (DeliveryRequestDetail detail : detailList)
				if (UtilsFunctions.compareDoubles(detail.getRemainingQuantity(), detail.getTmpQuantity(), 4) != 0) {
					isPartial = true;
					break;
				}
		} catch (Exception e) {
		}
	}

	public Boolean getIsPartial() {
		if (isPartial == null)
			initIsPartial();
		return isPartial;
	}

	public void setIsPartial(Boolean isPartial) {
		this.isPartial = isPartial;
	}

	@Column(nullable = false)
	public Integer getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(Integer referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	@Enumerated(EnumType.STRING)
//	@Column(name = "external_company_type")
	public CompanyType getDeliverToCompanyType() {
		return deliverToCompanyType;
	}

	public void setDeliverToCompanyType(CompanyType deliverToCompanyType) {
		this.deliverToCompanyType = deliverToCompanyType;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
//	@JoinColumn(name = "external_company_customer_idcustomer")
	public Customer getDeliverToCustomer() {
		return deliverToCustomer;
	}

	public void setDeliverToCustomer(Customer deliverToCustomer) {
		this.deliverToCustomer = deliverToCustomer;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
//	@JoinColumn(name = "external_company_supplier_idsupplier")
	public Supplier getDeliverToSupplier() {
		return deliverToSupplier;
	}

	public void setDeliverToSupplier(Supplier deliverToSupplier) {
		this.deliverToSupplier = deliverToSupplier;
	}

//	@Column(name = "external_company")
	public String getDeliverToOther() {
		return deliverToOther;
	}

	public void setDeliverToOther(String deliverToOther) {
		this.deliverToOther = deliverToOther;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Location getPreferedLocation() {
		return preferedLocation;
	}

	public void setPreferedLocation(Location preferedLocation) {
		this.preferedLocation = preferedLocation;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Transporter getTransporter() {
		return transporter;
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	public void generateReference() {
		this.reference = "DN" + (type.ordinal() + 1) + String.format("%05d", referenceNumber);
	}

	public void calculateMissingOutboundDeliveryNote() {
		this.missingOutboundDeliveryNote = getIsOutbound() //
				&& Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED).contains(status) //
				&& fileList.stream().filter(f -> "Outbound Delivery Note".equals(f.getType())).count() == 0;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Transient
	public String getFullReference() {
		String result = getReference();

		if (getIsInboundNew())
			result += ", New";
		else if (getIsInboundReturn())
			result += ", Return From Outbound, " + outboundDeliveryRequestReturn.getReference();
		else if (getIsInboundTransfer())
			result += ", Transfer From Outbound, " + outboundDeliveryRequestReturn.getReference();

		return result;
	}

	@Transient
	public String getFullType() {
		String result = type.getValue();

		if (getIsInboundNew())
			result += ", New";
		else if (getIsInboundReturn())
			result += ", Return From Outbound, " + outboundDeliveryRequestReturn.getReference();
		else if (getIsInboundTransfer())
			result += ", Transfer From Outbound, " + outboundDeliveryRequestReturn.getReference();

		return result;
	}

	@Transient
	public String getOriginName() {
		return origin != null ? origin.getName() : null;
	}

	@Transient
	public void setOriginName(String originName) {
		if (origin == null)
			origin = new Site();
		origin.setName(originName);
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	@Transient
	public String getToUserUsername() {
		return toUser == null ? null : toUser.getUsername();
	}

	public void setToUserUsername(String toUserUsername) {
		if (toUser == null || !toUserUsername.equals(toUser.getUsername()))
			toUser = new User();
		toUser.setUsername(toUserUsername);
	}

	@Transient
	public Integer getEndCustomerId() {
		return endCustomer == null ? null : endCustomer.getId();
	}

	@Transient
	public void setEndCustomerId(Integer endCustomerId) {
		if (endCustomer == null || !endCustomer.getId().equals(endCustomerId))
			endCustomer = new Customer();
		endCustomer.setId(endCustomerId);
	}

//	@Transient
//	public Integer getToSupplierId() {
//		return toSupplier == null ? null : toSupplier.getId();
//	}
//
//	@Transient
//	public void setToSupplierId(Integer toSupplierId) {
//		if (toSupplier == null || !toSupplier.getId().equals(toSupplierId))
//			toSupplier = new Supplier();
//		toSupplier.setId(toSupplierId);
//	}

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "deliveryRequest")
	public TransportationRequest getTransportationRequest() {
		return transportationRequest;
	}

	public void setTransportationRequest(TransportationRequest transportationRequest) {
		this.transportationRequest = transportationRequest;
	}

	public Boolean getTransportationNeeded() {
		return transportationNeeded;
	}

	public void setTransportationNeeded(Boolean transportationNeeded) {
		this.transportationNeeded = transportationNeeded;
	}

	@Transient
	public Boolean getHasTransportationRequest() {
		return hasTransportationRequest;
	}

	@Transient
	public String getTransporterName() {
		return transporterName;
	}

	@Transient
	public Integer getDestinationProjectId() {
		return destinationProjectId;
	}

	@Transient
	public void setDestinationProjectId(Integer destinationProjectId) {
		this.destinationProjectId = destinationProjectId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public Double getqTotalCost() {
		return qTotalCost;
	}

	@Transient
	public Double getqTotalRevenue() {
		return qTotalRevenue;
	}

	@Transient
	public Double getqTotalCrossCharge() {
		return qTotalCrossCharge;
	}

	@Transient
	public String getDestinationProjectCustomerName() {
		return destinationProject != null ? destinationProject.getCustomerName() : null;
	}

	@Transient
	public void setDestinationProjectCustomerName(String destinationProjectCustomerName) {
		if (destinationProject == null)
			destinationProject = new Project();
		destinationProject.setCustomerName(destinationProjectCustomerName);
	}

	@Transient
	public Double getMargin() {
		try {
			return qTotalRevenue - qTotalCost;
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public Double getMarginPercentage() {
		try {
			return (qTotalRevenue - qTotalCost) / qTotalRevenue;
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public Double getqAssociatedCost() {
		return qAssociatedCost;
	}

	@Transient
	public Double getDelta() {
		try {
			return qTotalCost - qAssociatedCost;
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public Double getDeltaPercentage() {
		try {
			return (qTotalCost - qAssociatedCost) / qTotalCost;
		} catch (Exception e) {
			return null;
		}
	}

	@Transient
	public Boolean getIsInboundNew() {
		return getIsInbound() && InboundType.NEW.equals(inboundType);
	}

	@Transient
	public Boolean getIsInboundReturn() {
		return getIsInbound() && InboundType.RETURN.equals(inboundType);
	}

	@Transient
	public Boolean getIsInboundTransfer() {
		return getIsInbound() && InboundType.TRANSFER.equals(inboundType);
	}

	public String getSmsRef() {
		return smsRef;
	}

	public void setSmsRef(String smsRef) {
		this.smsRef = smsRef;
	}

	@Transient
	public Integer getCrossChargeId() {
		return crossChargeId;
	}
	
	@Transient
	public Integer getDeliverToCompanyId() {
		return deliverToCompany == null ? null : deliverToCompany.getId();
	}

	@Transient
	public void setDeliverToCompanyId(Integer deliverToCompanyId) {
		if (deliverToCompany == null || !deliverToCompany.getId().equals(deliverToCompanyId))
			deliverToCompany = new Company();
		deliverToCompany.setId(deliverToCompanyId);
	}

	@Transient
	public Integer getDeliverToCustomerId() {
		return deliverToCustomer == null ? null : deliverToCustomer.getId();
	}

	@Transient
	public void setDeliverToCustomerId(Integer deliverToCustomerId) {
		if (deliverToCustomer == null || !deliverToCustomer.getId().equals(deliverToCustomerId))
			deliverToCustomer = new Customer();
		deliverToCustomer.setId(deliverToCustomerId);
	}

	@Transient
	public Integer getDeliverToSupplierId() {
		return deliverToSupplier == null ? null : deliverToSupplier.getId();
	}

	@Transient
	public void setDeliverToSupplierId(Integer deliverToSupplierId) {
		if (deliverToSupplier == null || !deliverToSupplier.getId().equals(deliverToSupplierId))
			deliverToSupplier = new Supplier();
		deliverToSupplier.setId(deliverToSupplierId);
	}

	@Transient
	public Integer getOutboundDeliveryRequestReturnId() {
		return outboundDeliveryRequestReturn != null ? outboundDeliveryRequestReturn.getId() : null;
	}

	@Transient
	public void setOutboundDeliveryRequestReturnId(Integer outboundDeliveryRequestReturnId) {
		if (outboundDeliveryRequestReturn == null || !outboundDeliveryRequestReturnId.equals(outboundDeliveryRequestReturn.getId()))
			outboundDeliveryRequestReturn = new DeliveryRequest();
		outboundDeliveryRequestReturn.setId(outboundDeliveryRequestReturnId);
	}

	@Transient
	public Integer getOutboundDeliveryRequestTransferId() {
		return outboundDeliveryRequestTransferId;
	}

	@Transient
	public void setOutboundDeliveryRequestTransferId(Integer outboundDeliveryRequestTransferId) {
		this.outboundDeliveryRequestTransferId = outboundDeliveryRequestTransferId;
	}

	@Transient
	public Integer getPoId() {
		return poId;
	}

	@Transient
	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public DeliveryRequest getOutboundDeliveryRequestReturn() {
		return outboundDeliveryRequestReturn;
	}

	public void setOutboundDeliveryRequestReturn(DeliveryRequest outboundDeliveryRequestReturn) {
		this.outboundDeliveryRequestReturn = outboundDeliveryRequestReturn;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public DeliveryRequest getOutboundDeliveryRequestTransfer() {
		return outboundDeliveryRequestTransfer;
	}

	public void setOutboundDeliveryRequestTransfer(DeliveryRequest outboundDeliveryRequestTransfer) {
		this.outboundDeliveryRequestTransfer = outboundDeliveryRequestTransfer;
	}

	@Enumerated(EnumType.STRING)
	public InboundType getInboundType() {
		return inboundType;
	}

	public void setInboundType(InboundType inboundType) {
		this.inboundType = inboundType;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public Po getPo() {
		return po;
	}

	public void setPo(Po po) {
		this.po = po;
	}

	@Temporal(TemporalType.DATE)
	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(String requestFrom) {
		this.requestFrom = requestFrom;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public User getExternalRequester() {
		return externalRequester;
	}

	public void setExternalRequester(User externalRequester) {
		this.externalRequester = externalRequester;
	}

	@Transient
	public String getTmpExternalRequesterUsername() {
		return tmpExternalRequesterUsername;
	}

	@Transient
	public void setTmpExternalRequesterUsername(String tmpExternalRequesterUsername) {
		this.tmpExternalRequesterUsername = tmpExternalRequesterUsername;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
//	@JoinColumn(name = "internal_company_idcompany")
	public Company getDeliverToCompany() {
		return deliverToCompany;
	}

	public void setDeliverToCompany(Company deliverToCompany) {
		this.deliverToCompany = deliverToCompany;
	}

	public Boolean getIsForTransfer() {
		return isForTransfer;
	}

	public void setIsForTransfer(Boolean isForTransfer) {
		this.isForTransfer = isForTransfer;
	}

	public Boolean getIsForReturn() {
		return isForReturn;
	}

	public void setIsForReturn(Boolean isForReturn) {
		this.isForReturn = isForReturn;
	}

	@Column(nullable = false)
	public String getQrKey() {
		return qrKey;
	}

	public void setQrKey(String qrKey) {
		this.qrKey = qrKey;
	}

	@Column(columnDefinition = "TEXT")
	public String getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}

	@Transient
	public String getPoNumero() {
		return po != null ? po.getNumeroInvoice() : null;
	}

	@Transient
	public void setPoNumero(String poNumero) {
		if (po == null)
			po = new Po();
		po.setNumeroInvoice(poNumero);
	}

	@Transient
	public String getOutboundDeliveryRequestTransferPoNumero() {
		return outboundDeliveryRequestTransfer != null ? outboundDeliveryRequestTransfer.getPoNumero() : null;
	}

	@Transient
	public void setOutboundDeliveryRequestTransferPoNumero(String outboundDeliveryRequestTransferPoNumero) {
		if (outboundDeliveryRequestTransfer == null)
			outboundDeliveryRequestTransfer = new DeliveryRequest();
		outboundDeliveryRequestTransfer.setPoNumero(outboundDeliveryRequestTransferPoNumero);
	}

	public Boolean getIsFullyReturned() {
		return isFullyReturned;
	}

	public void setIsFullyReturned(Boolean isFullyReturned) {
		this.isFullyReturned = isFullyReturned;
	}

	@Transient
	public String getEndCustomerName() {
		if (endCustomer == null)
			return null;
		return endCustomer.getName();
	}

	@Transient
	public void setEndCustomerName(String name) {
		if (endCustomer == null)
			endCustomer = new Customer();
		endCustomer.setName(name);
	}

	@Transient
	public String getCustomerName() {
		if (customer == null)
			return null;
		return customer.getName();
	}

	@Transient
	public void setCustomerName(String name) {
		if (customer == null)
			customer = new Customer();
		customer.setName(name);
	}

	@Transient
	public String getSupplierName() {
		if (supplier == null)
			return null;
		return supplier.getName();
	}

	@Transient
	public void setSupplierName(String name) {
		if (supplier == null)
			supplier = new Supplier();
		supplier.setName(name);
	}

	@Transient
	public String getCompanyName() {
		if (company == null)
			return null;
		return company.getName();
	}

	@Transient
	public void setCompanyName(String name) {
		if (company == null)
			company = new Company();
		company.setName(name);
	}

	public Boolean getMissingSerialNumber() {
		return missingSerialNumber;
	}

	public void setMissingSerialNumber(Boolean missingSerialNumber) {
		this.missingSerialNumber = missingSerialNumber;
	}

	public Boolean getMissingExpiry() {
		return missingExpiry;
	}

	public void setMissingExpiry(Boolean missingExpiry) {
		this.missingExpiry = missingExpiry;
	}

	public Boolean getSdm() {
		return sdm;
	}

	public void setSdm(Boolean sdm) {
		this.sdm = sdm;
	}

	public Boolean getIsm() {
		return ism;
	}

	public void setIsm(Boolean ism) {
		this.ism = ism;
	}

	@Transient
	public List<CommentGroup<DeliveryRequestComment>> getCommentGroupList() {
		if (commentGroupList == null)
			generateCommentGroupList();
		return commentGroupList;
	}

	@Transient
	public void setCommentGroupList(List<CommentGroup<DeliveryRequestComment>> commentGroupList) {
		this.commentGroupList = commentGroupList;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public String getDestinationProjectName() {
		return destinationProject != null ? destinationProject.getName() : null;
	}

	@Transient
	public void setDestinationProjectName(String destinationProjectName) {
		if (destinationProject == null)
			destinationProject = new Project();
		destinationProject.setName(destinationProjectName);
	}

	@Transient
	public String getExternalRequesterFullName() {
		return externalRequester != null ? externalRequester.getFullName() : null;
	}

	@Transient
	public void setExternalRequesterFullName(String externalRequesterFullName) {
		if (externalRequester == null)
			externalRequester = new User();
		externalRequester.setFullName(externalRequesterFullName);
	}

	@Transient
	public String getDestinationName() {
		return destination != null ? destination.getName() : null;
	}

	@Transient
	public void setDestinationName(String destinationName) {
		if (destination == null)
			destination = new Site();
		destination.setName(destinationName);
	}

	@Transient
	public Boolean getProjectSdm() {
		return project != null ? project.getSdm() : null;
	}

	@Transient
	public Boolean getProjectIsm() {
		return project != null ? project.getIsm() : null;
	}

	@Transient
	public Boolean getDestinationProjectSdm() {
		return destinationProject != null ? destinationProject.getSdm() : null;
	}
	
	@Transient
	public Boolean getDestinationProjectIsm() {
		return destinationProject != null ? destinationProject.getIsm() : null;
	}

	public Long getCountIssues1() {
		return countIssues1;
	}

	public void setCountIssues1(Long countIssues1) {
		this.countIssues1 = countIssues1;
	}

	public Long getCountIssues2() {
		return countIssues2;
	}

	public void setCountIssues2(Long countIssues2) {
		this.countIssues2 = countIssues2;
	}

	public Long getCountIssues3() {
		return countIssues3;
	}

	public void setCountIssues3(Long countIssues3) {
		this.countIssues3 = countIssues3;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "deliveryRequest", cascade = CascadeType.ALL)
	public List<Issue> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<Issue> issueList) {
		this.issueList = issueList;
	}

	@Override
	public String toString() {
		return this.reference;
	}

	@Override
	public int compareTo(DeliveryRequest o) {
		try {
			return neededDeliveryDate.compareTo(o.neededDeliveryDate);
		} catch (Exception e) {
		}
		return 0;
	}

	public Boolean getContainsBoqMapping() {
		return containsBoqMapping;
	}

	public void setContainsBoqMapping(Boolean containsBoqMapping) {
		this.containsBoqMapping = containsBoqMapping;
	}

	public Boolean getMissingPo() {
		return missingPo;
	}

	public void setMissingPo(Boolean missingPo) {
		this.missingPo = missingPo;
	}

	public Boolean getMissingOutboundDeliveryNote() {
		return missingOutboundDeliveryNote;
	}

	public void setMissingOutboundDeliveryNote(Boolean missingOutboundDeliveryNote) {
		this.missingOutboundDeliveryNote = missingOutboundDeliveryNote;
	}

	public Integer getCountFiles() {
		return countFiles;
	}

	public void setCountFiles(Integer countFiles) {
		this.countFiles = countFiles;
	}

	public Boolean getAutoBoqMapped() {
		return autoBoqMapped;
	}

	public void setAutoBoqMapped(Boolean autoBoqMapped) {
		this.autoBoqMapped = autoBoqMapped;
	}

	public Integer getHardwareSwapInboundId() {
		return hardwareSwapInboundId;
	}

	public void setHardwareSwapInboundId(Integer hardwareSwapInboundId) {
		this.hardwareSwapInboundId = hardwareSwapInboundId;
	}

	@Transient
	public StockRowState getStockRowState() {
		return stockRowState;
	}

	@Transient
	public void setStockRowState(StockRowState stockRowState) {
		this.stockRowState = stockRowState;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Acceptance getAcceptance() {
		return acceptance;
	}

	public void setAcceptance(Acceptance acceptance) {
		this.acceptance = acceptance;
	}

	@Transient
	public Boolean getFilter() {
		return filter;
	}

	@Transient
	public void setFilter(Boolean filter) {
		this.filter = filter;
	}

	@Transient
	public String getFilterType() {
		return filterType;
	}

	@Transient
	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	@Transient
	public String getFilterValue() {
		return filterValue;
	}

	@Transient
	public void setFilterValue(String filterValue) {
		this.filterValue = filterValue;
	}
	
	
	
	

}
