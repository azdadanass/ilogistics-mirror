package ma.azdad.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ma.azdad.mobile.model.Token;
import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.repos.DeliveryRequestSerialNumberRepos;
import ma.azdad.service.DeliveryRequestSerialNumberService;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.TokenService;

@RestController
public class SerialNumberController {

	@Autowired
	DeliveryRequestSerialNumberRepos deliveryRequestSerialNumberRepos;
	@Autowired
	DeliveryRequestSerialNumberService deliveryRequestSerialNumberService;
	@Autowired
	DeliveryRequestService deliveryRequestService;
	@Autowired
	TokenService tokenService;

	@GetMapping("/mobile/sn/scan/{key}/{id}/{dnId}/{serial}")
	public void updateSerialNumber(@PathVariable Integer id,@PathVariable Integer dnId, @PathVariable String serial, @PathVariable String key) {
	    Token token = tokenService.getBykey(key);
	    System.out.println("/mobile/sn/scan/" + id + "/" + serial + "/" + token.getKey());

	    DeliveryRequestSerialNumber sn = deliveryRequestSerialNumberRepos.findById(id)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Serial number not found"));

	    List<DeliveryRequestSerialNumber> serials = deliveryRequestSerialNumberRepos.findByDeliveryRequest(dnId);

	    boolean serialExists = serials.stream()
	        .anyMatch(s -> serial.equals(s.getSerialNumber()));

	    if (serialExists) {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "Serial already exists");
	    }

	    sn.setSerialNumber(serial);
	    deliveryRequestSerialNumberRepos.save(sn);
	}

	
	@GetMapping("/mobile/sn/scanOutbound")
	public ResponseEntity<String> scanOutboundSnMobile(@RequestParam String key,
		    @RequestParam Integer id,
		    @RequestParam String serial) {
		Token token = tokenService.getBykey(key);
		System.out.println("/mobile/sn/scanOutbound/");
	    return deliveryRequestSerialNumberService.scanOutboundSnMobile(id, serial);
	}

	
	

	@GetMapping("/mobile/sn/findone/{key}/{id}")
	public ma.azdad.mobile.model.DeliveryRequestSerialNumber findone(@PathVariable Integer id, @PathVariable String key) {
		Token token = tokenService.getBykey(key);
		System.out.println("/mobile/sn/findone/" + id + "/" + token.getKey());
		DeliveryRequestSerialNumber sn = deliveryRequestSerialNumberRepos.findById(id).get();
		
		return new ma.azdad.mobile.model.DeliveryRequestSerialNumber(id, sn.getPackingDetail().getParent().getName(), sn.getPackingDetail().getParent().getPartNumber().getName(),
				sn.getSerialNumber(), sn.getPackingDetail().getParent().getPartNumber().getImage(),  sn.getPackingDetail().getSnType(),
				sn.getPackingNumero(),sn.getInboundStockRow().getStatusValue(),sn.getInboundStockRow().getLocation().getName());
		

	}
	
	@GetMapping("/mobile/sn/clear/{id}/{key}")
	public void clearSerialNumber(@PathVariable Integer id,  @PathVariable String key) {
		Token token = tokenService.getBykey(key);
		System.out.println("/mobile/sn/clear/" + id + "/" + token.getKey());
		DeliveryRequestSerialNumber sn = deliveryRequestSerialNumberRepos.findById(id).get();
		if(sn.getOutboundDeliveryRequest() != null) {
		sn.setOutboundDeliveryRequest(null);
		}else {
			sn.setSerialNumber(null);
		}
		deliveryRequestSerialNumberRepos.save(sn);

	}
	
	@GetMapping("/mobile/sn/list/{key}/{id}")
	public List<ma.azdad.mobile.model.DeliveryRequestSerialNumber> findSnByDnId(@PathVariable Integer id, @PathVariable String key) {
		Token token = tokenService.getBykey(key);
		System.out.println("/mobile/sn/list/" + id + "/" + token.getKey());
		
		return deliveryRequestService.findSnByDnId(id);
	
	}
	
	@GetMapping("/mobile/sn/list/pn/{key}/{id}/{pn}")
	public List<ma.azdad.mobile.model.DeliveryRequestSerialNumber> findSnByDnIdAndPn(@PathVariable Integer id, @PathVariable String key,
			@PathVariable String pn) {
		Token token = tokenService.getBykey(key);
		System.out.println("/mobile/sn/list/pn/" + id + "/" + token.getKey());
		
		return deliveryRequestService.findSnByDnIdAndPn(id, pn);
	
	}
	
	@GetMapping("/mobile/sn/stock/{key}/{id}")
	public List<ma.azdad.mobile.model.DeliveryRequestSerialNumber> findSnStock(@PathVariable Integer id, @PathVariable String key) {
		Token token = tokenService.getBykey(key);
		System.out.println("/mobile/sn/stock/" + id + "/" + token.getKey());
		
		return deliveryRequestService.findSnStock(id);
	
	}
	
	@GetMapping("/mobile/sn/summary/{key}/{id}")
	public List<ma.azdad.mobile.model.SerialNumberSummary> findSnSummary(@PathVariable Integer id, @PathVariable String key) {
		Token token = tokenService.getBykey(key);
		System.out.println("/mobile/sn/summary/" + id + "/" + token.getKey());
		
		return deliveryRequestService.findSnSummary(id);
	
	}
	
	
	

}
