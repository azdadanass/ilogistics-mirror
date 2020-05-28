package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Expensepayment;
import ma.azdad.service.ExpensepaymentService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class ExpensepaymentView {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public ExpensepaymentService expensepaymentService;
	public Expensepayment expensepayment;

	@PostConstruct
	public void init() {

	}

	public void refreshExpensepayment(Integer expensepaymentId) {
		expensepayment = expensepaymentService.findOne(expensepaymentId);
	}

	public Expensepayment getExpensepayment() {
		return expensepayment;
	}

	public void setExpensepayment(Expensepayment expensepayment) {
		this.expensepayment = expensepayment;
	}

}