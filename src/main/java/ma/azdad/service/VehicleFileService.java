package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.VehicleFile;
import ma.azdad.repos.VehicleFileRepos;

@Component
public class VehicleFileService extends GenericService<Integer, VehicleFile, VehicleFileRepos> {

}
