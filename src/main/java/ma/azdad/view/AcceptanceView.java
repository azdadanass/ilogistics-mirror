package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Acceptance;
import ma.azdad.repos.AcceptanceRepos;
import ma.azdad.service.AcceptanceService;

@ManagedBean
@Component
@Scope("view")
public class AcceptanceView extends GenericView<Integer, Acceptance, AcceptanceRepos, AcceptanceService> {

	@PostConstruct
	public void init() {
		super.init();
	}

	public void refreshList() {
		if ("/viewPo.xhtml".equals(currentPath)) 
			list1 = service.findByPo(id);
	}

	public void refreshAcceptance(Integer acceptanceId) {
		model = service.findOne(acceptanceId);
	}
	
	public Double getList1TotalAmount() {
		return list1.stream().mapToDouble(i -> i.getAmountHt()).sum();
	}

	// getters & setters
	public Acceptance getModel() {
		return model;
	}

	public void setModel(Acceptance model) {
		this.model = model;
	}

}