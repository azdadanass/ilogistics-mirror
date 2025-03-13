package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.DocType;
import ma.azdad.repos.DocTypeRepos;

@Component
public class DocTypeService extends GenericService<Integer, DocType, DocTypeRepos> {

	@Value("${application}")
	private String application;

	@Autowired
	DocTypeRepos docTypeRepos;
	
	@Autowired
	DeliveryRequestService deliveryRequestService;

	public List<String> findByType(String type) {
		return docTypeRepos.findByAppAndType(application, type);
	}

	public List<String> findByType(String type, Integer filter) {
		return docTypeRepos.findByAppAndType(application, type, filter);
	}
	
	public List<String> findByTypeMobile(String type, Integer id) {
		DeliveryRequest deliveryRequest = deliveryRequestService.findOne(id);
		if(deliveryRequest.getType().equals(DeliveryRequestType.INBOUND) ||deliveryRequest.getType().equals(DeliveryRequestType.XBOUND))
		return docTypeRepos.findByAppAndType(application, type, 1);
		
		return docTypeRepos.findByAppAndType(application, type, 2);
	}

}