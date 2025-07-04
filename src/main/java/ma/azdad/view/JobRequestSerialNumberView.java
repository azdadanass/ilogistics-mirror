package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.JobRequestSerialNumber;
import ma.azdad.repos.JobRequestSerialNumberRepos;
import ma.azdad.service.JobRequestSerialNumberService;

@ManagedBean
@Component
@Scope("view")
public class JobRequestSerialNumberView extends GenericView<Integer, JobRequestSerialNumber, JobRequestSerialNumberRepos, JobRequestSerialNumberService> {

	@Autowired
	private SessionView sessionView;

	@PostConstruct
	public void init() {
		super.init();
		time();
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	public void refreshList() {
		
	}

	// getters & setters
	public JobRequestSerialNumber getModel() {
		return model;
	}

	public void setModel(JobRequestSerialNumber model) {
		this.model = model;
	}

}
