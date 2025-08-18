package ma.azdad.model;

import java.util.Date;

public class TransportationJobItineraryDto {
		private Integer id;
	    private double latitude;
	    private double longitude;
	    private String username;
	    private Date date;
	    private String imageUrl;


	    public TransportationJobItineraryDto() {}

	    public TransportationJobItineraryDto(Integer id,double latitude, double longitude, String username, Date date,String imageUrl) {
	        this.id = id;
	    	this.latitude = latitude;
	        this.longitude = longitude;
	        this.username = username;
	        this.date = date;
	        this.imageUrl = imageUrl;
	    }

	    // Getters and Setters
	    
	    public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}
		
		public double getLatitude() {
	        return latitude;
	    }

		public void setLatitude(double latitude) {
	        this.latitude = latitude;
	    }

	    public double getLongitude() {
	        return longitude;
	    }

	    public void setLongitude(double longitude) {
	        this.longitude = longitude;
	    }

	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public Date getDate() {
	        return date;
	    }

	    public void setDate(Date date) {
	        this.date = date;
	    }

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
	    

}
