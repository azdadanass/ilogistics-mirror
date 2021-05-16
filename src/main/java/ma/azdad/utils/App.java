package ma.azdad.utils;

public enum App {
	APPS("apps", "apps.orange.gcom", "192.168.100.111", true),
	IADMIN("iadmin", "iadmin.orange.gcom", "192.168.100.112", true),
	ILOGISTICS("ilogistics", "ilogistics.orange.gcom", "192.168.100.113", true),
	QR("qr", "qr.orange.gcom", "192.168.100.114", true),
	SDM("sdm", "sdm.orange.gcom", "192.168.100.115", true),
	MYREPORTS("myreports", "myreports.orange.gcom", "192.168.100.116", true),
	UTILS("utils", "utils.gcom", "192.168.100.177", false);

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
