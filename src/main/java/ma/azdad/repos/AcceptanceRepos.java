package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Acceptance;

@Repository
public interface AcceptanceRepos extends JpaRepository<Acceptance, Integer> {

	@Query("select count(*) from Acceptance a where a.idacceptance in (select b.acceptance.idacceptance from AppLink b where b.transportationRequest.id = ?1) and  a.idacceptance in (select b.acceptance.idacceptance from Payment b where b.dateCash is null group by b.acceptance.idacceptance) ")
	public Long countPendingAcceptances(Integer transportationRequestId);
}
