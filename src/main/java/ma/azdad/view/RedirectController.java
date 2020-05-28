package ma.azdad.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RedirectController {

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String baseUrlRedirect(HttpServletRequest request, HttpServletResponse httpServletResponse) {
		
		System.err.println("baseUrlRedirect");
		return "redirect:" + request.getRequestURL().append("index.xhtml").toString();
	}
}