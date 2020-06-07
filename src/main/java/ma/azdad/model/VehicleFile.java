package ma.azdad.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;

import org.apache.commons.io.FilenameUtils;

import ma.azdad.service.UtilsFunctions;

@Entity

public class VehicleFile extends GenericFile<Vehicle> implements Serializable {

	public VehicleFile() {

	}

	public VehicleFile(String folder, File file, String type, String name, Vehicle parent, User user) {
		this.date = new Date();
		this.link = folder + "/" + file.getName();
		this.extension = FilenameUtils.getExtension(this.link);
		this.size = UtilsFunctions.getFormattedSize(file.length());
		this.type = type;
		this.name = name;
		this.user = user;
		this.parent = parent;
	}

}
