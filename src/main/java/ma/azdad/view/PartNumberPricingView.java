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

import ma.azdad.model.PartNumberPricing;
import ma.azdad.model.PartNumberPricingComment;
import ma.azdad.model.PartNumberPricingFile;
import ma.azdad.model.PartNumberPricingHistory;
import ma.azdad.repos.PartNumberPricingRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CurrencyService;
import ma.azdad.service.PartNumberPricingService;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class PartNumberPricingView extends GenericView<Integer, PartNumberPricing, PartNumberPricingRepos, PartNumberPricingService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private PartNumberService partNumberService;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private FileUploadView fileUploadView;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			initLists(service.findLight());
	}

	// save
	public Boolean canSave() {
		return sessionView.isSE();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model.setPartNumber(partNumberService.findOne(model.getPartNumberId()));
		model.setCurrency(currencyService.findOne(model.getCurrencyId()));
		model.setCompany(companyService.findOne(model.getCompanyId()));

		model.addHistory(new PartNumberPricingHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(), getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (service.countByPartNumberAndDate(model.getPartNumberId(), model.getDate(), model.getId()) > 0)
			return FacesContextMessages.ErrorMessages("PN / Date should be unique");
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

	// files
	private PartNumberPricingFile file;
	private String fileType;

	public Boolean canAddFile() {
		return true;
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		PartNumberPricingFile modelFile = new PartNumberPricingFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (PartNumberPricingView.class) {
			model.calculateCountFiles();
			model = service.saveAndRefresh(model);
		}
	}

	public Boolean canDeleteFile() {
		return canAddFile();
	}

	public void deleteFile() {
		if (!canDeleteFile())
			return;
		model.removeFile(file);
		model = service.saveAndRefresh(model);
	}

	// comments
	private PartNumberPricingComment comment = new PartNumberPricingComment();

	public Boolean canAddComment() {
		return true;
	}

	public void addComment() {
		if (!canAddComment())
			return;
		comment.setDate(new Date());
		comment.setUser(sessionView.getUser());
		model.addComment(comment);
		model = service.saveAndRefresh(model);
	}

	public Boolean canDeleteComment(PartNumberPricingComment comment) {
		return sessionView.isTheConnectedUser(comment.getUser());
	}

	public void deleteComment() {
		if (!canDeleteComment(comment))
			return;
		model.removeComment(comment);
		model = service.saveAndRefresh(model);
	}

	// generic
	public String updateQuantities() {
		service.updateQuantities();
		return addParameters(currentPath, "faces-redirect=true");
	}

	// getters & setters
	public PartNumberPricing getModel() {
		return model;
	}

	public void setModel(PartNumberPricing model) {
		this.model = model;
	}

	public PartNumberPricingFile getFile() {
		return file;
	}

	public void setFile(PartNumberPricingFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public PartNumberPricingComment getComment() {
		return comment;
	}

	public void setComment(PartNumberPricingComment comment) {
		this.comment = comment;
	}

}
