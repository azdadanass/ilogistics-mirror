package ma.azdad.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ma.azdad.model.User;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		if (s == null)
			return null;
		User user = userService.findByLogin(s);

		return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), user.isEnabled(), true, true, user.getAccountNonLocked(), //
				user.getRoleList().stream().map(ur -> new SimpleGrantedAuthority(ur.getRole().name())).collect(Collectors.toList()));
//		return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_SDM")));
	}
}