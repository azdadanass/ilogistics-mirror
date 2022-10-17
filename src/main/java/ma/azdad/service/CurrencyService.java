package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Currency;
import ma.azdad.repos.CurrencyRepos;

@Component
public class CurrencyService extends GenericService<Integer, Currency, CurrencyRepos> {

	@Autowired
	private CurrencyRepos repos;

	@Override
	@Cacheable("currencyService.findAll")
	public List<Currency> findAll() {
		return repos.findAll();
	}

	@Override
	@Cacheable("currencyService.findOne")
	public Currency findOne(Integer id) {
		Currency currency = super.findOne(id);
		return currency;
	}

	@Cacheable("currencyService.findName")
	public String findName(Integer id) {
		return repos.findName(id);
	}

}
