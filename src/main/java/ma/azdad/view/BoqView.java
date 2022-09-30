package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.Boq;
import ma.azdad.repos.BoqRepos;
import ma.azdad.service.BoqService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class BoqView extends GenericView<Integer, Boq, BoqRepos, BoqService> {

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		switch (currentPath) {
		case "/viewPo.xhtml":
			initLists(service.findByPo(id));
			break;
		}
	}

	public Double getList1TotalDeliveredPrice() {
		return list1.stream().mapToDouble(i -> i.getDeliveredQuantity() * i.getUnitPrice()).sum();
	}

	public Double getList1TotalRemainingPrice() {
		return list1.stream().mapToDouble(i -> (i.getTotalQuantity() - i.getDeliveredQuantity()) * i.getUnitPrice()).sum();
	}

	public Double getList2TotalPrice() {
		return list2.stream().mapToDouble(i -> i.getTotalPrice() * i.getPoDetailQuantity()).sum();
	}

	// save
	public Boolean canSave() {
		if (getIsAddPage() || getIsListPage())
			return true;
		if (getIsEditPage() || getIsViewPage())
			return true;
		return false;
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		return true;
	}

	// delete
	public Boolean canDelete() {
		return true;
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			service.delete(model);
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	// generic
	@Cacheable("boqView.findDeliveredSummaryByPo")
	public List<Boq> findDeliveredSummaryByPo(Integer poId) {
		return service.findDeliveredSummaryByPo(poId);
	}

	@Cacheable("boqView.findBoqSummaryByPo")
	public List<Boq> findBoqSummaryByPo(Integer poId){
		return service.findBoqSummaryByPo(poId);
	}
	// getters & setters
	public Boq getModel() {
		return model;
	}

	public void setModel(Boq model) {
		this.model = model;
	}

}
