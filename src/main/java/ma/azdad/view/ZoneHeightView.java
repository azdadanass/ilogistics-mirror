package ma.azdad.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.StockRowDetail;
import ma.azdad.model.ZoneHeight;
import ma.azdad.repos.ZoneHeightRepos;
import ma.azdad.service.ZoneHeightService;

@ManagedBean
@Component
@Scope("view")
public class ZoneHeightView extends GenericView<Integer, ZoneHeight, ZoneHeightRepos, ZoneHeightService> {

	@Autowired
	private SessionView sessionView;

	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	public void refreshList() {
		if (isListPage)
			initDatatableList(service.findAll());
	}

	// generic
	public List<ZoneHeight> findByLocation(Integer locationId) {
		return service.findByLocation(locationId);
	}

	@Cacheable("zoneHeightView.findReferenceById")
	public String findReferenceById(Integer id) {
		return service.findReferenceById(id);
	}

	// getters & setters
	public ZoneHeight getModel() {
		return model;
	}

	public void setModel(ZoneHeight model) {
		this.model = model;
	}

}
