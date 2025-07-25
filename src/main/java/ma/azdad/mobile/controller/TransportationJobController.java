package ma.azdad.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.mobile.model.Token;
import ma.azdad.mobile.model.TransportationJob;
import ma.azdad.service.TokenService;
import ma.azdad.service.TransportationJobService;

@RestController
@RequestMapping("/mobile/tj")
public class TransportationJobController {
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	TransportationJobService transportationJobService;
	
	@GetMapping("/findbystatus/{key}/{state}")
	public List<TransportationJob> findLightByWarehouseListMobile(@PathVariable String key,@PathVariable Integer state) {
		System.out.println("/findbystatus/{key}/{state}");
		Token token = tokenService.getBykey(key);
		return transportationJobService.findMobileByStatus(state);
	}

}
