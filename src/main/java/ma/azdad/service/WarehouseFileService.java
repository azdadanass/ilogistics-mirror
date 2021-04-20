package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.WarehouseFile;
import ma.azdad.repos.WarehouseFileRepos;

@Component
@Transactional
public class WarehouseFileService extends GenericService<Integer, WarehouseFile, WarehouseFileRepos> {

}
