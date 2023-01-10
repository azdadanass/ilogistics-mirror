package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Acceptance;

@Repository
public interface AcceptanceRepos extends JpaRepository<Acceptance, Integer> {
	
	String c1 = "select new Acceptance(a.id,a.date,a.status,a.numero,a.amountHt,a.amountTtc2,a.oldInvoiceTerm.po.numero,a.oldInvoiceTerm.po.project.name,a.oldInvoiceTerm.po.currency.name,a.oldInvoiceTerm.phase,a.oldInvoiceTerm.po.project.customer.name,(select b.name from Supplier b where b.id = a.oldInvoiceTerm.po.supplier.id))";
	
	@Query(c1 + "from Acceptance a where a.oldInvoiceTerm.po.id = ?1")
	List<Acceptance> findByPo(Integer poId);

	@Query("select count(*) from Acceptance a where a.id in (select b.acceptance.id from AppLink b where b.transportationRequest.id = ?1) and  a.id in (select b.acceptance.id from Payment b where b.dateCash is null group by b.acceptance.id) ")
	public Long countPendingAcceptances(Integer transportationRequestId);
}
