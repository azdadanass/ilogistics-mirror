package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DeliveryRequestFile;
import ma.azdad.repos.DeliveryRequestFileRepos;

@Component
@Transactional
public class DeliveryRequestFileService extends GenericService<Integer, DeliveryRequestFile, DeliveryRequestFileRepos> {

}
