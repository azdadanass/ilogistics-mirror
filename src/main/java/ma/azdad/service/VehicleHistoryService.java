package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.VehicleHistory;
import ma.azdad.repos.VehicleHistoryRepos;

@Component
@Transactional
public class VehicleHistoryService extends GenericService<Integer, VehicleHistory, VehicleHistoryRepos> {

}
