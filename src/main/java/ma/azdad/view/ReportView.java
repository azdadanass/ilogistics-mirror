package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Report;
import ma.azdad.rest.RestClientService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class ReportView {

	@Autowired
	private RestClientService restClientService;

	private Report report;

	@PostConstruct
	public void init() {

	}

	public void findReportByProject(Integer projectId) {
		report = restClientService.getReport(projectId);
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

}