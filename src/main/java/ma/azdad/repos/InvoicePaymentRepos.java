package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.InvoicePayment;

@Repository
public interface InvoicePaymentRepos extends JpaRepository<InvoicePayment, Integer> {
	String c1 = "select distinct new InvoicePayment(a.invoicePayment.id,a.invoicePayment.reference,a.invoicePayment.mode,a.invoicePayment.paymentDate,a.invoicePayment.cashDate,a.invoicePayment.amount,a.invoicePayment.paidTtc,a.invoicePayment.isMapped,a.invoicePayment.countFiles,a.invoicePayment.madConversionRate,a.invoicePayment.status,a.invoicePayment.customer.name,a.invoicePayment.bankAccount.fullName,a.invoicePayment.currency.name) ";
	
	@Query(c1 + "from Payment a where a.acceptance.oldInvoiceTerm.po.id = ?1")
	List<InvoicePayment> findByPo(Integer poId);

}

