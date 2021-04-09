package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.Filterable;

@MappedSuperclass
public abstract class GenericBean implements Serializable, Filterable {

	protected Integer id;

	public GenericBean() {
	}

	public GenericBean(Integer id) {
		this.id = id;
	}

	public Integer id() {
		return this.id;
	}

	protected Boolean contains(String query, Object... objects) {
		for (int i = 0; i < objects.length; i++) {
			Object o = objects[i];
			if (o == null)
				continue;
			if (o instanceof String && ((String) o).toLowerCase().contains(query))
				return true;
			if (o instanceof Double && ((Double) o).toString().contains(query))
				return true;
			if (o instanceof Integer && ((Integer) o).toString().contains(query))
				return true;
			if (o instanceof Date && UtilsFunctions.getFormattedDate(((Date) o)).contains(query))
				return true;
		}
		return false;
	}

	@Transient
	public String getIdentifierName() {
		return getIdStr();
	}

	@Transient
	public String getIdStr() {
		return String.format("%05d", id);
	}

	@Override
	public boolean filter(String query) {
		return contains(query, getIdStr());
	}

	@Transient
	public String getClassName() {
		return getClass().getSimpleName();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericBean other = (GenericBean) obj;
		if (id == null)
			return false; // id == null && this != obj
		else if (!id.equals(other.id))
			return false;
		return true;
	}

}
