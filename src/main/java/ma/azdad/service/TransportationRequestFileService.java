package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.TransportationRequestFile;

@Component
@Transactional
public class TransportationRequestFileService extends GenericService<TransportationRequestFile> {

}

