package ma.azdad.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.mobile.model.DeliveryRequest;
import ma.azdad.mobile.model.HardwareStatusList;
import ma.azdad.mobile.model.Token;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.TokenService;
import ma.azdad.service.UserService;
import ma.azdad.service.WarehouseService;

@RestController
public class DeliveryRequestController {

	@Autowired
	DeliveryRequestService service;

	@Autowired
	UserService userService;

	@Autowired
	WarehouseService warehouseService;

	@Autowired
	TokenService tokenService;

	@Autowired
	ProjectService projectService;
	
	@GetMapping("/mobile/dn/findone/{key}/{id}")
	public DeliveryRequest findOneLightMobile(@PathVariable String key,@PathVariable Integer id) {
		System.out.println("/mobile/dn/findone/{key}/{id}");
		Token token = tokenService.getBykey(key);
		return service.findOneLightMobile(id);
	}

	@GetMapping("/mobile/dn/{key}")
	public List<DeliveryRequest> findLightByWarehouseListMobile(@PathVariable String key) {
		System.out.println("/mobile/dn/{key}");
		Token token = tokenService.getBykey(key);
		return service.findLightByWarehouseListMobile(token.getWarehouseList());
	}
	
	@PostMapping("/mobile/dn/handle-in/{key}/{id}/{category}")
	public void handle(@PathVariable String key,@PathVariable Integer id,@RequestBody HardwareStatusList list,@PathVariable String category) {
		Token token = tokenService.getBykey(key);
		System.out.println("/mobile/dn/handle-in/{key}");
		service.handleIn(list.getHardwareStatusDataList(), id,category,token.getUsername());
	}
	
	@GetMapping("/mobile/dn/handle-out/{key}/{id}")
	public void handle(@PathVariable String key,@PathVariable Integer id) {
		Token token = tokenService.getBykey(key);
		System.out.println("/mobile/dn/handle-out/{key}");
		service.handleOut(id,token.getUsername());
	}
	
	@GetMapping("/mobile/dn/new/{key}")
	public List<DeliveryRequest> findLightNewByWarehouseListMobile(@PathVariable String key) {
		System.out.println("/mobile/dn/new/{key}");
		Token token = tokenService.getBykey(key);
		return service.findLightNewByWarehouseListMobile(token.getWarehouseList());
	}
	
	@GetMapping("/mobile/dn/delivered/{key}")
	public List<DeliveryRequest> findLightDeliveredByWarehouseListMobile(@PathVariable String key) {
		System.out.println("/mobile/dn/delivered/{key}");
		Token token = tokenService.getBykey(key);
		return service.findLightDeliveredByWarehouseListMobile(token.getWarehouseList());
	}
	
	@GetMapping("/mobile/dn/missing-sn/{key}")
	public List<DeliveryRequest> findLightByMissingSerialNumberMobile(@PathVariable String key) {
		System.out.println("/mobile/dn/missing-sn/{key}");
		Token token = tokenService.getBykey(key);
		return service.findLightByMissingSerialNumberMobile(token.getWarehouseList());
	}
	
	@GetMapping("/mobile/dn/missing-expiry/{key}")
	public List<DeliveryRequest> findLightByMissingExpiryMobile(@PathVariable String key) {
		System.out.println("/mobile/dn/missing-expiry/{key}");
		Token token = tokenService.getBykey(key);
		return service.findLightByMissingExpiryMobile(token.getWarehouseList());
	}
	
	@GetMapping("/mobile/dn/missing-bl/{key}")
	public List<DeliveryRequest> findLightByMissingBlMobile(@PathVariable String key) {
		System.out.println("/mobile/dn/missing-bl/{key}");
		Token token = tokenService.getBykey(key);
		return service.findByMissingOutboundDeliveryNoteFilemobile(token.getUsername(),token.getWarehouseList());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}