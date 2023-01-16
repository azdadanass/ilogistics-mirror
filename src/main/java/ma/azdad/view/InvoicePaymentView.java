package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.InvoicePayment;
import ma.azdad.repos.InvoicePaymentRepos;
import ma.azdad.service.InvoicePaymentService;

@ManagedBean
@Component
@Scope("view")
public class InvoicePaymentView extends GenericView<Integer, InvoicePayment, InvoicePaymentRepos, InvoicePaymentService> {

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
	public InvoicePayment getModel() {
		return model;
	}

	public void setModel(InvoicePayment model) {
		this.model = model;
	}

}
