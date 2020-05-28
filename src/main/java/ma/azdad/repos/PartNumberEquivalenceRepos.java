package ma.azdad.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumberEquivalence;

@Repository
public interface PartNumberEquivalenceRepos extends JpaRepository<PartNumberEquivalence, Integer> {

	@Query("select a.partNumber.id from PartNumberEquivalenceDetail a where a.parent.partNumber.id in (?1)")
	public Set<Integer> findPartNumberIdListByEquivalence(Set<Integer> partNumberSourceList);

	@Query("from PartNumberEquivalence where formula is null or oneToOne is null")
	public List<PartNumberEquivalence> findByHavingNullFormulaOrType();

	@Query("from PartNumberEquivalence a where a.inverseFormula is null and (select count(*) from PartNumberEquivalenceDetail b where b.parent.id = a.id) = 1")
	public List<PartNumberEquivalence> findByHavingNullInverseFormula();

	@Query("from PartNumberEquivalence a where a.partNumber.id = ?1 and (select count(*) from PartNumberEquivalenceDetail b where b.parent.id = a.id and b.partNumber.id = ?2 ) > 0")
	public List<PartNumberEquivalence> findByPartNumberAndContainingPartNumber(Integer partNumberId1, Integer partNumberId2);

}
