package ma.azdad.mobile.model;

public class IssueSupplier {
	
	private Integer id;
	private String name;
	
	
	public IssueSupplier() {
		super();
	}
	
	public IssueSupplier(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
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
	
	

}
