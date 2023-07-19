package ma.azdad.view;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean
@Component
@Scope("session")
public class MenuView implements Serializable {

	private String menu;
	private Integer companyId;
	private Integer customerId;

	public String redirect(String path, String... tab) {
		path += "?" + tab[0];
		if (tab.length > 1)
			for (int i = 1; i < tab.length; i++)
				path += "&" + tab[i];
		return path;
	}

	public String selectMenu() {
		if (menu == null)
			return null;
		switch (menu) {
		case "INVENTORY":
			return "stockRowList.xhtml?faces-redirect=true&pageIndex=1";
		case "DELIVERY":
			return "deliveryReporting.xhtml?faces-redirect=true";
		case "LOGISTIC_ANALYSIS":
			return "stockRowList.xhtml?faces-redirect=true&pageIndex=2";
		case "FINANCIAL_REPORT":
			return "companyFinancial.xhtml?faces-redirect=true&id="+companyId;

		default:
			return null;
		}
	}

	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
		if (this.companyId != null)
			this.customerId = null;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
		if (this.customerId != null)
			this.companyId = null;
	}

}