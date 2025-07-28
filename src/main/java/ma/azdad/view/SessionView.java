package ma.azdad.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Profile;
import ma.azdad.model.User;
import ma.azdad.service.CompanyProfileService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;

@ManagedBean
@Component
@Transactional
@Scope("session")
public class SessionView implements Serializable {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected UserService userService;

	@Autowired
	private CompanyProfileService companyProfileService;
	private static List<User> onlineUsers = new ArrayList<>();
	private String login;
	private String serverName;
	private User user;

	private List<Integer> companyCfoIdList = new ArrayList<>(Arrays.asList(-1));

	@PostConstruct
	public void init() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		serverName = UtilsFunctions.getServerName();
		login = auth.getName().toLowerCase().trim();
		user = userService.findByLogin(login);
		addUser(user);
		companyCfoIdList.addAll(companyProfileService.findCompanyIdList(user.getUsername(), Profile.CFO));
		System.out.println("**********************************");
		System.out.println(user.getFullName() + "(" + user.getUsername() + ")" + " is connected");
		System.out.println("serverName : " + serverName);
		System.out.println("**********************************");
	}

	// for chat javascript

	public String getJavascriptUsername() {

		return "var username = '" + user.getUsername() + "';";
	}

	public String getJavascriptUsername2() {

		return "var username2 = '" + user.getUsername() + "';";
	}

	public String getJavascriptOnlineUsernames() {
		List<String> fullNames = new ArrayList<>();
		for (User user : onlineUsers) {
			fullNames.add(user.getFullName());
		}
		return "var onlineUsers = '" + fullNames + "';";
	}

	//

	public void addUser(User user) {
		onlineUsers.add(user);
	}

	public void removeUser(User user) {
		onlineUsers.remove(user);
	}

	public Boolean getInternal() {
		return user.getInternal();
	}

	public String getFullName() {
		return user.getFullName();
	}

	public String getPhoto() {
		return user.getPhoto();
	}

	public Boolean isTheConnectedUser(String username) {
		return user.getUsername().equalsIgnoreCase(username);
	}

	public Boolean isTheConnectedUser(User user) {
		if (user == null)
			return false;
		return isTheConnectedUser(user.getUsername());
	}

	public Boolean isTheConnectedUser(User... users) {
		return Stream.of(users).anyMatch(i -> isTheConnectedUser(i));
	}

	public Boolean getIsCfo() {
		return companyCfoIdList.size() > 1;
	}

	public Boolean getIsCfo(Integer companyId) {
		return companyCfoIdList.contains(companyId);
	}

	public Boolean isUser() {
		return user.getIsUser();
	}

	public Integer getSupplierId() {
		return getIsSupplierUser() ? user.getSupplierId() : null;
	}

	public Boolean getIsSupplierUser() {
		return user.getIsSupplierUser();
	}

	public Boolean getIsCustomerUser() {
		return user.getIsCustomerUser();
	}

	public Integer getCustomerId() {
		return getIsCustomerUser() ? user.getCustomerId() : null;
	}

	public Boolean getIsUser() {
		return isUser();
	}

	public Boolean isSE() {
		return user.getIsSE();
	}

	public Boolean getIsSE() {
		return isSE();
	}

	public Boolean isPM() {
		return user.getIsPM();
	}

	public Boolean getIsPM() {
		return isPM();
	}

	public Boolean getIsInternalPM() {
		return user.getIsInternalPM();
	}

	public Boolean getIsExternalPM() {
		return user.getIsExternalPM();
	}

	public Boolean getIsLobManager() {
		return user.getIsLobManager();
	}

	public Boolean getIsBuManager() {
		return user.getIsBuManager();
	}

	public Boolean isWM() {
		return user.getIsWM();
	}

	public Boolean getIsWM() {
		return isWM();
	}

	public Boolean isTM() {
		return user.getIsTM();
	}

	public Boolean getIsDriver() {
		return user.getIsDriver();
	}

	public Boolean isDriver() {
		return user.getIsDriver();
	}

	public Boolean getIsTM() {
		return isTM();
	}

	public Boolean getIsPm() {
		return isPM();
	}

	public Boolean getIsExternalPm() {
		return user.getIsExternalPm();
	}

	public Boolean isAdmin() {
		return user.getIsAdmin();
	}

	public Boolean getIsAdmin() {
		return isAdmin();
	}

	public String getUsername() {
		return user.getUsername();
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Logger getLog() {
		return log;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CompanyProfileService getCompanyProfileService() {
		return companyProfileService;
	}

	public void setCompanyProfileService(CompanyProfileService companyProfileService) {
		this.companyProfileService = companyProfileService;
	}

	public List<Integer> getCompanyCfoIdList() {
		return companyCfoIdList;
	}

	public void setCompanyCfoIdList(List<Integer> companyCfoIdList) {
		this.companyCfoIdList = companyCfoIdList;
	}

	public List<User> getOnlineUsers() {
		return onlineUsers;
	}

	public void setOnlineUsers(List<User> onlineUsers) {
		this.onlineUsers = onlineUsers;
	}

}