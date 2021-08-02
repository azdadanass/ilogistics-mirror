package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.AppLink;
import ma.azdad.repos.AppLinkRepos;
import ma.azdad.service.AppLinkService;

@ManagedBean
@Component
@Scope("view")
public class AppLinkView extends GenericView<Integer, AppLink, AppLinkRepos, AppLinkService> {

	@Autowired
	protected AppLinkService appLinkService;

	@Autowired
	protected CacheView cacheView;

	private AppLink appLink = new AppLink();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		refreshList();
	}

	@Override
	public void refreshList() {
		if ("/viewDeliveryRequest.xhtml".equals(currentPath)) {
			list1 = appLinkService.findByDeliveryRequest(id, true);
			list2 = appLinkService.findByDeliveryRequest(id, false);
		} else if ("/viewTransportationRequest.xhtml".equals(currentPath))
			list2 = list1 = appLinkService.findByTransportationRequest(id);
		else if ("/viewWarehouse.xhtml".equals(currentPath))
			list2 = list1 = appLinkService.findByWarehouse(id);
	}

	public Double getTotalAmount1() {
		Double result = 0.0;
		if (list1 != null)
			for (AppLink appLink : list1)
				result += appLink.getAmount() * appLink.getMadConversionRate();
		return result;
	}

	public Double getTotalAmount2() {
		Double result = 0.0;
		if (list2 != null)
			for (AppLink appLink : list2)
				result += appLink.getAmount() * appLink.getMadConversionRate();
		return result;
	}

	// GENERIC

	// GETTERS & SETTERS




	public AppLinkService getAppLinkService() {
		return appLinkService;
	}

	public void setAppLinkService(AppLinkService appLinkService) {
		this.appLinkService = appLinkService;
	}

	public AppLink getAppLink() {
		return appLink;
	}

	public void setAppLink(AppLink appLink) {
		this.appLink = appLink;
	}

}
