package ma.azdad.utils;

public enum App {
	APPS("apps", "apps.orange.telodigital.com", "192.168.100.121", true),
	IADMIN("iadmin", "iadmin.orange.telodigital.com", "192.168.100.121", true),
	SDM("sdm", "sdm.orange.telodigital.com", "192.168.100.121", true),
	ILOGISTICS("ilogistics", "ilogistics.orange.telodigital.com", "192.168.100.121", true),
	QR("qr", "qr.orange.telodigital.com", "192.168.100.121", true);

	private String value;
	private String link;
	private String ip;
	private Boolean cacheable;

	private App(String value, String link, String ip, Boolean cacheable) {
		this.value = value;
		this.link = link;
		this.ip = ip;
		this.cacheable = cacheable;
	}

	public String getValue() {
		return value;
	}

	public String getLink() {
		return link;
	}

	public String getIp() {
		return ip;
	}

	public Boolean getCacheable() {
		return cacheable;
	}

	public String getHttpLink() {
		return "http://" + link;
	}

	public String getHttpsLink() {
		return "https://" + link;
	}

}
