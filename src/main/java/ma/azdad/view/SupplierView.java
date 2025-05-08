package ma.azdad.view;

import java.util.List;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Customer;
import ma.azdad.model.Supplier;
import ma.azdad.service.SupplierService;
import ma.azdad.utils.LabelValue;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class SupplierView {

	@Autowired
	protected SupplierService service;

	// generic
	public List<LabelValue> findLabelValueList() {
		return service.findLabelValueList();
	}

	public List<Supplier> findLight() {
		return service.findLight();
	}

	public String findNameByPo(Integer poId) {
		return service.findNameByPo(poId);
	}
	
	public List<Supplier> findByHavingActiveProjectAssignment(Integer projectId){
		return service.findByHavingActiveProjectAssignment(projectId);
	}
	
	public String findName(Integer id) {
		return service.findNameMap().get(id);
	}
	
	public List<Supplier> findAssignedToProject(Integer projectId){
		return service.findAssignedToProject(projectId);
	}

}
