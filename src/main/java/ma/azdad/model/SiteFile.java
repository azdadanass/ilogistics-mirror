package ma.azdad.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

import org.apache.commons.io.FilenameUtils;

import ma.azdad.service.UtilsFunctions;

@Entity

public class SiteFile extends GenericFile<Site> implements Serializable {

	public SiteFile() {

	}

	public SiteFile(Date date, String name, String type, File file, Site parent, User user) {
		this.date = date;
		this.name = name;
		this.type = type;
		this.link = file.getName();
		this.extension = FilenameUtils.getExtension(this.link);
		this.size = UtilsFunctions.getFormattedSize(file.length());
		this.parent = parent;
		this.user = user;
	}

}
