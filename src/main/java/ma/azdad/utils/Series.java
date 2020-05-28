package ma.azdad.utils;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Series {
	String name;
	String color;
	Double[] data;

	public Series(String name, String color, Double[] data) {
		super();
		this.name = name;
		this.color = color;
		this.data = data;
	}

	@Override
	public String toString() {
		try {
			if (!name.startsWith("'"))
				name = "'" + name + "'";
			if (!color.startsWith("'"))
				color = "'" + color + "'";
			ObjectMapper mapper = new ObjectMapper();
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			return mapper.writeValueAsString(this).replace("\"", "");
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}