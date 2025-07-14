package ma.azdad.mobile.model;

public class Vehicule {

	private Integer id;
	private String category;
	private String type;
	private String matricule;
	
	
	public Vehicule() {
		super();
	} 
	
	public Vehicule(Integer id, String category, String type, String matricule) {
		super();
		this.id = id;
		this.category = category;
		this.type = type;
		this.matricule = matricule;
	}  
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	
	
}
