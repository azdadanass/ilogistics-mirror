package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumber;
import ma.azdad.utils.File;

@Repository
public interface PartNumberRepos extends JpaRepository<PartNumber, Integer> {

	@Query("select concat('\"',concat(name,'\"')) from PartNumber group by name")
	public List<String> getAllNames();

	@Query("select new PartNumber(id,name) from PartNumber where unit = ?1")
	public List<PartNumber> findLight(Boolean unit);

	@Query("select new PartNumber(id,name) from PartNumber where stockItem = ?1")
	public List<PartNumber> findLightByStockItem(Boolean stockItem);

	@Query("select new PartNumber(id,name) from PartNumber where id in (?1)")
	public List<PartNumber> findLight(List<Integer> idList);

	@Query("select new PartNumber(id, name,description) from PartNumber")
	public List<PartNumber> findLight();

	@Query("from PartNumber where user.username = ?1")
	public List<PartNumber> find(String username);

	@Query("select count(*) from PartNumber where name = ?1")
	public Long countByName(String name);

	@Query("select count(*) from PartNumber where name = ?1 and id != ?2")
	public Long countByName(String name, Integer id);

	@Query("from PartNumber where name = ?1")
	public PartNumber findByName(String name);

	@Query("from PartNumber where name like ?1 or description like ?1")
	public List<PartNumber> findLikeNameOrDescription(String query);

	@Query("from PartNumber where (name like ?1 or description like ?1) and stockItem = ?2")
	public List<PartNumber> findLikeNameOrDescription(String query, Boolean stockItem);

	@Modifying
	@Query("update PartNumber a set a.image = (select min(b.link) from PartNumberFile b where b.parent.id = a.id and b.extension in ('png','gif','jpg','jpeg') )")
	public void updateImage();

	@Modifying
	@Query("update PartNumber a set a.image = (select min(b.link) from PartNumberFile b where b.parent.id = a.id and b.extension in ('png','gif','jpg','jpeg') ) where a.id = ?1")
	public void updateImage(Integer id);

	@Modifying
	@Query("update PartNumber a set a.image = 'files/no-image.png' where a.image is null")
	public void updateNullImage();

	@Query("from PartNumber where id not in (select distinct b.partNumber.id from Packing b)")
	public List<PartNumber> findWithoutPackingList();

	@Modifying
	@Query("update PartNumber set hasPacking = ?2 where id = ?1")
	public void updateHasPacking(Integer id, Boolean hasPacking);

	@Query("select hasPacking from PartNumber where id = ?1")
	public Boolean getHasPacking(Integer id);

	@Modifying
	@Query("update PartNumber a set a.brandName = ?2 where a.brand.id = ?1")
	void updateBrandName(Integer brandId, String brandName);

	@Modifying
	@Query("update PartNumber a set a.typeName = ?2 where a.partNumberType.id = ?1")
	void updateTypeName(Integer typeId, String typeName);

	@Modifying
	@Query("update PartNumber a set a.categoryName = ?2 where a.partNumberType.category.id = ?1")
	void updateCategoryName(Integer cateogryId, String categoryName);

	@Modifying
	@Query("update PartNumber a set a.industryName = ?2 where a.partNumberType.category.industry.id = ?1")
	void updateIndustryName(Integer industryId, String industryName);
	
	@Query("select new ma.azdad.utils.File('Part Number',a.parent.name,a.id,a.date,a.link,a.extension,a.type,a.size,a.name,user) from PartNumberFile a where a.parent.id in (select distinct b.partNumber.id from Boq b where b.podetails.po.id = ?1) order by a.parent.id")
	public List<File> findFileListByPo(Integer poId);
}
