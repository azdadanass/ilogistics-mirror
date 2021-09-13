package ma.azdad.utils;

public enum App {
	APPS("apps", "apps.orange.telodigital.com", "192.168.100.111", true),
	IADMIN("iadmin", "iadmin.orange.telodigital.com", "192.168.100.112", true),
	ILOGISTICS("ilogistics", "ilogistics.orange.telodigital.com", "192.168.100.113", true),
	QR("qr", "qr.orange.telodigital.com", "192.168.100.114", true),
	SDM("sdm", "sdm.orange.telodigital.com", "192.168.100.115", true),
	MYREPORTS("myreports", "myreports.orange.telodigital.com", "192.168.100.116", false),
	UTILS("utils", "192.168.100.61", "192.168.100.61", false),
	PUBLIC("public", "public.orange.telodigital.com", "192.168.100.117", false);

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
