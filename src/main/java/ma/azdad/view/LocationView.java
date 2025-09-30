package ma.azdad.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.Location;
import ma.azdad.model.LocationDetail;
import ma.azdad.model.ZoneIndustry;
import ma.azdad.repos.LocationRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.LocationService;
import ma.azdad.service.PartNumberIndustryService;
import ma.azdad.service.SupplierService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class LocationView extends GenericView<Integer, Location, LocationRepos, LocationService> {

	@Autowired
	protected SessionView sessionView;

	@Autowired
	protected LocationService locationService;

	@Autowired
	protected CompanyService companyService;

	@Autowired
	protected CustomerService customerService;

	@Autowired
	protected SupplierService supplierService;
	
	@Autowired
	protected PartNumberIndustryService partNumberIndustryService;

	private Location location = new Location();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			location = locationService.findOne(id);
		else if (isViewPage)
			location = locationService.findOne(id);

	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = locationService.findAll();
	}

	public void refreshLocation() {
		locationService.flush();
		location = locationService.findOne(location.getId());
	}

	public void refreshLocation(Integer id) {
		location = service.findOne(id);
	}

	// SAVE LOCATION
	public Boolean canSaveLocation() {
		return true;
	}

	public String saveLocation() {
		if (canSaveLocation()) {
			location = locationService.save(location);
			return addParameters(viewPage, "faces-redirect=true", "id=" + location.getId());
		}
		return listPage;
	}

	// details
	private Boolean editDetailList = false;

	public Boolean canAddDetail() {
		return editDetailList && canSaveLocation();
	}

	public void addDetail() {
		if (canAddDetail())
			location.addDetail(new LocationDetail());
	}

	public Boolean canDeleteDetail() {
		return canAddDetail();
	}

	public void deleteDetail(LocationDetail detail) {
		if (canDeleteDetail())
			location.removeDetail(detail);

	}

	public Boolean canSaveDetailList() {
		return editDetailList && canSaveLocation();
	}

	public Boolean validateDetailList() {
		Set<String> keySet = new HashSet<String>();
		for (LocationDetail detail : location.getDetailList()) {
			if (detail.getOwnerType() == null)
				return FacesContextMessages.ErrorMessages("Owner Type should not be null");
			if (detail.getOwnerId() == null)
				return FacesContextMessages.ErrorMessages("Owner shoud not be null");
			if (!keySet.add(detail.getOwnerType() + ";" + detail.getOwnerId()))
				return FacesContextMessages.ErrorMessages("Duplicate row !");
		}

		return true;
	}

	public void saveDetailList() {
		if (!canSaveDetailList())
			return;
		if (!validateDetailList())
			return;

		for (LocationDetail detail : location.getDetailList()) {
			switch (detail.getOwnerType()) {
			case COMPANY:
				detail.setCompany(companyService.findOne(detail.getCompanyId()));
				detail.setCustomer(null);
				detail.setSupplier(null);
				break;
			case CUSTOMER:
				detail.setCustomer(customerService.findOne(detail.getCustomerId()));
				detail.setCompany(null);
				detail.setSupplier(null);
				break;
			case SUPPLIER:
				detail.setSupplier(supplierService.findOne(detail.getSupplierId()));
				detail.setCompany(null);
				detail.setCustomer(null);
				break;
			default:
				break;
			}
		}

		service.save(location);
		location = service.findOne(location.getId());
		editDetailList = false;

	}

	// DELETE LOCATION
	public Boolean canDeleteLocation() {
		return true;
	}

	public String deleteLocation() {
		if (canDeleteLocation())
			try {
				locationService.delete(location);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return listPage;
	}

	// industry / category / type management
	private Integer partNumberIndustryId;

	public Boolean canAddIndustry() {
		return sessionView.getIsAdmin();
	}
	
	public Boolean validateAddIndustry() {
		if(model.getIndustryList().stream().filter(i->i.getIndustryId().equals(partNumberIndustryId)).count()>0)
			return FacesContextMessages.ErrorMessages("Already exists");
		
		return true;
	}

	public void addIndustry() {
		if (!canAddIndustry())
			return;
		if (!validateAddIndustry())
			return;
		ZoneIndustry industry = new ZoneIndustry();
		industry.setIndustry(partNumberIndustryService.findOneLight(partNumberIndustryId));
		model.addIndustry(industry);
		service.save(model);
		refreshModel();
	}
	
	public Boolean canDeleteIndustry() {
		return canAddIndustry();
	}
	
	public void deleteIndustry(ZoneIndustry zoneIndustry) {
		model.removeIndustry(zoneIndustry);
		service.save(model);
		refreshModel();
	}

	// generate zoning

	public Boolean canGenerateZoning() {
		return sessionView.getIsAdmin() && !model.getZoning() && model.getLineList().isEmpty();
	}

	public void generateZoning(FileUploadEvent event) {
		try {
			service.generateZoning(event.getFile().getInputstream()).forEach(line -> model.addLine(line));
			service.save(model);
			refreshModel();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}
	}

	public String getHeightListJson() {
		model.getHeightList().forEach(i -> {
			System.out.println(i.getReference());
		});
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(model.getHeightList());
	}

	// generic
	public List<Location> findByWarehouseAndStockRowStateAndOwner(DeliveryRequest deliveryRequest) {
		return service.findByWarehouseAndStockRowStateAndOwner(deliveryRequest);
	}

	public List<Location> findLightByWarehouse(Integer warehouseId) {
		return service.findLightByWarehouse(warehouseId);
	}

	// GETTERS & SETTERS

	public LocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(LocationService locationService) {
		this.locationService = locationService;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Boolean getEditDetailList() {
		return editDetailList;
	}

	public void setEditDetailList(Boolean editDetailList) {
		this.editDetailList = editDetailList;
	}

	public Location getModel() {
		return this.model;
	}

	public void setModel(Location model) {
		this.model = model;
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public CompanyService getCompanyService() {
		return companyService;
	}

	public void setCompanyService(CompanyService companyService) {
		this.companyService = companyService;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public SupplierService getSupplierService() {
		return supplierService;
	}

	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	public Integer getPartNumberIndustryId() {
		return partNumberIndustryId;
	}

	public void setPartNumberIndustryId(Integer partNumberIndustryId) {
		this.partNumberIndustryId = partNumberIndustryId;
	}

}
