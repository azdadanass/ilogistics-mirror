package ma.azdad.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Expensepayment;
import ma.azdad.model.ExpensepaymentStatus;
import ma.azdad.repos.ExpensepaymentRepos;

@Component
@Transactional
public class ExpensepaymentService {

	@Autowired
	private ExpensepaymentRepos expensepaymentRepos;

	public Expensepayment findOne(Integer id) {
		Expensepayment expensepayment = expensepaymentRepos.findById(id).get();
		Hibernate.initialize(expensepayment.getInternalBeneficiary());
		Hibernate.initialize(expensepayment.getExternalBeneficiary());
		Hibernate.initialize(expensepayment.getBudgetdetail());
		if (expensepayment.getBudgetdetail() != null)
			Hibernate.initialize(expensepayment.getBudgetdetail().getBudget());
		return expensepayment;
	}

	public Long countNotAcknowledgedExpensepayments(Integer transportationRequestId) {
		Long l = expensepaymentRepos.countNotAcknowledgedExpensepayments(transportationRequestId, ExpensepaymentStatus.ACKNOWLEDGED.getValue());
		return l != null ? l : 0;
	}

}
