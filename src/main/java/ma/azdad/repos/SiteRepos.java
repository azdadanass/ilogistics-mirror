package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Site;

@Repository
public interface SiteRepos extends JpaRepository<Site, Integer> {

	String select1 = "select new Site(a.id, a.name ,a.model,a.latitude,a.longitude, a.type, a.user,a.googleRegion,a.googleAddress,(select b.name from Customer b where a.customer.id = b.id),(select b.name from Supplier b where a.supplier.id = b.id),a.owner) ";

	@Query(select1 + "from Site a")
	public List<Site> findLight();
	
	@Query(select1 + "from Site a where (?1 is null or a.type.category.id = ?1) and (?2 is null or ?2 = 'null' or a.googleRegion = ?2)")
	public List<Site> findByCategoryAndGoogleRegion(Integer categoryId,String googleRegion);

	@Query(select1 + "from Site a where a.warehouse is not null")
	public List<Site> findLightAndHavingWarehouse();

	@Query("select new Site(id,name,latitude,longitude) from Site where id in (?1)")
	public List<Site> findLight(List<Integer> idList);

	@Query("select new Site(id,name,latitude,longitude) from Site")
	public List<Site> findAllCoordinates();

	@Query("select count(*) from Site where name = ?1")
	public Long countByName(String name);

	@Query("select count(*) from Site where name = ?1 and id != ?2")
	public Long countByName(String name, Integer id);

	@Query("select count(*) from Site where code = ?1 and (?2 is null or id != ?2)")
	public Long countByCode(String code, Integer id);

	@Query(select1 + " from Site a where a.type.id = ?1")
	public List<Site> findLight(Integer typeId);

	@Query("select new Site(a.id,a.name,a.latitude,a.longitude,(select b.id from Customer b where a.customer.id = b.id),(select b.id from Supplier b where a.supplier.id = b.id),a.owner) from Site a where a.type.id = ?1")
	public List<Site> findAllCoordinates(Integer typeId);

	@Query(select1 + " from Site a where a.type.id = ?1 and a.user.username = ?2")
	public List<Site> findLight(Integer typeId, String username);

	@Query(select1 + " from Site a where a.user.username = ?1")
	public List<Site> findLight(String username);

	@Modifying
	@Query("update Site set latitude = ?2  where id = ?1")
	public void updateLatitude(Integer siteId, Double latitude);

	@Modifying
	@Query("update Site set longitude = ?2 where id = ?1")
	public void updateLongitude(Integer siteId, Double longitude);

	@Modifying
	@Query("update Site set googleAddress = ?2 where id = ?1")
	public void updateGoogleAddress(Integer siteId, String googleAddress);

	@Modifying
	@Query("update Site set googleAddress = ?2,googleCity=?3,googleRegion=?4  where id = ?1")
	public void updateGoogleGeocodeData(Integer siteId, String googleAddress, String googleCity, String googleRegion);

	@Query("from Site where googleAddress is null or googleAddress='' ")
	public List<Site> findByNotHavingGoogleAddress();
	
	@Query("select distinct googleRegion from Site where googleRegion is not null and googleRegion != '' ")
	public List<String> findGoogleRegionList();
}
