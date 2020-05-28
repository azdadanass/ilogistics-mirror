package ma.azdad.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumber;
import ma.azdad.model.PartNumberHistory;

@Component
@Transactional
public class PartNumberHistoryService extends GenericService<PartNumberHistory> {

	public void created(PartNumber partNumber) {
		try {
			PartNumberHistory partNumberHistory = new PartNumberHistory(new Date(), "Created", partNumber, partNumber.getUser());
			save(partNumberHistory);
		} catch (Exception e) {
			log.error("error creating partNumberHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(PartNumber partNumber, String changes) {
		try {
			// TODO fill Description
			PartNumberHistory partNumberHistory = new PartNumberHistory(new Date(), "Edited", changes, partNumber, partNumber.getUser());
			save(partNumberHistory);
		} catch (Exception e) {
			log.error("error creating partNumberHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}
}
