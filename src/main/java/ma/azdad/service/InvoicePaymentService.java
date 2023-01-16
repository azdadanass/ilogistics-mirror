package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.InvoicePayment;
import ma.azdad.repos.InvoicePaymentRepos;

@Component
public class InvoicePaymentService extends GenericService<Integer, InvoicePayment, InvoicePaymentRepos> {

	@Cacheable("invoicePaymentService.findAll")
	public List<InvoicePayment> findAll() {
		return repos.findAll();
	}

	public InvoicePayment findOne(Integer id) {
		InvoicePayment invoicePayment = super.findOne(id);

		return invoicePayment;
	}

	@Cacheable("invoicePaymentService.findByPo")
	public List<InvoicePayment> findByPo(Integer poId) {
		return repos.findByPo(poId);
	}

}
