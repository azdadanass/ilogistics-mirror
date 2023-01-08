package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Acceptance;

@Repository
public interface AcceptanceRepos extends JpaRepository<Acceptance, Integer> {
	
	String c1 = "";
	
	@Query(c1 + "from Acceptance a where a.paymentterm.po.id = ?1")
	List<Acceptance> findByPo(Integer poId);

	@Query("select count(*) from Acceptance a where a.idacceptance in (select b.acceptance.idacceptance from AppLink b where b.transportationRequest.id = ?1) and  a.idacceptance in (select b.acceptance.idacceptance from Payment b where b.dateCash is null group by b.acceptance.idacceptance) ")
	public Long countPendingAcceptances(Integer transportationRequestId);
}
