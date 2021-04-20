package ma.azdad.model;

import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@MappedSuperclass
public abstract class GenericModel<ID> {

	protected ID id;

	public GenericModel() {
	}

	public GenericModel(ID id) {
		this.id = id;
	}

	public ID id() {
		return this.id;
	}

	public boolean filter(String query) {
		return contains(query, getIdStr());
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
		if (id == null)
			return null;
		if (id instanceof Integer)
			return String.format("%05d", id);
		else
			return id.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericModel<ID> other = (GenericModel<ID>) obj;
		if (id == null)
			return false; // id == null && this != obj
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[id=" + id + "]";
	}

}
