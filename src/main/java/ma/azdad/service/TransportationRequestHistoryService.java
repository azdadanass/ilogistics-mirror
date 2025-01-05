package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestHistory;
import ma.azdad.model.User;
import ma.azdad.repos.TransportationRequestHistoryRepos;

@Component
public class TransportationRequestHistoryService extends GenericService<Integer, TransportationRequestHistory, TransportationRequestHistoryRepos> {

	public void created(TransportationRequest transportationRequest,User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Created",user, null, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(TransportationRequest transportationRequest,User user) {
		try {
			// TODO fill Description
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Edited", user, null, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void requestedNew(TransportationRequest transportationRequest,User user) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory("Requested", user, null, transportationRequest));
	}

	public void requested(TransportationRequest transportationRequest,User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Requested", user, null, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (requested) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void approvedNew(TransportationRequest transportationRequest, User user) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory("Approved", user, null, transportationRequest));
	}

	public void approved(TransportationRequest transportationRequest, User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Approved", user, null, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (approved) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void rejectedNew(TransportationRequest transportationRequest, User user, String reason) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory("Rejected", user, reason, transportationRequest));
	}

	public void rejected(TransportationRequest transportationRequest, User user, String reason) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Rejected", user, reason, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (rejected) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void delivredNew(TransportationRequest transportationRequest, User user) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory("Delivred", user, null, transportationRequest));
	}

	public void delivred(TransportationRequest transportationRequest, User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Delivred", user, null, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (delivred) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void assignedNew(TransportationRequest transportationRequest, User user) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory("Assigned", user, null, transportationRequest));
	}

	public void assigned(TransportationRequest transportationRequest, User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Assigned", user, null, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (assigned) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void pickedupNew(TransportationRequest transportationRequest, User user) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory("Picked up", user, null, transportationRequest));
	}

	public void pickedup(TransportationRequest transportationRequest, User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Picked up", user, null, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (pickedup) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void acknowledgedNew(TransportationRequest transportationRequest) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory("Acknowledged", transportationRequest.getDeliveryRequest().getRequester(), null, transportationRequest));
	}

	public void acknowledged(TransportationRequest transportationRequest,User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Acknowledged", user, null, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (acknowledged) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void canceledNew(TransportationRequest transportationRequest, User user, String reason) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory("Canceled", user, reason, transportationRequest));
	}

	public void canceled(TransportationRequest transportationRequest, User user, String reason) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory("Canceled", user, reason, transportationRequest);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (canceled) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
