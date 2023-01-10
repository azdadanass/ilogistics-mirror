package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Invoice;
import ma.azdad.repos.InvoiceRepos;

@Component
public class InvoiceService extends GenericService<Integer, Invoice, InvoiceRepos> {

	@Cacheable("invoiceService.findAll")
	public List<Invoice> findAll() {
		return repos.findAll();
	}

	public Invoice findOne(Integer id) {
		Invoice invoice = super.findOne(id);

		return invoice;
	}

	@Cacheable("invoiceService.findByPo")
	public List<Invoice> findByPo(Integer poId) {
		return repos.findByPo(poId);
	}
}
