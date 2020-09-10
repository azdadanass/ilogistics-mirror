package ma.azdad.view;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ma.azdad.model.GenericBean;
import ma.azdad.service.CacheService;
import ma.azdad.service.GenericService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.PageType;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GenericView<A extends GenericBean, B extends GenericService> {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected B service;

	@Autowired
	protected SessionView sessionView;

	@Autowired
	protected CacheService cacheService;

	@Autowired
	protected FileView fileView;

	protected List<A> list1 = new ArrayList<>();
	protected List<A> list2 = new ArrayList<>();
	protected List<A> list3;
	protected List<A> list4;

	protected A model;
	protected A old;
	protected Integer id;

	protected String currentPath;

	protected String searchBean = "";

	protected String className1 = getParameterClassName();
	protected String className2 = className1.substring(0, 1).toLowerCase() + className1.substring(1);
	protected String listPage = className2 + "List.xhtml";
	protected String addEditPage = "addEdit" + className1 + ".xhtml";
	protected String viewPage = "view" + className1 + ".xhtml";

	protected PageType pageType = PageType.NONE;

	protected Integer pageIndex;

	protected long start;
	protected String username;

	protected Boolean isViewPage = false;
	protected Boolean isAddPage = false;
	protected Boolean isEditPage = false;
	protected Boolean isListPage = false;

	public void init() {
		log.info("init " + getClass().getSimpleName());
//		cacheEvictView();
		start();
		currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		initParameters();

		if (("/" + listPage).equals(currentPath))
			pageType = PageType.LIST;
		else if (("/" + addEditPage).equals(currentPath) && id == null)
			pageType = PageType.ADD;
		else if (("/" + addEditPage).equals(currentPath) && id != null)
			pageType = PageType.EDIT;
		else if (("/" + viewPage).equals(currentPath))
			pageType = PageType.VIEW;

		switch (pageType) {
		case ADD:
			addPage();
			isAddPage = true;
			break;
		case EDIT:
			editPage();
			isEditPage = true;
			break;
		case VIEW:
			viewPage();
			isViewPage = true;
			break;
		case LIST:
			isListPage = true;
			break;
		default:
			break;
		}
		username = sessionView.getUsername();
		refreshList();
	}

	public void redirect() {
		if (canAccess())
			return;
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(ec.getRequestContextPath() + "ad.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Boolean isPage(String page) {
		return ("/" + page + ".xhtml").equals(currentPath);
	}

	protected Boolean canAccess() {
		return true;
	}

	public void initLists(List<A> list) {
		list2 = list1 = list;
	}

	public void refreshList() {
		if (isListPage)
			initLists(service.findAll());
	}

	protected void refreshModel(A a) {
		model = (A) service.findOne(a.id());
	}

	protected void addPage() {
		ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
		Class<A> type = (Class<A>) superClass.getActualTypeArguments()[0];
		try {
			model = type.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void editPage() {
		model = (A) service.findOne(id);
		cacheEvict();
		old = (A) service.findOne(id);
		System.err.println(model == old);
	}

	protected void viewPage() {
		model = (A) service.findOne(id);
	}

	protected void initParameters() {
		id = UtilsFunctions.getIntegerParameter("id");
		pageIndex = UtilsFunctions.getIntegerParameter("pageIndex");
	}

	protected void filterBean(String query) {
		list3 = null;
		List<A> list = new ArrayList<A>();
		query = query.toLowerCase().trim();
		for (A bean : list1) {
			if (bean.filter(query))
				list.add(bean);
		}
		list2 = list;
	}

	public Integer getRowsNumber() {
		if (list3 != null)
			return list3.size();
		else
			return list2.size();
	}

	public Integer getSelectedRowsNumber() {
		if (list4 != null)
			return list4.size();
		return 0;
	}

	public String addParameters(String path, String... tab) {
		path += "?" + tab[0];
		if (tab.length > 1)
			for (int i = 1; i < tab.length; i++)
				path += "&" + tab[i];
		return path;
	}

	public void execJavascript(String script) {
		RequestContext.getCurrentInstance().execute(script);
	}

	public String cacheEvict() {
		service.cacheEvict();
//		cacheEvictService();
//		cacheEvictView();
		return addParameters(currentPath, "faces-redirect=true", "id=" + id, "pageIndex=" + pageIndex);
	}

//	public void cacheEvictService() {
//		service.cacheEvict();
//	}

//	public void cacheEvictView() {
//		cacheService.evictCachePrefix(getClassName2() + "View");
//	}

	public void start() {
		start = System.currentTimeMillis();
	}

	public void time() {
		log.info("time: " + Long.toString(System.currentTimeMillis() - start) + " ms");
	}

	public String getParameterClassName() {
		return ((Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public List<A> getList1() {
		return list1;
	}

	public void setList1(List<A> list1) {
		this.list1 = list1;
	}

	public List<A> getList2() {
		return list2;
	}

	public void setList2(List<A> list2) {
		this.list2 = list2;
	}

	public List<A> getList3() {
		return list3;
	}

	public void setList3(List<A> list3) {
		this.list3 = list3;
	}

	public List<A> getList4() {
		return list4;
	}

	public void setList4(List<A> list4) {
		this.list4 = list4;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		model = (A) service.findOne(id);
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public String getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;
		filterBean(searchBean);
	}

	public Logger getLog() {
		return log;
	}

	public String getClassName1() {
		return className1;
	}

	public void setClassName1(String className1) {
		this.className1 = className1;
	}

	public String getClassName2() {
		return className2;
	}

	public void setClassName2(String className2) {
		this.className2 = className2;
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

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Boolean getIsViewPage() {
		return isViewPage;
	}

	public Boolean getIsAddPage() {
		return isAddPage;
	}

	public Boolean getIsEditPage() {
		return isEditPage;
	}

	public Boolean getIsListPage() {
		return isListPage;
	}

}
