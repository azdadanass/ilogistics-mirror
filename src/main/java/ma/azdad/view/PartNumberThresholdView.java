package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumberThreshold;
import ma.azdad.model.Project;
import ma.azdad.repos.PartNumberThresholdRepos;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.PartNumberThresholdService;
import ma.azdad.service.ProjectService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class PartNumberThresholdView extends GenericView<Integer, PartNumberThreshold, PartNumberThresholdRepos, PartNumberThresholdService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private PartNumberThresholdService partNumberThresholdService;

	@Autowired
	private CacheView cacheView;

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private ProjectView projectView;

	@Autowired
	private PartNumberService partNumberService;

	private PartNumberThreshold partNumberThreshold = new PartNumberThreshold();

	private Project project = new Project();

	private Boolean editMode = false;

	@Override
	@PostConstruct
	public void init() {
		super.init();
//		if ("/partNumberThresholdList.xhtml".contentEquals(currentPath)) {
//			project = projectService.findOne(id);
//			refreshList();
//		}
		
		if(isPage("viewProject")) {
			project = projectView.getProject();
			refreshList();
		}
			
		
		
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if (isPage("viewProject"))
			list2 = list1 = partNumberThresholdService.findByProject(id);
	}

	public void flushPartNumberThreshold() {
		partNumberThresholdService.flush();
	}

	public void refreshPartNumberThreshold() {
		if (partNumberThreshold.getId() != null)
			partNumberThreshold = partNumberThresholdService.findOne(partNumberThreshold.getId());
	}

	// EDIT MODE
	public Boolean canEdit() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername()) && project.getCustomerStockManagement() && !editMode;
	}

	public Boolean canToggleEditMode() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername()) && project.getCustomerStockManagement() && !editMode;
	}

	public void toggleEditMode() {
		if (!canToggleEditMode())
			return;
		editMode = true;
	}

	public Boolean canAddNewRow() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername()) && project.getCustomerStockManagement() && editMode;
	}

	public void addNewRow() {
		if (!canSavePartNumberThreshold())
			return;
		list2.add(new PartNumberThreshold(project));
	}

	public Boolean canSave() {
		return canAddNewRow();
	}

	public void save() {
		if (!canSave())
			return;
		if (!validate())
			return;
		list2.forEach(i -> {
			i.setProject(project);
			i.setPartNumber(partNumberService.findOne(i.getPartNumberId()));
			partNumberThresholdService.save(i);
		});
		refreshList();
		System.out.println(list2.size());
		editMode = false;
	}

	private Boolean validate() {
		for (PartNumberThreshold pnt : list2) {
			if (pnt.getPartNumber() == null)
				return FacesContextMessages.ErrorMessages("Part Number should not be null");
			if (pnt.getStockMin() != null && pnt.getStockMax() != null && pnt.getStockMin() > pnt.getStockMax())
				return FacesContextMessages.ErrorMessages("Stock Min should be lower than stock Max");
		}

		if (list2.stream().map(i -> i.getPartNumberId()).distinct().count() < list2.size())
			return FacesContextMessages.ErrorMessages("Duplicate Part Number");

		return true;
	}

	public Boolean canDeleteRow() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername()) && project.getCustomerStockManagement() && editMode;
	}

	public void deleteRow(int index) {
		if (!canDeleteRow())
			return;
		try {
			PartNumberThreshold pnt = list2.get(index);
			if (pnt.getId() != null)
				partNumberThresholdService.delete(pnt);

			list2.remove(index);
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewPartNumberThreshold())
			cacheView.accessDenied();
	}

	public Boolean canViewPartNumberThreshold() {
		return true;
	}

	// SAVE PARTNUMBERTHRESHOLD
	public void initPartNumberThreshold() {
		partNumberThreshold = new PartNumberThreshold(project);
	}

	public Boolean canSavePartNumberThreshold() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername()) && project.getCustomerStockManagement();
	}

	public void savePartNumberThreshold() {
		if (!canSavePartNumberThreshold())
			return;
		if (!validatePartNumberThreshold())
			return;
		partNumberThreshold = partNumberThresholdService.save(partNumberThreshold);
		refreshList();
		System.out.println(list2.size());
		execJavascript("PF('addDlg').hide()");
	}

	public Boolean validatePartNumberThreshold() {
		if (partNumberThresholdService.countByProjectAndPartNumber(project.getId(), partNumberThreshold.getPartNumber().getId()) > 0)
			return FacesContextMessages.ErrorMessages("Part Number already exists");
		if (partNumberThreshold.getStockMin() > partNumberThreshold.getStockMax())
			return FacesContextMessages.ErrorMessages("Stock Min should be lower than stock Max");

		return true;
	}

	// DELETE PARTNUMBERTHRESHOLD
	public Boolean canDeletePartNumberThreshold() {
		return sessionView.isTheConnectedUser(project.getManager().getUsername());
	}

	public void deletePartNumberThreshold() {
		if (!canDeletePartNumberThreshold())
			return;
		try {
			partNumberThresholdService.delete(partNumberThreshold);
			refreshList();
			execJavascript("PF('deleteDlg').hide()");
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}

	}

	// GETTERS & SETTERS
	public PartNumberThreshold getPartNumberThreshold() {
		return partNumberThreshold;
	}

	public void setPartNumberThreshold(PartNumberThreshold partNumberThreshold) {
		this.partNumberThreshold = partNumberThreshold;
	}

	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}

}
