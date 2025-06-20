package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import ma.azdad.service.UtilsFunctions;

@Entity

public class DeliveryRequestSerialNumber extends GenericModel<Integer> implements Serializable {

	private Integer packingNumero;
	private String serialNumber;
	private String box;
	private PackingDetail packingDetail;
	private StockRow inboundStockRow;
	private DeliveryRequest outboundDeliveryRequest;

	// tmp
	private PartNumber tmpPartNumber;
	private DeliveryRequest tmpInboundDeliveryRequest;
	private Location tmpLocation;
	private StockRowStatus tmpStockRowStatus;

	// sn mobile infos
	private Double longitude;
	private Double latitude;
	private String entryMode;

	public void init() {
		if (inboundStockRow != null) {
			tmpPartNumber = inboundStockRow.getPartNumber();
			tmpInboundDeliveryRequest = inboundStockRow.getDeliveryRequest();
		}

	}
	
	public void changeSerialNumberListener() {
		this.entryMode = "Manual";
	}

	public DeliveryRequestSerialNumber() {
		super();
	}

	public DeliveryRequestSerialNumber(Integer packingNumero, PackingDetail packingDetail, StockRow inboundStockRow) {
		super();
		this.packingNumero = packingNumero;
		this.packingDetail = packingDetail;
		this.inboundStockRow = inboundStockRow;
		this.tmpPartNumber = inboundStockRow.getPartNumber();
	}

	public DeliveryRequestSerialNumber(Integer packingNumero, PackingDetail packingDetail, DeliveryRequest outboundDeliveryRequest, StockRow outboundStockRow) {
		super();
		this.packingNumero = packingNumero;
		this.packingDetail = packingDetail;
		this.outboundDeliveryRequest = outboundDeliveryRequest; // = outboundStockRow.getDeliveryRequest
		this.tmpPartNumber = packingDetail.getParent().getPartNumber();
		this.tmpInboundDeliveryRequest = outboundStockRow.getInboundDeliveryRequest();
		this.tmpLocation = outboundStockRow.getLocation();
		this.tmpStockRowStatus = outboundStockRow.getStatus();
	}

	// c1
	public DeliveryRequestSerialNumber(Integer id, Integer packingNumero, String serialNumber, String box, //
			String partNumberName, String partNumberDescription, StockRowStatus inboundStockRowStatus, Integer inboundDeliveryRequestId, String inboundDeliveryRequestReference, //
			String packingDetailType, String locationName) {
		super(id);
		this.packingNumero = packingNumero;
		this.serialNumber = serialNumber;
		this.box = box;
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setInboundStockRowStatus(inboundStockRowStatus);
		this.setInboundDeliveryRequestId(inboundDeliveryRequestId);
		this.setInboundDeliveryRequestReference(inboundDeliveryRequestReference);
		this.setPackingDetailType(packingDetailType);
		this.setLocationName(locationName);
	}

