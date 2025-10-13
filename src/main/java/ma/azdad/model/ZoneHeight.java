package ma.azdad.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.google.gson.annotations.Expose;

@Entity
public class ZoneHeight extends GenericModel<Integer> implements Comparable<ZoneHeight> {

	@Expose
	private Integer id;
	private Integer numero;
	private ZoneColumn column;

	@Expose
	private String reference;

	private Double usedVolume = 0.0;

	@Expose
	private Double fillPercentage = 0.0;

	public boolean filter(String query) {
		return contains(query, numero);
	}

	public void generateReference() {
		this.reference = "" + column.getLine() + column + this;
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return String.valueOf(numero);
	}

	public ZoneHeight() {
		super();
	}

	public ZoneHeight(Integer numero) {
		super();
		this.numero = numero;
	}

	// getters & setters

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public ZoneColumn getColumn() {
		return column;
	}

	public void setColumn(ZoneColumn column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return "H" + numero;
	}

	@Override
	public int compareTo(ZoneHeight o) {
		return numero.compareTo(o.getNumero());
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public Double getFillPercentage() {
		return fillPercentage;
	}

	public void setFillPercentage(Double fillPercentage) {
		this.fillPercentage = fillPercentage;
	}

	public Double getUsedVolume() {
		return usedVolume;
	}

	public void setUsedVolume(Double usedVolume) {
		this.usedVolume = usedVolume;
	}

}
