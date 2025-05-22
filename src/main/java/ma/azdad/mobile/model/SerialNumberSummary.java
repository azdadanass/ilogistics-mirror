package ma.azdad.mobile.model;

import ma.azdad.utils.Public;

public class SerialNumberSummary {
	
	private String packingDetail;
	private Integer packingNumero;
    private String inboundRequest;
    private Integer inboundRequestId;
	private String partNumber;
	private String partNumberImage;
    private double quantity;
    private double usedQuantity;
    
    
    
	public SerialNumberSummary() {
		super();

	}
	public SerialNumberSummary(String packingDetail, Integer packingNumero, String inboundRequest,
			Integer inboundRequestId, String partNumber, String partNumberImage, double quantity,
			double usedQuantity) {
		super();
		this.packingDetail = packingDetail;
		this.packingNumero = packingNumero;
		this.inboundRequest = inboundRequest;
		this.inboundRequestId = inboundRequestId;
		this.partNumber = partNumber;
		this.partNumberImage = Public.getPublicUrl(partNumberImage);
		this.quantity = quantity;
		this.usedQuantity = usedQuantity;
	}
	
	public String getPackingDetail() {
		return packingDetail;
	}
	public void setPackingDetail(String packingDetail) {
		this.packingDetail = packingDetail;
	}
	public Integer getPackingNumero() {
		return packingNumero;
	}
	public void setPackingNumero(Integer packingNumero) {
		this.packingNumero = packingNumero;
	}
	public String getInboundRequest() {
		return inboundRequest;
	}
	public void setInboundRequest(String inboundRequest) {
		this.inboundRequest = inboundRequest;
	}
	public Integer getInboundRequestId() {
		return inboundRequestId;
	}
	public void setInboundRequestId(Integer inboundRequestId) {
		this.inboundRequestId = inboundRequestId;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getPartNumberImage() {
		return partNumberImage;
	}
	public void setPartNumberImage(String partNumberImage) {
		this.partNumberImage = partNumberImage;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getUsedQuantity() {
		return usedQuantity;
	}
	public void setUsedQuantity(double usedQuantity) {
		this.usedQuantity = usedQuantity;
	}
	
    
    


}
