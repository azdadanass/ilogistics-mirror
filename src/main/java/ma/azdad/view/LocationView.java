package ma.azdad.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.Location;
import ma.azdad.model.LocationDetail;
import ma.azdad.model.StockRowDetail;
import ma.azdad.model.ZoneCategory;
import ma.azdad.model.ZoneIndustry;
import ma.azdad.model.ZoneType;
import ma.azdad.repos.LocationRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.LocationService;
import ma.azdad.service.PartNumberCategoryService;
import ma.azdad.service.PartNumberIndustryService;
import ma.azdad.service.PartNumberTypeService;
import ma.azdad.service.StockRowDetailService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.service.WarehouseService;
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

	@Autowired
	protected PartNumberCategoryService partNumberCategoryService;

	@Autowired
	protected PartNumberTypeService partNumberTypeService;

	@Autowired
	protected WarehouseService warehouseService;

	@Autowired
	protected StockRowDetailService stockRowDetailService;

	private Location location = new Location();
	private Integer warehouseId;

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
	protected void initParameters() {
		super.initParameters();
		warehouseId = UtilsFunctions.getIntegerParameter("warehouseId");
	}

	@Override
	protected void addPage() {
		super.addPage();
		model.setWarehouse(warehouseService.findOneLight(warehouseId));
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
	public Boolean canSave() {
		return sessionView.getIsAdmin();
	}

	public Boolean validate() {
		if (service.isNameExists(model))
			return FacesContextMessages.ErrorMessages("Name already exits !");

		return true;
	}

	public String save() {
		if (!canSave())
			return null;
		if (!validate())
			return null;
		service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	// details
	private Boolean editDetailList = false;

	public Boolean canAddDetail() {
		return editDetailList && canSave();
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
		return editDetailList && canSave();
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

	// options
	private Boolean editOptionList = false;

	public void saveOptionList() {
		location.calculateOptions();
		service.save(location);
		location = service.findOne(location.getId());
		editOptionList = false;
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
		if (model.getIndustryList().stream().filter(i -> i.getIndustryId().equals(partNumberIndustryId)).count() > 0)
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

	private ZoneIndustry industry;
	private Integer partNumberCategoryId;

	public Boolean canAddCategory() {
		return sessionView.getIsAdmin();
	}

	public Boolean validateAddCategory() {
		if (industry.getCategoryList().stream().filter(i -> i.getCategoryId().equals(partNumberCategoryId)).count() > 0)
			return FacesContextMessages.ErrorMessages("Already exists");

		return true;
	}

	public void addCategory() {
		if (!canAddCategory())
			return;
		if (!validateAddCategory())
			return;
		ZoneCategory category = new ZoneCategory();
		category.setCategory(partNumberCategoryService.findOneLight(partNumberCategoryId));
		industry.addCategory(category);
		service.save(model);
		refreshModel();

		industry = model.getIndustryList().stream().filter(i -> i.getId().equals(industry.getId())).findFirst().get();
	}

	public Boolean canDeleteCategory() {
		return canAddCategory();
	}

	public void deleteCategory(ZoneCategory zoneCategory) {
		industry.removeCategory(zoneCategory);
		service.save(model);
		refreshModel();

		industry = model.getIndustryList().stream().filter(i -> i.getId().equals(industry.getId())).findFirst().get();
	}

	private ZoneCategory category;
	private Integer partNumberTypeId;

	public Boolean canAddType() {
		return sessionView.getIsAdmin();
	}

	public Boolean validateAddType() {
		if (category.getTypeList().stream().filter(i -> i.getTypeId().equals(partNumberTypeId)).count() > 0)
			return FacesContextMessages.ErrorMessages("Already exists");

		return true;
	}

	public void addType() {
		if (!canAddType())
			return;
		if (!validateAddType())
			return;
		ZoneType type = new ZoneType();
		type.setType(partNumberTypeService.findOneLight(partNumberTypeId));
		category.addType(type);
		service.save(model);
		refreshModel();

		industry = model.getIndustryList().stream().filter(i -> i.getId().equals(industry.getId())).findFirst().get();
		category = industry.getCategoryList().stream().filter(i -> i.getId().equals(category.getId())).findFirst().get();
	}

	public Boolean canDeleteType() {
		return canAddType();
	}

	public void deleteType(ZoneType zoneType) {
		category.removeType(zoneType);
		service.save(model);
		refreshModel();

		industry = model.getIndustryList().stream().filter(i -> i.getId().equals(industry.getId())).findFirst().get();
		category = industry.getCategoryList().stream().filter(i -> i.getId().equals(category.getId())).findFirst().get();
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

	private DatatableList<StockRowDetail> stockRowDetailDatatable;

	public void initStockRowDetailDatatable() {
		System.out.println("initStockRowDetailDatatable : ");
		Integer zoneHeightId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
		System.out.println("zoneHeightId : " + zoneHeightId);
		stockRowDetailDatatable = new DatatableList<StockRowDetail>(stockRowDetailService.findRemainingByZoneHight(zoneHeightId));
		System.out.println(stockRowDetailDatatable.getValue());
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

	public ZoneIndustry getIndustry() {
		return industry;
	}

	public void setIndustry(ZoneIndustry industry) {
		this.industry = industry;
	}

	public PartNumberIndustryService getPartNumberIndustryService() {
		return partNumberIndustryService;
	}

	public void setPartNumberIndustryService(PartNumberIndustryService partNumberIndustryService) {
		this.partNumberIndustryService = partNumberIndustryService;
	}

	public PartNumberCategoryService getPartNumberCategoryService() {
		return partNumberCategoryService;
	}

	public void setPartNumberCategoryService(PartNumberCategoryService partNumberCategoryService) {
		this.partNumberCategoryService = partNumberCategoryService;
	}

	public PartNumberTypeService getPartNumberTypeService() {
		return partNumberTypeService;
	}

	public void setPartNumberTypeService(PartNumberTypeService partNumberTypeService) {
		this.partNumberTypeService = partNumberTypeService;
	}

	public Integer getPartNumberCategoryId() {
		return partNumberCategoryId;
	}

	public void setPartNumberCategoryId(Integer partNumberCategoryId) {
		this.partNumberCategoryId = partNumberCategoryId;
	}

	public ZoneCategory getCategory() {
		return category;
	}

	public void setCategory(ZoneCategory category) {
		this.category = category;
	}

	public Integer getPartNumberTypeId() {
		return partNumberTypeId;
	}

	public void setPartNumberTypeId(Integer partNumberTypeId) {
		this.partNumberTypeId = partNumberTypeId;
	}

	public DatatableList<StockRowDetail> getStockRowDetailDatatable() {
		return stockRowDetailDatatable;
	}

	public void setStockRowDetailDatatable(DatatableList<StockRowDetail> stockRowDetailDatatable) {
		this.stockRowDetailDatatable = stockRowDetailDatatable;
	}

	public Boolean getEditOptionList() {
		return editOptionList;
	}

	public void setEditOptionList(Boolean editOptionList) {
		this.editOptionList = editOptionList;
	}

}
