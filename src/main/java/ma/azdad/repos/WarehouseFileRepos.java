package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.WarehouseFile;

@Repository
public interface WarehouseFileRepos extends JpaRepository<WarehouseFile, Integer> {

}

