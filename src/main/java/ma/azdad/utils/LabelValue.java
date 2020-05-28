package ma.azdad.utils;

public class LabelValue {
	private String label;
	private String stringValue;
	private Integer integerValue;
	private String beanName;

	public LabelValue() {
		super();
	}

	public LabelValue(String label, Integer integerValue, String beanName) {
		super();
		this.label = label;
		this.integerValue = integerValue;
		this.beanName = beanName;
	}

	public LabelValue(String label, String stringValue, String beanName) {
		super();
		this.label = label;
		this.stringValue = stringValue;
		this.beanName = beanName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof LabelValue))
			return false;
		LabelValue o = (LabelValue) obj;
		if (integerValue != null)
			return integerValue.equals(o.getIntegerValue()) && (beanName == null || beanName.equals(o.getBeanName()));
		if (stringValue != null)
			return stringValue.equals(o.getStringValue()) && (beanName == null || beanName.equals(o.getBeanName()));
		return false;
	}

	public String getLabel() {
		return label;
	}

	public String getStringValue() {
		return stringValue;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public String toString() {
		return "LabelValue [label=" + label + ", stringValue=" + stringValue + ", integerValue=" + integerValue + ", beanName=" + beanName + "]\n";
	}

}
