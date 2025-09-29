package ma.azdad.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class ZoneColumn extends GenericModel<Integer> implements Comparable<ZoneColumn> {

	private Integer numero;
	private ZoneLine line;

	private List<ZoneHeight> heightList = new ArrayList<ZoneHeight>();

	public boolean filter(String query) {
		return contains(query, numero);
	}

	public ZoneColumn() {
		super();
	}

	public ZoneColumn(Integer numero) {
		super();
		this.numero = numero;
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return String.valueOf(numero);
	}

	public void addHeight(ZoneHeight height) {
		height.setColumn(this);
		heightList.add(height);
	}

	public void removeHeight(ZoneHeight height) {
		height.setColumn(null);
		heightList.remove(height);
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
	public ZoneLine getLine() {
		return line;
	}

	public void setLine(ZoneLine line) {
		this.line = line;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ZoneHeight> getHeightList() {
		return heightList;
	}

	public void setHeightList(List<ZoneHeight> heightList) {
		this.heightList = heightList;
	}

	@Override
	public String toString() {
		return "C" + numero;
	}

	@Override
	public int compareTo(ZoneColumn o) {
		return numero.compareTo(o.getNumero());
	}

}
