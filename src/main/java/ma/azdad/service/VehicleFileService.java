package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.VehicleFile;
import ma.azdad.repos.VehicleFileRepos;

@Component
@Transactional
public class VehicleFileService extends GenericService<Integer, VehicleFile, VehicleFileRepos> {

}
