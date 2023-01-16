package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.IbuyPayment;
import ma.azdad.repos.IbuyPaymentRepos;
import ma.azdad.service.IbuyPaymentService;

@ManagedBean
@Component
@Scope("view")
public class IbuyPaymentView extends GenericView<Integer, IbuyPayment, IbuyPaymentRepos, IbuyPaymentService> {

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
		if ("/viewPo.xhtml".equals(currentPath))
			list2 = list1 = service.findByPo(id);
	}
	

	public Double getList2TotalAmount() {
		return list2.stream().mapToDouble(i -> i.getAmount()).sum();
	}

	// getters & setters
	public IbuyPayment getModel() {
		return model;
	}

	public void setModel(IbuyPayment model) {
		this.model = model;
	}

}
