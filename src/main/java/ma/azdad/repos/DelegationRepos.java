package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Delegation;

@Repository
public interface DelegationRepos extends JpaRepository<Delegation, Integer> {

	@Query("select a.project.id  from DelegationDetail a,Delegation b where a.delegation.id = b.id and b.delegate.username = :username and b.status = :delegationStatus and a.type = :delegationDetailType group by a.project.id")
	public List<Integer> findDelegatedProjects(@Param("username") String username, @Param("delegationStatus") String delegationStatus, @Param("delegationDetailType") String delegationDetailType);

}
