package ma.azdad.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
		if (s != null) {
			ma.azdad.model.User user = userService.findOne(s);
			return new User(user.getUsername(), user.getPassword(), user.getRoleList().stream().map(i -> new SimpleGrantedAuthority(i.getRole().toString())).collect(Collectors.toList()));
		}

//		if (s != null)
//			if (!s.contains("@")) {
////				if (!"localhost".equals(UtilsFunctions.getServerName()))
////					throw new UsernameNotFoundException("");
//				ma.azdad.model.User user = userService.findOne(s);
//				return new User(user.getUsername(), user.getPassword(), user.getUserRoleList().stream().map(i -> new SimpleGrantedAuthority(i.getRole().toString())).collect(Collectors.toList()));
//			} else {
////				if (!"il.com".equals(UtilsFunctions.getServerName()))
////					throw new UsernameNotFoundException("");
//				ExternalResource er = externalResourceService.findOne(s);
//				return new User(er.getEmail(), er.getPassword(), er.getRoleList().stream().map(i -> new SimpleGrantedAuthority(i.getRole().toString())).collect(Collectors.toList()));
//			}

		return null;
	}

}