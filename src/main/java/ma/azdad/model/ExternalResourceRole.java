package ma.azdad.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ExternalResourceRole extends GenericModel<Integer> {

	private Role role;
	private ExternalResource externalResource;

	public ExternalResourceRole() {
	}

	public ExternalResourceRole(Role role) {
		this.role = role;
	}

	@Enumerated(EnumType.STRING)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public ExternalResource getExternalResource() {
		return externalResource;
	}

	public void setExternalResource(ExternalResource externalResource) {
		this.externalResource = externalResource;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((externalResource == null) ? 0 : externalResource.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		ExternalResourceRole other = (ExternalResourceRole) obj;
		if (externalResource == null) {
			if (other.externalResource != null)
				return false;
		} else if (!externalResource.equals(other.externalResource))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
