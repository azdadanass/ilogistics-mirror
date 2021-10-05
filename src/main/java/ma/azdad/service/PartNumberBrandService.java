package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumberBrand;
import ma.azdad.repos.PartNumberBrandRepos;

@Component
public class PartNumberBrandService extends GenericService<Integer, PartNumberBrand, PartNumberBrandRepos> {

	@Autowired
	PartNumberBrandRepos brandRepos;

	@Override
	public PartNumberBrand findOne(Integer id) {
		PartNumberBrand brand = super.findOne(id);
		Hibernate.initialize(brand.getSupplierList());
		return brand;
	}

	public Boolean isNameExists(String name, Integer id) {
		return ObjectUtils.firstNonNull(id != null ? brandRepos.countByName(name, id) : brandRepos.countByName(name), 0l) > 0;
	}

	public List<String> findNameList() {
		return brandRepos.findNameList();
	}

	public PartNumberBrand findByName(String name) {
		return brandRepos.findByName(name);
	}
}
