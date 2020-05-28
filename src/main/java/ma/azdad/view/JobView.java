package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.service.JobService;
import ma.azdad.service.TransportationJobService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class JobView {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public JobService jobService;

	@Autowired
	public TransportationJobService transportationJobService;
	@Autowired
	public SessionView sessionView;

	public Boolean canExecuteJob;

	@PostConstruct
	public void init() {
		canExecuteJob = "a.azdad".equals(sessionView.getUsername());
	}

	public void updateSitesGoogleGeocodeData() {
		if (canExecuteJob)
			jobService.updateSitesGoogleGeocodeData();
	}

	public void correctExistingTransportationRequestList() {
		if (canExecuteJob)
			transportationJobService.correctExistingTransportationRequestList();
	}

	public void updatePaymentStatus() {
		if (canExecuteJob)
			jobService.updatePaymentStatus();
	}

	public void addOrUpdateCrossChargeScript() {
		if (canExecuteJob)
			jobService.addOrUpdateCrossChargeScript();
	}

}