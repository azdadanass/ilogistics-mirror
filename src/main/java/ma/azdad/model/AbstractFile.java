package ma.azdad.model;

import java.util.Arrays;
import java.util.Date;

import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.App;

public abstract class AbstractFile extends GenericModel<Integer> {

	public abstract String getFileName();

	public abstract String getFileLink();

	public abstract String getFileExtension();

	@Transient
	public Boolean getIsImage() {
		return Arrays.asList("png", "jpg", "jpeg", "gif", "bmp").contains(getFileExtension());
	}

	@Transient
	public Boolean getIsOfficeDocument() {
		return Arrays.asList("doc", "docx", "xls", "xlsx").contains(getFileExtension());
	}

	@Transient
	public Boolean getIsTextFile() {
		return Arrays.asList("txt", "conf").contains(getFileExtension());
	}

	@Transient
	public Boolean getIsGeographicFile() {
		return Arrays.asList("kml", "kmz").contains(getFileExtension());
	}

	@Transient
	public Boolean getIsPdf() {
		return "pdf".equals(getFileExtension());
	}

	@Transient
	public String getPublicUrl() {
		return App.PUBLIC.getHttpLink() + "/file/" + key(getFileLink()) + "/" + getFileLink();
	}

	@Transient
	public String getIframeSrc() {
		if (getFileLink() == null)
			return null;
		if (getIsTextFile() || getIsGeographicFile())
			return App.PUBLIC.getHttpsLink() + "/file/preview/txt/" + getFileLink();
		else if (getIsOfficeDocument())
			return "https://view.officeapps.live.com/op/embed.aspx?src=" + App.PUBLIC.getHttpsLink() + "/file/" + getFileLink();
		return null;
	}

	@Transient
	public String getFileUrl() {
		return App.PUBLIC.getHttpsLink() + "/file/" + getFileLink();
	}

	private String key(String fileName) { // same as public.fileController
		String k = "pioneer" + UtilsFunctions.getFormattedDate(new Date());
		return UtilsFunctions.stringToMD5(fileName + k).substring(10, 20);
	}

}
