package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.TransportationRequestFile;
import ma.azdad.repos.TransportationRequestFileRepos;

@Component
@Transactional
public class TransportationRequestFileService extends GenericService<Integer, TransportationRequestFile, TransportationRequestFileRepos> {

}
