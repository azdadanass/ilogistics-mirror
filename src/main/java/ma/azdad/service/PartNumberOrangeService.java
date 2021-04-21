package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumberOrange;
import ma.azdad.repos.PartNumberOrangeRepos;

@Component
public class PartNumberOrangeService extends GenericService<Integer, PartNumberOrange, PartNumberOrangeRepos> {
	@Override
	public PartNumberOrange findOne(Integer id) {
		PartNumberOrange partNumberOrange = super.findOne(id);
		return partNumberOrange;
	}
}
