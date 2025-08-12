package ma.azdad.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.ObjectUtils;

import ma.azdad.utils.Color;

public enum TransportationJobState {
	// new workflow
	PENDING("Pending", Color.ORANGE, Arrays.asList(TransportationJobStatus.EDITED)), //
	ASSIGNED("Assigned", Color.BLUE, Arrays.asList(TransportationJobStatus.ASSIGNED1, TransportationJobStatus.ASSIGNED2)), //
	ACCEPTED("Accepted", Color.L_BLUE, Arrays.asList(TransportationJobStatus.ACCEPTED)), //
	IN_PROGRESS("In Progress", Color.PURPLE, Arrays.asList(TransportationJobStatus.STARTED, TransportationJobStatus.IN_PROGRESS)), //
	COMPLETED("Completed", Color.GREEN, Arrays.asList(TransportationJobStatus.COMPLETED,TransportationJobStatus.CLOSED)),
	NEW("New", Color.GREEN, Arrays.asList(TransportationJobStatus.EDITED,TransportationJobStatus.ASSIGNED1,TransportationJobStatus.ASSIGNED2)),
	
	
	;

	private final String value;
	private final Color color;
	private final List<TransportationJobStatus> statusList;

	private TransportationJobState(String value, Color color, List<TransportationJobStatus> statusList) {
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

	public List<TransportationJobStatus> getStatusList() {
		return statusList;
	}

	public String getBadge() {
		return "badge badge-" + color;
	}

	@Override
	public String toString() {
		return this.value;
	}
	
	static public TransportationJobState get(Integer ordinal) {
		if(ObjectUtils.equals(ordinal, -1))
			return null;
		try {
			return TransportationJobState.values()[ordinal];
		} catch (Exception e) {
			return null;
		}
	}

	public static TransportationJobState getByValue(String value) {
		try {
			return Stream.of(values()).filter(i -> value.equals(i.getValue())).findFirst().get();
		} catch (Exception e) {
			return null;
		}
	}
}
