package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Base;

@Repository
public interface BaseRepos extends JpaRepository<Base, Integer> {

}
