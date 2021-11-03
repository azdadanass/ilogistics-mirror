package ma.azdad.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import ma.azdad.model.User;
import ma.azdad.service.AccessLogService;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private UserService userService;

	@Autowired
	private AccessLogService accessLogService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		User user = userService.findByLogin(((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername());
		if (user.getFailedAttempt() > 0)
			userService.resetFailedAttempts(user.getLogin());
		accessLogService.addSuccess("succesful login : " + user.getLogin(), UtilsFunctions.getRemoteIpAddress(request));
		super.onAuthenticationSuccess(request, response, authentication);
	}

}