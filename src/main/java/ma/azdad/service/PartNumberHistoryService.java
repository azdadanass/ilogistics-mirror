package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumber;
import ma.azdad.model.PartNumberHistory;
import ma.azdad.repos.PartNumberHistoryRepos;

@Component
public class PartNumberHistoryService extends GenericService<Integer, PartNumberHistory, PartNumberHistoryRepos> {

	public void created(PartNumber partNumber) {
		try {
			PartNumberHistory partNumberHistory = new PartNumberHistory("Created", partNumber.getUser(), null, partNumber);
			save(partNumberHistory);
		} catch (Exception e) {
			log.error("error creating partNumberHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(PartNumber partNumber, String changes) {
		try {
			// TODO fill Description
			PartNumberHistory partNumberHistory = new PartNumberHistory("Edited", partNumber.getUser(), changes, partNumber);
			save(partNumberHistory);
		} catch (Exception e) {
			log.error("error creating partNumberHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
