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

	@Query("select new Po(idpo,numeroInvoice) from Po where idpo in (?1)")
	public List<Po> findLight(List<Integer> idList);

	@Query("select ibuy from Po where id = ?1")
	public Boolean getIbuy(Integer id);

	@Query("select boqStatus from Po where id = ?1")
	public PoBoqStatus getBoqStatus(Integer id);

	@Modifying
	@Query("update Po set boqStatus = ?2 where idpo = ?1 ")
	public void updateBoqStatus(Integer poId, PoBoqStatus boqStatus);

	@Modifying
	@Query("update Po set deliveryStatus = ?2 where idpo = ?1 ")
	public void updateDeliveryStatus(Integer poId, PoDeliveryStatus deliveryStatus);

	@Query("select distinct a.po.idpo  from Podetails a where a.revenueType  =?1 ")
	public Set<Integer> findPoIdListContainingGoodsSupply(RevenueType revenueTypeGoodsSupply);

	@Query("select distinct a.po.idpo  from Podetails a where a.costType  =?1 ")
	public Set<Integer> findPoIdListContainingProjectGoodsPurchase(CostType projectGoodsPurchase);
	
	// c1
	@Query("select distinct new Po(a.po.idpo,a.po.ibuy,a.po.numero,a.po.date,a.po.amountHt,a.po.status,a.po.boqStatus,a.po.deliveryStatus,a.po.currency.name,a.po.project.name,a.po.supplier.name) from DeliveryRequest a where a.type = 'INBOUND' and (a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.customer.manager.username = ?1 or a.project.id in (?2))")
	List<Po> findSupplierPoList(String username,List<Integer> assignedProjectList);
	
	@Query("select distinct new Po(a.po.idpo,a.po.ibuy,a.po.numero,a.po.date,a.po.amountHt,a.po.status,a.po.boqStatus,a.po.deliveryStatus,a.po.currency.name,a.po.project.name,a.po.project.customer.name) from DeliveryRequest a where a.type = 'OUTBOUND' and (a.project.manager.username = ?1 or a.project.costcenter.lob.manager.username = ?1 or a.project.customer.manager.username = ?1 or a.project.id in (?2))")
	List<Po> findCustomerPoList(String username,List<Integer> assignedProjectList);

}
