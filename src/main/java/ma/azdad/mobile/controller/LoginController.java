package ma.azdad.mobile.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ma.azdad.mobile.model.Token;
import ma.azdad.mobile.model.User;
import ma.azdad.service.JobRequestService;
import ma.azdad.service.TokenService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;

@RestController
public class LoginController {

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	UserService userService;

	@Autowired
	TokenService tokenService;

	@PostMapping(path = "/mobile/login", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Token login(@RequestBody User user) {
		System.out.println("/mobile/login "+user.getLogin()+" "+user.getPassword());
		ma.azdad.model.User dbUser = userService.findByLogin(user.getLogin());
		if (dbUser == null)
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login not found");
		if (!StringUtils.equals(dbUser.getPassword(), UtilsFunctions.stringToMD5(user.getPassword())))
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad password");
		if(!dbUser.getIsWM())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied !");
		Token token = tokenService.generateToken(dbUser.getUsername());
		return token;
	}
	
	@GetMapping("/mobile/logout/{key}")
	public void logout(@PathVariable String key) {
		Token token = tokenService.getBykey(key);
		tokenService.clearToken(token.getUsername());
	}

}
