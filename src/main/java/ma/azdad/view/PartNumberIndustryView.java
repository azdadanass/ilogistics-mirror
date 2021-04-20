package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberIndustry;
import ma.azdad.repos.PartNumberIndustryRepos;
import ma.azdad.service.PartNumberIndustryService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class PartNumberIndustryView extends GenericView<Integer, PartNumberIndustry, PartNumberIndustryRepos, PartNumberIndustryService> {

	@Autowired
	private PartNumberIndustryService partNumberIndustryService;

	@Autowired
	private CacheView cacheView;

	private PartNumberIndustry partNumberIndustry = new PartNumberIndustry();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		refreshList();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if ("/partNumberConfiguration.xhtml".equals(currentPath))
			list2 = list1 = partNumberIndustryService.findAll();
	}

	public void flushPartNumberIndustry() {
		partNumberIndustryService.flush();
	}

	public void refreshPartNumberIndustry() {
		if (partNumberIndustry.getId() != null)
			partNumberIndustry = partNumberIndustryService.findOne(partNumberIndustry.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewPartNumberIndustry())
			cacheView.accessDenied();
	}

	public Boolean canViewPartNumberIndustry() {
		return sessionView.getIsAdmin();
	}

	public void initPartNumberIndustry() {
		partNumberIndustry = new PartNumberIndustry();
	}

	// SAVE PARTNUMBERINDUSTRY
	public Boolean canSavePartNumberIndustry() {
		return sessionView.getIsAdmin();
	}

	public void savePartNumberIndustry() {
		if (!canSavePartNumberIndustry())
			return;
		if (!validatePartNumberIndustry())
			return;
		partNumberIndustry = partNumberIndustryService.save(partNumberIndustry);

		refreshList();
	}

	public Boolean validatePartNumberIndustry() {
		if (partNumberIndustryService.isNameExists(partNumberIndustry.getName(), partNumberIndustry.getId()))
			return FacesContextMessages.ErrorMessages("Name already exists");

		return true;
	}

	// DELETE PARTNUMBERINDUSTRY
	public Boolean canDeletePartNumberIndustry() {
		return sessionView.getIsAdmin();
	}

	public void deletePartNumberIndustry() {
		if (!canDeletePartNumberIndustry())
			return;
		try {
			partNumberIndustryService.delete(partNumberIndustry);
			refreshList();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}

	}

	// GENERIC
	public List<PartNumberIndustry> findAll() {
		return partNumberIndustryService.findAll();
	}

	// GETTERS & SETTERS
	public PartNumberIndustry getPartNumberIndustry() {
		return partNumberIndustry;
	}

	public void setPartNumberIndustry(PartNumberIndustry partNumberIndustry) {
		this.partNumberIndustry = partNumberIndustry;
	}

}
