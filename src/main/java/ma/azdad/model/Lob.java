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
@Table(name = "lob")

public class Lob implements Serializable {

	private Integer idlob;
	private String name;
	private User manager;
	private Bu bu;

	public Lob() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idlob", unique = true, nullable = false)
	public Integer getIdlob() {
		return this.idlob;
	}

	public void setIdlob(Integer idlob) {
		this.idlob = idlob;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "manager_idmanager", nullable = false)
	public User getManager() {
		return manager;
	}

	public void setManager(User manager) {
		this.manager = manager;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Bu getBu() {
		return bu;
	}

	public void setBu(Bu bu) {
		this.bu = bu;
	}

}