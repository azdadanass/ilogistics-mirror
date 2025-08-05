package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.CompanyType;
import ma.azdad.model.Conversation;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.Role;
import ma.azdad.model.Team;
import ma.azdad.model.User;
import ma.azdad.model.UserFile;
import ma.azdad.service.CustomerService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.SupplierService;
import ma.azdad.service.UserRoleService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class UserView {

	@Autowired
	private SessionView sessionView;

	@Autowired
	protected UserService userService;

	@Autowired
	protected FileUploadView fileUploadView;

	@Autowired
	protected CustomerService customerService;

	@Autowired
	protected SupplierService supplierService;

	@Autowired
	protected ProjectService projectService;

	@Autowired
	protected UserRoleService userRoleService;

	private String toNotifyUserUsername;
	private Integer toNotifyExternalResourceId;

	protected List<User> list1 = new ArrayList<>();
	protected List<User> list2 = new ArrayList<>();
	protected List<User> list3;
	protected List<User> list4;
	private String searchBean = "";
	protected String listPage = "userList.xhtml";
	protected String addEditPage = "addEditUser.xhtml";
	protected String viewPage = "viewUser.xhtml";
	protected Boolean isListPage = false;
	protected Boolean isAddPage = false;
	protected Boolean isEditPage = false;
	protected Boolean isViewPage = false;
	protected String currentPath;
	protected String username;
	protected User user;
	private Integer parent;
	private Boolean filterByUser = true;

	private String sortBy = "Full Name";

	@PostConstruct
	public void init() {
		currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		initParameters();
		isListPage = ("/" + listPage).equals(currentPath);
		isAddPage = ("/" + addEditPage).equals(currentPath) && username == null;
		isEditPage = ("/" + addEditPage).equals(currentPath) && username != null;
		isViewPage = ("/" + viewPage).equals(currentPath);

		if (isListPage)
			refreshList();
		else if (isEditPage)
			user = userService.findOne(username);
		else if (isViewPage)
			user = userService.findOne(username);
	}

	protected void initParameters() {
		username = UtilsFunctions.getParameter("username");
	}

	public void sort() {
		System.out.println("sort");
		Collections.sort(list2, new Comparator<User>() {
			@Override
			public int compare(User o1, User o2) {
				switch (sortBy) {
				case "Full Name":
					return o1.getFullName().compareTo(o2.getFullName());
				}
				return 1;
			}
		});

	}

	public void initLists(List<User> list) {
		list2 = list1 = list;
	}

	private void filterBean(String query) {
		list3 = null;
		List<User> list = new ArrayList<>();
		query = query.toLowerCase().trim();
		for (User bean : list1) {
			if (bean.filter(query))
				list.add(bean);
		}
		list2 = list;
	}

	public void refreshList() {
		if (isListPage)
			list2 = list1 = filterByUser ? userService.findLightByUser(false, sessionView.getUsername()) : userService.findLight(false);
	}

	public void refreshList(List<User> list) {
		list2 = list1 = list;
	}

	public Integer getRowsNumber() {
		if (list3 != null)
			return list3.size();
		else
			return list2.size();
	}

	public void refreshUser() {
		userService.flush();
		user = userService.findOne(user.getUsername());
	}

	// SAVE EXTERNALRESOURCE
	public Boolean canSaveUser() {
		if (isEditPage || isViewPage)
			return sessionView.isTheConnectedUser(user.getUser());
		return true;
	}

	public Boolean validateUser() {
		formatFirstName();
		formatLastName();
		formatCin();
		formatEmail();
		formatPhone();

		if (user.getEmail() != null && user.getEmail().isEmpty())
			user.setEmail(null);

		if (userService.isFirstNameAndLastNameExists(user)) {
			FacesContextMessages.ErrorMessages("First name and Last name already exist in database ! ");
			return false;
		}

		if (user.getCin() != null && !user.getCin().isEmpty())
			if (userService.isCinExists(user)) {
				FacesContextMessages.ErrorMessages("CIN already exists in database ! ");
				return false;
			}

		if (user.getEmail() != null && !user.getEmail().isEmpty())
			if (userService.isEmailExists(user)) {
				FacesContextMessages.ErrorMessages("Email already exists in database ! ");
				return false;
			}

		if (user.getPhone() != null && !user.getPhone().isEmpty())
			if (userService.isPhoneExists(user)) {
				FacesContextMessages.ErrorMessages("Phone already exists in database ! ");
				return false;
			}
		return true;
	}

	// for chat
	@Cacheable("userView.findLightByActive")
	public List<User> findLightByActive(String username) {

		return userService.findLightByActive(username);
	}

	public List<Conversation> findOnlineUserConversations(String username) {

		List<User> users = userService.findLightByActive(username);
		List<Conversation> conversations = new ArrayList<>();
		for (User user1 : users) {

			if (sessionView.getOnlineUsers().contains(user1))
				conversations.add(new Conversation(user1, null, null, null));

		}

		return conversations;
	}

	//

	public void formatFirstName() {
		user.setFirstName(UtilsFunctions.formatName(user.getFirstName()));
	}

	public void formatLastName() {
		user.setLastName(UtilsFunctions.formatName(user.getLastName()));
	}

	public void formatCin() {
		user.setCin(UtilsFunctions.cleanString(user.getCin()).replace(" ", "").toUpperCase());
	}

	public void formatEmail() {
		user.setEmail(UtilsFunctions.cleanString(user.getEmail()).replace(" ", "").toLowerCase());
	}

	public void formatPhone() {
		user.setPhone(UtilsFunctions.cleanString(user.getPhone().replace(" ", "")));
	}

	// can update in place
	public Boolean canUpdateInplace() {
		return sessionView.isTheConnectedUser(user.getUser());
	}

	public void updateInplace() {
		if (canUpdateInplace())
			user = userService.saveAndRefresh(user);
	}

	// CHANGE PASSWORD
	public Boolean canChangePassword() {
		return sessionView.isTheConnectedUser(user.getUser());
	}

	public void changePassword() {
		if (!canChangePassword())
			return;
		user.setPassword(UtilsFunctions.stringToMD5(user.getPassword()));
		userService.save(user);
		refreshUser();
	}

	// PHOTO MANAGEMENT
	public Boolean canUploadPhoto() {
		return sessionView.isTheConnectedUser(user.getUser());
	}

	public void uploadPhoto(FileUploadEvent event) {
		if (!canUploadPhoto())
			return;
		String fileName = user.getUsername();
		String link = fileUploadView.uploadFileOld(event, fileName, "photos");
		user.setPhoto(link);
		userService.save(user);
		refreshUser();
	}

	// TOGGLE STATUS
	public Boolean canToggleStatus() {
		return sessionView.isTheConnectedUser(user.getUser());
	}

	public void toggleStatus() {
		if (!canToggleStatus())
			return;
		user.setActive(!user.getActive());
		userService.save(user);
		refreshUser();
	}

	// DELETE EXTERNALRESOURCE
	public Boolean canDeleteUser() {
		return sessionView.isTM();
	}

	public String deleteUser() {
		if (canDeleteUser())
			userService.delete(user);
		return UtilsFunctions.addParameters(listPage, "faces-redirect=true");
	}

	// FILES MANAGEMENT
	private UserFile userFile;
	private String userFileType;
	private Integer userFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileUploadView.handleFileUpload(event, "user");
		UserFile userFile = new UserFile(file, userFileType, event.getFile().getFileName(), user, sessionView.getUser());
		user.addFile(userFile);
		userService.save(user);
		synchronized (UserView.class) {
			refreshUser();
		}
	}

	public void deleteFile(UserFile file) {
		user.removeFile(file);
		userService.save(user);
		refreshUser();
	}

	// generic
	public List<User> findLight() {
		return userService.findLight();
	}
	
	public List<User> findLight2(){
		return userService.findLight2();
	}

	public List<User> findActiveByCompanyType(CompanyType companyType, Integer companyId, Integer customerId, Integer supplierId) {
		return userService.findActiveByCompanyType(companyType, companyId, customerId, supplierId);
	}

	public List<User> findActiveByCompany(Integer companyId) {
		return userService.findActiveByCompany(companyId);
	}

	public List<User> findActiveByCustomer(Integer customerId) {
		return userService.findActiveByCustomer(customerId);
	}

	public List<User> findActiveBySupplier(Integer supplierId) {
		return userService.findActiveBySupplier(supplierId);
	}

	public List<User> findLightByStatus(Boolean active) {
		return userService.findLightByStatus(active);
	}

	public List<User> findLightAndActive() {
		return userService.findLightByStatus(true);
	}

	public List<User> findWarehouseManagerList() {
		return userService.findWarehouseManagerList();
	}

	public List<User> findLightByLineManagerAndActive(String lineManagerUsername) {
		return userService.findLightByLineManagerAndActive(lineManagerUsername);
	}

	public List<User> findLightByLineManagerAndActive() {
		return userService.findLightByLineManagerAndActive(sessionView.getUsername());
	}

	public List<User> findByJob(String job) {
		return userService.findByJob(job);
	}

	public List<User> findLightByTransporter(Integer transporterId) {
		return userService.findLightByTransporter(transporterId);
	}

	public List<User> findLightByProject(Integer projectId) {
		return userService.findLightByProject(projectId);
	}

	public List<User> findLightByDeliverToOther(User user) {
		return userService.findLightByDeliverToOther(user);
	}

	public List<User> findLightActive(Boolean internal) {
		return userService.findLight(internal, true);
	}

	public List<User> findLightActive() {
		return findLightByStatus(true);
	}

	public List<User> findLightByCompany(Integer companyId, Boolean active) {
		return userService.findLightByCompany(companyId, active);
	}

	public List<User> findExternalActive() {
		return userService.findExternalActive();
	}

	public List<User> findLightBySupplierAndHasRolePm(Integer supplierId) {
		return userService.findLightBySupplierAndHasRole(supplierId, Role.ROLE_ILOGISTICS_PM);
	}

	public List<User> findLightByCustomerAndHasRolePm(Integer customerId) {
		return userService.findLightByCustomerAndHasRole(customerId, Role.ROLE_ILOGISTICS_PM);
	}

	public Boolean hasRole(String username, Role role) {
		return userRoleService.isHavingRole(username, role);
	}

	public Boolean hasRoleSdm(String username) {
		return hasRole(username, Role.ROLE_SDM_USER);
	}

	public Boolean hasRoleIlogistics(String username) {
		return hasRole(username, Role.ROLE_ILOGISTICS_USER);
	}

	public Boolean hasRoleTeamLeader(String username) {
		return hasRole(username, Role.ROLE_SDM_TEAM_LEADER);
	}

	public Boolean hasRolePm(String username) {
		return hasRole(username, Role.ROLE_ILOGISTICS_PM);
	}

	public User findFirstByRoleTM() {
		return userService.findFirstByRoleTM();
	}

	public List<User> findHandoverUserList(DeliveryRequest deliveryRequest) {
		return userService.findHandoverUserList(deliveryRequest);
	}

	public List<User> findByAssignementAndCompany(Integer projectId, Integer companyId) {
		return userService.findByAssignementAndCompany(projectId, companyId);
	}

	public List<User> findByAssignementAndCustomer(Integer projectId, Integer customerId) {
		return userService.findByAssignementAndCustomer(projectId, customerId);
	}

	public List<User> findByAssignementAndSupplier(Integer projectId, Integer supplierId) {
		return userService.findByAssignementAndSupplier(projectId, supplierId);
	}

	public CompanyType findCompanyType(String username) {
		return userService.findCompanyType(username);
	}

	public String findEntityName(User user) {
		return userService.findEntityName(user);
	}

	// getters & setters

	public String getToNotifyUserUsername() {
		return toNotifyUserUsername;
	}

	public void setToNotifyUserUsername(String toNotifyUserUsername) {
		this.toNotifyUserUsername = toNotifyUserUsername;
	}

	public Integer getToNotifyExternalResourceId() {
		return toNotifyExternalResourceId;
	}

	public void setToNotifyExternalResourceId(Integer toNotifyExternalResourceId) {
		this.toNotifyExternalResourceId = toNotifyExternalResourceId;
	}

	public List<User> getList1() {
		return list1;
	}

	public void setList1(List<User> list1) {
		this.list1 = list1;
	}

	public List<User> getList2() {
		return list2;
	}

	public void setList2(List<User> list2) {
		this.list2 = list2;
	}

	public List<User> getList3() {
		return list3;
	}

	public void setList3(List<User> list3) {
		this.list3 = list3;
	}

	public List<User> getList4() {
		return list4;
	}

	public void setList4(List<User> list4) {
		this.list4 = list4;
	}

	public String getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;
		filterBean(searchBean);
	}

	public Boolean getIsListPage() {
		return isListPage;
	}

	public void setIsListPage(Boolean isListPage) {
		this.isListPage = isListPage;
	}

	public Boolean getIsAddPage() {
		return isAddPage;
	}

	public void setIsAddPage(Boolean isAddPage) {
		this.isAddPage = isAddPage;
	}

	public Boolean getIsEditPage() {
		return isEditPage;
	}

	public void setIsEditPage(Boolean isEditPage) {
		this.isEditPage = isEditPage;
	}

	public Boolean getIsViewPage() {
		return isViewPage;
	}

	public void setIsViewPage(Boolean isViewPage) {
		this.isViewPage = isViewPage;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public FileUploadView getFileUploadView() {
		return fileUploadView;
	}

	public void setFileUploadView(FileUploadView fileUploadView) {
		this.fileUploadView = fileUploadView;
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

	public ProjectService getProjectService() {
		return projectService;
	}

	public void setProjectService(ProjectService projectService) {
		this.projectService = projectService;
	}

	public String getListPage() {
		return listPage;
	}

	public void setListPage(String listPage) {
		this.listPage = listPage;
	}

	public String getAddEditPage() {
		return addEditPage;
	}

	public void setAddEditPage(String addEditPage) {
		this.addEditPage = addEditPage;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getFilterByUser() {
		return filterByUser;
	}

	public void setFilterByUser(Boolean filterByUser) {
		this.filterByUser = filterByUser;
	}

	public String getUserFileType() {
		return userFileType;
	}

	public void setUserFileType(String userFileType) {
		this.userFileType = userFileType;
	}

	public Integer getUserFileId() {
		return userFileId;
	}

	public void setUserFileId(Integer userFileId) {
		this.userFileId = userFileId;
	}

	public UserFile getUserFile() {
		return userFile;
	}

	public void setUserFile(UserFile userFile) {
		this.userFile = userFile;
	}

	public UserRoleService getUserRoleService() {
		return userRoleService;
	}

	public void setUserRoleService(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	
}
