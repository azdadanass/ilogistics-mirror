package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.IbuyPayment;

@Repository
public interface IbuyPaymentRepos extends JpaRepository<IbuyPayment, Integer> {
	
	String c1 = "select distinct new IbuyPayment(a.ibuyPayment.id,a.ibuyPayment.reference,a.ibuyPayment.mode,a.ibuyPayment.paymentDate,a.ibuyPayment.cashDate,a.ibuyPayment.amount,a.ibuyPayment.isMapped,a.ibuyPayment.isLc,a.ibuyPayment.hasLc,a.ibuyPayment.countFiles,a.ibuyPayment.madConversionRate,a.ibuyPayment.status,a.ibuyPayment.supplier.name,a.ibuyPayment.bankAccount.fullName,a.ibuyPayment.currency.name) ";
	
	@Query(c1 + "from Payment a where a.acceptance.oldInvoiceTerm.po.id = ?1")
	List<IbuyPayment> findByPo(Integer poId);

}

