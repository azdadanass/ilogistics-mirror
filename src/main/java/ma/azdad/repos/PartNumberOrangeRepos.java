package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.PartNumberOrange;

@Repository
public interface PartNumberOrangeRepos extends JpaRepository<PartNumberOrange, Integer> {

}
