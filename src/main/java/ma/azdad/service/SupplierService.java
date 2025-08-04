package ma.azdad.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Supplier;
import ma.azdad.repos.SupplierRepos;
import ma.azdad.utils.LabelValue;

@Component
@Transactional
public class SupplierService {

	@Autowired
	private SupplierRepos repos;

	public List<LabelValue> findLabelValueList() {
		return repos.findLabelValueList();
	}

	public Supplier findOne(Integer id) {
		return repos.findById(id).get();
	}

	public Supplier findOneNullable(Integer id) {
		if (id == null)
			return null;
		return repos.findById(id).get();
	}

	public List<Supplier> findLight() {
		return repos.findLight();
	}
	
	public List<Supplier> findActive(){
		return repos.findActive();
	}

	public String findNameByPo(Integer poId) {
		return repos.findNameByPo(poId);
	}
	
	public List<Supplier> findByHavingActiveProjectAssignment(Integer projectId){
		return repos.findByHavingActiveProjectAssignment(projectId);
	}
	
	@Cacheable("supplierService.findNameMap")
	public Map<Integer, String> findNameMap() {
		return repos.findLight().stream().collect(Collectors.toMap(s -> s.getId(), s -> s.getName()));
	}
	
	public List<Supplier> findAssignedToProject(Integer projectId){
		return repos.findAssignedToProject(projectId);
	}
}
