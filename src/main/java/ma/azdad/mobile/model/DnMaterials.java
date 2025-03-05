package ma.azdad.mobile.model;

public class DnMaterials {
    private int id;
    private String partNumber;
    private String status;
    private String origin;
    private String inboundRequest;
    private String location;
    private double quantity;

    // Constructor
    public DnMaterials(int id, String partNumber, String status, String location, 
                       String origin, double quantity, String inboundRequest) {
        this.id = id;
        this.partNumber = partNumber;
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

