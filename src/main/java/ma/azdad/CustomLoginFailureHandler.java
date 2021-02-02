package ma.azdad;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import ma.azdad.model.User;
import ma.azdad.service.UserService;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserService userService;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		String login = request.getParameter("username");
		User user = userService.findByLogin(login);

		if (user != null) {
			if (user.isEnabled() && user.isAccountNonLocked()) {
				if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
					userService.increaseFailedAttempts(user);
				} else {
					userService.lock(user);
					exception = new LockedException("Your account has been locked due to 3 failed attempts." + " It will be unlocked after 24 hours.");
				}
			} else if (!user.isAccountNonLocked()) {
				if (userService.unlockWhenTimeExpired(user)) {
					exception = new LockedException("Your account has been unlocked. Please try to login again.");
				}
			}

		}

		super.setDefaultFailureUrl("/login.xhtml");
		super.onAuthenticationFailure(request, response, exception);
	}

}