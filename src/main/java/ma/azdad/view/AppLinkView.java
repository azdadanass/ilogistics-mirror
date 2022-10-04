package ma.azdad.view;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.AppLink;
import ma.azdad.repos.AppLinkRepos;
import ma.azdad.service.AppLinkService;
import ma.azdad.service.TransportationRequestService;

@ManagedBean
@Component
@Scope("view")
public class AppLinkView extends GenericView<Integer, AppLink, AppLinkRepos, AppLinkService> {

	@Autowired
	protected AppLinkService appLinkService;

	@Autowired
	protected TransportationRequestService transportationRequestService;

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

			Integer transportationRequestId = transportationRequestService.findIdByDeliveryRequest(id);

			list1 = new ArrayList<AppLink>();
			list1.addAll(appLinkService.findByDeliveryRequest(id, true));
			if (transportationRequestId != null)
				list1.addAll(appLinkService.findByTransportationRequest(transportationRequestId));

			list2 = new ArrayList<AppLink>();
			list2.addAll(appLinkService.findByDeliveryRequest(id, false));
			if (transportationRequestId != null)
				list2.addAll(appLinkService.findByTransportationRequest(transportationRequestId));
			
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

	// generic

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
