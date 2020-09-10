package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DeliveryRequestFile;

@Component
@Transactional
public class DeliveryRequestFileService extends GenericServiceOld<DeliveryRequestFile> {

}

