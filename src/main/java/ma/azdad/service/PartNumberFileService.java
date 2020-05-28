package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberFile;

@Component
@Transactional
public class PartNumberFileService extends GenericService<PartNumberFile> {

}

