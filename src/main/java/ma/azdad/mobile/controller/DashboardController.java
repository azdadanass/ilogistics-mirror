package ma.azdad.mobile.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.mobile.model.Dashboard;
import ma.azdad.mobile.model.Token;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.TokenService;

@RestController
public class DashboardController {
	
	@Autowired
	TokenService tokenService;
	
	
	@Autowired
	DeliveryRequestService deliveryRequestService;
	
	
	@GetMapping("/mobile/dashboard/{key}")
	public Dashboard dashboard(@PathVariable String key) {
		Token token = tokenService.getBykey(key);
		Dashboard dashboard = deliveryRequestService.getDashboard(token.getUsername(),token.getWarehouseList());
		return dashboard;
	}

}
