package ma.azdad.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import ma.azdad.model.Report;

@Component
public class RestClientService {

	@Value("${myreportsAddress}")
	private String myreportsAddress;

	public Report getReport(Integer projectId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(myreportsAddress + "/rest/service/get/report-full/" + projectId, Report.class);
		} catch (Exception e) {
			return null;
		}
	}

}
