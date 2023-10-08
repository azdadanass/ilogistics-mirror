package ma.azdad.model;

import java.util.Arrays;
import java.util.List;

public enum RevenueType {
	GOODS_SUPPLY("Goods Supply"),
	SERVICE_SUPPLY("Service Supply"),
	GOODS_AND_SERVICE_SUPPLY("Goods And Service Supply"),
	FREIGHT_AND_TRANSIT("Customs, Freight & Transit");


	public static List<RevenueType> MAPPABLE_LIST;
	static {
		MAPPABLE_LIST = Arrays.asList(RevenueType.GOODS_SUPPLY);
	}

	private final String value;

	private RevenueType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value;
	}

}
