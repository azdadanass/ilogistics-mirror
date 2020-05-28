package ma.azdad.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import ma.azdad.service.UtilsFunctions;

@MappedSuperclass
public abstract class GenericBean implements Serializable {
	protected static final long serialVersionUID = -2866941365258232556L;
	protected Integer id;

	public GenericBean() {
	}

	public GenericBean(Integer id) {
		this.id = id;
	}

	protected Boolean contains(String string, String query) {
		return string != null && string.toLowerCase().contains(query);
	}

	protected Boolean contains(Double d, String query) {
		return d != null && String.valueOf(d).toLowerCase().contains(query);
	}

	protected Boolean contains(Date date, String query) {
		return date != null && UtilsFunctions.getFormattedDate(date).toLowerCase().contains(query);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public String getNumero() {
		return String.format("%05d", id);
	}

	@Transient
	public boolean filter(String query) {
		if (id != null)
			return getNumero().contains(query);
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
		try {
			return id.equals(((GenericBean) obj).getId());
		} catch (Exception e) {
			return false;
		}
	}

}
