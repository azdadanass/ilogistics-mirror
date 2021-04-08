package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.primefaces.model.map.LatLng;

@MappedSuperclass

public class GenericPlace extends GenericBean implements Serializable {

	protected String name;
	protected Double latitude = 33.966171;
	protected Double longitude = -6.8678663;
	protected String address1;
	protected String address2;
	protected String address3;
	protected String phone;
	protected String fax;

	protected String googleAddress;
	protected String googleCity;
	protected String googleRegion;

	public GenericPlace() {
		super();
	}

	public GenericPlace(Integer id, String name) {
		super(id);
		this.name = name;
	}

	public GenericPlace(String name, Double latitude, Double longitude) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GenericPlace(Integer id, String name, Double latitude, Double longitude) {
		super(id);
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public GenericPlace(String name, Double latitude, Double longitude, String address1, String address2, String address3, String phone, String fax) {
		super();
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.phone = phone;
		this.fax = fax;
	}

	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && name != null)
			result = name.toLowerCase().contains(query);
		if (!result && phone != null)
			result = phone.toLowerCase().contains(query);
		if (!result && fax != null)
			result = fax.toLowerCase().contains(query);
		if (!result && address1 != null)
			result = address1.toLowerCase().contains(query);
		if (!result && address2 != null)
			result = address2.toLowerCase().contains(query);
		if (!result && address3 != null)
			result = address3.toLowerCase().contains(query);
		return result;
	}

	@Transient
	public String getAddress() {
		return (address1 != null ? address1 + "\n" : "") + (address2 != null ? address2 + "\n" : "") + (address3 != null ? address3 : "");
	}

	@Transient
	public String getValue() {
		return latitude + "," + longitude;
	}

	@Transient
	public LatLng getLatLng() {
		return new LatLng(latitude, longitude);
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

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGoogleAddress() {
		return googleAddress;
	}

	public void setGoogleAddress(String googleAddress) {
		this.googleAddress = googleAddress;
	}

	public String getGoogleCity() {
		return googleCity;
	}

	public void setGoogleCity(String googleCity) {
		this.googleCity = googleCity;
	}

	public String getGoogleRegion() {
		return googleRegion;
	}

	public void setGoogleRegion(String googleRegion) {
		this.googleRegion = googleRegion;
	}

}
