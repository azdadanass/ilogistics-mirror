package ma.azdad.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
public class ZoneLine extends GenericModel<Integer> implements Comparable<ZoneLine> {

	private Integer numero;
	private Location location;
	private List<ZoneColumn> columnList = new ArrayList<ZoneColumn>();

	public boolean filter(String query) {
		return contains(query, numero);
	}

	public ZoneLine() {
		super();
	}

	public ZoneLine(Integer numero, String columnsStr, String heightsStr) {
		super();
		this.numero = numero;
		Arrays.stream(columnsStr.split(";")).forEach(j -> addColumn(new ZoneColumn(Integer.valueOf(j))));

		Integer[] heights = Arrays.stream(heightsStr.split(";")).map(Integer::valueOf).toArray(Integer[]::new);

		for (int i = 0; i < columnList.size(); i++) {
			ZoneColumn column = columnList.get(i);
			Integer height = heights[i];
			for (int j = 1; j <= height; j++) {
				ZoneHeight zh = new ZoneHeight(j);
				column.addHeight(zh);
				zh.generateReference();
			}
		}
		Collections.sort(columnList);
	}

	@Transient
	@Override
	public String getIdentifierName() {
		return String.valueOf(numero);
	}

	public void addColumn(ZoneColumn column) {
		column.setLine(this);
		columnList.add(column);
	}

	public void removeColumn(ZoneColumn column) {
		column.setLine(null);
		columnList.remove(column);
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
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ZoneColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ZoneColumn> columnList) {
		this.columnList = columnList;
	}

	@Override
	public String toString() {
		return "L" + numero;
	}

	@Override
	public int compareTo(ZoneLine o) {
		return numero.compareTo(o.getNumero());
	}

}
