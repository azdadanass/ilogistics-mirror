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
import javax.persistence.Table;

@Entity
@Table(name = "delegationdetail")
public class Delegationdetail implements Serializable {

	private Integer iddelegationDetail;
	private String type;

	private Delegation delegation;
	private Project project;
	private User resource;

	public Delegationdetail() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iddelegationDetail", unique = true, nullable = false)
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

}
