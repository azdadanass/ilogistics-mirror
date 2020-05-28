package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.SiteCategory;
import ma.azdad.service.SiteCategoryService;
import ma.azdad.service.SiteTypeService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class SiteCategoryView extends GenericView<SiteCategory> {

	@Autowired
	protected SiteCategoryService siteCategoryService;

	@Autowired
	protected SiteTypeService siteTypeService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected FileView fileView;

	private SiteCategory siteCategory = new SiteCategory();

	private Integer typeId = null;

	@PostConstruct
	public void init() {
		super.init();

		initParameters();

		if (typeId != null)
			siteCategory = siteTypeService.findOne(typeId).getCategory();

		if (isListPage)
			refreshList();
		else if (isEditPage)
			siteCategory = siteCategoryService.findOne(selectedId);
		else if (isViewPage)
			siteCategory = siteCategoryService.findOne(selectedId);

	}

	public void initParameters() {
		super.initParameters();
		try {
			typeId = Integer.valueOf(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("typeId"));
		} catch (Exception e) {
			typeId = null;
		}

	}

	public void refreshList() {
		if (isListPage) {
			List<SiteCategory> list = siteCategoryService.findAll();
			list.add(new SiteCategory("All", "resources/img/site_category/0.jpg"));
			list2 = list1 = list;
		}

	}

	public void refreshSiteCategory() {
		siteCategoryService.flush();
		siteCategory = siteCategoryService.findOne(siteCategory.getId());
	}

	// SAVE SITECATEGORY
	public Boolean canSaveSiteCategory() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveSiteCategory() {
		if (!canSaveSiteCategory())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateSiteCategory())
			return null;
		siteCategory = siteCategoryService.save(siteCategory);
		return addParameters(viewPage, "faces-redirect=true", "id=" + siteCategory.getId());
	}

	public Boolean validateSiteCategory() {
		return true;
	}

	// GENERIC
	public List<SiteCategory> findAll() {
		return siteCategoryService.findAll();
	}

	// DELETE SITECATEGORY
	public Boolean canDeleteSiteCategory() {
		return true;
	}

	public String deleteSiteCategory() {
		if (canDeleteSiteCategory())
			siteCategoryService.delete(siteCategory);
		return addParameters(listPage, "faces-redirect=true");
	}

	// GETTERS & SETTERS
	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public SiteCategoryService getSiteCategoryService() {
		return siteCategoryService;
	}

	public void setSiteCategoryService(SiteCategoryService siteCategoryService) {
		this.siteCategoryService = siteCategoryService;
	}

	public SiteCategory getSiteCategory() {
		return siteCategory;
	}

	public void setSiteCategory(SiteCategory siteCategory) {
		this.siteCategory = siteCategory;
	}

	public FileView getFileView() {
		return fileView;
	}

	public void setFileView(FileView fileView) {
		this.fileView = fileView;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

}
