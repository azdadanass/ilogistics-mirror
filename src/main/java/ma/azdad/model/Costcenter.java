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
@Table(name = "costcenter")

public class Costcenter implements Serializable {

	private Integer idcostcenter;
	private Integer year;
	private Lob lob;

	public Costcenter() {
	}

	public Costcenter(Lob lob) {
		this.lob = lob;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idcostcenter", unique = true, nullable = false)
	public Integer getIdcostcenter() {
		return idcostcenter;
	}

	public void setIdcostcenter(Integer idcostcenter) {
		this.idcostcenter = idcostcenter;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lob_idlob", nullable = false)
	public Lob getLob() {
		return this.lob;
	}

	public void setLob(Lob lob) {
		this.lob = lob;
	}
	
	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

}
