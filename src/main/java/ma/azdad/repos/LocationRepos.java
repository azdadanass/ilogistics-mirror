package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.CompanyType;
import ma.azdad.model.Location;
import ma.azdad.model.StockRowState;
import ma.azdad.model.ZoneHeight;

@Repository
public interface LocationRepos extends JpaRepository<Location, Integer> {

	@Query("select count(*) from Location where warehouse.id = ?1 and name = ?2")
	public Long countByWarehouseAndName(Integer warehouseId, String name);

	@Query("select count(*) from Location where warehouse.id = ?1 and name = ?2 and id != ?3")
	public Long countByWarehouseAndName(Integer warehouseId, String name, Integer id);

	@Query("select distinct a.location from LocationDetail a where " //
			+ "a.location.warehouse.id = ?1 "//
			+ "and (a.location.stockRowState is null or a.location.stockRowState = ?2) "//
			+ "and a.ownerType = ?3 "//
			+ "and (a.company.id = ?4 or a.customer.id = ?4 or a.supplier.id = ?4)")
	List<Location> findByWarehouseAndStockRowStateAndOwner(Integer warehouseId, StockRowState stockRowState, CompanyType ownerType, Integer ownerId);

	@Query("select new Location(id,name) from Location where warehouse.id = ?1")
	List<Location> findLightByWarehouse(Integer warehouseId);

	@Query("from ZoneHeight where column.line.location.id = ?1")
	List<ZoneHeight> findHeightListByLocation(Integer locationId);
	

}
