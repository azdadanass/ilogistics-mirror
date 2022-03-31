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

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.Issue;
import ma.azdad.model.IssueComment;
import ma.azdad.model.IssueFile;
import ma.azdad.model.IssueHistory;
import ma.azdad.model.IssueStatus;
import ma.azdad.repos.IssueRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.EmailService;
import ma.azdad.service.IssueService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class IssueView extends GenericView<Integer, Issue, IssueRepos, IssueService> {

	@Autowired
	private FileUploadView fileUploadView;

	@Autowired
	private SessionView sessionView;

	@Autowired
	private CacheView cacheView;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private DeliveryRequestService deliveryRequestService;
	
	@Autowired
	private DeliveryRequestView deliveryRequestView;

	private Integer deliveryRequestId;
	private Integer projectId;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
		deliveryRequestId = UtilsFunctions.getIntegerParameter("deliveryRequestId");
	}

	@Override
	protected void addPage() {
		model = new Issue(deliveryRequestService.findOne(deliveryRequestId));
	}

	@Override
	public void refreshList() {
		if (isListPage)
			if (pageIndex == null)
				initLists(service.findByProject(projectId));
			else
				switch (pageIndex) {
				case 1:
					initLists(service.findToConfirm(sessionView.getUsername()));
					break;
				case 2:
					initLists(service.findToAssign(sessionView.getUsername()));
					break;
				case 3:
					initLists(service.findToResolve(sessionView.getUsername()));
					break;
				case 4:
					initLists(service.findToAcknowledge(sessionView.getUsername()));
					break;

				default:
					break;
				}
	}

	// save
	public Boolean canSave() {
		if("/viewDeliveryRequest.xhtml".equals(currentPath))
			return sessionView.isTheConnectedUser(deliveryRequestView.getDeliveryRequest().getRequester(), deliveryRequestView.getDeliveryRequest().getProject().getManager()) //
					|| (sessionView.getIsExternalPM() && cacheView.getAssignedProjectList().contains(deliveryRequestView.getDeliveryRequest().getProject().getId()));
		else if (isAddPage)
			return sessionView.isTheConnectedUser(model.getDeliveryRequest().getRequester(), model.getDeliveryRequest().getProject().getManager()) //
					|| (sessionView.getIsExternalPM() && cacheView.getAssignedProjectList().contains(model.getDeliveryRequest().getProject().getId()));
		else if (isViewPage || isEditPage)
			return IssueStatus.RAISED.equals(model.getStatus()) && model.getUser1() != null && sessionView.isTheConnectedUser(model.getUser1());
		return false;
	}
	
	

	public String save() {
		if (!canSave())
			return null;
		if (!validate())
			return null;

		model.setDate1(new Date());
		model.setUser1(sessionView.getUser());

		model.addHistory(new IssueHistory(getIsAddPage() ? "Created" : "Edited", sessionView.getUser(), getIsAddPage() ? null : UtilsFunctions.getChanges(model, old)));

		model = service.save(model);
		return addParameters("viewDeliveryRequest.xhtml", "faces-redirect=true", "id=" + model.getDeliveryRequestId());
	}

	public Boolean validate() {
		return true;
	}

	// confirm
	public Boolean canConfirm() {
		return IssueStatus.RAISED.equals(model.getStatus()) //
				&& (sessionView.isTheConnectedUser(model.getDeliveryRequest().getRequester(), model.getDeliveryRequest().getProject().getManager()) //
						|| projectService.isQualityManager(sessionView.getUsername(), model.getDeliveryRequest().getProjectId()));
	}

	public void confirm() {
		if (!canConfirm())
			return;
		model.setStatus(IssueStatus.CONFIRMED);
		model.setDate2(new Date());
		model.setUser2(sessionView.getUser());
		model.addHistory(new IssueHistory(IssueStatus.CONFIRMED.getValue(), sessionView.getUser(), model.getTmpComment()));
		model = service.saveAndRefresh(model);
		deliveryRequestService.updateCountIssues(model.getDeliveryRequest().getId());
	}

	public void reject() {
		if (!canConfirm())
			return;
		model.setStatus(IssueStatus.REJECTED);
		model.setDate7(new Date());
		model.setUser7(sessionView.getUser());
		model.addHistory(new IssueHistory(IssueStatus.REJECTED.getValue(), sessionView.getUser(), model.getTmpComment()));
		model = service.saveAndRefresh(model);
		deliveryRequestService.updateCountIssues(model.getDeliveryRequest().getId());
	}

	// assign
	public Boolean canAssign() {
		return IssueStatus.CONFIRMED.equals(model.getStatus()) //
				&& (sessionView.isTheConnectedUser(model.getDeliveryRequest().getRequester(), model.getDeliveryRequest().getProject().getManager()) //
						|| projectService.isQualityManager(sessionView.getUsername(), model.getDeliveryRequest().getProjectId()));
	}

	public void assign() {
		if (!canAssign())
			return;
		model.setStatus(IssueStatus.ASSIGNED);
		model.setDate3(new Date());
		model.setUser3(sessionView.getUser());
		model.setOwnershipUser(userService.findOneLight(model.getOwnershipUserUsername()));
		model.addHistory(new IssueHistory(IssueStatus.ASSIGNED.getValue(), sessionView.getUser(), "Assigned to : " + model.getOwnershipUserFullName()));
		switch (model.getOwnershipType()) {
		case COMPANY:
			model.setCompany(companyService.findOne(model.getCompanyId()));
			break;
		case CUSTOMER:
			model.setCustomer(customerService.findOne(model.getCustomerId()));
			break;
		case SUPPLIER:
			model.setSupplier(supplierService.findOne(model.getSupplierId()));
			break;
		default:
			break;
		}
		model = service.saveAndRefresh(model);
		deliveryRequestService.updateCountIssues(model.getDeliveryRequest().getId());

		emailService.sendIssueNotification(model, model.getOwnershipUser());

	}

	// unassign
	public Boolean canUnassign() {
		return IssueStatus.ASSIGNED.equals(model.getStatus()) //
				&& (sessionView.isTheConnectedUser(model.getDeliveryRequest().getRequester(), model.getDeliveryRequest().getProject().getManager()) //
						|| projectService.isQualityManager(sessionView.getUsername(), model.getDeliveryRequest().getProjectId()));
	}

	public void unassign() {
		if (!canUnassign())
			return;
		model.setStatus(IssueStatus.CONFIRMED);
		model.setDate3(null);
		model.setUser3(null);
		model.setOwnershipUser(null);
		model.addHistory(new IssueHistory("Unassigned", sessionView.getUser(), model.getTmpComment()));
		model.setOwnershipType(null);
		model.setCompany(null);
		model.setCustomer(null);
		model.setSupplier(null);
		model = service.saveAndRefresh(model);
		deliveryRequestService.updateCountIssues(model.getDeliveryRequest().getId());
	}

	// resolve
	public Boolean canResolve() {
		return IssueStatus.ASSIGNED.equals(model.getStatus()) && sessionView.isTheConnectedUser(model.getOwnershipUser());
	}

	public void resolve() {
		if (!canResolve())
			return;
		model.setStatus(IssueStatus.RESOLVED);
		model.setDate4(new Date());
		model.setUser4(sessionView.getUser());
		model.addHistory(new IssueHistory(IssueStatus.RESOLVED.getValue(), sessionView.getUser(), model.getTmpComment()));
		model = service.saveAndRefresh(model);
		deliveryRequestService.updateCountIssues(model.getDeliveryRequest().getId());
	}

	public void notResolve() {
		if (!canResolve())
			return;
		model.setStatus(IssueStatus.NOT_RESOLVED);
		model.setDate6(new Date());
		model.setUser6(sessionView.getUser());
		model.addHistory(new IssueHistory(IssueStatus.NOT_RESOLVED.getValue(), sessionView.getUser(), model.getTmpComment()));
		model = service.saveAndRefresh(model);
		deliveryRequestService.updateCountIssues(model.getDeliveryRequest().getId());
	}

	// acknowledge
	public Boolean canAcknowledge() {
		return IssueStatus.RESOLVED.equals(model.getStatus()) && sessionView.isTheConnectedUser(model.getUser3());
	}

	public void acknowledge() {
		if (!canAcknowledge())
			return;
		model.setStatus(IssueStatus.ACKNOWLEDGED);
		model.setDate5(new Date());
		model.setUser5(sessionView.getUser());
		model.addHistory(new IssueHistory(IssueStatus.ACKNOWLEDGED.getValue(), sessionView.getUser(), model.getTmpComment()));
		model = service.saveAndRefresh(model);
		deliveryRequestService.updateCountIssues(model.getDeliveryRequest().getId());
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
	private IssueFile file;
	private String fileType;

	public Boolean canAddFile() {
		return true;
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		IssueFile modelFile = new IssueFile(file, fileType, event.getFile().getFileName(), sessionView.getUser());
		model.addFile(modelFile);
		synchronized (IssueView.class) {
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
	private IssueComment comment = new IssueComment();

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

	public Boolean canDeleteComment(IssueComment comment) {
		return sessionView.isTheConnectedUser(comment.getUser());
	}

	public void deleteComment() {
		if (!canDeleteComment(comment))
			return;
		model.removeComment(comment);
		model = service.saveAndRefresh(model);
	}

	// generic
	public Long countToConfirm() {
		return service.countToConfirm(sessionView.getUsername());
	}

	public Long countToAssign() {
		return service.countToAssign(sessionView.getUsername());
	}

	public Long countToResolve() {
		return service.countToResolve(sessionView.getUsername());
	}

	public Long countToAcknowledge() {
		return service.countToAcknowledge(sessionView.getUsername());
	}

	// getters & setters
	public Issue getModel() {
		return model;
	}

	public void setModel(Issue model) {
		this.model = model;
	}

	public IssueFile getFile() {
		return file;
	}

	public void setFile(IssueFile file) {
		this.file = file;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public IssueComment getComment() {
		return comment;
	}

	public void setComment(IssueComment comment) {
		this.comment = comment;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

}
