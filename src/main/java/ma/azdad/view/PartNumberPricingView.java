package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumberPricing;
import ma.azdad.repos.PartNumberPricingRepos;
import ma.azdad.service.CurrencyService;
import ma.azdad.service.PartNumberPricingService;
import ma.azdad.service.PartNumberService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class PartNumberPricingView extends GenericView<Integer, PartNumberPricing, PartNumberPricingRepos, PartNumberPricingService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private PartNumberService partNumberService;

	@Autowired
	private CurrencyService currencyService;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			initLists(service.findLight());
	}

	// save
	public Boolean canSave() {
		return sessionView.isSE();
	}

	public String save() {
		if (!canSave())
			return addParameters(listPage, "faces-redirect=true");
		if (!validate())
			return null;

		model.setPartNumber(partNumberService.findOne(model.getPartNumberId()));
		model.setCostCurrency(currencyService.findOne(model.getCostCurrencyId()));
		model.setPriceCurrency(currencyService.findOne(model.getPriceCurrencyId()));

		model = service.save(model);
		return addParameters(viewPage, "faces-redirect=true", "id=" + model.getId());
	}

	public Boolean validate() {
		if (service.countByPartNumberAndDate(model.getPartNumberId(), model.getDate(), model.getId()) > 0)
			return FacesContextMessages.ErrorMessages("PN / Date should be unique");
		return true;
	}

	// delete
	public Boolean canDelete() {
		return true;
	}

	public String delete() {
		if (!canDelete())
			return null;
		try {
			service.delete(model);
		} catch (DataIntegrityViolationException e) {
			FacesContextMessages.ErrorMessages("Can not delete this item (contains childs)");
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages("Error !");
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	// getters & setters
	public PartNumberPricing getModel() {
		return model;
	}

	public void setModel(PartNumberPricing model) {
		this.model = model;
	}

}
