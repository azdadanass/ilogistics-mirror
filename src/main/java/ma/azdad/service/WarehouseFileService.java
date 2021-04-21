package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.WarehouseFile;
import ma.azdad.repos.WarehouseFileRepos;

@Component
public class WarehouseFileService extends GenericService<Integer, WarehouseFile, WarehouseFileRepos> {

}
