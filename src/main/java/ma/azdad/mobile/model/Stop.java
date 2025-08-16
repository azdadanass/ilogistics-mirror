package ma.azdad.mobile.model;

import java.util.Date;

public class Stop {
	
	private Integer id;
	private Date date;
	private String placeName;
	private String type;
	
	
	public Stop(Integer id, Date date, String placeName, String type) {
		super();
		this.id = id;
		this.date = date;
		this.placeName = placeName;
		this.type = type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	

}
