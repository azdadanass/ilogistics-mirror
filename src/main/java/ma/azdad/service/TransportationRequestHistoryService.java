package ma.azdad.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestHistory;
import ma.azdad.model.User;

@Component
@Transactional
public class TransportationRequestHistoryService extends GenericService<TransportationRequestHistory> {

	public void created(TransportationRequest transportationRequest) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Created", transportationRequest,
					transportationRequest.getDeliveryRequest().getRequester());
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(TransportationRequest transportationRequest) {
		try {
			// TODO fill Description
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Edited", transportationRequest,
					transportationRequest.getDeliveryRequest().getRequester());
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void requestedNew(TransportationRequest transportationRequest) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory(new Date(), "Requested", transportationRequest, transportationRequest.getDeliveryRequest().getRequester()));
	}

	public void requested(TransportationRequest transportationRequest) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Requested", transportationRequest,
					transportationRequest.getDeliveryRequest().getRequester());
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (requested) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void approvedNew(TransportationRequest transportationRequest, User user) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory(new Date(), "Approved", transportationRequest, user));
	}

	public void approved(TransportationRequest transportationRequest, User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Approved", transportationRequest, user);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (approved) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void rejectedNew(TransportationRequest transportationRequest, User user, String reason) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory(new Date(), "Rejected", transportationRequest, user, reason));
	}

	public void rejected(TransportationRequest transportationRequest, User user, String reason) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Rejected", transportationRequest, user, reason);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (rejected) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void delivredNew(TransportationRequest transportationRequest, User user) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory(new Date(), "Delivred", transportationRequest, user));
	}

	public void delivred(TransportationRequest transportationRequest, User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Delivred", transportationRequest, user);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (delivred) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void assignedNew(TransportationRequest transportationRequest, User user) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory(new Date(), "Assigned", transportationRequest, user));
	}

	public void assigned(TransportationRequest transportationRequest, User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Assigned", transportationRequest, user);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (assigned) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void pickedupNew(TransportationRequest transportationRequest, User user) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory(new Date(), "Picked up", transportationRequest, user));
	}

	public void pickedup(TransportationRequest transportationRequest, User user) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Picked up", transportationRequest, user);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (pickedup) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void acknowledgedNew(TransportationRequest transportationRequest) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory(new Date(), "Acknowledged", transportationRequest, transportationRequest.getDeliveryRequest().getRequester()));
	}

	public void acknowledged(TransportationRequest transportationRequest) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Acknowledged", transportationRequest,
					transportationRequest.getDeliveryRequest().getRequester());
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (acknowledged) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void canceledNew(TransportationRequest transportationRequest, User user, String reason) {
		transportationRequest.getHistoryList().add(new TransportationRequestHistory(new Date(), "Canceled", transportationRequest, user, reason));
	}

	public void canceled(TransportationRequest transportationRequest, User user, String reason) {
		try {
			TransportationRequestHistory transportationRequestHistory = new TransportationRequestHistory(new Date(), "Canceled", transportationRequest, user, reason);
			save(transportationRequestHistory);
		} catch (Exception e) {
			log.error("error creating transportationRequestHistory History (canceled) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
