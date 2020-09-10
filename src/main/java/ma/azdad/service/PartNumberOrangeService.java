package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberOrange;

@Component
@Transactional
public class PartNumberOrangeService extends GenericServiceOld<PartNumberOrange> {
	@Override
	public PartNumberOrange findOne(Integer id) {
		PartNumberOrange partNumberOrange = super.findOne(id);
		return partNumberOrange;
	}
}
