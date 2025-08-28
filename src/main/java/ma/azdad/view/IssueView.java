package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.CompanyType;
import ma.azdad.model.Issue;
import ma.azdad.model.IssueComment;
import ma.azdad.model.IssueFile;
import ma.azdad.model.IssueStatus;
import ma.azdad.model.ProjectManagerType;
import ma.azdad.model.Role;
import ma.azdad.model.ToNotify;
import ma.azdad.model.User;
import ma.azdad.repos.IssueRepos;
import ma.azdad.service.CompanyService;
import ma.azdad.service.CustomerService;
import ma.azdad.service.DelegationService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.EmailService;
import ma.azdad.service.IssueFileService;
import ma.azdad.service.IssueService;
import ma.azdad.service.ProjectAssignmentService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.TransportationRequestService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class IssueView extends GenericView<Integer, Issue, IssueRepos, IssueService> {

	@Autowired
	private IssueService issueService;

	@Autowired
	private SessionView sessionView;

	@Autowired
	private IssueFileService issueFileService;

	@Autowired
	private DeliveryRequestService deliveryRequestService;

	@Autowired
	private TransportationRequestView transportationRequestView;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private DeliveryRequestView deliveryRequestView;

	@Autowired
	private CacheView cacheView;

	@Autowired
	private DelegationService delegationService;

	@Autowired
	private ProjectAssignmentService projectAssignmentService;

	@Autowired
	private TransportationRequestService transportationRequestService;

	private Issue issue = new Issue();
	private IssueFile issueFile;

	private Integer deliveryRequestId;
	private Integer transportationRequestId;
	private Integer projectId;

	private int step = 1;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isAddPage) {
			if (deliveryRequestId != null)
				issue = new Issue(deliveryRequestService.findOne(deliveryRequestId));
			else if (transportationRequestId != null)
				issue = new Issue(transportationRequestService.findOne(transportationRequestId));
			comment = new IssueComment("Creation Comment", sessionView.getUser());
			issue.addComment(comment);
		} else if (isEditPage) {
			issue = issueService.findOne(id);
			comment = new IssueComment("Edit Comment", sessionView.getUser());
			issue.addComment(comment);
		} else if (isViewPage)
			issue = issueService.findOne(id);
		else if (isPage("confirmIssue")) {
			issue = issueService.findOne(id);
			comment = new IssueComment("Confirmation Comment", sessionView.getUser());
			issue.addComment(comment);
		} else if (isPage("resolveIssue")) {
			issue = issueService.findOne(id);
			comment = new IssueComment("Resolution Comment", sessionView.getUser());
			issue.addComment(comment);
		}

	}

	@Override
	protected void initParameters() {
		super.initParameters();
		deliveryRequestId = UtilsFunctions.getIntegerParameter("deliveryRequestId");
		transportationRequestId = UtilsFunctions.getIntegerParameter("transportationRequestId");
	}

	@Override
	public void refreshList() {
		if (isListPage)
			if (pageIndex != null)
				switch (pageIndex) {
				case 1:
					initLists(issueService.findToConfirm(sessionView.getUsername()));
					break;
				case 2:
					initLists(issueService.findToAssign(sessionView.getUsername()));
					break;
				case 3:
					initLists(issueService.findToResolve(sessionView.getUsername()));
					break;
				case 4:
					initLists(issueService.findToAcknowledge(sessionView.getUsername()));
					break;
				case 5:
					initLists(service.findDeliveryRequestIssueListByUser(sessionView.getUsername(), cacheView.getAllProjectList(), cacheView.getDelegatedLobIdList()));
					break;
				default:
					break;
				}
	}

	public void flushIssue() {
		issueService.flush();
	}

	public void refreshIssue() {
		issue = issueService.findOne(issue.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewIssue())
			cacheView.accessDenied();
	}

	public Boolean canViewIssue() {
		return ((sessionView.getIsUser() || sessionView.isPM() || sessionView.getIsBuManager() || //
				sessionView.isTheConnectedUser(issue.getAssignator()) || sessionView.isTheConnectedUser(issue.getConfirmator()) || //
				sessionView.getIsExternalPm() && cacheView.getUserProjectList().contains(issue.getDeliveryRequest().getProject().getId()))) //
				|| (issue.getOwnershipUser() != null && sessionView.isTheConnectedUser(issue.getOwnershipUser()));

	}

	// save
	public Boolean canSave() {
		if (isPage("viewDeliveryRequest"))
			return sessionView.isTheConnectedUser(deliveryRequestView.getDeliveryRequest().getRequester(), deliveryRequestView.getDeliveryRequest().getProject().getManager()) //
					|| (sessionView.getIsExternalPM() && cacheView.getAssignedProjectList().contains(deliveryRequestView.getDeliveryRequest().getProject().getId()));
		else if (isPage("viewTransportationRequest"))
			return sessionView.isTheConnectedUser(transportationRequestView.getTransportationRequest().getDeliveryRequest().getRequester()) //
					|| sessionView.isTheConnectedUser(transportationRequestView.getTransportationRequest().getDriver());
		else if (isAddPage) {
			if (deliveryRequestId != null)
				return sessionView.isTheConnectedUser(issue.getDeliveryRequest().getRequester(), issue.getDeliveryRequest().getProject().getManager()) //
						|| (sessionView.getIsExternalPM() && cacheView.getAssignedProjectList().contains(issue.getDeliveryRequest().getProject().getId()));
			else if (transportationRequestId != null)
				return sessionView.isTheConnectedUser(issue.getTransportationRequest().getDeliveryRequest().getRequester()) //
						|| sessionView.isTheConnectedUser(issue.getTransportationRequest().getDriver());
		} else if (isViewPage || isEditPage)
			return IssueStatus.RAISED.equals(issue.getStatus()) && issue.getUser1() != null && sessionView.isTheConnectedUser(issue.getUser1());
		return false;
	}

	public String saveNextStep() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		switch (step) {
		case 1:
			System.out.println("step1");
			step++;
			break;
		case 2:
			System.out.println("step2");
			switch (issue.getAssignatorCompanyType()) {
			case COMPANY:
			case CUSTOMER:
				issue.setAssignatorSupplier(null);
				break;
			case SUPPLIER:
				issue.setAssignatorSupplier(supplierService.findOne(issue.getAssignatorSupplierId()));
				break;
			default:
				break;
			}
			switch (issue.getConfirmatorCompanyType()) {
			case COMPANY:
			case CUSTOMER:
				issue.setConfirmatorSupplier(null);
				break;
			case SUPPLIER:
				issue.setConfirmatorSupplier(supplierService.findOne(issue.getConfirmatorSupplierId()));
				break;
			default:
				break;
			}

			if (isAddPage)
				initToNotifiyList();

			step++;
			break;
		case 3:
			step++;
			break;
		case 4:
			return save();
		}
		return null;
	}

	private void initToNotifiyList() {
		if (deliveryRequestId != null) {
			addToNotify(issue.getDeliveryRequest().getRequester());
			addToNotify(issue.getDeliveryRequest().getProject().getManager());
			issue.getDeliveryRequest().getProject().getManagerList().stream()
					.filter(i -> Arrays.asList(ProjectManagerType.HARDWARE_MANAGER, ProjectManagerType.QUALITY_MANAGER).contains(i.getType()))
					.forEach(i -> addToNotify(i.getUser()));
			issue.getDeliveryRequest().getWarehouse().getManagerList().stream().forEach(i -> addToNotify(i.getUser()));
			delegationService.findDelegateUserListByProject(issue.getProjectId()).forEach(i -> addToNotify(i));
			projectAssignmentService.findCompanyUserListAssignedToProject(issue.getProjectId()).forEach(i -> addToNotify(i));

		}
	}

	public void savePreviousStep() {
		if (step > 1)
			step--;
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		issue.setDate1(new Date());
		issue.setUser1(sessionView.getUser());

		issue.addHistory(sessionView.getUser(), issue.getDescription());

		issue = issueService.save(issue);

		if (deliveryRequestId != null)
			deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());

		return addParameters(viewPage, "faces-redirect=true", "id=" + issue.getId());
	}

	public Boolean validate() {
		return true;
	}

	public List<User> findUserSelectionList(String type) {
		CompanyType companyType = null;
		Integer customerId = null;
		Integer supplierId = null;
		switch (type) {
		case "ownershipUser":
			companyType = issue.getOwnershipType();
			customerId = issue.getCustomerId();
			supplierId = issue.getSupplierId();
			break;
		case "confirmator":
			companyType = issue.getConfirmatorCompanyType();
			customerId = issue.getConfirmatorCustomerId();
			supplierId = issue.getConfirmatorSupplierId();
			break;
		case "assignator":
			companyType = issue.getAssignatorCompanyType();
			customerId = issue.getAssignatorCustomerId();
			supplierId = issue.getAssignatorSupplierId();
			break;
		}
		if (companyType == null)
			return new ArrayList<User>();
		Set<User> result = new HashSet<User>();
		switch (companyType) {
		case COMPANY:
			switch (issue.getParentType()) {
			case DN:
				result.add(issue.getDeliveryRequest().getRequester());
				result.add(issue.getDeliveryRequest().getProject().getManager());
				issue.getDeliveryRequest().getProject().getManagerList().stream()
						.filter(i -> Arrays.asList(ProjectManagerType.HARDWARE_MANAGER, ProjectManagerType.QUALITY_MANAGER).contains(i.getType()))
						.forEach(i -> result.add(i.getUser()));
				issue.getDeliveryRequest().getWarehouse().getManagerList().stream().forEach(i -> result.add(i.getUser()));
				delegationService.findDelegateUserListByProject(issue.getProjectId()).forEach(i -> result.add(i));
				projectAssignmentService.findCompanyUserListAssignedToProject(issue.getProjectId()).forEach(i -> result.add(i));
				break;
			case TR:
				result.add(issue.getTransportationRequest().getDeliveryRequest().getRequester());
				result.add(issue.getTransportationRequest().getDeliveryRequest().getProject().getManager());
				issue.getTransportationRequest().getDeliveryRequest().getProject().getManagerList().stream()
						.filter(i -> Arrays.asList(ProjectManagerType.HARDWARE_MANAGER, ProjectManagerType.QUALITY_MANAGER).contains(i.getType()))
						.forEach(i -> result.add(i.getUser()));
				if (issue.getTransportationRequest().getDeliveryRequest().getWarehouse() != null)
					issue.getTransportationRequest().getDeliveryRequest().getWarehouse().getManagerList().stream().forEach(i -> result.add(i.getUser()));
				delegationService.findDelegateUserListByProject(issue.getTransportationRequest().getDeliveryRequest().getProjectId()).forEach(i -> result.add(i));
				projectAssignmentService.findCompanyUserListAssignedToProject(issue.getTransportationRequest().getDeliveryRequest().getProjectId()).forEach(i -> result.add(i));
				break;
			default:
				break;
			}

			break;
		case CUSTOMER:
			userService.findLightByCustomerAndHasRole(customerId, Role.ROLE_ILOGISTICS_PM);
			break;
		case SUPPLIER:
			userService.findLightBySupplierAndHasRole(supplierId, Role.ROLE_ILOGISTICS_PM);
			break;
		default:
			break;
		}

		return new ArrayList<User>(result);

	}

