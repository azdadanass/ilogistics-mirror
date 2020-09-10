package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Brand;
import ma.azdad.repos.BrandRepos;

@Component
@Transactional
public class BrandService extends GenericServiceOld<Brand> {

	@Autowired
	BrandRepos brandRepos;

	@Override
	public Brand findOne(Integer id) {
		Brand brand = super.findOne(id);
		Hibernate.initialize(brand.getSupplierList());
		return brand;
	}

	public Boolean isNameExists(String name, Integer id) {
		return ObjectUtils.firstNonNull(id != null ? brandRepos.countByName(name, id) : brandRepos.countByName(name), 0l) > 0;
	}

	public List<String> findNameList() {
		return brandRepos.findNameList();
	}

	public Brand findByName(String name) {
		return brandRepos.findByName(name);
	}
}
