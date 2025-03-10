package ma.azdad.mobile.model;

import ma.azdad.utils.Public;

public class DnMaterials {
    private int id;
    private String partNumber;
    private String partNumberImage;
    private String status;
    private String origin;
    private String inboundRequest;
    private String location;
    private double quantity;

    // Constructor
    public DnMaterials(int id, String partNumber, String status, String location, 
                       String origin, double quantity, String inboundRequest,String partNumberImage) {
        this.id = id;
        this.partNumber = partNumber;
        this.partNumberImage = Public.getPublicUrl(partNumberImage);
        this.status = status;
        this.location = location;
        this.origin = origin;
        this.quantity = quantity;
        this.inboundRequest = inboundRequest;
    }

    // Getters
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getInboundRequest() {
		return inboundRequest;
	}

	public void setInboundRequest(String inboundRequest) {
		this.inboundRequest = inboundRequest;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	
	
	
	 public String getPartNumberImage() {
		return partNumberImage;
	}

	public void setPartNumberImage(String partNumberImage) {
		this.partNumberImage = partNumberImage;
	}

	@Override
	    public String toString() {
	        return "DnMaterials{" +
	                "id=" + id +
	                ", partNumber='" + partNumber + '\'' +
	                ", status='" + status + '\'' +
	                ", origin='" + origin + '\'' +
	                ", inboundRequest='" + inboundRequest + '\'' +
	                ", location='" + location + '\'' +
	                ", quantity=" + quantity +
	                '}';
	    }

}

