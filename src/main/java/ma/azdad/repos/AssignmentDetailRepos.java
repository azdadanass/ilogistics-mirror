package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.AssignmentDetail;

@Repository
public interface AssignmentDetailRepos extends JpaRepository<AssignmentDetail, Integer> {

}
