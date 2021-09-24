package ma.azdad.view;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean(eager = true)
@Component
@Scope("application")
public class ErpView {

	@Value("${spring.profiles.active}")
	private String erp;

	public String getErp() {
		return erp.replace("-dev", "");
	}

	public Boolean getGcom() {
		return "gcom".contentEquals(getErp());
	}

	public Boolean getOrange() {
		return "orange".contentEquals(getErp());
	}

	public Boolean getMise() {
		return "mise".contentEquals(getErp());
	}

}