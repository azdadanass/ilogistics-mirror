package ma.azdad.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.ExternalResource;
import ma.azdad.model.ExternalResourceHistory;
import ma.azdad.model.User;

@Component
@Transactional
public class ExternalResourceHistoryService extends GenericService<ExternalResourceHistory> {

	public void created(ExternalResource externalResource, User user) {
		try {
			ExternalResourceHistory externalResourceHistory = new ExternalResourceHistory(new Date(), "Created", externalResource, user);
			save(externalResourceHistory);
		} catch (Exception e) {
			log.error("error creating externalResourceHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(ExternalResource externalResource, User user) {
		try {
			// TODO fill Description
			ExternalResourceHistory externalResourceHistory = new ExternalResourceHistory(new Date(), "Edited", externalResource, user);
			save(externalResourceHistory);
		} catch (Exception e) {
			log.error("error creating externalResourceHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
