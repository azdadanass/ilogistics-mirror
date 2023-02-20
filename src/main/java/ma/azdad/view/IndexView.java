package ma.azdad.view;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@ManagedBean
@Component
@Transactional
@Scope("session")
public class IndexView implements Serializable {

	@Autowired
	private SessionView sessionView;

	private Integer selectedMenu = 2;
	

	@PostConstruct
	public void init() {
		
	}
	
	public void changeMenuByCurrentPath() {
		String currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		switch (currentPath) {
		case "/partNumberList.xhtml":
		case "/partNumberPricingList.xhtml":
		case "/vehicleTypeList.xhtml":
		case "/transporterList.xhtml":
		case "/warehouseList.xhtml":
		case "/partNumberBrandList.xhtml":
		case "/partNumberConfiguration.xhtml":
		case "/siteCategoryList.xhtml":
		case "/projectAssignmentList.xhtml":
		case "/projectList.xhtml":
			this.selectedMenu = 1;
			break;
		case "/deliveryRequestList.xhtml":
		case "/viewDeliveryRequest.xhtml":
		case "/addEditDeliveryRequest.xhtml":
		case "/prepareOutboundDeliveryRequest.xhtml":
		case "/storeDeliveryRequest.xhtml":
			this.selectedMenu = 2;
			break;
		case "/transportationRequestList.xhtml":
		case "/viewTransportationRequest.xhtml":
		case "/addEditTransportationRequest.xhtml":
		case "/transportationJobList.xhtml":
		case "/viewTransportationJob.xhtml":
		case "/addEditTransportationJob.xhtml":
			this.selectedMenu = 3;
			break;
		case "/stockRowList.xhtml":
		case "/projectReporting.xhtml":
		case "/warehouseReporting.xhtml":
		case "/deliveryReporting.xhtml":
		case "/sdmDeliveryReporting.xhtml":
		case "/maxMinThreshold.xhtml":
		case "/companyFinancial.xhtml":
		case "/projectFinancial.xhtml":
		case "/deliveryRequestfinancialReporting.xhtml":
		case "/poList.xhtml":
		case "/companyList.xhtml":
		case "/customerList.xhtml":
		case "/partNumberReporting.xhtml":
		case "/viewPo.xhtml":
			this.selectedMenu = 4;
			break;
		default:
			break;
		}
	}

	public Boolean canAccessMenu(Integer menu) {
		switch (menu) {
		case 1:
			return sessionView.getIsSE() || sessionView.getIsAdmin();
		case 2:
			return sessionView.getIsUser() || sessionView.getIsPM() || sessionView.getIsWM() || sessionView.getIsLobManager() || sessionView.getIsBuManager();
		case 3:
			return sessionView.getIsPM() || sessionView.getIsUser() || sessionView.getIsTM();
		case 4:
			return sessionView.getIsInternalPM() || sessionView.getIsLobManager() || sessionView.getIsBuManager() || sessionView.getIsWM() || sessionView.getIsUser() || sessionView.getIsExternalPm();
		default:
			return false;
		}
	}

	public String getWelcomePage(Integer selectedMenu) {
		this.selectedMenu = selectedMenu;
		switch (selectedMenu) {
		case 1:
			if (canAccessMenu(1))
				return addParameters(sessionView.getIsAdmin() ? "warehouseList.xhtml" : sessionView.getIsPM() ? "projectList.xhtml" : "partNumberList.xhtml", "faces-redirect=true");
		case 2:
			if (canAccessMenu(2))
				return addParameters("deliveryRequestList.xhtml", "faces-redirect=true", "state=0", "pageIndex=1");
		case 3:
			if (canAccessMenu(3))
				return addParameters("transportationRequestList.xhtml", "faces-redirect=true", "state=0", "pageIndex=1");
		case 4:
			if (canAccessMenu(4))
				return addParameters(sessionView.getInternal() || sessionView.getIsWM()?"reporting.xhtml":"deliveryReporting.xhtml", "faces-redirect=true");
		default:
			return null;
		}
	}

	public static String addParameters(String path, String... tab) {
		path += "?" + tab[0];
		if (tab.length > 1)
			for (int i = 1; i < tab.length; i++)
				path += "&" + tab[i];
		return path;
	}

	public Integer getSelectedMenu() {
		return selectedMenu;
	}

	public void setSelectedMenu(Integer selectedMenu) {
		this.selectedMenu = selectedMenu;
	}

}