//	public List<User> findOwnerShipUserSelectionList(String type) {
//		CompanyType companyType = null;
//		Integer customerId = null;
//		Integer supplierId = null;
//		switch (type) {
//		case "confirmator":
//			companyType = issue.getConfirmatorCompanyType();
//			customerId = issue.getConfirmatorCustomerId();
//			supplierId = issue.getConfirmatorSupplierId();
//			break;
//		case "assignator":
//			companyType = issue.getAssignatorCompanyType();
//			customerId = issue.getAssignatorCustomerId();
//			supplierId = issue.getAssignatorSupplierId();
//			break;
//		}
//		if (companyType == null)
//			return new ArrayList<User>();
//		Set<User> result = new HashSet<User>();
//		switch (companyType) {
//		case COMPANY:
//			result.add(issue.getDeliveryRequest().getRequester());
//			result.add(issue.getDeliveryRequest().getProject().getManager());
//			issue.getDeliveryRequest().getProject().getManagerList().stream().filter(i->Arrays.asList(ProjectManagerType.HARDWARE_MANAGER,ProjectManagerType.QUALITY_MANAGER).contains(i.getType())).forEach(i->result.add(i.getUser()));
//			issue.getDeliveryRequest().getWarehouse().getManagerList().stream().forEach(i -> result.add(i.getUser()));
//			delegationService.findDelegateUserListByProject(issue.getProjectId()).forEach(i -> result.add(i));
//			projectAssignmentService.findCompanyUserListAssignedToProject(issue.getProjectId()).forEach(i -> result.add(i));
//			break;
//		case CUSTOMER:
//			userService.findLightByCustomerAndHasRole(customerId, Role.ROLE_ILOGISTICS_PM);
//			break;
//		case SUPPLIER:
//			userService.findLightBySupplierAndHasRole(supplierId, Role.ROLE_ILOGISTICS_PM);
//			break;
//		default:
//			break;
//		}
//
//		return new ArrayList<User>(result);
//	}

	// inplace
	public void editAssignator() {
		if (!canEditAssignator())
			return;
		issue.setAssignator(userService.findOne(issue.getAssignatorUsername()));
		issue.addHistory("Edited", sessionView.getUser(), "Change assignator to <b class='blue'>" + issue.getAssignatorFullName() + "</b>");
		service.save(issue);
		issue = service.findOne(issue.getId());
	}

	public void editConfirmator() {
		if (!canEditConfirmator())
			return;
		issue.setConfirmator(userService.findOne(issue.getConfirmatorUsername()));
		issue.addHistory("Edited", sessionView.getUser(), "Change confirmator to <b class='blue'>" + issue.getConfirmatorFullName() + "</b>");
		service.save(issue);
		issue = service.findOne(issue.getId());
	}

	public Boolean canEditAssignator() {
		return sessionView.isTheConnectedUser(issue.getUser1()) && Arrays.asList(IssueStatus.RAISED, IssueStatus.SUBMITTED, IssueStatus.CONFIRMED).contains(issue.getStatus());
	}

	public Boolean canEditConfirmator() {
		return sessionView.isTheConnectedUser(issue.getUser1()) && Arrays.asList(IssueStatus.RAISED, IssueStatus.SUBMITTED).contains(issue.getStatus());
	}

	// to notify
	private String toNotifyUserUsername;

	public void addToNotifyItem() {
		System.out.println("addToNotifyItem");
		addToNotify(userService.findOne(toNotifyUserUsername));
		System.out.println(issue.getToNotifyList());
	}

	public void addToNotify(User user) {
		if (issue.getToNotifyList().stream().filter(i -> i.getInternalResource().getUsername().equals(user.getUsername())).count() == 0)
			issue.getToNotifyList().add(new ToNotify(user, issue));
	}

	public void removeToNotifyItem(int index) {
		issue.getToNotifyList().get(index).setDeliveryRequest(null);
		issue.getToNotifyList().remove(index);
	}

	// submit
	public Boolean canSubmit() {
		return IssueStatus.RAISED.equals(issue.getStatus()) && sessionView.isTheConnectedUser(issue.getUser1());
	}

	public void submit() {
		if (!canSubmit())
			return;
		issue.setStatus(IssueStatus.SUBMITTED);
		issue.setDate8(new Date());
		issue.setUser8(sessionView.getUser());
		issue.addHistory(sessionView.getUser(), issue.getTmpComment());
		issueService.save(issue);
		deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());
		refreshIssue();
	}

	// confirm
	public Boolean canConfirm() {
//		if (issue.getIsJrIssue())
//		return IssueStatus.SUBMITTED.equals(issue.getStatus()) //
//				&& (sessionView.isTheConnectedUser(issue.getDeliveryRequest().getRequester(), issue.getDeliveryRequest().getProject().getManager()) //
//						|| projectService.isQualityManager(sessionView.getUsername(), issue.getDeliveryRequest().getProjectId()));
//	else if (issue.getIsTicketIssue())
//		return IssueStatus.SUBMITTED.equals(issue.getStatus()) //
//				&& (sessionView.isTheConnectedUser(issue.getTicket().getRequester(), issue.getTicket().getProject().getManager()) //
//						|| projectService.isQualityManager(sessionView.getUsername(), issue.getTicket().getProjectId()));
//	return false;
		return IssueStatus.SUBMITTED.equals(issue.getStatus()) && sessionView.isTheConnectedUser(issue.getConfirmator());
	}

	public String confirmNextStep() {
		if (!canConfirm())
			return addParameters(listPage, "faces-redirect=true");
		switch (step) {
		case 1:
			System.out.println("step1");
			step++;
			if (!issue.getConfirmation())
				step++;
			break;
		case 2:
			System.out.println("step2");
			switch (issue.getAssignatorCompanyType()) {
			case COMPANY:
			case CUSTOMER:
				issue.setAssignatorSupplier(null);
				break;
			case SUPPLIER:
				issue.setAssignatorSupplier(supplierService.findOne(issue.getAssignatorSupplierId()));
				break;
			default:
				break;
			}
			step++;
			break;
		case 3:
			if (issue.getConfirmation())
				return confirm();
			else
				return reject();
		}
		return null;
	}

	public void confirmPreviousStep() {
		if (step > 1)
			step--;
		if (step == 3 && !issue.getConfirmation())
			step--;
	}

	public String confirm() {
		if (!canConfirm())
			return null;
		issue.setStatus(IssueStatus.CONFIRMED);
		issue.setDate2(new Date());
		issue.setUser2(sessionView.getUser());
		issue.addHistory(sessionView.getUser(), issue.getTmpComment());
		issueService.save(issue);
		deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());

		emailService.sendIssueNotification(issue);

		return addParameters(viewPage, "faces-redirect=true", "id=" + issue.getId());
	}

	public String reject() {
		if (!canConfirm())
			return null;
		issue.setStatus(IssueStatus.CLOSED);
		issue.setDate7(new Date());
		issue.setUser7(sessionView.getUser());
		issue.addHistory(sessionView.getUser(), issue.getTmpComment());
		issueService.save(issue);
		deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());
		return addParameters(viewPage, "faces-redirect=true", "id=" + issue.getId());
	}

	// assign
	public Boolean canAssign() {
//		if (issue.getIsJrIssue())
//			return IssueStatus.CONFIRMED.equals(issue.getStatus()) //
//					&& (sessionView.isTheConnectedUser(issue.getDeliveryRequest().getRequester(), issue.getDeliveryRequest().getProject().getManager()) //
//							|| projectService.isQualityManager(sessionView.getUsername(), issue.getDeliveryRequest().getProjectId()));
//		else if (issue.getIsTicketIssue())
//			return IssueStatus.CONFIRMED.equals(issue.getStatus()) //
//					&& (sessionView.isTheConnectedUser(issue.getTicket().getRequester(), issue.getTicket().getProject().getManager()) //
//							|| projectService.isQualityManager(sessionView.getUsername(), issue.getTicket().getProjectId()));
//		return false;
		return IssueStatus.CONFIRMED.equals(issue.getStatus()) && sessionView.isTheConnectedUser(issue.getAssignator());
	}

	public void assign() {
		if (!canAssign())
			return;
		issue.setStatus(IssueStatus.ASSIGNED);
		issue.setDate3(new Date());
		issue.setUser3(sessionView.getUser());
		issue.setOwnershipUser(userService.findOneLight(issue.getOwnershipUserUsername()));
		issue.addHistory(sessionView.getUser(), "Assigned to : " + issue.getOwnershipUserFullName());
		switch (issue.getOwnershipType()) {
		case COMPANY:
			issue.setCompany(companyService.findOne(issue.getCompanyId()));
			issue.setCustomer(null);
			issue.setSupplier(null);
			break;
		case CUSTOMER:
			issue.setCustomer(customerService.findOne(issue.getCustomerId()));
			issue.setCompany(null);
			issue.setSupplier(null);
			break;
		case SUPPLIER:
			issue.setSupplier(supplierService.findOne(issue.getSupplierId()));
			issue.setCompany(null);
			issue.setCustomer(null);
			break;
		default:
			break;
		}
		issueService.save(issue);
		deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());
		refreshIssue();

		emailService.sendIssueNotification(issue);

	}

	// unassign
	public Boolean canUnassign() {
		return IssueStatus.ASSIGNED.equals(issue.getStatus()) //
				&& (sessionView.isTheConnectedUser(issue.getDeliveryRequest().getRequester(), issue.getDeliveryRequest().getProject().getManager()) //
						|| projectService.isQualityManager(sessionView.getUsername(), issue.getDeliveryRequest().getProjectId()));
	}

	public void unassign() {
		if (!canUnassign())
			return;
		issue.setStatus(IssueStatus.CONFIRMED);
		issue.setDate3(null);
		issue.setUser3(null);
		issue.setOwnershipUser(null);
		issue.addHistory("Unassigned", sessionView.getUser());
		issue.setOwnershipType(null);
		issue.setCompany(null);
		issue.setCustomer(null);
		issue.setSupplier(null);
		issueService.save(issue);
		deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());
		refreshIssue();
	}

	// resolve
	public Boolean canResolve() {
		return IssueStatus.ASSIGNED.equals(issue.getStatus()) && sessionView.isTheConnectedUser(issue.getOwnershipUser());
	}

	public String resolveNextStep() {
		if (!canResolve())
			return addParameters(listPage, "faces-redirect=true");
		switch (step) {
		case 1:
			System.out.println("step1");
			step++;
			break;
		case 2:
			if (issue.getConfirmResolution())
				return resolve();
			else {
				issueService.save(issue);
				return addParameters(viewPage, "faces-redirect=true", "id=" + issue.getId());
			}
		}
		return null;
	}

	public void resolvePreviousStep() {
		if (step > 1)
			step--;
		if (step == 2 && !issue.getConfirmation())
			step--;
	}

	public String resolve() {
		if (!canResolve())
			return null;
		issue.setStatus(IssueStatus.RESOLVED);
		issue.setDate4(new Date());
		issue.setUser4(sessionView.getUser());
		issue.addHistory(sessionView.getUser(), issue.getTmpComment());
		issueService.save(issue);
		deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());
		emailService.sendIssueNotification(issue);
		return addParameters(viewPage, "faces-redirect=true", "id=" + issue.getId());
	}

	public void notResolve() {
		if (!canResolve())
			return;
		issue.setStatus(IssueStatus.NOT_RESOLVED);
		issue.setDate6(new Date());
		issue.setUser6(sessionView.getUser());
		issue.addHistory(sessionView.getUser(), issue.getTmpComment());
		issueService.save(issue);
		deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());
		refreshIssue();
	}

	// acknowledge
	public Boolean canAcknowledge() {
		return IssueStatus.RESOLVED.equals(issue.getStatus()) && sessionView.isTheConnectedUser(issue.getUser3());
	}

	public void acknowledge() {
		if (!canAcknowledge())
			return;
		issue.setStatus(IssueStatus.ACKNOWLEDGED);
		issue.setDate5(new Date());
		issue.setUser5(sessionView.getUser());
		issue.addHistory(sessionView.getUser(), issue.getTmpComment());
		issueService.save(issue);
		deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());
		refreshIssue();
		emailService.sendIssueNotification(issue);
	}

	// open
	public Boolean canOpen() {
		return Arrays.asList(IssueStatus.CLOSED, IssueStatus.RESOLVED).contains(issue.getStatus()) && sessionView.isTheConnectedUser(issue.getUser1());
	}

	public void open() {
		if (!canOpen())
			return;
		issue.setStatus(IssueStatus.SUBMITTED);
		issue.setDate2(null);
		issue.setUser2(null);
		issue.setDate3(null);
		issue.setUser3(null);
		issue.setDate4(null);
		issue.setUser4(null);
		issue.setDate5(null);
		issue.setUser5(null);
		issue.setDate7(null);
		issue.setUser7(null);
		issue.addHistory("Re-open", sessionView.getUser(), issue.getTmpComment());
		issueService.save(issue);
		deliveryRequestService.updateCountIssues(issue.getDeliveryRequest().getId());
		refreshIssue();
	}

	// handover
	private User newOwnershipUser;

	public Boolean canHandover() {
		return IssueStatus.ASSIGNED.equals(issue.getStatus()) && sessionView.isTheConnectedUser(issue.getOwnershipUser());
	}

	public void handover() {
		if (!canHandover())
			return;
		String oldOwnershipFullName = issue.getOwnershipUserFullName();
		issue.setOwnershipUser(userService.findOneLight(newOwnershipUser.getUsername()));
		issue.addHistory("Handover", sessionView.getUser(), "Handover from " + oldOwnershipFullName + " to " + issue.getOwnershipUserFullName());
		switch (issue.getOwnershipType()) {
		case COMPANY:
			issue.setCompany(companyService.findOne(issue.getCompanyId()));
			issue.setCustomer(null);
			issue.setSupplier(null);
			break;
		case CUSTOMER:
			issue.setCustomer(customerService.findOne(issue.getCustomerId()));
			issue.setCompany(null);
			issue.setSupplier(null);
			break;
		case SUPPLIER:
			issue.setSupplier(supplierService.findOne(issue.getSupplierId()));
			issue.setCompany(null);
			issue.setCustomer(null);
			break;
		default:
			break;
		}
		issueService.save(issue);
		refreshIssue();
	}

	// DELETE ISSUE
	public Boolean canDeleteIssue() {
		return IssueStatus.RAISED.equals(issue.getStatus()) && issue.getUser1() != null && sessionView.isTheConnectedUser(issue.getUser1());
	}

	public String deleteIssue() {
		if (canDeleteIssue()) {
			Integer deliveryRequestId = issue.getDeliveryRequest().getId();
			try {
				issueService.delete(issue);
				deliveryRequestService.updateCountIssues(deliveryRequestId);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}
		}
		return addParameters(listPage, "faces-redirect=true");
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
		issue.addComment(comment);
		issue = service.saveAndRefresh(issue);
	}

	public Boolean canDeleteComment(IssueComment comment) {
		return sessionView.isTheConnectedUser(comment.getUser());
	}

	public void deleteComment() {
		if (!canDeleteComment(comment))
			return;
		issue.removeComment(comment);
		issue = service.saveAndRefresh(issue);
	}

	// FILES MANAGEMENT
	private String issueFileType;
	private Integer issueFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		IssueFile issueFile = new IssueFile(file, issueFileType, event.getFile().getFileName(), sessionView.getUser(), issue);
		issueFileService.save(issueFile);
		synchronized (IssueView.class) {
			flushIssue();
			refreshIssue();
		}
	}

	public void deleteIssueFile() {
		try {
			issueFileService.delete(issueFileId);
			Iterator<IssueFile> i = issue.getFileList().iterator();
			while (i.hasNext()) {
				IssueFile current = i.next();
				if (current.getId().equals(issueFileId)) {
					issue.getFileList().remove(current);
					break;
				}
			}

			refreshIssue();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}
	}

	// generic
	public Long countToConfirm() {
		return issueService.countToConfirm(sessionView.getUsername());
	}

	public Long countToAssign() {
		return issueService.countToAssign(sessionView.getUsername());
	}

	public Long countToResolve() {
		return issueService.countToResolve(sessionView.getUsername());
	}

	public Long countToAcknowledge() {
		return issueService.countToAcknowledge(sessionView.getUsername());
	}

	// GETTERS & SETTERS

	public IssueService getIssueService() {
		return issueService;
	}

	public void setIssueService(IssueService issueService) {
		this.issueService = issueService;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public IssueFileService getIssueFileService() {
		return issueFileService;
	}

	public void setIssueFileService(IssueFileService issueFileService) {
		this.issueFileService = issueFileService;
	}

	public String getIssueFileType() {
		return issueFileType;
	}

	public void setIssueFileType(String issueFileType) {
		this.issueFileType = issueFileType;
	}

	public Integer getIssueFileId() {
		return issueFileId;
	}

	public void setIssueFileId(Integer issueFileId) {
		this.issueFileId = issueFileId;
	}

	public IssueFile getIssueFile() {
		return issueFile;
	}

	public void setIssueFile(IssueFile issueFile) {
		this.issueFile = issueFile;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public IssueComment getComment() {
		return comment;
	}

	public void setComment(IssueComment comment) {
		this.comment = comment;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getToNotifyUserUsername() {
		return toNotifyUserUsername;
	}

	public void setToNotifyUserUsername(String toNotifyUserUsername) {
		this.toNotifyUserUsername = toNotifyUserUsername;
	}

	public User getNewOwnershipUser() {
		return newOwnershipUser;
	}

	public void setNewOwnershipUser(User newOwnershipUser) {
		this.newOwnershipUser = newOwnershipUser;
	}

}
