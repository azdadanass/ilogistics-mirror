package ma.azdad.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.mobile.model.DeliveryRequest;
import ma.azdad.mobile.model.DeliveryRequestExpiryDate;
import ma.azdad.mobile.model.DeliveryRequestFile;
import ma.azdad.mobile.model.HardwareStatusList;
import ma.azdad.mobile.model.Token;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.DocTypeService;
import ma.azdad.service.ProjectService;
import ma.azdad.service.TokenService;
import ma.azdad.service.UserService;
import ma.azdad.service.WarehouseService;

@RestController
public class DeliveryRequestController {

	@Autowired
	DeliveryRequestService service;
	
	@Autowired
	DocTypeService docTypeService;

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
	
	@GetMapping("/mobile/dn/generate-stamp/{key}/{id}")
    public ResponseEntity<byte[]> generateStampMobile(@PathVariable String key,@PathVariable Integer id) {
        try {
        	System.out.println("/mobile/dn/generate-stamp/{key}/{id}");
        	Token token = tokenService.getBykey(key);
        	ma.azdad.model.DeliveryRequest deliveryRequest = service.findOne(id);
            byte[] pdfBytes = service.generateStampMobile(deliveryRequest);
            System.out.println("Generated PDF size: " + pdfBytes.length);
            if (pdfBytes.length == 0) {
                System.out.println("PDF generation failed.");
            }
            // Prepare response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=stamp_" + deliveryRequest.getId() + ".pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/mobile/dn/generate-pdf/{key}/{id}")
    public ResponseEntity<byte[]> generatePdfMobile(@PathVariable String key,@PathVariable Integer id) {
        try {
        	System.out.println("/mobile/dn/generate-pdf/{key}/{id}");
        	Token token = tokenService.getBykey(key);
        	ma.azdad.model.DeliveryRequest deliveryRequest = service.findOne(id);
            byte[] pdfBytes = service.generatePdf2(deliveryRequest);
            if (pdfBytes.length == 0) {
                System.out.println("PDF generation failed.");
            }
            // Prepare response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=dn_" + deliveryRequest.getId() + ".pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/mobile/dn/find-doc-type/{key}/{id}")
    public List<String> findDocByType(@PathVariable String key,@PathVariable Integer id) {
    	Token token = tokenService.getBykey(key);
    	return docTypeService.findByTypeMobile("deliveryRequest", id);
    }
    
    @GetMapping("/mobile/dn/find-doc/{key}/{id}")
    public List<DeliveryRequestFile> findDnAttachments(@PathVariable String key,@PathVariable Integer id) {
    	System.out.println("/mobile/dn/find-doc/{key}/{id}");
    	Token token = tokenService.getBykey(key);
    	
    	return service.findDnAttachments(id);
    }
    
    @GetMapping("/mobile/dn/expiry-list/{key}/{id}")
    public List<DeliveryRequestExpiryDate> findDnExpiry(@PathVariable String key,@PathVariable Integer id) {
    	System.out.println("/mobile/dn/expiry-list/{key}/{id}");
    	Token token = tokenService.getBykey(key);
    	
    	return service.findDnExpiry(id);
    }
    
    @PutMapping("/mobile/dn/update-expiry/{key}")
    public void updateDnExpiryById(@PathVariable String key,@RequestBody DeliveryRequestExpiryDate expiry) {
    	Token token = tokenService.getBykey(key);
    	service.updateDnExpiryById(expiry);
    }
    
    @PostMapping("/mobile/dn/create-expiry/{key}")
    public void createDnExpiryById(@PathVariable String key,@RequestBody DeliveryRequestExpiryDate expiry) {
    	Token token = tokenService.getBykey(key);
    	service.createDnExpiryById(expiry);
    }
	
	
  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}