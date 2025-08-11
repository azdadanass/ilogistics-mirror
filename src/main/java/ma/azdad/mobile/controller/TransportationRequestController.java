package ma.azdad.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ma.azdad.mobile.model.Token;
import ma.azdad.mobile.model.TransportationJob;
import ma.azdad.mobile.model.TransportationRequest;
import ma.azdad.mobile.model.TransportationRequestImage;
import ma.azdad.model.Role;
import ma.azdad.repos.TransportationRequestImageRepos;
import ma.azdad.service.TokenService;
import ma.azdad.service.TransportationJobService;
import ma.azdad.service.TransportationRequestService;

@RestController
@RequestMapping("/mobile/tr")
public class TransportationRequestController {
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	TransportationRequestImageRepos transportationRequestImageRepos;
	
	@Autowired
	TransportationRequestService transportationRequestService;
	
	@GetMapping("/findone/{key}/{id}")
	public TransportationRequest findOneLightMobile(@PathVariable String key,@PathVariable Integer id) {
		System.out.println("/findone/{key}/{id}");
		Token token = tokenService.getBykey(key);
		return transportationRequestService.findOneMobile(id);
	}
	
	@GetMapping("/findbystatus/{key}/{state}")
	public List<TransportationRequest> findLightByUserMobile(@PathVariable String key,
			@PathVariable Integer state) {
		System.out.println("/mobile/tr/findbystatus/{key}/{state}");
		Token token = tokenService.getBykey(key);
		if (token.getRoleList().contains(Role.ROLE_ILOGISTICS_TM)) {
			return transportationRequestService.findByTmMobileByStatus(state);
		} else {
			return transportationRequestService.findByDriverMobileByStatus(state, token.getUsername());

		}
	}
	

	@RequestMapping(value = "/uploadphoto/{id}/{type}/{key}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String uploadTaskDetailPhoto(
		    @RequestParam(value = "file") MultipartFile file,
		    @PathVariable int id,
		    @PathVariable String type,
		    @PathVariable String key
		) throws Exception {
		System.out.println("/uploadphoto/" + id);
		Token token = tokenService.getBykey(key);

		if (!file.isEmpty()) {
			System.out.println(file.getSize());
			System.out.println(file.getOriginalFilename());
			System.out.println(id);
			return "\"" + transportationRequestService.uploadTaskDetailPhoto(transportationRequestService.findOne(id),type,
					file.getInputStream(), file.getOriginalFilename(), file.getSize(), false) + "\"";
		}
		return "\"null\"";
	}
	
	@PutMapping("/updatetags/{type}/{key}")
	public void updateTags(@RequestBody TransportationRequestImage detail,@PathVariable String type,@PathVariable String key) {
		System.out.println("/updatetags/" );
		Token token = tokenService.getBykey(key);
		List<ma.azdad.model.TransportationRequestImage> list = transportationRequestImageRepos.findByTypeAndTransportationRequestId(type,detail.getId());
		ma.azdad.model.TransportationRequestImage image = list.get(list.size()-1);

		image.setLatitude(detail.getLatitude());
		image.setLongitude(detail.getLongitude());
		image.setTakenDate(detail.getTakenDate());
		image.setPhoneModel(detail.getPhoneModel());
		image.setGoogleAddress(detail.getGoogleAddress());

		transportationRequestImageRepos.save(image);

	}
	
	@GetMapping("/find-trimages/{key}/{type}/{id}")
	public List<TransportationRequestImage> findLightByWarehouseListMobile(@PathVariable String key,@PathVariable String type,@PathVariable Integer id) {
		System.out.println("/find-trimages/{key}/{state}");
		Token token = tokenService.getBykey(key);
		return transportationRequestService.findTrImages(type,id);
	}
	

}
