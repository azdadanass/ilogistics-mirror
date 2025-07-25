package ma.azdad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity

public class Location extends GenericModel<Integer> implements Serializable {

	private String name;
	private Double surface;
	private Double volume;

	private StockRowState stockRowState; // null = normal & faulty

	private Warehouse warehouse;
	private List<LocationDetail> detailList = new ArrayList<>();

	public Location() {
		super();
	}
	
	

	public Location(Integer id,String name) {
		super(id);
		this.name = name;
	}



	public Location(Warehouse warehouse) {
		super();
		this.warehouse = warehouse;
	}

	public StockRowState getStockRowState() {
		return stockRowState;
	}

	public void setStockRowState(StockRowState stockRowState) {
		this.stockRowState = stockRowState;
	}

	@Override
	public boolean filter(String query) {
		return contains(query, name);
	}

	public void addDetail(LocationDetail detail) {
		detail.setLocation(this);
		detailList.add(detail);
	}

	public void removeDetail(LocationDetail detail) {
		detail.setLocation(null);
		detailList.remove(detail);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getSurface() {
		return surface;
	}

	public void setSurface(Double surface) {
		this.surface = surface;
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<LocationDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<LocationDetail> detailList) {
		this.detailList = detailList;
	}

}
