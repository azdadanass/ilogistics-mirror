package ma.azdad.utils;

public class ChartContainer {

	String title;
	private String id;
	private String chart;

	public ChartContainer(String title, String id, String chart) {
		super();
		this.title = title;
		this.id = id;
		this.chart = chart;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChart() {
		return chart;
	}

	public void setChart(String chart) {
		this.chart = chart;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}