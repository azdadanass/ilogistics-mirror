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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Vehicle extends GenericModel<Integer> implements Serializable {

	private Boolean active = true;
	private String category;
	private String type;
	private String matricule;
	private Boolean fromMyTools = false;
	private Boolean geolocalised = false;

	private Double maxWeight = 0.0;
	private Double maxVolume = 0.0;

	private Transporter transporter;
	private Tool tool;
	private VehicleType vehicleType;

	private List<VehicleFile> fileList = new ArrayList<>();
	private List<VehicleHistory> historyList = new ArrayList<>();
	private List<UserVehicle> userList = new ArrayList<UserVehicle>();

	// tmp
	private Integer toolId;

	public void init() {
		if (tool != null)
			toolId = tool.getId();
	}

	@Override
	public boolean filter(String query) {
		boolean result = super.filter(query);
		return result;
	}

	public Vehicle() {
		super();
	}

	public Vehicle(Transporter transporter) {
		super();
		this.transporter = transporter;
	}

	public Vehicle(Integer id, Boolean fromMyTools, String matricule, String toolMatricule) {
		super(id);
		this.matricule = matricule;
		this.fromMyTools = fromMyTools;
		if (fromMyTools)
			tool = new Tool(toolMatricule);
	}

	public void addUser(UserVehicle userVehicle) {
		userVehicle.setVehicle(this);
		userList.add(userVehicle);
	}

	public void removeUser(UserVehicle userVehicle) {
		userVehicle.setVehicle(null);
		userList.remove(userVehicle);
	}

	@Transient
	public String getCorrectMatricule() {
		return fromMyTools ? tool.getMatricule() : matricule;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Transporter getTransporter() {
		return transporter;
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	public Boolean getFromMyTools() {
		return fromMyTools;
	}

	public void setFromMyTools(Boolean fromMyTools) {
		this.fromMyTools = fromMyTools;
	}

	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(unique = true)
	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	@OneToOne(fetch = FetchType.EAGER, optional = true)
	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<VehicleFile> getFileList() {
		return fileList;
	}

	public void setFileList(List<VehicleFile> fileList) {
		this.fileList = fileList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", cascade = CascadeType.ALL)
	public List<VehicleHistory> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<VehicleHistory> historyList) {
		this.historyList = historyList;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UserVehicle> getUserList() {
		return userList;
	}

	public void setUserList(List<UserVehicle> userList) {
		this.userList = userList;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getGeolocalised() {
		return geolocalised;
	}

	public void setGeolocalised(Boolean geolocalised) {
		this.geolocalised = geolocalised;
	}

	@Transient
	public Integer getToolId() {
		return toolId;
	}

	@Transient
	public void setToolId(Integer toolId) {
		this.toolId = toolId;
	}

	public Double getMaxWeight() {
		return maxWeight;
	}

	public void setMaxWeight(Double maxWeight) {
		this.maxWeight = maxWeight;
	}

	public Double getMaxVolume() {
		return maxVolume;
	}

	public void setMaxVolume(Double maxVolume) {
		this.maxVolume = maxVolume;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	

}
