package ma.azdad.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Acceptance;
import ma.azdad.service.AcceptanceService;
import ma.azdad.service.UtilsFunctions;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class AcceptanceView {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public AcceptanceService service;

	private Integer id;
	public Acceptance acceptance;

	private List<Acceptance> list1 = new ArrayList<>();
	private List<Acceptance> list2 = new ArrayList<>();
	private String searchBean = "";
	private String currentPath;

	@PostConstruct
	public void init() {
		currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		initParameters();
		refreshList();
	}

	public void refreshList() {
		if ("/viewPo.xhtml".equals(currentPath)) {
			list1 = service.findByPo(id);
			list2 = list1.stream().filter(i -> i.getIsInvoiced()).collect(Collectors.toList());
		}
	}

	protected void initParameters() {
		id = UtilsFunctions.getIntegerParameter("id");
	}

	protected void filterBean(String query) {
		List<Acceptance> list = new ArrayList<Acceptance>();
		query = query.toLowerCase().trim();
		for (Acceptance bean : list1) {
			if (bean.filter(query))
				list.add(bean);
		}
		list2 = list;
	}

	public void refreshAcceptance(Integer acceptanceId) {
		acceptance = service.findOne(acceptanceId);
	}

	public Acceptance getAcceptance() {
		return acceptance;
	}

	public void setAcceptance(Acceptance acceptance) {
		this.acceptance = acceptance;
	}

	public String getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;
		filterBean(searchBean);
	}

	public List<Acceptance> getList1() {
		return list1;
	}

	public void setList1(List<Acceptance> list1) {
		this.list1 = list1;
	}

	public List<Acceptance> getList2() {
		return list2;
	}

	public void setList2(List<Acceptance> list2) {
		this.list2 = list2;
	}

}