package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PackingDetail;

@Repository
public interface PackingDetailRepos extends JpaRepository<PackingDetail, Integer> {

	@Query("from PackingDetail where parent.partNumber.id = ?1")
	public List<PackingDetail> findByPartNumber(Integer partNumberId);

}
