package ma.azdad.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.mobile.model.DeliveryRequest;
import ma.azdad.mobile.model.DnMaterials;
import ma.azdad.mobile.model.HardwareStatusData;
import ma.azdad.mobile.model.Location;
import ma.azdad.mobile.model.Token;
import ma.azdad.model.StockRowState;
import ma.azdad.model.StockRowStatus;
import ma.azdad.service.DeliveryRequestDetailService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.LocationService;
import ma.azdad.service.TokenService;

@RestController
public class DeliveryRequestDetailController {
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	LocationService locationService;
	
	@Autowired
	DeliveryRequestDetailService service;
	
	@PostMapping("/mobile/dnd/init-sr/{key}/{countOk}")
	public List<HardwareStatusData> initSr(@PathVariable String key,@RequestBody DeliveryRequest request,@PathVariable String countOk) {
		System.out.println("/mobile/init-sr/{key}/"+countOk);
		Token token = tokenService.getBykey(key);
		return service.initStockRowList(request,countOk);

	}
	
	@PostMapping("/mobile/dnd/init-sr2/{key}")
	public List<DnMaterials> initSr2(@PathVariable String key,@RequestBody DeliveryRequest request) {
		System.out.println("/mobile/init-sr2/{key}/");
		Token token = tokenService.getBykey(key);
		return service.initStockRowList(request);

	}
	
	@GetMapping("/mobile/dnd/location/{key}/{id}/{state}")
	public List<Location> findLocations(@PathVariable String key,@PathVariable Integer id,@PathVariable String state){
		System.out.println("/mobile/dnd/location/{key}/{id}");
		Token token = tokenService.getBykey(key);
		return locationService.findByWarehouseAndStockRowStateAndOwnerMobile(id,state);
	}
	
	
	
	

}
