package ma.azdad.view;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ManagedBean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.User;
import ma.azdad.service.UserService;
import ma.azdad.service.UtilsFunctions;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class HtmlView implements Serializable {

	@Autowired
	private UserService userService;

	public String generateOverlayPanelContent(String identifer) {
		return generateOverlayPanelContent(identifer, null);
	}

	public String generateOverlayPanelContent(String identifer1, String identifer2, Date date) {
		return generateOverlayPanelContent(StringUtils.firstNonBlank(identifer1, identifer2), date);
	}

	public String generateOverlayPanelContent(String username, Date date) {
		if (username == null || username.isEmpty())
			return null;
		User user = userService.findAsMap().get(username);
		StringBuilder sb = new StringBuilder();
		sb.append("	<div class=\"col-xs-3\">");
		sb.append("		<img src=\"" + user.getPublicPhoto() + "\" class=\"grid_images\" />");
		sb.append("	</div>");
		sb.append("	<div class=\"col-xs-9\">");
		sb.append("		<span class=\"bolder green lead\" >" + user.getFullName() + "</span>");
		sb.append("		<br />");
		sb.append("		<span class=\"bolder blue\" >" + user.getJob() + "</span>");
		sb.append("		<br />");
		sb.append("		<i class=\"ace-icon fa fa-phone middle bigger-120 orange\"></i>");
		sb.append("		<span class=\"orange\">" + user.getPhone() + "</span>");
		sb.append("		<br />");
		sb.append("		<i class=\"middle bigger-120 pink bolder\">@</i>");
		sb.append("		<span class=\"pink\">" + user.getEmail() + "</span>");
		sb.append("		<br />");
		if (date != null) {
			sb.append("		<i class=\"ace-icon fa fa-calendar middle bigger-120 red\"></i>");
			sb.append("		<span class=\"red\">" + "Action Date: " + UtilsFunctions.getFormattedDateTime(date) + "</span>");
		}
		sb.append("		<hr />");
		sb.append("	</div>");
		return sb.toString();
	}

}
