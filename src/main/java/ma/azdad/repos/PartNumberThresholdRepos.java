package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumberThreshold;

@Repository
public interface PartNumberThresholdRepos extends JpaRepository<PartNumberThreshold, Integer> {

	String select1 = "select new PartNumberThreshold(id,stockMin,stockMax,dormancy,a.partNumber.id,a.partNumber.name,a.partNumber.description) ";

	@Query(select1 + "from PartNumberThreshold a where a.project.id = ?1")
	public List<PartNumberThreshold> findByProject(Integer projectId);

	@Query("select count(*) from PartNumberThreshold a where a.project.id = ?1 and a.partNumber.id = ?2")
	public Long countByProjectAndPartNumber(Integer projectId, Integer partNumberId);
}
