package ma.azdad.utils;

public class GenericChart {
	protected String id;
	protected String title;
	protected String subtitle;
	protected String yAxisTitle;

	public GenericChart(String id, String title) {
		this.id = id;
		this.title = title;
	}

	public GenericChart(String id, String title, String subtitle) {
		this.id = id;
		this.title = title;
		this.subtitle = subtitle;
	}

}