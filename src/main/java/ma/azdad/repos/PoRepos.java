package ma.azdad.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.CostType;
import ma.azdad.model.Po;
import ma.azdad.model.PoIlogisticsStatus;
import ma.azdad.model.GoodsDeliveryStatus;
import ma.azdad.model.PoFile;
import ma.azdad.model.PoStatus;
import ma.azdad.model.RevenueType;

@Repository
public interface PoRepos extends JpaRepository<Po, Integer> {
	

	@Query("from Po a where a.type = ?1 and a.project.id = ?2 and a.ilogisticsStatus is not null and a.ilogisticsStatus  != ?3  and a.status not in (?4)")
	public List<Po> findByTypeAndProjectAndNotIlogisticsStatus(String type, Integer projectId, PoIlogisticsStatus ilogisticsStatus, List<PoStatus> notInStatus);

	@Query("select new Po(id,numeroInvoice) from Po where id in (?1)")
	public List<Po> findLight(List<Integer> idList);

	@Query("select ibuy from Po where id = ?1")
	public Boolean getIbuy(Integer id);

	@Query("select ilogisticsStatus from Po where id = ?1")
	public PoIlogisticsStatus getIlogisticsStatus(Integer id);

	@Modifying
	@Query("update Po set ilogisticsStatus = ?2 where id = ?1 ")
	public void updateIlogisticsStatus(Integer poId, PoIlogisticsStatus ilogisticsStatus);

	@Modifying
	@Query("update Po set goodsDeliveryStatus = ?2 where id = ?1 ")
	public void updateGoodsDeliveryStatus(Integer poId, GoodsDeliveryStatus goodsDeliveryStatus);

	@Query("select distinct a.po.id  from Podetails a where a.revenueType  =?1 ")
	public Set<Integer> findPoIdListContainingGoodsSupply(RevenueType revenueTypeGoodsSupply);

	@Query("select distinct a.po.id  from Podetails a where a.costType  =?1 ")
	public Set<Integer> findPoIdListContainingProjectGoodsPurchase(CostType projectGoodsPurchase);
	
	
	@Query("from PoFile where parent.id = ?1")
	List<PoFile> findFileList(Integer id);
	
	// c1
	@Query("select distinct new Po(a.id,a.ibuy,a.numero,a.date,a.amountHt,a.status,a.ilogisticsStatus,a.goodsDeliveryStatus,a.currency.name,a.project.name,a.supplier.name) from Po a where a.ibuy is true and a.ilogisticsStatus is not null and a.company.id = ?1 and (a.project.manager.username = ?2 or a.project.costcenter.lob.manager.username = ?2 or a.project.customer.manager.username = ?2 or a.project.id in (?3)) order by date desc")
	List<Po> findSupplierPoList(Integer companyId,String username,List<Integer> assignedProjectList);
	
	@Query("select distinct new Po(a.id,a.ibuy,a.numero,a.date,a.amountHt,a.status,a.ilogisticsStatus,a.goodsDeliveryStatus,a.currency.name,a.project.name,a.supplier.name) from Po a where a.ibuy is true and a.ilogisticsStatus is not null and a.company.id = ?1 and (a.project.manager.username = ?2 or a.project.costcenter.lob.manager.username = ?2 or a.project.customer.manager.username = ?2 or a.project.id in (?3)) and a.goodsDeliveryStatus = ?4 order by date desc")
	List<Po> findSupplierPoListByGoodsDeliveryStatus(Integer companyId,String username,List<Integer> assignedProjectList,GoodsDeliveryStatus goodsDeliveryStatus);
	
	@Query("select distinct new Po(a.id,a.ibuy,a.numero,a.date,a.amountHt,a.status,a.ilogisticsStatus,a.goodsDeliveryStatus,a.currency.name,a.project.name,a.supplier.name) from Po a where a.ibuy is true and a.ilogisticsStatus is not null and a.company.id = ?1 and (a.project.manager.username = ?2 or a.project.costcenter.lob.manager.username = ?2 or a.project.customer.manager.username = ?2 or a.project.id in (?3)) and a.goodsDeliveryStatus is null order by date desc")
	List<Po> findSupplierPoListByGoodsDeliveryStatusNull(Integer companyId,String username,List<Integer> assignedProjectList);
	
	@Query("select distinct new Po(a.id,a.ibuy,a.numero,a.date,a.amountHt,a.status,a.ilogisticsStatus,a.goodsDeliveryStatus,a.currency.name,a.project.name,a.project.customer.name) from Po a where a.ibuy is false and a.ilogisticsStatus is not null and a.company.id = ?1 and (a.project.manager.username = ?2 or a.project.costcenter.lob.manager.username = ?2 or a.project.customer.manager.username = ?2 or a.project.id in (?3)) order by date desc")
	List<Po> findCustomerPoList(Integer companyId,String username,List<Integer> assignedProjectList);
	
	@Query("select distinct new Po(a.id,a.ibuy,a.numero,a.date,a.amountHt,a.status,a.ilogisticsStatus,a.goodsDeliveryStatus,a.currency.name,a.project.name,a.project.customer.name) from Po a where a.ibuy is false and a.ilogisticsStatus is not null and a.company.id = ?1 and (a.project.manager.username = ?2 or a.project.costcenter.lob.manager.username = ?2 or a.project.customer.manager.username = ?2 or a.project.id in (?3)) and a.goodsDeliveryStatus = ?4 order by date desc")
	List<Po> findCustomerPoListByGoodsDeliveryStatus(Integer companyId,String username,List<Integer> assignedProjectList,GoodsDeliveryStatus goodsDeliveryStatus);
	
	@Query("select distinct new Po(a.id,a.ibuy,a.numero,a.date,a.amountHt,a.status,a.ilogisticsStatus,a.goodsDeliveryStatus,a.currency.name,a.project.name,a.project.customer.name) from Po a where a.ibuy is false and a.ilogisticsStatus is not null and a.company.id = ?1 and (a.project.manager.username = ?2 or a.project.costcenter.lob.manager.username = ?2 or a.project.customer.manager.username = ?2 or a.project.id in (?3)) and a.goodsDeliveryStatus is null order by date desc")
	List<Po> findCustomerPoListByGoodsDeliveryStatusNull(Integer companyId,String username,List<Integer> assignedProjectList);

}
