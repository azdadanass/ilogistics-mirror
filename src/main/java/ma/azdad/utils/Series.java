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
	
	public static class Data1 implements Comparable<Data1> {
		String name;
		Double y;

		public Data1(String name, Double y) {
			super();
			this.name = name;
			this.y = y;
		}

		public String getName() {
			return "'" + name + "'";
		}

		public Double getY() {
			return y;
		}

		@Override
		public String toString() {
			return "Data1 [name=" + name + ", y=" + y + "]";
		}

		@Override
		public int compareTo(Data1 o) {
			try {
				return o.getY().compareTo(y);
			} catch (Exception e) {
				return -1;
			}
		}

	}
}