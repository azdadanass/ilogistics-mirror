package ma.azdad.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.service.PoService;

@RestController
public class RestStatusService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PoService poService;

	@RequestMapping("/rest/status")
	public String checkStatus() {
		return "UP";
	}

}
