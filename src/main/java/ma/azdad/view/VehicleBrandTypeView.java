package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

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

import ma.azdad.model.VehicleBrandType;
import ma.azdad.repos.VehicleBrandTypeRepos;
import ma.azdad.service.VehicleBrandTypeService;

@ManagedBean
@Component
@Scope("view")
public class VehicleBrandTypeView extends GenericView<Integer, VehicleBrandType, VehicleBrandTypeRepos, VehicleBrandTypeService> {

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

	// generic
	public List<VehicleBrandType> findByBrand(Integer brandId) {
		return service.findByBrand(brandId);
	}

	// getters & setters
	public VehicleBrandType getModel() {
		return model;
	}

	public void setModel(VehicleBrandType model) {
		this.model = model;
	}

}
