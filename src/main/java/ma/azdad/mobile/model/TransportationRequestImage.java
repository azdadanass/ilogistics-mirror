package ma.azdad.mobile.model;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import ma.azdad.model.GenericModel;
import ma.azdad.model.TransportationRequest;
import ma.azdad.utils.Public;

public class TransportationRequestImage {

	private Integer id;
	private String type;
	private String value;
	// photo tags
	private Double latitude;
	private Double longitude;
	private String googleAddress;
	private Date takenDate;
	private String phoneModel;

	public TransportationRequestImage() {
		super();
	}

	public TransportationRequestImage(Integer id, String type, String value) {
		super();
		this.id = id;
		this.type = type;
		this.value = Public.getPublicUrl(value);
	}
	
	

	public TransportationRequestImage(Integer id, String type, String value, Double latitude, Double longitude,
			String googleAddress, Date takenDate, String phoneModel) {
		super();
		this.id = id;
		this.type = type;
		this.value = value;
		this.latitude = latitude;
		this.longitude = longitude;
		this.googleAddress = googleAddress;
		this.takenDate = takenDate;
		this.phoneModel = phoneModel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getGoogleAddress() {
		return googleAddress;
	}

	public void setGoogleAddress(String googleAddress) {
		this.googleAddress = googleAddress;
	}

	public Date getTakenDate() {
		return takenDate;
	}

	public void setTakenDate(Date takenDate) {
		this.takenDate = takenDate;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}
	
	
	
	

}
