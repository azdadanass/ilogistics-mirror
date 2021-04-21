package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.TransportationJobFile;
import ma.azdad.repos.TransportationJobFileRepos;

@Component
public class TransportationJobFileService extends GenericService<Integer, TransportationJobFile, TransportationJobFileRepos> {

}
