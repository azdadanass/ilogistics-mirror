package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.User;
import ma.azdad.model.Warehouse;
import ma.azdad.model.WarehouseHistory;
import ma.azdad.repos.WarehouseHistoryRepos;

@Component
@Transactional
public class WarehouseHistoryService extends GenericService<Integer, WarehouseHistory, WarehouseHistoryRepos> {

	public void created(Warehouse warehouse, User user) {
		try {
			WarehouseHistory warehouseHistory = new WarehouseHistory("Created", user, null, warehouse);
			save(warehouseHistory);
		} catch (Exception e) {
			log.error("error creating warehouseHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(Warehouse warehouse, User user) {
		try {
			// TODO fill Description
			WarehouseHistory warehouseHistory = new WarehouseHistory("Edited", user, null, warehouse);
			save(warehouseHistory);
		} catch (Exception e) {
			log.error("error creating warehouseHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
