package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Currency;

@Repository
public interface CurrencyRepos extends JpaRepository<Currency, Integer> {

}
