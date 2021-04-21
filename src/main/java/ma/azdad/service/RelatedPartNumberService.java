package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.RelatedPartNumber;
import ma.azdad.repos.RelatedPartNumberRepos;

@Component
public class RelatedPartNumberService extends GenericService<Integer, RelatedPartNumber, RelatedPartNumberRepos> {

	@Autowired
	RelatedPartNumberRepos relatedPartNumberRepos;

	public List<RelatedPartNumber> findByPartNumber(Integer partNumberId) {
		return relatedPartNumberRepos.findByPartNumber(partNumberId);
	}

}
