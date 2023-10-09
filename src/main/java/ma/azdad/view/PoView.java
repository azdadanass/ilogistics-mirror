package ma.azdad.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Po;
import ma.azdad.model.GoodsDeliveryStatus;
import ma.azdad.model.PoFile;
import ma.azdad.service.PoService;
import ma.azdad.service.UtilsFunctions;

@ManagedBean
@Component
@Scope("view")
public class PoView {

	@Autowired
	private PoService service;

	@Autowired
	private SessionView sessionView;

	@Autowired
	private CacheView cacheView;
	
	@Autowired
	private MenuView menuView;

	private String currentPath;
	private Integer id;
	private Integer companyId;

	private Po model;
	private List<Po> list1 = new ArrayList<>();
	private List<Po> list2 = new ArrayList<>();
	private String searchBean = "";

	private Boolean ibuy = true;

	private GoodsDeliveryStatus goodsDeliveryStatus;

	@PostConstruct
	public void init() {
		currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		id = UtilsFunctions.getIntegerParameter("id");
		companyId = menuView.getCompanyId();
		switch (currentPath) {
		case "/poList.xhtml":
			refreshList();
			break;
		case "/viewPo.xhtml":
			model = service.findOne(id);
			break;
		default:
			break;
		}

	}

	public void refreshList() {
		if ("/poList.xhtml".equals(currentPath))
			list2 = list1 = service.find(ibuy, companyId, sessionView.getUsername(), cacheView.getAssignedProjectList(), goodsDeliveryStatus);
	}

	private void filterBean(String query) {
		List<Po> list = new ArrayList<>();
		query = query.toLowerCase().trim();
		for (Po bean : list1) {
			if (bean.filter(query))
				list.add(bean);
		}
		list2 = list;
	}

	// generic
	
	public List<PoFile> findFileList(Integer id) {
		return service.findFileList(id);
	}

	// getters & setters

	public Integer getRowsNumber() {
		return list2.size();
	}

	public String getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;
		filterBean(searchBean);
	}

	public List<Po> getList1() {
		return list1;
	}

	public void setList1(List<Po> list1) {
		this.list1 = list1;
	}

	public List<Po> getList2() {
		return list2;
	}

	public void setList2(List<Po> list2) {
		this.list2 = list2;
	}

	public Boolean getIbuy() {
		return ibuy;
	}

	public void setIbuy(Boolean ibuy) {
		this.ibuy = ibuy;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Po getModel() {
		return model;
	}

	public void setModel(Po model) {
		this.model = model;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public GoodsDeliveryStatus getGoodsDeliveryStatus() {
		return goodsDeliveryStatus;
	}

	public void setGoodsDeliveryStatus(GoodsDeliveryStatus goodsDeliveryStatus) {
		this.goodsDeliveryStatus = goodsDeliveryStatus;
	}

}
