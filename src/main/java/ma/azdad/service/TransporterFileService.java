package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.TransporterFile;

@Component
@Transactional
public class TransporterFileService extends GenericService<TransporterFile> {

}

