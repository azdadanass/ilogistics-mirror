package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.InternalPartNumber;
import ma.azdad.repos.InternalPartNumberRepos;

@Component
public class InternalPartNumberService extends GenericService<Integer, InternalPartNumber, InternalPartNumberRepos> {
	@Override
	public InternalPartNumber findOne(Integer id) {
		InternalPartNumber internalPartNumber = super.findOne(id);
		return internalPartNumber;
	}
}
