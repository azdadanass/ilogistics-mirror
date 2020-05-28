package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.TransportationJobFile;

@Component
@Transactional
public class TransportationJobFileService extends GenericService<TransportationJobFile> {

}
