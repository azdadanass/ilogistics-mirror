package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Invoice;
import ma.azdad.repos.InvoiceRepos;
import ma.azdad.service.InvoiceService;

@ManagedBean
@Component
@Scope("view")
public class InvoiceView extends GenericView<Integer, Invoice, InvoiceRepos, InvoiceService> {

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

	@Override
	public void refreshList() {
		if ("/viewPo.xhtml".equals(currentPath))
			initLists(service.findByPo(id));
	}
	
	public Double getList2TotalAmountTtc2Mad() {
		return list2.stream().mapToDouble(i -> i.getAmountTtc2() * i.getMadConversionRate()).sum();
	}

	// getters & setters
	public Invoice getModel() {
		return model;
	}

	public void setModel(Invoice model) {
		this.model = model;
	}

}
