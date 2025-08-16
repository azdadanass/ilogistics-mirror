package ma.azdad.view;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.VehicleBrand;
import ma.azdad.model.VehicleBrandType;
import ma.azdad.repos.VehicleBrandRepos;
import ma.azdad.service.VehicleBrandService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class VehicleBrandView extends GenericView<Integer, VehicleBrand, VehicleBrandRepos, VehicleBrandService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private FileUploadView fileUploadView;

	private VehicleBrandType brandType;

	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	public void refreshList() {
		if (isPage("vehicleConfig"))
			datatableList = new DatatableList<VehicleBrand>(service.findAll());
	}

	// save
	public Boolean canSave() {
		return sessionView.getIsTrAdmin();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		return true;
	}

	// delete
	public Boolean canDelete() {
		return true;
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			service.delete(model);
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	// photos
	public Boolean canUploadPhoto() {
		return true;
	}

	public void handlePhotoUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		model.setImage("files/" + getClassName2() + "/" + file.getName());
		synchronized (VehicleBrandView.class) {
			model = service.saveAndRefresh(model);
		}
	}

	// brand type list

	private Boolean editBrandTypeList = false;

	public Boolean canEditBrandTypeList() {
		return !editBrandTypeList && sessionView.getIsTrAdmin();
	}

	public Boolean canSaveBrandTypeList() {
		return editBrandTypeList && sessionView.getIsTrAdmin();
	}

	private Boolean validateBrandTypeList() {
		if (model.getBrandTypeList().stream().filter(i -> StringUtils.isBlank(i.getName())).count() > 0)
			return FacesContextMessages.ErrorMessages("Name should not be null");
		if (model.getBrandTypeList().stream().map(i -> i.getName()).distinct().count() < model.getBrandTypeList().size())
			return FacesContextMessages.ErrorMessages("Name should be unique");

		return true;
	}

	public void saveBrandTypeList() {
		if (!canSaveBrandTypeList())
			return;
		if (!validateBrandTypeList())
			return;
		service.save(model);
		refreshModel();
		editBrandTypeList = false;
	}

	public Boolean canAddBrandType() {
		return canSaveBrandTypeList();
	}

	public void addBrandType() {
		if (!canAddBrandType())
			return;
		model.addBrandType(new VehicleBrandType());
	}

	public Boolean canDeleteBrandType() {
		return canSaveBrandTypeList();
	}

	public void deleteBrandType(VehicleBrandType vehicleBrandType) {
		if (!canDeleteBrandType())
			return;
		model.removeBrandType(vehicleBrandType);
	}

	public Boolean canUploadBrandTypePhoto() {
		return canSaveBrandTypeList();
	}

	public void handleBrandTypePhotoUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, "vehicleBrandType");
		brandType.setImage("files/vehicleBrandType/" + file.getName());
		synchronized (VehicleBrandView.class) {
			model = service.saveAndRefresh(model);
		}
	}

	// getters & setters
	public VehicleBrand getModel() {
		return model;
	}

	public void setModel(VehicleBrand model) {
		this.model = model;
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public FileUploadView getFileUploadView() {
		return fileUploadView;
	}

	public void setFileUploadView(FileUploadView fileUploadView) {
		this.fileUploadView = fileUploadView;
	}

	public Boolean getEditBrandTypeList() {
		return editBrandTypeList;
	}

	public void setEditBrandTypeList(Boolean editBrandTypeList) {
		this.editBrandTypeList = editBrandTypeList;
	}

	public VehicleBrandType getBrandType() {
		return brandType;
	}

	public void setBrandType(VehicleBrandType brandType) {
		this.brandType = brandType;
	}

}
