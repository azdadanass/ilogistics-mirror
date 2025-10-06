package ma.azdad.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import ma.azdad.mobile.model.Token;

@Component
public class TokenService {

	@Autowired
	UserService userService;
	
	@Autowired
	WarehouseService warehouseService;

	private Map<String, Token> userTokenMap = new HashMap<String, Token>();

	private String key;

	public Token generateToken(String username,String version) {
		Token token = userTokenMap.getOrDefault(username, new Token());
		do {
			key = UtilsFunctions.generateKey();
		} while (userTokenMap.values().stream().filter(i -> i.getKey().equals(key)).count() > 0);
		token.setUsername(username);
		token.setKey(key);
		token.setUser(userService.findOneMobile(username));
		token.setRoleList(userService.findRoleList(username));
		token.setWarehouseList(warehouseService.findIdListByManager(username));
		token.updateExpirationTime();
		if(version.equals("2.0.1")) {
			token.setIsValidVersion(true);
		}
		else {
			token.setIsValidVersion(false);
		}
		System.out.println("Version = "+version);
		System.out.println("Version = "+token.getIsValidVersion());
		userTokenMap.put(username, token);
		System.out.println(userTokenMap);
		return token;
	}
	
	
	
	public void clearToken(String username) {
		userTokenMap.remove(username);
		System.out.println(userTokenMap);
	}

	public Token getBykey(String key) throws ResponseStatusException  {
		Token token = userTokenMap.values().stream().filter(i -> i.getKey().equals(key)).findFirst().orElse(null); 
		if(token ==null || !token.isActive())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired Token");
		token.updateExpirationTime();
		return token;
	}

}
