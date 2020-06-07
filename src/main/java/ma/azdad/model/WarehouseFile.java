package ma.azdad.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.io.FilenameUtils;

import ma.azdad.service.UtilsFunctions;

@Entity
@Table(name = "il_warehouse_file")

public class WarehouseFile extends GenericFile<Warehouse> implements Serializable {

	public WarehouseFile() {

	}

	public WarehouseFile(String folder, File file, String type, String name, Warehouse parent, User user) {
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
