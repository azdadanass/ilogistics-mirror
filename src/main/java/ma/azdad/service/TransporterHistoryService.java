package ma.azdad.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Transporter;
import ma.azdad.model.TransporterHistory;

@Component
@Transactional
public class TransporterHistoryService extends GenericService<TransporterHistory> {

	public void created(Transporter transporter) {
		try {
			TransporterHistory transporterHistory = new TransporterHistory(new Date(), "Created", transporter, transporter.getUser());
			save(transporterHistory);
		} catch (Exception e) {
			log.error("error creating transporterHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(Transporter transporter) {
		try {
			// TODO fill Description
			TransporterHistory transporterHistory = new TransporterHistory(new Date(), "Edited", transporter, transporter.getUser());
			save(transporterHistory);
		} catch (Exception e) {
			log.error("error creating transporterHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
