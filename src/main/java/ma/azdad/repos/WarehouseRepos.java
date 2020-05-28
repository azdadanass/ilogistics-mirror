package ma.azdad.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Warehouse;

@Repository
public interface WarehouseRepos extends JpaRepository<Warehouse, Integer> {

	@Query("select new Warehouse(id,name) from Warehouse")
	public List<Warehouse> findLight();

	@Query("select a.inboundDeliveryRequest.warehouse.id from StockRow a where a.inboundDeliveryRequest.project.id = ?1 group by a.partNumber.id,a.inboundDeliveryRequest.warehouse.id having sum(a.quantity) > 0 ")
	public List<Integer> findNonEmptyWarehouseList(Integer projectId);

	@Query("from Warehouse where id in (?1)")
	public List<Warehouse> find(Set<Integer> warehouseList);

	@Query("select distinct a.warehouse.id from WarehouseManager a where a.user.username = ?1 ")
	public List<Integer> findIdListByManager(String userUsername);
}
