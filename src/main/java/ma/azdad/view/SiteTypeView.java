package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.SiteType;
import ma.azdad.repos.SiteTypeRepos;
import ma.azdad.service.SiteTypeService;

@ManagedBean
@Component
@Scope("view")
public class SiteTypeView extends GenericView<Integer, SiteType, SiteTypeRepos, SiteTypeService> {

	@Autowired
	protected SiteTypeService siteTypeService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected FileView fileView;

	private SiteType siteType = new SiteType();

	private Integer categoryId = null;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		initParameters();

		if (isListPage)
			refreshList();
		else if (isEditPage)
			siteType = siteTypeService.findOne(id);
		else if (isViewPage)
			siteType = siteTypeService.findOne(id);

	}

	@Override
	public void initParameters() {
		super.initParameters();
		try {
			categoryId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("categoryId"));
		} catch (Exception e) {
			categoryId = null;
		}

	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = siteTypeService.findByCategory(categoryId);
	}

	public void refreshSiteType() {
		siteTypeService.flush();
		siteType = siteTypeService.findOne(siteType.getId());
	}

	// SAVE SITETYPE
	public Boolean canSaveSiteType() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveSiteType() {
		if (!canSaveSiteType())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateSiteType())
			return null;
		siteType = siteTypeService.save(siteType);
		return addParameters(viewPage, "faces-redirect=true", "id=" + siteType.getId());
	}

	public Boolean validateSiteType() {
		return true;
	}

	// DELETE SITETYPE
	public Boolean canDeleteSiteType() {
		return true;
	}

	public String deleteSiteType() {
		if (canDeleteSiteType())
			try {
				siteTypeService.delete(siteType);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return addParameters(listPage, "faces-redirect=true");
	}

	// generic
	public List<SiteType> findAll() {
		return siteTypeService.findAll();
	}

	// GETTERS & SETTERS
	@Override
	public SessionView getSessionView() {
		return sessionView;
	}

	@Override
	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public SiteTypeService getSiteTypeService() {
		return siteTypeService;
	}

	public void setSiteTypeService(SiteTypeService siteTypeService) {
		this.siteTypeService = siteTypeService;
	}

	public SiteType getSiteType() {
		return siteType;
	}

	public void setSiteType(SiteType siteType) {
		this.siteType = siteType;
	}

	public FileView getFileView() {
		return fileView;
	}

	public void setFileView(FileView fileView) {
		this.fileView = fileView;
	}

}
