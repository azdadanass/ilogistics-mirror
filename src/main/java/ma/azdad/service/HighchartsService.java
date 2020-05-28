package ma.azdad.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.utils.GenericChart;
import ma.azdad.utils.Series;

@Component
@Transactional
public class HighchartsService {

	@Autowired
	private FileReaderService fileReaderService;

	public String generateLineBasic(String id, String title, String subtitle, String yAxisTitle, Series[] series) {
		return new LineBasic(id, title, subtitle, yAxisTitle, series).generateChart();
	}

	public String generateColumnChart(String id, String title, String subtitle, String[] categories, Series[] series) {
		ColumnChart chart = new ColumnChart(id, title, subtitle, categories, series);
		return chart.generateChart();
	}

	class LineBasic extends GenericChart {
		Series[] series;

		LineBasic(String id, String title, String subtitle, String yAxisTitle, Series[] series) {
			super(id, title, subtitle);
			this.series = series;
			this.yAxisTitle = yAxisTitle;
		}

		public String generateChart() {
			Map<String, String> map = new HashMap<>();
			map.put("id", id);
			map.put("title", title);
			map.put("subtitle", subtitle);
			map.put("yAxisTitle", yAxisTitle);
			map.put("series", Arrays.toString(series));
			return fileReaderService.readFile("classpath:hs/lb.htm", map);
		}

	}

	class ColumnChart extends GenericChart {
		String[] categories;
		Series[] series;

		ColumnChart(String id, String title, String subtitle, String[] categories, Series[] series) {
			super(id, title, subtitle);
			this.categories = categories;
			this.series = series;
		}

		public String generateChart() {
			Map<String, String> map = new HashMap<>();
			map.put("id", id);
			map.put("title", title);
			map.put("categories", UtilsFunctions.formatTab(categories));
			map.put("series", Arrays.toString(series));
			return fileReaderService.readFile("classpath:hs/cc.htm", map);
		}
	}

}
