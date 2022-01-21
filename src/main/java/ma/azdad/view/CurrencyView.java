package ma.azdad.view;

import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Currency;
import ma.azdad.service.CurrencyService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class CurrencyView {

	@Autowired
	protected CurrencyService service;

	public List<Currency> findAll() {
		return service.findAll();
	}

}
