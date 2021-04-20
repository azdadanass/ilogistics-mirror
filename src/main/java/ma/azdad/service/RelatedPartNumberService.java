package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.RelatedPartNumber;
import ma.azdad.repos.RelatedPartNumberRepos;

@Component
@Transactional
public class RelatedPartNumberService extends GenericService<Integer, RelatedPartNumber, RelatedPartNumberRepos> {

	@Autowired
	RelatedPartNumberRepos relatedPartNumberRepos;

	public List<RelatedPartNumber> findByPartNumber(Integer partNumberId) {
		return relatedPartNumberRepos.findByPartNumber(partNumberId);
	}

}
