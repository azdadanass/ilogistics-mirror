package ma.azdad.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Site;
import ma.azdad.model.SiteHistory;
import ma.azdad.model.User;

@Component
@Transactional
public class SiteHistoryService extends GenericService<SiteHistory> {

	public void created(Site site, User user) {
		try {
			SiteHistory siteHistory = new SiteHistory(new Date(), "Created", site, user);
			save(siteHistory);
		} catch (Exception e) {
			log.error("error creating siteHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(Site site, User user) {
		try {
			// TODO fill Description
			SiteHistory siteHistory = new SiteHistory(new Date(), "Edited", site, user);
			save(siteHistory);
		} catch (Exception e) {
			log.error("error creating siteHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
