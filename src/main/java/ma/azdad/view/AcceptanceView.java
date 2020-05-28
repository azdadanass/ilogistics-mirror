package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Acceptance;
import ma.azdad.service.AcceptanceService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class AcceptanceView {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public AcceptanceService acceptanceService;
	public Acceptance acceptance;

	@PostConstruct
	public void init() {

	}

	public void refreshAcceptance(Integer acceptanceId) {
		acceptance = acceptanceService.findOne(acceptanceId);
	}

	public Acceptance getAcceptance() {
		return acceptance;
	}

	public void setAcceptance(Acceptance acceptance) {
		this.acceptance = acceptance;
	}

}