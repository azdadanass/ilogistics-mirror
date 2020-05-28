package ma.azdad.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import  ma.azdad.model.PartNumberDetail;


@Repository
public interface PartNumberDetailRepos extends JpaRepository<PartNumberDetail, Integer> {

	
}

