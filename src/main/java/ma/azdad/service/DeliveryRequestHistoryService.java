package ma.azdad.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestHistory;
import ma.azdad.model.User;

@Component
@Transactional
public class DeliveryRequestHistoryService extends GenericService<DeliveryRequestHistory> {

	public void created(DeliveryRequest deliveryRequest) {
		try {
			DeliveryRequestHistory deliveryRequestHistory = new DeliveryRequestHistory(new Date(), "Created", deliveryRequest, deliveryRequest.getRequester());
			save(deliveryRequestHistory);
		} catch (Exception e) {
			log.error("error creating deliveryRequestHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(DeliveryRequest deliveryRequest) {
		try {
			// TODO fill Description
			DeliveryRequestHistory deliveryRequestHistory = new DeliveryRequestHistory(new Date(), "Edited", deliveryRequest, deliveryRequest.getRequester());
			save(deliveryRequestHistory);
		} catch (Exception e) {
			log.error("error creating deliveryRequestHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void requested(DeliveryRequest deliveryRequest) {
		try {
			DeliveryRequestHistory deliveryRequestHistory = new DeliveryRequestHistory(new Date(), "Requested", deliveryRequest, deliveryRequest.getRequester());
			save(deliveryRequestHistory);
		} catch (Exception e) {
			log.error("error creating deliveryRequestHistory History (requested) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void requestedNew(DeliveryRequest deliveryRequest) {
		deliveryRequest.getHistoryList().add(new DeliveryRequestHistory(new Date(), "Requested", deliveryRequest, deliveryRequest.getRequester()));
	}

	public void approvedNew(DeliveryRequest deliveryRequest, User user) {
		deliveryRequest.getHistoryList().add(new DeliveryRequestHistory(new Date(), "Approved L1", deliveryRequest, user));
	}

	public void approvedHm(DeliveryRequest deliveryRequest, User user) {
		deliveryRequest.getHistoryList().add(new DeliveryRequestHistory(new Date(), "Approved L2", deliveryRequest, user));
	}

	public void rejectedNew(DeliveryRequest deliveryRequest, User user) {
		deliveryRequest.getHistoryList().add(new DeliveryRequestHistory(new Date(), "Rejected", deliveryRequest, user));
	}

	public void canceledNew(DeliveryRequest deliveryRequest, User user) {
		deliveryRequest.getHistoryList().add(new DeliveryRequestHistory(new Date(), "Canceled", deliveryRequest, user));
	}

	public void approved(DeliveryRequest deliveryRequest, User user) {
		try {
			DeliveryRequestHistory deliveryRequestHistory = new DeliveryRequestHistory(new Date(), "Approved", deliveryRequest, user);
			save(deliveryRequestHistory);
		} catch (Exception e) {
			log.error("error creating deliveryRequestHistory History (approved) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void rejected(DeliveryRequest deliveryRequest, User user) {
		try {
			DeliveryRequestHistory deliveryRequestHistory = new DeliveryRequestHistory(new Date(), "Rejected", deliveryRequest, user, deliveryRequest.getRejectionReason());
			save(deliveryRequestHistory);
		} catch (Exception e) {
			log.error("error creating deliveryRequestHistory History (rejected) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void deliveredNew(DeliveryRequest deliveryRequest, User user) {
		deliveryRequest.getHistoryList().add(new DeliveryRequestHistory(new Date(), "Delivred", deliveryRequest, user));
	}

	public void acknowledgedNew(DeliveryRequest deliveryRequest) {
		deliveryRequest.getHistoryList().add(new DeliveryRequestHistory(new Date(), "Acknowledged", deliveryRequest, deliveryRequest.getRequester()));
	}

	public void delivred(DeliveryRequest deliveryRequest, User user) {
		try {
			DeliveryRequestHistory deliveryRequestHistory = new DeliveryRequestHistory(new Date(), "Delivred", deliveryRequest, user);
			save(deliveryRequestHistory);
		} catch (Exception e) {
			log.error("error creating deliveryRequestHistory History (delivred) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void partiallyDelivred(DeliveryRequest deliveryRequest, User user) {
		try {
			DeliveryRequestHistory deliveryRequestHistory = new DeliveryRequestHistory(new Date(), "Partially Delivered", deliveryRequest, user);
			save(deliveryRequestHistory);
		} catch (Exception e) {
			log.error("error creating deliveryRequestHistory History (partiallyDelivred) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void acknowledged(DeliveryRequest deliveryRequest) {
		try {
			DeliveryRequestHistory deliveryRequestHistory = new DeliveryRequestHistory(new Date(), "Acknowledged", deliveryRequest, deliveryRequest.getRequester());
			save(deliveryRequestHistory);
		} catch (Exception e) {
			log.error("error creating deliveryRequestHistory History (acknowledged) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void canceled(DeliveryRequest deliveryRequest, User user) {
		try {
			DeliveryRequestHistory deliveryRequestHistory = new DeliveryRequestHistory(new Date(), "Canceled", deliveryRequest, user);
			save(deliveryRequestHistory);
		} catch (Exception e) {
			log.error("error creating deliveryRequestHistory History (canceled) : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
