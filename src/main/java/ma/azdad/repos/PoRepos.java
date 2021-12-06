package ma.azdad.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.CostType;
import ma.azdad.model.Po;
import ma.azdad.model.PoDeliveryStatus;
import ma.azdad.model.PoStatus;
import ma.azdad.model.RevenueType;

@Repository
public interface PoRepos extends JpaRepository<Po, Integer> {

	@Query("from Po a where a.type = ?1 and a.project.id = ?2 and a.deliveryStatus is not null and a.deliveryStatus  != ?3  and a.status not in (?4)")
	public List<Po> findByTypeAndProjectAndNotDeliveryStatus(String type, Integer projectId, PoDeliveryStatus deliveryStatus, List<PoStatus> notInStatus);

	@Query("select new Po(idpo,numeroInvoice) from Po where idpo in (?1)")
	public List<Po> findLight(List<Integer> idList);

	@Query("select ibuy from Po where id = ?1")
	public Boolean getIbuy(Integer id);

	@Modifying
	@Query("update Po set deliveryStatus = ?2 where idpo = ?1 ")
	public void updateDeliveryStatus(Integer poId, PoDeliveryStatus deliveryStatus);

	@Query("select distinct a.po.idpo  from Podetails a where a.revenueType  =?1 ")
	public Set<Integer> findPoIdListContainingGoodsSupply(RevenueType revenueTypeGoodsSupply);

	@Query("select distinct a.po.idpo  from Podetails a where a.costType  =?1 ")
	public Set<Integer> findPoIdListContainingProjectGoodsPurchase(CostType projectGoodsPurchase);
}
