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
import javax.persistence.Transient;

import ma.azdad.utils.Pair;

@Entity
public class Location extends GenericModel<Integer> implements Serializable {

	private String name;
	private Double surface;
	private Double volume;
	private Boolean zoning = false;
	private Double slotSize;

	// null --> ALL values
	private StockRowState stockRowState;
	private Boolean stackable;
	private Boolean fragile;
	private Boolean flammable;

	private Warehouse warehouse;
	private List<LocationDetail> detailList = new ArrayList<>();
	private List<ZoneLine> lineList = new ArrayList<>();
	private List<ZoneIndustry> industryList = new ArrayList<>();

	// tmp
	private List<Pair<String, String>> options;

	@Transient
	public void initOptions() {
		options = new ArrayList<Pair<String, String>>();
		options.add(new Pair<String, String>("Stackable", stackable == null ? "All" : stackable ? "Yes" : "No"));
		options.add(new Pair<String, String>("Fragile", fragile == null ? "All" : fragile ? "Yes" : "No"));
		options.add(new Pair<String, String>("Flammable", flammable == null ? "All" : flammable ? "Yes" : "No"));
	}

	public void calculateOptions() {
		String stackableStr = options.stream().filter(i -> "Stackable".equals(i.getKey())).findFirst().get().getValue();
		String fragileStr = options.stream().filter(i -> "Fragile".equals(i.getKey())).findFirst().get().getValue();
		String flammableStr = options.stream().filter(i -> "Flammable".equals(i.getKey())).findFirst().get().getValue();

		stackable = "All".equals(stackableStr) ? null : "Yes".equals(stackableStr);
		fragile = "All".equals(fragileStr) ? null : "Yes".equals(fragileStr);
		flammable = "All".equals(flammableStr) ? null : "Yes".equals(flammableStr);
	}

	public Location() {
		super();
	}

	public Location(Integer id, String name) {
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

	public void addLine(ZoneLine line) {
		line.setLocation(this);
		lineList.add(line);
	}

	public void removeLine(ZoneLine line) {
		line.setLocation(null);
		lineList.remove(line);
	}

	public void addIndustry(ZoneIndustry industry) {
		industry.setLocation(this);
		industryList.add(industry);
	}

	public void removeIndustry(ZoneIndustry industry) {
		industry.setLocation(null);
		industryList.remove(industry);
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ZoneLine> getLineList() {
		return lineList;
	}

	public void setLineList(List<ZoneLine> lineList) {
		this.lineList = lineList;
	}

	@Transient
	public List<ZoneHeight> getHeightList() {
		List<ZoneHeight> result = new ArrayList<ZoneHeight>();
		lineList.forEach(l -> l.getColumnList().forEach(c -> result.addAll(c.getHeightList())));
		return result;
	}

	public Boolean getZoning() {
		return zoning;
	}

	public void setZoning(Boolean zoning) {
		this.zoning = zoning;
	}

	public Double getSlotSize() {
		return slotSize;
	}

	public void setSlotSize(Double slotSize) {
		this.slotSize = slotSize;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<ZoneIndustry> getIndustryList() {
		return industryList;
	}

	public void setIndustryList(List<ZoneIndustry> industryList) {
		this.industryList = industryList;
	}

	public Boolean getStackable() {
		return stackable;
	}

	public void setStackable(Boolean stackable) {
		this.stackable = stackable;
	}

	public Boolean getFragile() {
		return fragile;
	}

	public void setFragile(Boolean fragile) {
		this.fragile = fragile;
	}

	public Boolean getFlammable() {
		return flammable;
	}

	public void setFlammable(Boolean flammable) {
		this.flammable = flammable;
	}

	@Transient
	public List<Pair<String, String>> getOptions() {
		return options;
	}

	@Transient
	public void setOptions(List<Pair<String, String>> options) {
		this.options = options;
	}

}
