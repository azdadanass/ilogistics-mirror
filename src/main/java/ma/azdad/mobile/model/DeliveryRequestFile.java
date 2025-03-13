package ma.azdad.mobile.model;

import java.util.Date;

public class DeliveryRequestFile {
	
	private Integer id;
	private Date date = new Date();
	private String link ;
	private String extension;
	private String type;
	private String size;
	private String name;
	
	
	
	public DeliveryRequestFile(Integer id, Date date, String link, String extension, String type, String size,
			String name) {
		super();
		this.id = id;
		this.date = date;
		this.link = link;
		this.extension = extension;
		this.type = type;
		this.size = size;
		this.name = name;
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
