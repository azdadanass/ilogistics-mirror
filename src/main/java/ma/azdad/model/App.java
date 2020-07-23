package ma.azdad.model;

public enum App {
	APPS("apps", "https://apps.orange.telodigital.com"),
	IADMIN("iadmin", "https://iadmin.orange.telodigital.com"),
	SDM("sdm", "https://sdm.orange.telodigital.com"),
	ILOGISTICS("ilogistics", "https://ilogistics.orange.telodigital.com"),
	QR("qr", "https://qr.orange.telodigital.com");

	private String value;
	private String link;

	private App(String value, String link) {
		this.value = value;
		this.link = link;
	}

	public String getValue() {
		return value;
	}

	public String getLink() {
		return link;
	}

}
