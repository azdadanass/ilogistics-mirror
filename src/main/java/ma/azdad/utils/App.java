package ma.azdad.utils;

public enum App {
	APPS("apps", "apps.3gcominside.com", "192.168.100.71", true),
	COMPTA("compta", "compta.3gcominside.com", "192.168.100.72", true),
	IADMIN("iadmin", "iadmin.3gcominside.com", "192.168.100.73", true),
	IBUY("ibuy", "ibuy.3gcominside.com", "192.168.100.74", true),
	IBUYSUPPLIER("ibuysupplier", "ibuy.3gcom.ma", "192.168.100.75", true),
	IEXPENSE("iexpense", "iexpense.3gcominside.com", "192.168.100.76", false),
	IFINANCE("ifinance", "ifinance.3gcominside.com", "192.168.100.77", false),
	ILOGISTICS("ilogistics", "ilogistics.3gcominside.com", "192.168.100.78", true),
	INVOICE("invoice", "invoice.3gcominside.com", "192.168.100.79", true),
	MYHR("myhr", "myhr.3gcominside.com", "192.168.100.80", false),
	MYOFFICE("myoffice", "myoffice.3gcominside.com", "192.168.100.81", true),
	MYREPORTS("myreports", "myreports.3gcominside.com", "192.168.100.82", false),
	MYTOOLS("mytools", "mytools.3gcominside.com", "192.168.100.83", false),
	QR("qr", "qr.3gcom.ma", "192.168.100.84", true),
	SDM("sdm", "sdm.3gcominside.com", "192.168.100.85", true),
	SDMMOBILE("sdmmobile", "sdm.3gcom.ma", "192.168.100.86", true),
	WTR("wtr", "wtr.3gcominside.com", "192.168.100.87", false),
	UTILS("utils", "utils.3gcominside.com", "192.168.100.177", false);

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
