package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.ExternalResourceFile;

@Component
@Transactional
public class ExternalResourceFileService extends GenericService<ExternalResourceFile> {

}

