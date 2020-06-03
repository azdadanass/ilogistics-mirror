package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Currency;
import ma.azdad.repos.CurrencyRepos;

@Component
@Transactional
public class CurrencyService {

	@Autowired
	private CurrencyRepos currencyRepos;

	public Currency findOne(Integer id) {
		return currencyRepos.findById(id).get();
	}

}
