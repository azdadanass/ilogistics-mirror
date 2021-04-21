package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.Transporter;
import ma.azdad.model.TransporterHistory;
import ma.azdad.repos.TransporterHistoryRepos;

@Component
public class TransporterHistoryService extends GenericService<Integer, TransporterHistory, TransporterHistoryRepos> {

	public void created(Transporter transporter) {
		try {
			TransporterHistory transporterHistory = new TransporterHistory("Created", transporter.getUser(), null, transporter);
			save(transporterHistory);
		} catch (Exception e) {
			log.error("error creating transporterHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(Transporter transporter) {
		try {
			// TODO fill Description
			TransporterHistory transporterHistory = new TransporterHistory("Edited", transporter.getUser(), null, transporter);
			save(transporterHistory);
		} catch (Exception e) {
			log.error("error creating transporterHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
