package ma.azdad.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.mobile.model.DeliveryRequest;
import ma.azdad.mobile.model.Token;
import ma.azdad.service.StockRowService;
import ma.azdad.service.TokenService;

@RestController
@RequestMapping("/mobile/sr")
public class StockRowController {
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	StockRowService stockRowService;
	
	@GetMapping("/dn-summary/{key}/{id}")
	public List<ma.azdad.mobile.model.StockRow> findByInboundDeliveryRequestMobile(@PathVariable String key,@PathVariable Integer id) {
		System.out.println("/dn-summary/{key}");
		Token token = tokenService.getBykey(key);
		return stockRowService.findByInboundDeliveryRequestMobile(id);
	}
	
	@GetMapping("/current-stock/{key}/{id}")
	public List<ma.azdad.mobile.model.StockRow> getStockSituationByInboundDeliveryRequestMobile(@PathVariable String key,@PathVariable Integer id) {
		System.out.println("/current-stock/{key}");
		Token token = tokenService.getBykey(key);
		return stockRowService.getStockSituationByInboundDeliveryRequestMobile(id);
	}
	
	@GetMapping("/associated-outbound/{key}/{id}")
	public List<ma.azdad.mobile.model.StockRow> findAttachedOutboundDeliveryRequestListMobile(@PathVariable String key,@PathVariable Integer id) {
		System.out.println("/associated-outbound/{key}");
		Token token = tokenService.getBykey(key);
		return stockRowService.findAttachedOutboundDeliveryRequestListMobile(id);
	}
	
	@GetMapping("/change-location/{key}/{srId}/{locationId}")
	public void changeLocation(@PathVariable String key,@PathVariable Integer srId,@PathVariable Integer locationId) {
		System.out.println("/changeLocation/{key}");
		Token token = tokenService.getBykey(key);
		stockRowService.changeStockRowLocation(srId, locationId);
	}
	
	@GetMapping("/getLocationsByDn/{key}/{id}")
	public List<ma.azdad.mobile.model.Location>  getLocationsByDn(@PathVariable String key,@PathVariable Integer id) {
		System.out.println("/getLocationsByDn/{key}");
		Token token = tokenService.getBykey(key);
		return stockRowService.findLocationsByDn(id);
	}
	
	
	
	



}
