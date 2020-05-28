package ma.azdad.service;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.User;
import ma.azdad.model.Warehouse;
import ma.azdad.model.WarehouseHistory;

@Component
@Transactional
public class WarehouseHistoryService extends GenericService<WarehouseHistory> {

	public void created(Warehouse warehouse, User user) {
		try {
			WarehouseHistory warehouseHistory = new WarehouseHistory(new Date(), "Created", warehouse, user);
			save(warehouseHistory);
		} catch (Exception e) {
			log.error("error creating warehouseHistory History (created) : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void edited(Warehouse warehouse, User user) {
		try {
			// TODO fill Description
			WarehouseHistory warehouseHistory = new WarehouseHistory(new Date(), "Edited", warehouse, user);
			save(warehouseHistory);
		} catch (Exception e) {
			log.error("error creating warehouseHistory History (edited) : " + e.getMessage());
			e.printStackTrace();
		}
	}

}
