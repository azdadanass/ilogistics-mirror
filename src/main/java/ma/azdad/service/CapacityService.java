package ma.azdad.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobCapacity;
import ma.azdad.model.TransportationRequest;
import ma.azdad.repos.TransportationJobCapacityRepos;
import ma.azdad.repos.TransportationRequestRepos;

@Service
public class CapacityService {

	@Autowired
	private TransportationRequestService transportationRequestService;

	@Autowired
	TransportationJobCapacityRepos transportationJobCapacityRepos;

	@Transactional
	public List<TransportationJobCapacity> calculatePlannedCapacities(Integer jobId) {
		// 1. Remove old planned capacities
		transportationJobCapacityRepos.deleteByTransportationJobIdAndType(jobId, "Planned");

		List<TransportationRequest> requests = transportationRequestService.findByTransportationJobId(jobId);
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
			TransportationRequest request = transportationRequestService.findOne(tr.getId());
			double w = request.getGrossWeight() != null ? request.getGrossWeight() : 0d;
			double v = request.getVolume() != null ? request.getVolume() : 0d;

			if (request.getPlannedPickupDate() != null) {
				events.add(new CapacityEvent(request.getPlannedPickupDate(), +w, +v, request.getTransportationJob()));
			}
			if (request.getPlannedDeliveryDate() != null) {
				events.add(new CapacityEvent(request.getPlannedDeliveryDate(), -w, -v, request.getTransportationJob()));
			}
		}

		// Sort by date
		events.sort(Comparator.comparing(e -> e.date));

		double cumulativeWeight = 0d;
		double cumulativeVolume = 0d;

		for (CapacityEvent e : events) {
			cumulativeWeight += e.weight;
			cumulativeVolume += e.volume;

			result.add(new TransportationJobCapacity(e.job, "Planned", e.date, round3(e.weight), round3(e.volume),
					round3(cumulativeWeight), round3(cumulativeVolume)));
		}

		// 2. Save new ones
		return transportationJobCapacityRepos.saveAll(result);
	}

	public void pickupVolumeAndWeight(Integer trId) {
	    TransportationRequest tr = transportationRequestService.findOne(trId);

	    // Get last known cumulative values (default to 0 if none exist)
	    Double maxCumulativeWeight = transportationJobCapacityRepos
	            .findMaxCumulativeWeightByTransportationJobIdAndType(tr.getTransportationJob().getId(), "Real");
	    if (maxCumulativeWeight == null) {
	        maxCumulativeWeight = 0d;
	    }

	    Double maxCumulativeVolume = transportationJobCapacityRepos
	            .findMaxCumulativeVolumeByTransportationJobIdAndType(tr.getTransportationJob().getId(), "Real");
	    if (maxCumulativeVolume == null) {
	        maxCumulativeVolume = 0d;
	    }

	    // Add this TR’s pickup
	    Double newCumulativeWeight = maxCumulativeWeight + (tr.getGrossWeight() != null ? tr.getGrossWeight() : 0d);
	    Double newCumulativeVolume = maxCumulativeVolume + (tr.getVolume() != null ? tr.getVolume() : 0d);

	    // Create capacity row
	    TransportationJobCapacity capacity = new TransportationJobCapacity(
	            tr.getTransportationJob(),
	            "Real",
	            new Date(), // or use tr.getPlannedPickupDate() if relevant
	            tr.getGrossWeight(),
	            tr.getVolume(),
	            newCumulativeWeight,
	            newCumulativeVolume
	    );

	    transportationJobCapacityRepos.save(capacity);
	}


	public void deliverVolumeAndWeight(Integer trId) {
	    TransportationRequest tr = transportationRequestService.findOne(trId);

	    // Get last known cumulative values (default to 0 if none exist)
	    Double maxCumulativeWeight = transportationJobCapacityRepos
	            .findMaxCumulativeWeightByTransportationJobIdAndType(tr.getTransportationJob().getId(), "Real");
	    if (maxCumulativeWeight == null) {
	        maxCumulativeWeight = 0d;
	    }

	    Double maxCumulativeVolume = transportationJobCapacityRepos
	            .findMaxCumulativeVolumeByTransportationJobIdAndType(tr.getTransportationJob().getId(), "Real");
	    if (maxCumulativeVolume == null) {
	        maxCumulativeVolume = 0d;
	    }

	    // Subtract this TR’s delivery
	    Double newCumulativeWeight = maxCumulativeWeight - (tr.getGrossWeight() != null ? tr.getGrossWeight() : 0d);
	    Double newCumulativeVolume = maxCumulativeVolume - (tr.getVolume() != null ? tr.getVolume() : 0d);

	    // Create capacity row
	    TransportationJobCapacity capacity = new TransportationJobCapacity(
	            tr.getTransportationJob(),
	            "Real",
	            new Date(), // or use tr.getPlannedDeliveryDate() if relevant
	            -(tr.getGrossWeight() != null ? tr.getGrossWeight() : 0d), // negative to indicate delivery
	            -(tr.getVolume() != null ? tr.getVolume() : 0d),           // negative to indicate delivery
	            newCumulativeWeight,
	            newCumulativeVolume
	    );

	    transportationJobCapacityRepos.save(capacity);
	}


	public List<TransportationJobCapacity> simulatePlannedCapacities(Integer jobId,
			List<TransportationRequest> newRequests) {

		// 1️⃣ Fetch existing requests for the job
		List<TransportationRequest> existingRequests = transportationRequestService.findByTransportationJobId(jobId);

		// 1.1 🔄 Reload each existing TR to ensure it’s fully initialized
		List<TransportationRequest> fullyLoadedExistingRequests = new ArrayList<>();
		for (TransportationRequest tr : existingRequests) {
		    if (tr.getId() != null) {
		        TransportationRequest fullTr = transportationRequestService.findOne(tr.getId());
		        if (fullTr != null) {
		            fullyLoadedExistingRequests.add(fullTr);
		        }
		    }
		}

		// 2️⃣ Fully load new requests from DB if provided
		List<TransportationRequest> fullyLoadedNewRequests = new ArrayList<>();
		if (newRequests != null) {
		    for (TransportationRequest tr : newRequests) {
		        if (tr.getId() != null) {
		            TransportationRequest fullTr = transportationRequestService.findOne(tr.getId());
		            if (fullTr != null) {
		                fullyLoadedNewRequests.add(fullTr);
		            }
		        }
		    }
		}

		// 3️⃣ Combine existing + fully loaded new requests
		List<TransportationRequest> allRequests = new ArrayList<>(fullyLoadedExistingRequests);
		allRequests.addAll(fullyLoadedNewRequests);


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

		for (TransportationRequest tr : allRequests) {
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

			result.add(new TransportationJobCapacity(e.job, "Planned", e.date, round3(e.weight), round3(e.volume),
					round3(cumulativeWeight), round3(cumulativeVolume)));
		}

		return result; // ✅ just return, do NOT save
	}

	private double round3(double value) {
		return BigDecimal.valueOf(value).setScale(3, RoundingMode.HALF_UP).doubleValue();
	}

}
