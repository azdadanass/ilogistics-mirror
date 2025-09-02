package ma.azdad.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobCapacity;
import ma.azdad.model.TransportationRequest;
import ma.azdad.repos.TransportationRequestRepos;

@Service
public class CapacityService {

	@Autowired
	private TransportationRequestRepos transportationRequestRepos;

	public List<TransportationJobCapacity> calculatePlannedCapacities(Integer jobId) {
		List<TransportationRequest> requests = transportationRequestRepos.findByTransportationJobId(jobId);

		List<TransportationJobCapacity> result = new ArrayList<>();

		// Build pickup & delivery events
		class CapacityEvent {
			Date date;
			double weight;
			double volume;
			TransportationJob job;

			CapacityEvent(Date date, double weight, double volume, TransportationJob job) {
				this.date = date;
				this.weight = weight;
				this.volume = volume;
				this.job = job;
			}
		}

		List<CapacityEvent> events = new ArrayList<>();

		for (TransportationRequest tr : requests) {
			double w = tr.getGrossWeight() != null ? tr.getGrossWeight() : 0d;
			double v = tr.getVolume() != null ? tr.getVolume() : 0d;

			if (tr.getPlannedPickupDate() != null) {
				events.add(new CapacityEvent(tr.getPlannedPickupDate(), +w, +v, tr.getTransportationJob()));
			}
			if (tr.getPlannedDeliveryDate() != null) {
				events.add(new CapacityEvent(tr.getPlannedDeliveryDate(), -w, -v, tr.getTransportationJob()));
			}
		}

		// Sort by date
		events.sort(Comparator.comparing(e -> e.date));

		double cumulativeWeight = 0d;
		double cumulativeVolume = 0d;

		for (CapacityEvent e : events) {
			cumulativeWeight += e.weight;
			cumulativeVolume += e.volume;

			result.add(new TransportationJobCapacity(e.job, "Planned", e.date, e.weight, e.volume, cumulativeWeight,
					cumulativeVolume));
		}

		return result;
	}
}
