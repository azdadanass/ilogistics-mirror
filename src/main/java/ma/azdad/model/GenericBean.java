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

	protected Boolean contains(String string, String query) {
		return string != null && string.toLowerCase().contains(query);
	}

	protected Boolean contains(Integer i, String query) {
		return i != null && String.valueOf(i).toLowerCase().contains(query);
	}

	protected Boolean contains(Double d, String query) {
		return d != null && String.valueOf(d).toLowerCase().contains(query);
	}

	protected Boolean contains(Date date, String query) {
		return date != null && UtilsFunctions.getFormattedDate(date).toLowerCase().contains(query);
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
		if (id != null)
			return getIdStr().contains(query);
		return false;
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
