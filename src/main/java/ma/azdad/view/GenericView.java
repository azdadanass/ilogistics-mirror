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
import org.springframework.data.jpa.repository.JpaRepository;

import ma.azdad.model.GenericModel;
import ma.azdad.service.CacheService;
import ma.azdad.service.GenericService;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class GenericView<K, M extends GenericModel<K>, R extends JpaRepository<M, K>, S extends GenericService<K, M, R>> {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected S service;

	@Autowired
	protected SessionView sessionView;

	@Autowired
	protected CacheService cacheService;

	@Autowired
	protected FileView fileView;

	protected List<M> list1 = new ArrayList<>();
	protected List<M> list2 = new ArrayList<>();
	protected List<M> list3;
	protected List<M> list4;

	protected M model;
	protected M old;
	protected K id;

	protected String currentPath;
	protected String searchBean = "";
	protected Integer pageIndex;

	protected long start;

	protected Boolean isViewPage = false;
	protected Boolean isAddPage = false;
	protected Boolean isEditPage = false;
	protected Boolean isListPage = false;

	protected String listPage = getListPage();
	protected String addEditPage = getAddEditPage();
	protected String viewPage = getViewPage();

	protected String username;

	public void init() {
		log.info("init " + getClass().getSimpleName());
		start();
		currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		initParameters();

		isListPage = getIsListPage();
		isAddPage = getIsAddPage();
		isEditPage = getIsEditPage();
		isViewPage = getIsViewPage();

		username = sessionView.getUsername();

		if (isListPage)
			listPage();
		else if (isAddPage)
			addPage();
		else if (isEditPage)
			editPage();
		else if (isViewPage)
			viewPage();

		refreshList();
	}

	public void listPage() {
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

	public void initLists(List<M> list) {
		list2 = list1 = list;
	}

	public void refreshList() {
		if (isListPage)
			initLists(service.findAll());
	}

	public void refreshModel(M a) {
		model = service.findOne(a.id());
	}

	public void refreshModel(K id) {
		model = service.findOne(id);
	}

	protected void addPage() {
		ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
		Class<M> type = (Class<M>) superClass.getActualTypeArguments()[1];
		try {
			model = type.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void editPage() {
		model = service.findOne(id);
		evictCache();
		old = service.findOne(id);
	}

	protected void viewPage() {
		model = service.findOne(id);
	}

	protected String getParameter(String name) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}

	protected Object getParameter(String name, Class keyClass) {
		try {
			if (String.class.equals(keyClass))
				return getParameter(name);
			else if (Integer.class.equals(keyClass))
				return Integer.valueOf(getParameter(name));
			else if (Double.class.equals(keyClass))
				return Integer.valueOf(getParameter(name));
			else if (Boolean.class.equals(keyClass))
				return "true".equals(getParameter(name));
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	protected Integer getIntegerParameter(String name) {
		return (Integer) getParameter(name, Integer.class);
	}

	protected void initParameters() {
		getParameter("test", getKeyClass());
		id = (K) getParameter("id", getKeyClass());
		pageIndex = getIntegerParameter("pageIndex");
	}

	protected void filterBean(String query) {
		list3 = null;
		List<M> list = new ArrayList<M>();
		query = query.toLowerCase().trim();
		for (M bean : list1) {
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

	public void hideDialog(String dlgName) {
		execJavascript("PF('" + dlgName + "').hide()");
	}

	public void showDialog(String dlgName) {
		execJavascript("PF('" + dlgName + "').show()");
	}

	public String evictCache() {
		service.evictCache();
		return addParameters(currentPath, "faces-redirect=true", "id=" + id, "pageIndex=" + pageIndex);
	}

	public void start() {
		start = System.currentTimeMillis();
	}

	public void time() {
		log.info("time: " + Long.toString(System.currentTimeMillis() - start) + " ms");
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public List<M> getList1() {
		return list1;
	}

	public void setList1(List<M> list1) {
		this.list1 = list1;
	}

	public List<M> getList2() {
		return list2;
	}

	public void setList2(List<M> list2) {
		this.list2 = list2;
	}

	public List<M> getList3() {
		return list3;
	}

	public void setList3(List<M> list3) {
		this.list3 = list3;
	}

	public List<M> getList4() {
		return list4;
	}

	public void setList4(List<M> list4) {
		this.list4 = list4;
	}

	public K getId() {
		return id;
	}

	public void setId(K id) {
		this.id = id;
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

	public Class getKeyClass() {
		return ((Class<K>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	public String getModelName1() {
		String viewClassName = getClass().getSimpleName();
		return viewClassName.substring(0, viewClassName.lastIndexOf("View"));
	}

	public String getModelName2() {
		return getModelName1().substring(0, 1).toLowerCase() + getModelName1().substring(1);
	}

	public String getClassName1() {
		return getModelName1();
//		return ((Class<M>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getSimpleName();
	}

	public String getClassName2() {
		return getModelName2();
//		return getClassName1().substring(0, 1).toLowerCase() + getClassName1().substring(1);
	}

	public String getListPage() {
		return getClassName2() + "List.xhtml";
	}

	public String getAddEditPage() {
		return "addEdit" + getClassName1() + ".xhtml";
	}

	public String getViewPage() {
		return "view" + getClassName1() + ".xhtml";
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Boolean getIsViewPage() {
		return ("/" + getViewPage()).equals(currentPath);
	}

	public Boolean getIsAddPage() {
		return ("/" + getAddEditPage()).equals(currentPath) && id == null;
	}

	public Boolean getIsEditPage() {
		return ("/" + getAddEditPage()).equals(currentPath) && id != null;
	}

	public Boolean getIsListPage() {
		return ("/" + getListPage()).equals(currentPath);
	}

}