	// c2
	public DeliveryRequestSerialNumber(Integer id, Integer packingNumero, String serialNumber, String box, //
			String partNumberName, String partNumberDescription,String partNumberBrandName, StockRowStatus inboundStockRowStatus, Integer inboundDeliveryRequestId, String inboundDeliveryRequestReference, //
			String packingDetailType, String locationName,// 
			String outboundDeliveryRequestReference,String outboundProjectName,CompanyType outboundDeliverToCompanyType,String outboundDeliverToCompanyName,String outboundDeliverToCustomerName,String outboundDeliverToSupplierName,String outboundDeliverToOther,//
			String outboundDestinationName,Date outboundDeliveryRequestDate4,String outboundDestinationProjectCustomerName,String outboundDestinationProjectName,String outboundEndCustomerName, //
			String outboundPoNumero,String  outboundToUserFullName,String outboundWarehouseName) {
		super(id);
		this.packingNumero = packingNumero;
		this.serialNumber = serialNumber;
		this.box = box;
		this.setPartNumberName(partNumberName);
		this.setPartNumberDescription(partNumberDescription);
		this.setPartNumberBrandName(partNumberBrandName);
		this.setInboundStockRowStatus(inboundStockRowStatus);
		this.setInboundDeliveryRequestId(inboundDeliveryRequestId);
		this.setInboundDeliveryRequestReference(inboundDeliveryRequestReference);
		this.setPackingDetailType(packingDetailType);
		this.setLocationName(locationName);

		this.setOutboundDeliveryRequestReference(outboundDeliveryRequestReference);
		this.setOutboundProjectName(outboundProjectName);
		this.setOutboundDeliverToCompanyType(outboundDeliverToCompanyType);
		this.setOutboundDeliverToCompanyName(outboundDeliverToCompanyName);
		this.setOutboundDeliverToCustomerName(outboundDeliverToCustomerName);
		this.setOutboundDeliverToSupplierName(outboundDeliverToSupplierName);
		this.setOutboundDeliverToOther(outboundDeliverToOther);
		this.setOutboundDestinationName(outboundDestinationName);
		this.setOutboundDeliveryRequestDate4(outboundDeliveryRequestDate4);
		this.setOutboundDestinationProjectCustomerName(outboundDestinationProjectCustomerName);
		this.setOutboundDestinationProjectName(outboundDestinationProjectName);
		this.setOutboundEndCustomerName(outboundEndCustomerName);
		this.setOutboundPoNumero(outboundPoNumero);
		this.setOutboundToUserFullName(outboundToUserFullName);
		this.setOutboundWarehouseName(outboundWarehouseName);
		
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getPartNumberName(), getPartNumberDescription(), serialNumber, box, getInboundDeliveryRequestReference());
	}

	@Transient
	public Boolean getIsEmpty() {
		return StringUtils.isBlank(serialNumber);
	}
	
	
	
	@Transient
	public String getOutboundToUserFullName() {
		if (outboundDeliveryRequest == null)
			return null;
		return outboundDeliveryRequest.getToUserFullName();
	}

	@Transient
	public void setOutboundToUserFullName(String toUserFullName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setToUserFullName(toUserFullName);
	}
	
	@Transient
	public String getOutboundDestinationProjectCustomerName() {
		if (outboundDeliveryRequest == null)
			return null;
		return outboundDeliveryRequest.getDestinationProjectCustomerName();
	}

	@Transient
	public void setOutboundDestinationProjectCustomerName(String destinationProjectCustomerName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setDestinationProjectCustomerName(destinationProjectCustomerName);
	}
	
	@Transient
	public String getOutboundDestinationName() {
		if (outboundDeliveryRequest == null)
			return null;
		return outboundDeliveryRequest.getDestinationName();
	}
	
	@Transient
	public String getOutboundPoNumero() {
		return outboundDeliveryRequest != null ? outboundDeliveryRequest.getPoNumero() : null;
	}

	@Transient
	public void setOutboundPoNumero(String poNumero) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setPoNumero(poNumero);
	}
	
	@Transient
	public String getOutboundDestinationProjectName() {
		if (outboundDeliveryRequest == null)
			return null;
		return outboundDeliveryRequest.getDestinationProjectName();
	}

	@Transient
	public void setOutboundDestinationProjectName(String destinationProjectName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setDestinationProjectName(destinationProjectName);
	}
	
	@Transient
	public Date getOutboundDeliveryRequestDate4() {
		if (outboundDeliveryRequest == null)
			return null;
		return outboundDeliveryRequest.getDate4();
	}

	@Transient
	public void setOutboundDeliveryRequestDate4(Date outboundDeliveryRequestDate4) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setDate4(outboundDeliveryRequestDate4);
	}
	
	@Transient
	public String getOutboundDeliveryYear() {
		if (getOutboundDeliveryRequestDate4() == null)
			return null;
		return String.valueOf(UtilsFunctions.getYear(getOutboundDeliveryRequestDate4()));
	}

	@Transient
	public String getOutboundDeliveryMonthAndYear() {
		if (getOutboundDeliveryRequestDate4() == null)
			return null;
		return String.valueOf(UtilsFunctions.getMonth(getOutboundDeliveryRequestDate4()) + "/" + UtilsFunctions.getYear(getOutboundDeliveryRequestDate4()));
	}

	@Transient
	public void setOutboundDestinationName(String destinationName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setDestinationName(destinationName);
	}

	@Transient
	public String getOutboundDeliveryRequestReference() {
		return outboundDeliveryRequest != null ? outboundDeliveryRequest.getReference() : null;
	}

	@Transient
	public void setOutboundDeliveryRequestReference(String outboundDeliveryRequestReference) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setReference(outboundDeliveryRequestReference);
	}

	@Transient
	public String getOutboundDeliverTo() {
		if (getOutboundDeliverToCompanyType() == null)
			return null;
		return getOutboundDeliverToCompanyType().getValue() + " / " + getOutboundDeliverToEntityName();
	}

	@Transient
	public String getOutboundDeliverToEntityName() {
		if (getOutboundDeliverToCompanyType() == null)
			return null;
		switch (getOutboundDeliverToCompanyType()) {
		case COMPANY:
			return getOutboundDeliverToCompanyName();
		case CUSTOMER:
			return getOutboundDeliverToCustomerName();
		case SUPPLIER:
			return getOutboundDeliverToSupplierName();
		default:
			return getOutboundDeliverToOther();
		}
	}

	@Transient
	public String getOutboundDeliverToCompanyName() {
		return outboundDeliveryRequest != null ? outboundDeliveryRequest.getDeliverToCompanyName() : null;
	}

	@Transient
	public void setOutboundDeliverToCompanyName(String deliverToCompanyName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setDeliverToCompanyName(deliverToCompanyName);
	}

	@Transient
	public String getOutboundDeliverToCustomerName() {
		return outboundDeliveryRequest != null ? outboundDeliveryRequest.getDeliverToCustomerName() : null;
	}

	@Transient
	public void setOutboundDeliverToCustomerName(String deliverToCustomerName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setDeliverToCustomerName(deliverToCustomerName);
	}

	@Transient
	public String getOutboundDeliverToSupplierName() {
		return outboundDeliveryRequest != null ? outboundDeliveryRequest.getDeliverToSupplierName() : null;
	}

	@Transient
	public void setOutboundDeliverToSupplierName(String deliverToSupplierName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setDeliverToSupplierName(deliverToSupplierName);
	}

	@Transient
	public String getOutboundDeliverToOther() {
		return outboundDeliveryRequest != null ? outboundDeliveryRequest.getDeliverToOther() : null;
	}

	@Transient
	public void setOutboundDeliverToOther(String deliverToOther) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setDeliverToOther(deliverToOther);
	}

	@Transient
	public CompanyType getOutboundDeliverToCompanyType() {
		return outboundDeliveryRequest != null ? outboundDeliveryRequest.getDeliverToCompanyType() : null;
	}

	@Transient
	public void setOutboundDeliverToCompanyType(CompanyType deliverToCompanyType) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setDeliverToCompanyType(deliverToCompanyType);
	}

	@Transient
	public String getOutboundProjectName() {
		return outboundDeliveryRequest != null ? outboundDeliveryRequest.getProjectName() : null;
	}

	@Transient
	public void setOutboundProjectName(String projectName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setProjectName(projectName);
	}
	
	@Transient
	public String getOutboundEndCustomerName() {
		if (outboundDeliveryRequest == null)
			return null;
		return outboundDeliveryRequest.getEndCustomerName();
	}

	@Transient
	public void setOutboundEndCustomerName(String endCustomerName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setEndCustomerName(endCustomerName);
	}
	
	@Transient
	public String getOutboundWarehouseName() {
		if (outboundDeliveryRequest == null)
			return null;
		return outboundDeliveryRequest.getWarehouseName();
	}

	@Transient
	public void setOutboundWarehouseName(String warehouseName) {
		if (outboundDeliveryRequest == null)
			outboundDeliveryRequest = new DeliveryRequest();
		outboundDeliveryRequest.setWarehouseName(warehouseName);
	}

	@Transient
	public StockRowStatus getInboundStockRowStatus() {
		return inboundStockRow != null ? inboundStockRow.getStatus() : null;
	}

	@Transient
	public void setInboundStockRowStatus(StockRowStatus inboundStockRowStatus) {
		if (inboundStockRow == null)
			inboundStockRow = new StockRow();
		inboundStockRow.setStatus(inboundStockRowStatus);
	}

	@Transient
	public String getPartNumberName() {
		return inboundStockRow != null ? inboundStockRow.getPartNumberName() : null;
	}

	@Transient
	public void setPartNumberName(String partNumberName) {
		if (inboundStockRow == null)
			inboundStockRow = new StockRow();
		inboundStockRow.setPartNumberName(partNumberName);
	}
	
	@Transient
	public String getPartNumberBrandName() {
		return inboundStockRow == null ? null : inboundStockRow.getPartNumberBrandName();
	}

	@Transient
	public void setPartNumberBrandName(String partNumberBrandName) {
		if (inboundStockRow == null)
			inboundStockRow = new StockRow();
		inboundStockRow.setPartNumberBrandName(partNumberBrandName);
	}

	@Transient
	public String getPartNumberDescription() {
		return inboundStockRow != null ? inboundStockRow.getPartNumberDescription() : null;
	}

	@Transient
	public void setPartNumberDescription(String partNumberDescription) {
		if (inboundStockRow == null)
			inboundStockRow = new StockRow();
		inboundStockRow.setPartNumberDescription(partNumberDescription);
	}

	@Transient
	public String getInboundDeliveryRequestReference() {
		return inboundStockRow != null ? inboundStockRow.getInboundDeliveryRequestReference() : null;
	}

	@Transient
	public void setInboundDeliveryRequestReference(String inboundDeliveryRequestReference) {
		if (inboundStockRow == null)
			inboundStockRow = new StockRow();
		inboundStockRow.setInboundDeliveryRequestReference(inboundDeliveryRequestReference);
	}

	@Transient
	public Integer getInboundDeliveryRequestId() {
		return inboundStockRow != null ? inboundStockRow.getInboundDeliveryRequestId() : null;
	}

	@Transient
	public void setInboundDeliveryRequestId(Integer inboundDeliveryRequestId) {
		if (inboundStockRow == null)
			inboundStockRow = new StockRow();
		inboundStockRow.setInboundDeliveryRequestId(inboundDeliveryRequestId);
	}

	@Transient
	public String getPackingDetailType() {
		return packingDetail != null ? packingDetail.getType() : null;
	}

	@Transient
	public void setPackingDetailType(String packingDetailType) {
		if (packingDetail == null)
			packingDetail = new PackingDetail();
		packingDetail.setType(packingDetailType);
	}

	@Transient
	public String getLocationName() {
		return inboundStockRow != null ? inboundStockRow.getLocationName() : null;
	}

	@Transient
	public void setLocationName(String locationName) {
		if (inboundStockRow == null)
			inboundStockRow = new StockRow();
		inboundStockRow.setLocationName(locationName);
	}

	@Transient
	public String getReference(Boolean isOutboundDeliveryRequest) {
		String result = "";
		if (isOutboundDeliveryRequest)
			result += tmpInboundDeliveryRequest.getReference() + " / ";
		result += tmpPartNumber + " / ";
		result += packingNumero;
		return result;
	}

	@Transient
	public String getKey() {
		String key = "";
		key += inboundStockRow.getId() + ";";
		key += packingNumero + ";";
		key += packingDetail.getId();
		return key;
	}

	@Transient
	public Integer getTmpPartNumberId() {
		if (tmpPartNumber == null)
			return null;
		return tmpPartNumber.getId();
	}

	@Transient
	public Integer getInboundStockRowId() {
		if (inboundStockRow == null)
			return null;
		return inboundStockRow.getId();
	}

	@Transient
	public Integer getNotNullInboundStockRowId() {
		if (inboundStockRow == null || inboundStockRow.getId() == null)
			return 0;
		return inboundStockRow.getId();
	}

	@Transient
	public Integer getNotNullId() {
		if (id == null)
			return 0;
		return id;
	}

	@Transient
	public String getKey1() {
		return inboundStockRow.getKey() + ";" + packingDetail.getId();
	}

	@Column(columnDefinition = "TEXT")
	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public PackingDetail getPackingDetail() {
		return packingDetail;
	}

	public void setPackingDetail(PackingDetail packingDetail) {
		this.packingDetail = packingDetail;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public StockRow getInboundStockRow() {
		return inboundStockRow;
	}

	public void setInboundStockRow(StockRow inboundStockRow) {
		this.inboundStockRow = inboundStockRow;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	public DeliveryRequest getOutboundDeliveryRequest() {
		return outboundDeliveryRequest;
	}

	public void setOutboundDeliveryRequest(DeliveryRequest outboundDeliveryRequest) {
		this.outboundDeliveryRequest = outboundDeliveryRequest;
	}

	public Integer getPackingNumero() {
		return packingNumero;
	}

	public void setPackingNumero(Integer packingNumero) {
		this.packingNumero = packingNumero;
	}

	@Transient
	public PartNumber getTmpPartNumber() {
		return tmpPartNumber;
	}

	@Transient
	public void setTmpPartNumber(PartNumber tmpPartNumber) {
		this.tmpPartNumber = tmpPartNumber;
	}

	@Transient
	public DeliveryRequest getTmpInboundDeliveryRequest() {
		return tmpInboundDeliveryRequest;
	}

	@Transient
	public void setTmpInboundDeliveryRequest(DeliveryRequest tmpInboundDeliveryRequest) {
		this.tmpInboundDeliveryRequest = tmpInboundDeliveryRequest;
	}

	@Transient
	public Location getTmpLocation() {
		return tmpLocation;
	}

	@Transient
	public StockRowStatus getTmpStockRowStatus() {
		return tmpStockRowStatus;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getEntryMode() {
		return entryMode;
	}

	public void setEntryMode(String entryMode) {
		this.entryMode = entryMode;
	}

}
