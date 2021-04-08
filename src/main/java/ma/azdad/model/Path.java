package ma.azdad.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity

public class Path extends GenericBean implements Serializable {

	private Double estimatedDuration = 0.0;
	private String estimatedDurationText;
	private Double estimatedDistance = 0.0;
	private String estimatedDistanceText;

	private TransportationJob transportationJob;
	private Stop from;
	private Stop to;

	public Path() {
		super();
	}

	public Path(Double estimatedDistance, String estimatedDistanceText) {
		super();
		this.estimatedDistance = estimatedDistance;
		this.estimatedDistanceText = estimatedDistanceText;
	}

	public Path(Double estimatedDuration, String estimatedDurationText, Double estimatedDistance, String estimatedDistanceText) {
		super();
		this.estimatedDuration = estimatedDuration;
		this.estimatedDurationText = estimatedDurationText;
		this.estimatedDistance = estimatedDistance;
		this.estimatedDistanceText = estimatedDistanceText;
	}

	public Path(Double estimatedDuration, String estimatedDurationText, Double estimatedDistance, String estimatedDistanceText, TransportationJob transportationJob, Stop from, Stop to) {
		super();
		this.estimatedDuration = estimatedDuration;
		this.estimatedDurationText = estimatedDurationText;
		this.estimatedDistance = estimatedDistance;
		this.estimatedDistanceText = estimatedDistanceText;
		this.transportationJob = transportationJob;
		this.from = from;
		this.to = to;
	}

	public boolean filter(String query) {
		boolean result = super.filter(query);
		if (!result && from != null)
			result = from.filter(query);
		return result;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Stop getFrom() {
		return from;
	}

	public void setFrom(Stop from) {
		this.from = from;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Stop getTo() {
		return to;
	}

	public void setTo(Stop to) {
		this.to = to;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	public TransportationJob getTransportationJob() {
		return transportationJob;
	}

	public void setTransportationJob(TransportationJob transportationJob) {
		this.transportationJob = transportationJob;
	}

	public Double getEstimatedDuration() {
		return estimatedDuration;
	}

	public void setEstimatedDuration(Double estimatedDuration) {
		this.estimatedDuration = estimatedDuration;
	}

	public String getEstimatedDurationText() {
		return estimatedDurationText;
	}

	public void setEstimatedDurationText(String estimatedDurationText) {
		this.estimatedDurationText = estimatedDurationText;
	}

	public Double getEstimatedDistance() {
		return estimatedDistance;
	}

	public void setEstimatedDistance(Double estimatedDistance) {
		this.estimatedDistance = estimatedDistance;
	}

	public String getEstimatedDistanceText() {
		return estimatedDistanceText;
	}

	public void setEstimatedDistanceText(String estimatedDistanceText) {
		this.estimatedDistanceText = estimatedDistanceText;
	}

	@Override
	public String toString() {
		return "Path [from=" + from.getPlaceName() + ", to=" + to.getPlaceName() + "]";
	}

}
