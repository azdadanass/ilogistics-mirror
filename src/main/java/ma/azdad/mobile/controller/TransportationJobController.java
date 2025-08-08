package ma.azdad.mobile.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.mobile.model.Token;
import ma.azdad.mobile.model.TransportationJob;
import ma.azdad.model.Role;
import ma.azdad.service.TokenService;
import ma.azdad.service.TransportationJobService;

@RestController
@RequestMapping("/mobile/tj")
public class TransportationJobController {

	@Autowired
	TokenService tokenService;

	@Autowired
	TransportationJobService transportationJobService;

	@GetMapping("/findone/{key}/{id}")
	public TransportationJob findOneLightMobile(@PathVariable String key, @PathVariable Integer id) {
		System.out.println("/findone/{key}/{id}");
		Token token = tokenService.getBykey(key);
		return transportationJobService.findOneMobile(id);
	}

	@GetMapping("/findbystatus/{key}/{state}")
	public List<TransportationJob> findLightByUserMobile(@PathVariable String key,
			@PathVariable Integer state) {
		System.out.println("/findbystatus/{key}/{state}");
		Token token = tokenService.getBykey(key);
		if (token.getRoleList().contains(Role.ROLE_ILOGISTICS_TM)) {
			return transportationJobService.findByUser1MobileByStatus(state, token.getUsername());
		} else {
			return transportationJobService.findByDriverMobileByStatus(state, token.getUsername());

		}
	}

}
