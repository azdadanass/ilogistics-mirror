package ma.azdad.mobile.model;

public class Vehicule {

	private Integer id;
	private String vehicleTypeName;
	private String matricule;

	public Vehicule() {
		super();
	}

	public Vehicule(Integer id, String vehicleTypeName, String matricule) {
		super();
		this.id = id;
		this.vehicleTypeName = vehicleTypeName;
		this.matricule = matricule;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVehicleTypeName() {
		return vehicleTypeName;
	}

	public void setVehicleTypeName(String vehicleTypeName) {
		this.vehicleTypeName = vehicleTypeName;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

}
