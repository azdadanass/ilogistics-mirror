package ma.azdad.repos;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Issue;
import ma.azdad.model.IssueStatus;

@Repository
public interface IssueRepos extends JpaRepository<Issue, Integer> {

	String c1 = "select new Issue(a.id,a.status,a.severity,a.category,a.type,a.blocking,a.ownershipType" + //
			",a.company.id,(select b.name from Company b where b.id = a.company.id)" + //
			",a.customer.id,(select b.name from Customer b where b.id = a.customer.id)" + //
			",a.supplier.id,(select b.name from Supplier b where b.id = a.supplier.id)" + //
			",a.deliveryRequest.id,a.deliveryRequest.reference,a.deliveryRequest.type,a.deliveryRequest.status" + //
			",a.deliveryRequest.project.id,a.deliveryRequest.project.name) ";

	@Query(c1 + "from Issue a where a.deliveryRequest.project.id = ?1")
	public List<Issue> findByProject(Integer projectId);

	@Query(c1 + "from Issue a where a.deliveryRequest.project.id in (?1)")
	public List<Issue> findByProject(List<Integer> projectId);

	@Query(c1
			+ "from Issue a where  (a.deliveryRequest.project.manager.username = ?2 or a.deliveryRequest.project.costcenter.lob.manager.username = ?2 or a.deliveryRequest.project.costcenter.lob.bu.director.username = ?2 or a.deliveryRequest.project.id in (?3) or a.deliveryRequest.project.costcenter.lob.id in (?4)) ")
	public List<Issue> findDeliveryRequestIssueListByUser(String username, Collection<Integer> projectIdList, Collection<Integer> lobIdList);

	@Query("select distinct a.deliveryRequest.project.id from Issue a where  (a.deliveryRequest.project.manager.username = ?2 or a.deliveryRequest.project.costcenter.lob.manager.username = ?2 or a.deliveryRequest.project.costcenter.lob.bu.director.username = ?2 or a.deliveryRequest.project.id in (?3) or a.deliveryRequest.project.costcenter.lob.id in (?4))   ")
	public List<Integer> findProjectIdList(String username, Collection<Integer> projectIdList, Collection<Integer> lobIdList);

	@Query(c1 + "from Issue a where a.confirmator.username = ?1 and a.status = 'SUBMITTED'")
	List<Issue> findToConfirm(String username);

	@Query("select count(*) from Issue a where a.confirmator.username = ?1 and a.status = 'SUBMITTED'")
	Long countToConfirm(String username);

	@Query(c1 + "from Issue a where a.assignator.username = ?1 and a.status = 'CONFIRMED'")
	List<Issue> findToAssign(String username);

	@Query("select count(*) from Issue a where a.assignator.username = ?1 and a.status = 'CONFIRMED'")
	Long countToAssign(String username);

	@Query(c1 + "from Issue a where a.ownershipUser.username = ?1 and a.status = ?2")
	List<Issue> findToResolve(String username, IssueStatus assigned);

	@Query("select count(*) from Issue a where a.ownershipUser.username = ?1 and a.status = ?2")
	Long countToResolve(String username, IssueStatus assigned);

	@Query(c1 + "from Issue a where a.user3.username = ?1 and a.status = ?2")
	List<Issue> findToAcknowledge(String username, IssueStatus assigned);

	@Query("select count(*) from Issue a where a.user3.username = ?1 and a.status = ?2")
	Long countToAcknowledge(String username, IssueStatus assigned);

}
