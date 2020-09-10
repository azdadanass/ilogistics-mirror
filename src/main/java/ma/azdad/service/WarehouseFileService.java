package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.WarehouseFile;

@Component
@Transactional
public class WarehouseFileService extends GenericServiceOld<WarehouseFile> {

}
