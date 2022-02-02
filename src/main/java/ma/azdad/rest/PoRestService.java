package ma.azdad.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.service.PoService;

@RestController
public class PoRestService {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PoService poService;

	@RequestMapping("/rest/poService/updateBoqStatus/{poId}")
	public void updateBoqStatus(@PathVariable Integer poId) {
		poService.updateBoqStatus(poId);
	}

}
