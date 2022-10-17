package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Currency;
import ma.azdad.repos.CurrencyRepos;
import ma.azdad.service.CurrencyService;

@ManagedBean
@Component
@Scope("view")
public class CurrencyView extends GenericView<Integer, Currency, CurrencyRepos, CurrencyService> {

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	// generic
	
	public List<Currency> findAll() {
		return service.findAll();
	}

	public String findName(Integer id) {
		return service.findName(id);
	}

}
