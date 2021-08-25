package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.Location;
import ma.azdad.model.Warehouse;
import ma.azdad.model.WarehouseFile;
import ma.azdad.model.WarehouseManager;
import ma.azdad.repos.WarehouseRepos;
import ma.azdad.service.CustomerService;
import ma.azdad.service.LocationService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.UserService;
import ma.azdad.service.WarehouseFileService;
import ma.azdad.service.WarehouseHistoryService;
import ma.azdad.service.WarehouseService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class WarehouseView extends GenericView<Integer, Warehouse, WarehouseRepos, WarehouseService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	protected WarehouseService warehouseService;

	@Autowired
	protected WarehouseFileService warehouseFileService;

	@Autowired
	protected FileUploadView fileUploadView;

	@Autowired
	protected UserService userService;

	@Autowired
	protected CustomerService customerService;

	@Autowired
	protected SupplierService supplierService;

	@Autowired
	protected LocationService locationService;

	@Autowired
	protected WarehouseHistoryService warehouseHistoryService;

	private Warehouse warehouse = new Warehouse();
	private WarehouseFile warehouseFile;
	private Location location;

	private MapModel mapModel;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage) {
			warehouse = warehouseService.findOne(id);
			warehouse.init();
		} else if (isViewPage)
			warehouse = warehouseService.findOne(id);
		if (isAddPage || isEditPage || isViewPage)
			refreshMapModel();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = warehouseService.findAll();
	}

	public void refreshWarehouse() {
		warehouseService.flush();
		warehouse = warehouseService.findOne(warehouse.getId());
	}

	// GPS
	public void refreshMapModel() {
		mapModel = new DefaultMapModel();
		mapModel.addOverlay(new Marker(new LatLng(warehouse.getLatitude(), warehouse.getLongitude()), "Marker"));
	}

	public void onPointSelect(PointSelectEvent event) {
		LatLng latlng = event.getLatLng();
		warehouse.setLatitude(latlng.getLat());
		warehouse.setLongitude(latlng.getLng());
		refreshMapModel();
	}

	// LOCATIONS MANAGEMENT
	public void initLocation() {
		location = new Location(warehouse);
	}

	public Boolean canSaveLocation() {
		return sessionView.getIsAdmin();
	}

	public void saveLocation() {
		if (!canSaveLocation())
			return;
		if (locationService.isNameExists(location)) {
			FacesContextMessages.ErrorMessages("Name already exits !");
			return;
		}

		locationService.save(location);
		refreshWarehouse();
		RequestContext.getCurrentInstance().execute("PF('addEditLocationDlg').hide()");
	}

	public Boolean canDeleteLocation() {
		return sessionView.getIsAdmin();
	}

	public void deleteLocation() {
		if (!canDeleteLocation())
			return;
		try {
			locationService.delete(location);
			refreshWarehouse();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}

	}

	// SAVE WAREHOUSE
	public Boolean canSaveWarehouse() {
		return sessionView.getIsAdmin();
	}

	public String saveWarehouse() {
		if (!canSaveWarehouse() || !validate())
			return null;

		if (warehouse.getOwner() != null) {
			warehouse.setCustomer(null);
			warehouse.setSupplier(null);
			if ("customer".equals(warehouse.getOwner().getBeanName()))
				warehouse.setCustomer(customerService.findOne(warehouse.getOwner().getIntegerValue()));
			else if ("supplier".equals(warehouse.getOwner().getBeanName()))
				warehouse.setSupplier(supplierService.findOne(warehouse.getOwner().getIntegerValue()));
		}
		warehouse = warehouseService.save(warehouse);

		if (isAddPage)
			warehouseHistoryService.created(warehouse, sessionView.getUser());
		else
			warehouseHistoryService.edited(warehouse, sessionView.getUser());

		return addParameters(viewPage, "faces-redirect=true", "id=" + warehouse.getId());
	}

	private Boolean validate() {
		if (warehouse.getManagerList().isEmpty())
			return FacesContextMessages.ErrorMessages("Manager List should not be empty");
		if (warehouse.getManagerList().stream().map(i -> i.getUser().getUsername()).distinct().count() < warehouse.getManagerList().size())
			return FacesContextMessages.ErrorMessages("Duplicate Managers");

		return true;
	}

	// DELETE WAREHOUSE
	public Boolean canDeleteWarehouse() {
		return sessionView.getIsAdmin();
	}

	public String deleteWarehouse() {
		if (canDeleteWarehouse())
			try {
				warehouseService.delete(warehouse);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return listPage;
	}

	// FILES MANAGEMENT
	private String warehouseFileType;
	private Integer warehouseFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		WarehouseFile warehouseFile = new WarehouseFile(file, warehouseFileType, event.getFile().getFileName(), sessionView.getUser(), warehouse);
		warehouseFileService.save(warehouseFile);
		synchronized (WarehouseView.class) {
			refreshWarehouse();
		}
	}

	public void deleteWarehouseFile() {
		try {
			warehouseFileService.delete(warehouseFileId);
			refreshWarehouse();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}

	}

	// manager
	public Boolean canAddManager() {
		return sessionView.getIsAdmin();
	}

	public void addManager() {
		if (canAddManager())
			warehouse.addManager(new WarehouseManager());
	}

	public Boolean canDeleteManager() {
		return sessionView.getIsAdmin();
	}

	public void deleteManager(WarehouseManager manager) {
		if (canDeleteManager())
			warehouse.removeManager(manager);
	}

	// GENERIC
	public List<Warehouse> findLight() {
		return warehouseService.findLight();
	}

	public List<Warehouse> findAll() {
		return warehouseService.findAll();
	}

	public List<Warehouse> find(DeliveryRequestType deliveryRequestType, Integer projectId) {
		if (deliveryRequestType == null)
			return null;
		switch (deliveryRequestType) {
		case OUTBOUND:
			return warehouseService.find(projectId);
		default:
			return findAll();
		}
	}

	// GETTERS & SETTERS




	public WarehouseService getWarehouseService() {
		return warehouseService;
	}

	public void setWarehouseService(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public FileUploadView getFileUploadView() {
		return fileUploadView;
	}

	public void setFileUploadView(FileUploadView fileUploadView) {
		this.fileUploadView = fileUploadView;
	}

	public WarehouseFileService getWarehouseFileService() {
		return warehouseFileService;
	}

	public void setWarehouseFileService(WarehouseFileService warehouseFileService) {
		this.warehouseFileService = warehouseFileService;
	}

	public String getWarehouseFileType() {
		return warehouseFileType;
	}

	public void setWarehouseFileType(String warehouseFileType) {
		this.warehouseFileType = warehouseFileType;
	}

	public Integer getWarehouseFileId() {
		return warehouseFileId;
	}

	public void setWarehouseFileId(Integer warehouseFileId) {
		this.warehouseFileId = warehouseFileId;
	}

	public WarehouseFile getWarehouseFile() {
		return warehouseFile;
	}

	public void setWarehouseFile(WarehouseFile warehouseFile) {
		this.warehouseFile = warehouseFile;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
