package ma.azdad.model;

import java.util.Arrays;
import java.util.List;

import ma.azdad.utils.Color;

public enum StockRowState {
	NORMAL("Normal", Color.GREEN, Arrays.asList(StockRowStatus.NORMAL, StockRowStatus.BRAND_NEW, StockRowStatus.USED)),
	FAULTY("Faulty", Color.RED, Arrays.asList(StockRowStatus.FAULTY, StockRowStatus.WATER_DAMAGE, StockRowStatus.PHYSICAL_DAMAGE, StockRowStatus.FIRE_DAMAGE)),;

	private final String value;
	private final Color color;
	private final List<StockRowStatus> statusList;

	private StockRowState(String value, Color color, List<StockRowStatus> statusList) {
		this.value = value;
		this.color = color;
		this.statusList = statusList;
	}

	public String getValue() {
		return value;
	}

	public Color getColor() {
		return color;
	}

	public List<StockRowStatus> getStatusList() {
		return statusList;
	}

	@Override
	public String toString() {
		return this.value;
	}
}
