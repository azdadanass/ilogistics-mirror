package ma.azdad.utils;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.stereotype.Component;

import ma.azdad.model.User;

@Component
@FacesConverter(value = "userConverter")
public class UserConverter implements Converter {

	private Map<String, User> map = new HashMap<>();

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String userUsername) {
		return map.get(userUsername);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value == null)
			return null;
		User user = (User) value;
		map.put(user.getUsername(), user);
		System.out.println("----------->" + map);
		return user.getUsername();
	}

}