package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DelegationDetail implements Serializable {

	private Integer iddelegationDetail;
	private String type;

	private Delegation delegation;
	private Project project;
	private Lob lob;
	private User resource;

	public DelegationDetail() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getIddelegationDetail() {
		return iddelegationDetail;
	}

	public void setIddelegationDetail(Integer iddelegationDetail) {
		this.iddelegationDetail = iddelegationDetail;
	}

	@Column(name = "type", length = 50)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delegation_iddelegation", nullable = false)
	public Delegation getDelegation() {
		return delegation;
	}

	public void setDelegation(Delegation delegation) {
		this.delegation = delegation;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_idproject", nullable = true)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username", nullable = true)
	public User getResource() {
		return resource;
	}

	public void setResource(User resource) {
		this.resource = resource;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public Lob getLob() {
		return lob;
	}

	public void setLob(Lob lob) {
		this.lob = lob;
	}

}
