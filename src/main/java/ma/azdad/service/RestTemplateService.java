package ma.azdad.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateService {

	protected final Logger log = LoggerFactory.getLogger(RestTemplateService.class);

	public String consumRest(String url) {
		log.info("consumRest : " + url);
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
			return response.getBody().toString();
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
	}

	public <T> T consumRest(String url, Class<T> type) {
		log.info("consumRest : " + url);
		try {
			RestTemplate restTemplate = new RestTemplate();
			T t = restTemplate.getForObject(url, type);
			return t;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

}