package ma.azdad.service;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Transactional
public class ChartService {

	public final static String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public String generateColumnChart(String id, String title, String[] categories, Series[] series) {
		return new ColumnChart(id, title, categories, series).generateChart();
	}

	static class GenericChart {
		String id;
		String title;

		public GenericChart(String id, String title) {
			this.id = id;
			this.title = title;
		}

		public static String formatTab(String[] tab) {
			String[] result = Arrays.copyOf(tab, tab.length);
			for (int i = 0; i < result.length; i++)
				result[i] = "'" + result[i] + "'";
			return Arrays.toString(result);
		}
	}

	static class ColumnChart extends GenericChart {
		String[] categories;
		Series[] series;

		ColumnChart(String id, String title, String[] categories, Series[] series) {
			super(id, title);
			this.categories = categories;
			this.series = series;

		}

		public String generateChart() {
			String result = "<script type=\"text/javascript\">";
			result += "$(function () {Highcharts.chart('" + id + "', {chart: {type: 'column'},title: {text: '" + title + "'},xAxis: {categories: " + formatTab(categories) + "},credits: {enabled: false},series: "
					+ Arrays.toString(series) + "});});";
			result += "</script>";
			return result;
		}

	}

	public static class Series {
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
}
