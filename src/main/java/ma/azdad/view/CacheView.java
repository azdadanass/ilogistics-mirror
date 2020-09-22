package ma.azdad.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.ProjectManagerType;
import ma.azdad.service.AffectationService;
import ma.azdad.service.DelegationService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.UserService;
import ma.azdad.service.WarehouseService;
import ma.azdad.utils.DataTypes;

@ManagedBean
@Component
@Transactional
@Scope("session")

public class CacheView implements Serializable {

	@Autowired
	AffectationService affectationService;

	@Autowired
	ProjectService projectService;

	@Autowired
	SessionView sessionView;

	@Autowired
	DelegationService delegationService;

	@Autowired
	WarehouseService warehouseService;

	@Autowired
	UserService userService;

	private Integer selectedMenu = 2;

	private Map<String, String> lm = new HashMap<String, String>();

	private List<Integer> assignedProjectList;
	private List<Integer> hmProjectList;
	private List<Integer> delegatedProjectList;
	private Set<Integer> allProjectList = new HashSet<Integer>();
	private List<Integer> warehouseList = new ArrayList<Integer>(Arrays.asList(-1));

	@PostConstruct
	public void init() {
		refreshAffectationData();
		lm = affectationService.getDatas(DataTypes.LM.getValue());
		assignedProjectList = projectService.findAssignedProjectIdListByResource(sessionView.getUsername());
		delegatedProjectList = delegationService.findDelegatedProjects(sessionView.getUsername());
		allProjectList.addAll(assignedProjectList);
		allProjectList.addAll(delegatedProjectList);

		hmProjectList = projectService.findIdListByManagerType(sessionView.getUsername(), ProjectManagerType.HARDWARE_MANAGER);
		warehouseList.addAll(warehouseService.findIdListByManager(sessionView.getUsername()));
	}

	public Boolean hasDelegation(Integer projectId) {
		return delegatedProjectList.contains(projectId);
	}

	public String getWelcomePage(Integer selectedMenu) {
		this.selectedMenu = selectedMenu;
		switch (selectedMenu) {
		case 1:
			return addParameters(sessionView.getIsAdmin() ? "warehouseList.xhtml" : sessionView.getIsPM() ? "projectList.xhtml" : "partNumberList.xhtml", "faces-redirect=true");
		case 2:
			if (sessionView.isPM() || sessionView.isWM() || sessionView.isUser() || sessionView.getIsLobManager())
				return addParameters("deliveryRequestList.xhtml", "faces-redirect=true", "state=0", "pageIndex=1");
		case 3:
			if (sessionView.isAdmin() || sessionView.isPM() || sessionView.isTM() || sessionView.isUser())
				return addParameters("transportationRequestList.xhtml", "faces-redirect=true", "state=0", "pageIndex=1");
		case 4:
			if (sessionView.isAdmin() || sessionView.isPM() || sessionView.isWM() || sessionView.isUser() || sessionView.getIsLobManager())
				return addParameters("reporting.xhtml", "faces-redirect=true");
		default:
			return null;
		}
	}

	public void accessDenied() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			// TODO access denied page
			ec.redirect(ec.getRequestContextPath() + "ad.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getProjectManager(Integer projectId) {
		return projectService.getManagerFullNameMap().get(projectId).getManagerFullName();
	}

	public Boolean isTheLineManagerOf(String resourceUsername) {
		return sessionView.isTheConnectedUser(getLineManager(resourceUsername));
	}

	private void refreshAffectationData() {
		lm = affectationService.getDatas(DataTypes.LM.getValue());
	}

	public String getPhoto(String username) {
		return userService.findAsMap().get(username).getPhoto();
	}

	public String getPublicPhoto(String username) {
		return userService.findAsMap().get(username).getPublicPhoto();
	}

	public String getFullName(String username) {
		return userService.findAsMap().get(username).getFullName();
	}

	public String getFullname(String username) {
		return getFullName(username);
	}

	public String getJob(String username) {
		return userService.findAsMap().get(username).getJob();
	}

	public String getPhone(String username) {
		return userService.findAsMap().get(username).getPhone();
	}

	public String getEmail(String username) {
		return userService.findAsMap().get(username).getEmail();
	}

	public String getLineManager(String username) {
		return lm.get(username);
	}

	public String getCin(String username) {
		return userService.findAsMap().get(username).getCin();
	}

	public Integer getSelectedMenu() {
		return selectedMenu;
	}

	public void setSelectedMenu(Integer selectedMenu) {
		this.selectedMenu = selectedMenu;
	}

	public static String addParameters(String path, String... tab) {
		path += "?" + tab[0];
		if (tab.length > 1)
			for (int i = 1; i < tab.length; i++)
				path += "&" + tab[i];
		return path;
	}

	public List<Integer> getAssignedProjectList() {
		return assignedProjectList;
	}

	public void setAssignedProjectList(List<Integer> assignedProjectList) {
		this.assignedProjectList = assignedProjectList;
	}

	public List<Integer> getDelegatedProjectList() {
		return delegatedProjectList;
	}

	public void setDelegatedProjectList(List<Integer> delegatedProjectList) {
		this.delegatedProjectList = delegatedProjectList;
	}

	public List<Integer> getWarehouseList() {
		return warehouseList;
	}

	public void setWarehouseList(List<Integer> warehouseList) {
		this.warehouseList = warehouseList;
	}

	public List<Integer> getHmProjectList() {
		return hmProjectList;
	}

	public void setHmProjectList(List<Integer> hmProjectList) {
		this.hmProjectList = hmProjectList;
	}

	public Set<Integer> getAllProjectList() {
		return allProjectList;
	}

	public void setAllProjectList(Set<Integer> allProjectList) {
		this.allProjectList = allProjectList;
	}

}
