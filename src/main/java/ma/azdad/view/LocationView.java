package ma.azdad.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.Location;
import ma.azdad.model.LocationDetail;
import ma.azdad.repos.LocationRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.LocationService;
import ma.azdad.service.SupplierService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class LocationView extends GenericView<Integer, Location, LocationRepos, LocationService> {

	@Autowired
	protected LocationService locationService;

	@Autowired
	protected CompanyService companyService;

	@Autowired
	protected CustomerService customerService;

	@Autowired
	protected SupplierService supplierService;

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

	// generic
	public List<Location> findByWarehouseAndStockRowStateAndOwner(DeliveryRequest deliveryRequest) {
		return service.findByWarehouseAndStockRowStateAndOwner(deliveryRequest);
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

}
