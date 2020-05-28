package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Supplier;
import ma.azdad.repos.SupplierRepos;
import ma.azdad.utils.LabelValue;

@Component
@Transactional
public class SupplierService {

	@Autowired
	private SupplierRepos supplierRepos;

	public List<LabelValue> findLabelValueList() {
		return supplierRepos.findLabelValueList();
	}

	public Supplier findOne(Integer id) {
		return supplierRepos.findOne(id);
	}

	public Supplier findOneNullable(Integer id) {
		if (id == null)
			return null;
		return supplierRepos.findOne(id);
	}

	public List<Supplier> findLight() {
		return supplierRepos.findLight();
	}
}
