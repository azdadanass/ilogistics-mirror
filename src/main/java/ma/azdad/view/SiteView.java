package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

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

import ma.azdad.model.Site;
import ma.azdad.model.SiteCategory;
import ma.azdad.model.SiteFile;
import ma.azdad.repos.SiteRepos;
import ma.azdad.service.CustomerService;
import ma.azdad.service.SiteFileService;
import ma.azdad.service.SiteHistoryService;
import ma.azdad.service.SiteService;
import ma.azdad.service.SiteTypeService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.SiteExcelFileException;

@ManagedBean
@Component
@Scope("view")
public class SiteView extends GenericView<Integer, Site, SiteRepos, SiteService> {

	@Autowired
	protected SiteService siteService;

	@Autowired
	protected CustomerService customerService;

	@Autowired
	protected SupplierService supplierService;

	@Autowired
	protected SiteFileService siteFileService;

	@Autowired
	protected SiteHistoryService siteHistoryService;

	@Autowired
	protected SiteTypeService siteTypeService;

	@Autowired
	protected FileView fileView;

	@Autowired
	protected DeliveryRequestView deliveryRequestView;

	private Site site = new Site();
	private SiteFile siteFile;

	private MapModel mapModel;

	private Integer step = 1;

	private Integer typeId = null;

	private Boolean filterByUser = true;

	private SiteCategory siteCategory;

	private Boolean ignore = false;

	private Boolean fromExcel = false;
	private String editSiteCoordinatesPage = "editSiteCoordinates.xhtml";

	@Override
	@PostConstruct
	public void init() {
		super.init();
		initParameters();

		Boolean isEditSiteCoordinatesPage = ("/" + editSiteCoordinatesPage).equals(currentPath);

		refreshList();
		if (isAddPage) {
			if (typeId != null) {
				site.setType(siteTypeService.findOne(typeId));
				siteCategory = site.getType().getCategory();
			}

		} else if (isEditPage) {
			site = siteService.findOne(id);
			site.init();
			siteCategory = site.getType().getCategory();
		} else if (isViewPage || isEditSiteCoordinatesPage)
			site = siteService.findOne(id);

		if (isAddPage || isEditPage || isViewPage || isEditSiteCoordinatesPage)
			refreshMapModel();
	}

	@Override
	public void initParameters() {
		super.initParameters();
		try {
			typeId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("typeId"));
		} catch (Exception e) {
			typeId = null;
		}

