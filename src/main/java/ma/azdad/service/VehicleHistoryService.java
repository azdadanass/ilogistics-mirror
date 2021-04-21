package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.VehicleHistory;
import ma.azdad.repos.VehicleHistoryRepos;

@Component
public class VehicleHistoryService extends GenericService<Integer, VehicleHistory, VehicleHistoryRepos> {

}
