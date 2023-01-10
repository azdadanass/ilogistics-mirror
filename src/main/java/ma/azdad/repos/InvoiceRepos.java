package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Invoice;

@Repository
public interface InvoiceRepos extends JpaRepository<Invoice, Integer> {
	
	String c1 = "select new Invoice(a.invoice.id,a.invoice.numero,a.invoice.date,a.invoice.dueDate,a.invoice.status,a.invoice.amountHt,a.invoice.amountTtc,a.invoice.amountHt1,a.invoice.amountTtc1,a.invoice.amountHt2,Double amountTtc2,a.invoice.deductedHt,a.invoice.deductedTtc,a.invoice.paidTtc,a.invoice.madConversionRate,a.invoice.company.name,a.invoice.currency.name,(select b.name from Customer b where b.id = a.invoice.customer.id),(select b.name from Supplier b where b.id = a.invoice.supplier.id))"; 

	@Query(c1 + "from Acceptance a where a.ibuy is true and a.oldInvoiceTerm.po.id = ?1")
	List<Invoice> findByPo(Integer poId);
}

