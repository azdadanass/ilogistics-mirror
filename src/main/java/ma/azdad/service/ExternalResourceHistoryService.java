package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.ExternalResource;
import ma.azdad.model.ExternalResourceHistory;
import ma.azdad.model.User;
import ma.azdad.repos.ExternalResourceHistoryRepos;

@Component
@Transactional
public class ExternalResourceHistoryService extends GenericService<Integer, ExternalResourceHistory, ExternalResourceHistoryRepos> {

	public void created(ExternalResource externalResource, User user) {
		try {
			ExternalResourceHistory externalResourceHistory = new ExternalResourceHistory("Created", user, null, externalResource);
			save(externalResourceHistory);
		} catch (Exception e) {
			log.error("error creating externalResourceHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(ExternalResource externalResource, User user) {
		try {
			// TODO fill Description
			ExternalResourceHistory externalResourceHistory = new ExternalResourceHistory("Edited", user, null, externalResource);
			save(externalResourceHistory);
		} catch (Exception e) {
			log.error("error creating externalResourceHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
