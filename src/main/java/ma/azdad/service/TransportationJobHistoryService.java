package ma.azdad.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobHistory;
import ma.azdad.model.User;

@Component
@Transactional
public class TransportationJobHistoryService extends GenericService<TransportationJobHistory> {

	public void created(TransportationJob transportationJob, User user) {
		try {
			TransportationJobHistory transportationJobHistory = new TransportationJobHistory(new Date(), "Created", transportationJob, user);
			save(transportationJobHistory);
		} catch (Exception e) {
			log.error("error creating transportationJobHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(TransportationJob transportationJob, User user) {
		try {
			// TODO fill Description
			TransportationJobHistory transportationJobHistory = new TransportationJobHistory(new Date(), "Edited", transportationJob, user);
			save(transportationJobHistory);
		} catch (Exception e) {
			log.error("error creating transportationJobHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void closed(TransportationJob transportationJob, User user) {
		try {
			// TODO fill Description
			TransportationJobHistory transportationJobHistory = new TransportationJobHistory(new Date(), "Closed", transportationJob, user);
			save(transportationJobHistory);
		} catch (Exception e) {
			log.error("error creating transportationJobHistory History (closed) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void opened(TransportationJob transportationJob, User user) {
		try {
			// TODO fill Description
			TransportationJobHistory transportationJobHistory = new TransportationJobHistory(new Date(), "Opened", transportationJob, user);
			save(transportationJobHistory);
		} catch (Exception e) {
			log.error("error creating transportationJobHistory History (closed) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
