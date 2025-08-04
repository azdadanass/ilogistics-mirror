package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

import ma.azdad.model.UserVehicle;
import ma.azdad.repos.UserVehicleRepos;
import ma.azdad.service.UserVehicleService;




@ManagedBean
@Component
@Scope("view")
public class UserVehicleView extends GenericView<Integer, UserVehicle, UserVehicleRepos, UserVehicleService> {
	
	@Autowired
	private SessionView sessionView;
	
	


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
		if (isListPage)
			datatableList = new DatatableList<UserVehicle>(service.findAll());
	}

	// save
	public Boolean canSave() {
		if (getIsAddPage() || getIsListPage())
			return true;
		if (getIsEditPage() || getIsViewPage())
			return true;
		return false;
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
	
	// toggle status
	public Boolean canToggle() {
		return true;
	}

	public void toggle() {
		if (!canToggle())
			return;
		model.setActive(!model.getActive());
		model = service.saveAndRefresh(model);
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
	
	

	
			
	

	

	// getters & setters
	public UserVehicle getModel() {
		return model;
	}

	public void setModel(UserVehicle model) {
		this.model = model;
	}

	

	

}

