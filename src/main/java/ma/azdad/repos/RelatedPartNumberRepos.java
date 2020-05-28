package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.RelatedPartNumber;

@Repository
public interface RelatedPartNumberRepos extends JpaRepository<RelatedPartNumber, Integer> {

	@Query("from RelatedPartNumber where partNumber1.id = ?1 or partNumber2.id = ?1")
	public List<RelatedPartNumber> findByPartNumber(Integer partNumberId);

}
