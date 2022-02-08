package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.PartNumberPricing;

@Repository
public interface PartNumberPricingRepos extends JpaRepository<PartNumberPricing, Integer> {

	String partNumberCategoryName = "(select c.name from PartNumberType b,PartNumberCategory c where b.category.id = c.id and a.partNumber.partNumberType.id = b.id)";
	String partNumberTypeName = "(select b.name from PartNumberType b where b.id = a.partNumber.partNumberType.id)";
	String partNumberBrandName = "(select b.name from PartNumberBrand b where b.id = a.partNumber.brand.id)";

	String c1 = "select new PartNumberPricing(a.id,a.baseLineCost,a.baseLinePrice,a.physicalQuantity,a.pendingQuantity,a.countFiles,a.currency.currency," //
			+ "a.partNumber.id,a.partNumber.name,a.partNumber.description," + partNumberCategoryName + "," + partNumberTypeName + "," + partNumberBrandName + ",a.company.name) ";

	@Query(c1 + "from PartNumberPricing a")
	List<PartNumberPricing> findLight();

	@Modifying
	@Query("update PartNumberPricing a set a.physicalQuantity = COALESCE((select sum(b.quantity) from StockRow b where b.partNumber.id = a.partNumber.id and b.deliveryRequest.project.type = 'Stock' and b.deliveryRequest.type in ('INBOUND','OUTBOUND') and b.deliveryRequest.project.costcenter.lob.bu.company.id = ?1),0) where a.company.id = ?1")
	void updatePhysicalQuantity(Integer companyId);

	@Modifying
	@Query("update PartNumberPricing a set a.pendingQuantity = COALESCE((select sum(b.quantity) from DeliveryRequestDetail b where b.partNumber.id = a.partNumber.id and b.deliveryRequest.project.type = 'Stock' and b.deliveryRequest.type = 'OUTBOUND' and b.deliveryRequest.project.costcenter.lob.bu.company.id = ?1 and b.deliveryRequest.status in (?2)),0) where a.company.id = ?1")
	void updatePendingQuantity(Integer companyId, List<DeliveryRequestStatus> pendingDnStatusList);
}
