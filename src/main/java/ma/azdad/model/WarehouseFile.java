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

	public WarehouseFile(Date date, String name, String type, File file, Warehouse parent, User user) {
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
