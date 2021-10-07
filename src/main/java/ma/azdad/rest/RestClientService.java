package ma.azdad.rest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ma.azdad.model.Report;
import ma.azdad.utils.App;

@Component
public class RestClientService {

	public Report getReport(Integer projectId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(App.MYREPORTS.getLink() + "/rest/service/get/report-full/" + projectId, Report.class);
		} catch (Exception e) {
			return null;
		}
	}

}
