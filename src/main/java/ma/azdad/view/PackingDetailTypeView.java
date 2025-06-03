package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.PackingDetailType;
import ma.azdad.model.PartNumberClass;
import ma.azdad.repos.PackingDetailTypeRepos;
import ma.azdad.service.PackingDetailTypeService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class PackingDetailTypeView extends GenericView<Integer, PackingDetailType, PackingDetailTypeRepos, PackingDetailTypeService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private FileUploadView fileUploadView;

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
		if (isPage("partNumberConfiguration"))
			datatableList = new DatatableList<PackingDetailType>(service.find());
	}

	// save
	public void initAdd() {
		model = new PackingDetailType();
	}

	public Boolean canSave() {
		return sessionView.getIsAdmin();
	}

	public void save() {
		if (!canSave())
			return;
		if (!validate())
			return;
		model = service.save(model);
		refreshList();
	}

	public Boolean validate() {
		if (service.countByNameAndClass(model.getName(),model.getPartNumberClass()) > 0)
			return FacesContextMessages.ErrorMessages("Name already exists");
		return true;
	}

	// toggle status
	public Boolean canToggle() {
		return sessionView.getIsAdmin();
	}

	public void toggle() {
		if (!canToggle())
			return;
		model.setActive(!model.getActive());
		model = service.save(model);
		refreshList();
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

	// images
	public Boolean canUploadImage() {
		return true;
	}

	public void handleImageUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		model.setImage(getClassName2() + "/" + file.getName());
		synchronized (PackingDetailTypeView.class) {
			model = service.saveAndRefresh(model);
		}
	}
	
	// generic
	public List<String> findNameListByClassAndActive(PartNumberClass partNumberClass){
		return service.findNameListByClassAndActive(partNumberClass);
	}

	// getters & setters
	public PackingDetailType getModel() {
		return model;
	}

	public void setModel(PackingDetailType model) {
		this.model = model;
	}

}