		try {
			fromExcel = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("fromExcel").equals("true");
		} catch (Exception e) {
			fromExcel = false;
		}

	}

	@Override
	public void refreshList() {
		if ("/addEditDeliveryRequest.xhtml".equals(currentPath)) {
			if (deliveryRequestView.getDeliveryRequest().getIsForTransfer() == null || !deliveryRequestView.getDeliveryRequest().getIsForTransfer())
				list2 = list1 = siteService.findLight();
			else
				list2 = list1 = siteService.findLightAndHavingWarehouse();
		}

		else if (isListPage)
			if (typeId != null)
				list2 = list1 = filterByUser ? siteService.findLight(typeId, sessionView.getUsername()) : siteService.findLight(typeId);
			else
				list2 = list1 = filterByUser ? siteService.findLight(sessionView.getUsername()) : siteService.findLight();

	}

	public void refreshSite() {
		siteService.flush();
		site = siteService.findOne(site.getId());
	}

	// GPS
	public void refreshMapModel() {
		mapModel = new DefaultMapModel();

		for (Site site : siteService.findAllCoordinates()) {
			Marker marker = new Marker(new LatLng(site.getLatitude(), site.getLongitude()), site.getName());
			marker.setIcon("https://maps.google.com/mapfiles/ms/micons/blue-dot.png");
			mapModel.addOverlay(marker);
		}
		mapModel.addOverlay(new Marker(new LatLng(site.getLatitude(), site.getLongitude()), "Marker"));

	}

	public void onPointSelect(PointSelectEvent event) {
		LatLng latlng = event.getLatLng();
		site.setLatitude(latlng.getLat());
		site.setLongitude(latlng.getLng());
		refreshMapModel();
	}

	// SAVE STEPS
	public void initSaveSite() {
		step = 1;
		site = new Site();
	}

	public String saveSiteNextStep() {
		System.out.println("saveSiteNextStep step : " + step);

		if (!canSaveSite())
			return addParameters(listPage, "faces-redirect=true");
		switch (step) {
		case 1:
			System.err.println("step 1");
			step++;
			break;
		case 2:
			System.err.println("step 2");
			System.out.println("fromExcel !!!!!!!!!!!!!!! : " + fromExcel);
			if (!fromExcel) {
				if (!validateStep2())
					return null;
				step++;
				break;
			}
			return addParameters(listPage, "faces-redirect=true", "typeId=" + site.getType().getId());
		case 3:
			System.err.println("step 3");
			// TODO validateStep3 (calculate distance category, type and site
			// Owner)
			List<String> nearbySites = siteService.getNearbySites(site);
			if (!ignore && !nearbySites.isEmpty()) {
				FacesContextMessages.InfoMessages("there are already sites in database with same category,type,owner and almost the same coordinates : ");
				FacesContextMessages.InfoMessages(nearbySites.toString());
				FacesContextMessages.InfoMessages("are you sure is the site not already added  ?");
				ignore = true;
				return null;
			}

			if (isAddPage || isEditPage)
				return saveSite();
			else {
				saveSite();
				RequestContext.getCurrentInstance().execute("PF('addSiteDlg').hide();");
			}
		case 4:

		default:
			break;
		}
		return null;
	}

	private Boolean validateStep2() {
		site.setName(UtilsFunctions.cleanString(site.getName()));
		if (siteService.isNameExists(site)) {
			FacesContextMessages.ErrorMessages("Name already exists in database : " + site.getName());
			return false;
		}
		return true;
	}

	// EXTERNAL ADD
	public void initAddExternalAdd() {
		step = 1;
		site = new Site();
	}

	public void nextStep() {
		if (step == 3)
			saveSite();
		else if (step == 4)
			RequestContext.getCurrentInstance().execute("PF('addSiteDlg').hide();");
		step++;
	}

	// SAVE SITE

	public Boolean canSaveSite() {
		if (isListPage || isAddPage || "/addEditDeliveryRequest.xhtml".equals(currentPath))
			return true;
		else if (isViewPage || isEditPage)
			return false;
		return false;
	}

	public String saveSite() {
		if (!canSaveSite())
			return listPage;
		if (!validateSite())
			return null;

		preSave();
		site = siteService.save(site);
		if (!isEditPage)
			siteHistoryService.created(site, sessionView.getUser());
		else
			siteHistoryService.edited(site, sessionView.getUser());
		return addParameters(viewPage, "faces-redirect=true", "id=" + site.getId());
	}

	private void preSave() {
		String owner = site.getOwner();
		if (isEditPage) {
			site.setCustomer(null);
			site.setSupplier(null);
			site.setOwner(null);
		}
		switch (site.getOwnerType()) {
		case 0:
			site.setCustomer(customerService.findOne(site.getCustomerId()));
			break;
		case 1:
			site.setSupplier(supplierService.findOne(site.getSupplierId()));
			break;
		case 2:
			site.setOwner(owner);
		}
		site.setUser(sessionView.getUser());
	}

	// INPLACE
	public Boolean canEditInplace() {
		return sessionView.isTheConnectedUser(site.getUser());
	}

	public void editInplace() {
		if (!canEditInplace())
			return;
		siteService.save(site);
	}

	public Boolean canEditSiteName() {
		return sessionView.isTheConnectedUser(site.getUser());
	}

	public void editSiteName() {
		if (!canEditSiteName())
			return;
		siteService.updateName(site.getId(), site.getName());
	}

	public Boolean canEditSiteType() {
		return sessionView.isTheConnectedUser(site.getUser());
	}

	public void editSiteType() {
		if (!canEditSiteType())
			return;
		siteService.updateType(site.getId(), site.getType());
	}

	// EDIT SITE COORDINATES
	public Boolean canEditSiteCoordinates() {
		return sessionView.isTheConnectedUser(site.getUser());
	}

	public String editSiteCoordinates() {
		if (!canEditSiteCoordinates())
			return null;
		if (!validatEditSiteCoordinates())
			return null;
		siteService.editSiteCoordinates(site.getId(), site.getLatitude(), site.getLongitude());
		siteHistoryService.edited(site, sessionView.getUser());
		return addParameters(viewPage, "faces-redirect=true", "id=" + site.getId());
	}

	public Boolean validatEditSiteCoordinates() {
		if (site.getLongitude() > 0.0 || site.getLongitude() < -17.0)
			return FacesContextMessages.ErrorMessages("Invalid Longitude : " + site.getName());

		if (site.getLatitude() > 40.0 || site.getLatitude() < 20.0)
			return FacesContextMessages.ErrorMessages("Invalid Latitude : " + site.getName());
		return true;
	}

	// Upload from Excel File
	public void uploadExcelFile(FileUploadEvent event) throws IOException {
		preSave();
		try {
			List<Site> list = siteService.readFile(event.getFile().getInputstream(), site);
			Set<String> nameList = new HashSet<>();
			for (Site site : list) {
				if (nameList.contains(site.getName()))
					continue;
				if (siteService.isNameExists(site)) {
					FacesContextMessages.ErrorMessages("Name already exists in database : " + site.getName());
					continue;
				}
				if (site.getLongitude() > 0.0 || site.getLongitude() < -17.0) {
					FacesContextMessages.ErrorMessages("Invalid Longitude : " + site.getName());
					continue;
				}

				if (site.getLatitude() > 40.0 || site.getLatitude() < 20.0) {
					FacesContextMessages.ErrorMessages("Invalid Latitude : " + site.getName());
					continue;
				}
				site = siteService.save(site);
				FacesContextMessages.InfoMessages("Site created with success : " + site.getName());
				siteHistoryService.created(site, sessionView.getUser());
				nameList.add(site.getName());
			}
			System.out.println(list);
		} catch (SiteExcelFileException e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
			return;
		}

	}

	// DELETE SITE
	public Boolean canDeleteSite() {
		return true;
	}

	public String deleteSite() {
		if (canDeleteSite())
			try {
				siteService.delete(site);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return listPage;
	}

	public Boolean validateSite() {
		site.setName(UtilsFunctions.cleanString(site.getName()));

		if (siteService.isNameExists(site)) {
			FacesContextMessages.ErrorMessages("Name already exists in database : " + site.getName());
			return false;
		}
		if (site.getLongitude() > 0.0 || site.getLongitude() < -17.0) {
			FacesContextMessages.ErrorMessages("Invalid Longitude");
			return false;
		}

		if (site.getLatitude() > 40.0 || site.getLatitude() < 20.0) {
			FacesContextMessages.ErrorMessages("Invalid Latitude");
			return false;
		}

		return true;
	}

	// FILES MANAGEMENT
	private String siteFileType;
	private Integer siteFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileView.handleFileUpload(event, getClassName2());
		SiteFile siteFile = new SiteFile(getClassName2(), file, siteFileType, event.getFile().getFileName(), sessionView.getUser(), site);
		siteFileService.save(siteFile);
		synchronized (SiteView.class) {
			refreshSite();
		}
	}

	public void deleteSiteFile() {
		try {
			siteFileService.delete(siteFileId);
			refreshSite();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}
	}

	public void setSite(Integer siteId) {
		site = siteService.findOne(siteId);
	}

	// GENERIC
	public List<Site> findLight() {
		return siteService.findLight();
	}

	// GETTERS & SETTERS
	@Override
	public SessionView getSessionView() {
		return sessionView;
	}

	@Override
	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public FileView getFileView() {
		return fileView;
	}

	public void setFileView(FileView fileView) {
		this.fileView = fileView;
	}

	public SiteFileService getSiteFileService() {
		return siteFileService;
	}

	public void setSiteFileService(SiteFileService siteFileService) {
		this.siteFileService = siteFileService;
	}

	public String getSiteFileType() {
		return siteFileType;
	}

	public void setSiteFileType(String siteFileType) {
		this.siteFileType = siteFileType;
	}

	public Integer getSiteFileId() {
		return siteFileId;
	}

	public void setSiteFileId(Integer siteFileId) {
		this.siteFileId = siteFileId;
	}

	public SiteFile getSiteFile() {
		return siteFile;
	}

	public void setSiteFile(SiteFile siteFile) {
		this.siteFile = siteFile;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public SiteHistoryService getSiteHistoryService() {
		return siteHistoryService;
	}

	public void setSiteHistoryService(SiteHistoryService siteHistoryService) {
		this.siteHistoryService = siteHistoryService;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}

	public Boolean getFilterByUser() {
		return filterByUser;
	}

	public void setFilterByUser(Boolean filterByUser) {
		this.filterByUser = filterByUser;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public SiteCategory getSiteCategory() {
		return siteCategory;
	}

	public void setSiteCategory(SiteCategory siteCategory) {
		this.siteCategory = siteCategory;
	}

	public Boolean getFromExcel() {
		return fromExcel;
	}

	public void setFromExcel(Boolean fromExcel) {
		this.fromExcel = fromExcel;
	}

}
