package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Site;
import ma.azdad.model.SiteHistory;
import ma.azdad.model.User;
import ma.azdad.repos.SiteHistoryRepos;

@Component
@Transactional
public class SiteHistoryService extends GenericService<Integer, SiteHistory, SiteHistoryRepos> {

	public void created(Site site, User user) {
		try {
			SiteHistory siteHistory = new SiteHistory("Created", user, null, site);
			save(siteHistory);
		} catch (Exception e) {
			log.error("error creating siteHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(Site site, User user) {
		try {
			// TODO fill Description
			SiteHistory siteHistory = new SiteHistory("Edited", user, null, site);
			save(siteHistory);
		} catch (Exception e) {
			log.error("error creating siteHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
