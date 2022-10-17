package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Currency;

@Repository
public interface CurrencyRepos extends JpaRepository<Currency, Integer> {

	@Query("select name from Currency where id = ?1")
	String findName(Integer id);

}
