package ma.azdad.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;

import ma.azdad.service.UtilsFunctions;

@MappedSuperclass
public abstract class GenericFile<A extends GenericBean> extends GenericBean implements Serializable {

	protected Date date;
	protected String link = "noimage.jpg";
	protected String extension;
	protected String type;
	protected String size;
	protected String name;
	protected User user;
	protected A parent;

	public GenericFile() {
	}

	public GenericFile(String folder, File file, String type, String name, User user) {
		this.date = new Date();
		this.link = folder + "/" + file.getName();
		this.extension = FilenameUtils.getExtension(this.link);
		this.size = UtilsFunctions.getFormattedSize(file.length());
		this.type = type;
		this.name = name;
		this.user = user;
	}

	@Transient
	public Boolean getIsImage() {
		return UtilsFunctions.contains(extension, "png", "jpg", "jpeg", "gif", "bmp");
	}

	@Transient
	public Boolean getIsPdf() {
		return "pdf".equals(extension);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(length = 200)
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

	@Column(length = 500)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_username")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
