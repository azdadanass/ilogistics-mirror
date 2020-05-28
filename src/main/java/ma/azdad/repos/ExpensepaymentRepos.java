package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Expensepayment;

@Repository
public interface ExpensepaymentRepos extends JpaRepository<Expensepayment, Integer> {

	@Query("select count(*) from Expensepayment a where a.idexpensepayment in (select b.expensepayment.idexpensepayment from AppLink b where b.transportationRequest.id = ?1) and a.status != ?2 ")
	public Long countNotAcknowledgedExpensepayments(Integer transportationRequestId, String expensepaymentStatusAcknowledged);

}
