package ma.azdad.mobile.model;

public class Location  {

	private Integer id;
	private String name;
	private Double surface;
	private Double volume;
	
	public Location() {
		super();
		
	}
	
	public Location(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Location(Integer id, String name, Double surface, Double volume) {
		super();
		this.id = id;
		this.name = name;
		this.surface = surface;
		this.volume = volume;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	
	


}
