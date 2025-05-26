package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

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

	public void init() {
		if (inboundStockRow != null) {
			tmpPartNumber = inboundStockRow.getPartNumber();
			tmpInboundDeliveryRequest = inboundStockRow.getDeliveryRequest();
		}

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
	public DeliveryRequestSerialNumber(Integer id,Integer packingNumero,String serialNumber,String box,//
			String partNumberName,StockRowStatus inboundStockRowStatus,Integer inboundDeliveryRequestId,String inboundDeliveryRequestReference,//
			String packingDetailType,String locationName) {
		super(id);
		this.packingNumero = packingNumero;
		this.serialNumber = serialNumber;
		this.box=box;
		this.setPartNumberName(partNumberName);
		this.setInboundStockRowStatus(inboundStockRowStatus);
		this.setInboundDeliveryRequestId(inboundDeliveryRequestId);
		this.setInboundDeliveryRequestReference(inboundDeliveryRequestReference);
		this.setPackingDetailType(packingDetailType);
		this.setLocationName(locationName);
	}
	
	
	
	

	@Override
	public boolean filter(String query) {
		return contains(query, serialNumber, box);
	}
	
	@Transient
	public Boolean getIsEmpty() {
		return StringUtils.isBlank(serialNumber);
	}
	
	@Transient
	public StockRowStatus getInboundStockRowStatus(){
		return inboundStockRow!=null?inboundStockRow.getStatus():null;
	}

	@Transient
	public void setInboundStockRowStatus(StockRowStatus inboundStockRowStatus){
		if(inboundStockRow==null)
			inboundStockRow=new StockRow();
		inboundStockRow.setStatus(inboundStockRowStatus);
	}
	
	@Transient
	public String getPartNumberName(){
		return inboundStockRow!=null?inboundStockRow.getPartNumberName():null;
	}

	@Transient
	public void setPartNumberName(String partNumberName){
		if(inboundStockRow==null)
			inboundStockRow=new StockRow();
		inboundStockRow.setPartNumberName(partNumberName);
	}
	
	@Transient
	public String getInboundDeliveryRequestReference(){
		return inboundStockRow!=null?inboundStockRow.getInboundDeliveryRequestReference():null;
	}

	@Transient
	public void setInboundDeliveryRequestReference(String inboundDeliveryRequestReference){
		if(inboundStockRow==null)
			inboundStockRow=new StockRow();
		inboundStockRow.setInboundDeliveryRequestReference(inboundDeliveryRequestReference);
	}
	
	@Transient
	public Integer getInboundDeliveryRequestId(){
		return inboundStockRow!=null?inboundStockRow.getInboundDeliveryRequestId():null;
	}

	@Transient
	public void setInboundDeliveryRequestId(Integer inboundDeliveryRequestId){
		if(inboundStockRow==null)
			inboundStockRow=new StockRow();
		inboundStockRow.setInboundDeliveryRequestId(inboundDeliveryRequestId);
	}
	
	@Transient
	public String getPackingDetailType(){
		return packingDetail!=null?packingDetail.getType():null;
	}

	@Transient
	public void setPackingDetailType(String packingDetailType){
		if(packingDetail==null)
			packingDetail=new PackingDetail();
		packingDetail.setType(packingDetailType);
	}
	
	@Transient
	public String getLocationName(){
		return inboundStockRow!=null?inboundStockRow.getLocationName():null;
	}

	@Transient
	public void setLocationName(String locationName){
		if(inboundStockRow==null)
			inboundStockRow=new StockRow();
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
}
