package ma.azdad.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumberEquivalence;
import ma.azdad.repos.PartNumberEquivalenceRepos;

@Component
public class PartNumberEquivalenceService extends GenericService<Integer, PartNumberEquivalence, PartNumberEquivalenceRepos> {

	@Autowired
	PartNumberEquivalenceRepos partNumberEquivalenceRepos;

	@Override
	public PartNumberEquivalence findOne(Integer id) {
		PartNumberEquivalence partNumberEquivalence = super.findOne(id);
		partNumberEquivalence.getDetailList().forEach(i -> Hibernate.initialize(i.getPartNumber()));
		Hibernate.initialize(partNumberEquivalence.getDetailList());
		return partNumberEquivalence;
	}

	public Set<Integer> findPartNumberIdListByEquivalence(Set<Integer> partNumberSourceList) {
		if (partNumberSourceList == null || partNumberSourceList.isEmpty())
			return new HashSet<>();
		return partNumberEquivalenceRepos.findPartNumberIdListByEquivalence(partNumberSourceList);
	}

	public void updateFormulaAndType() {
		partNumberEquivalenceRepos.findByHavingNullFormulaOrType().forEach(item -> {
			item.calculateFileds();
			System.out.println(item.getFormula());
			save(item);
		});
	}

	public void updateInverseFormula() {
		partNumberEquivalenceRepos.findByHavingNullInverseFormula().forEach(item -> {
			item.calculateFileds();
			System.out.println(item.getInverseFormula());
			save(item);
		});
	}

	public List<PartNumberEquivalence> findByPartNumberAndContainingPartNumber(Integer partNumberId1, Integer partNumberId2) {
		return partNumberEquivalenceRepos.findByPartNumberAndContainingPartNumber(partNumberId1, partNumberId2);
	}
}
