package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationJobHistory;
import ma.azdad.model.User;
import ma.azdad.repos.TransportationJobHistoryRepos;

@Component
public class TransportationJobHistoryService extends GenericService<Integer, TransportationJobHistory, TransportationJobHistoryRepos> {

	public void created(TransportationJob transportationJob, User user) {
		try {
			TransportationJobHistory transportationJobHistory = new TransportationJobHistory("Created", user, null, transportationJob);
			save(transportationJobHistory);
		} catch (Exception e) {
			log.error("error creating transportationJobHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(TransportationJob transportationJob, User user) {
		try {
			// TODO fill Description
			TransportationJobHistory transportationJobHistory = new TransportationJobHistory("Edited", user, null, transportationJob);
			save(transportationJobHistory);
		} catch (Exception e) {
			log.error("error creating transportationJobHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void closed(TransportationJob transportationJob, User user) {
		try {
			// TODO fill Description
			TransportationJobHistory transportationJobHistory = new TransportationJobHistory("Closed", user, null, transportationJob);
			save(transportationJobHistory);
		} catch (Exception e) {
			log.error("error creating transportationJobHistory History (closed) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void opened(TransportationJob transportationJob, User user) {
		try {
			// TODO fill Description
			TransportationJobHistory transportationJobHistory = new TransportationJobHistory("Opened", user, null, transportationJob);
			save(transportationJobHistory);
		} catch (Exception e) {
			log.error("error creating transportationJobHistory History (closed) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
