package ma.azdad.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.CostType;
import ma.azdad.model.Po;
import ma.azdad.model.PoBoqStatus;
import ma.azdad.model.PoDeliveryStatus;
import ma.azdad.model.PoStatus;
import ma.azdad.model.RevenueType;

@Repository
public interface PoRepos extends JpaRepository<Po, Integer> {
	

	@Query("from Po a where a.type = ?1 and a.project.id = ?2 and a.boqStatus is not null and a.boqStatus  != ?3  and a.status not in (?4)")
	public List<Po> findByTypeAndProjectAndNotBoqStatus(String type, Integer projectId, PoBoqStatus boqStatus, List<PoStatus> notInStatus);

	@Query("select new Po(id,numeroInvoice) from Po where id in (?1)")
	public List<Po> findLight(List<Integer> idList);

	@Query("select ibuy from Po where id = ?1")
	public Boolean getIbuy(Integer id);

	@Query("select boqStatus from Po where id = ?1")
	public PoBoqStatus getBoqStatus(Integer id);

	@Modifying
	@Query("update Po set boqStatus = ?2 where id = ?1 ")
	public void updateBoqStatus(Integer poId, PoBoqStatus boqStatus);

	@Modifying
	@Query("update Po set deliveryStatus = ?2 where id = ?1 ")
	public void updateDeliveryStatus(Integer poId, PoDeliveryStatus deliveryStatus);

	@Query("select distinct a.po.id  from Podetails a where a.revenueType  =?1 ")
	public Set<Integer> findPoIdListContainingGoodsSupply(RevenueType revenueTypeGoodsSupply);

	@Query("select distinct a.po.id  from Podetails a where a.costType  =?1 ")
	public Set<Integer> findPoIdListContainingProjectGoodsPurchase(CostType projectGoodsPurchase);
	
	// c1
	@Query("select distinct new Po(a.id,a.ibuy,a.numero,a.date,a.amountHt,a.status,a.boqStatus,a.deliveryStatus,a.currency.name,a.project.name,a.supplier.name) from Po a where a.ibuy is true and a.boqStatus is not null and a.company.id = ?1 and (a.project.manager.username = ?2 or a.project.costcenter.lob.manager.username = ?2 or a.project.customer.manager.username = ?2 or a.project.id in (?3)) order by date desc")
	List<Po> findSupplierPoList(Integer companyId,String username,List<Integer> assignedProjectList);
	
	@Query("select distinct new Po(a.id,a.ibuy,a.numero,a.date,a.amountHt,a.status,a.boqStatus,a.deliveryStatus,a.currency.name,a.project.name,a.project.customer.name) from Po a where a.ibuy is false and a.boqStatus is not null and a.company.id = ?1 and (a.project.manager.username = ?2 or a.project.costcenter.lob.manager.username = ?2 or a.project.customer.manager.username = ?2 or a.project.id in (?3)) order by date desc")
	List<Po> findCustomerPoList(Integer companyId,String username,List<Integer> assignedProjectList);

}
