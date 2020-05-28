package ma.azdad.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Warehouse;
import ma.azdad.repos.WarehouseRepos;

@Component
@Transactional
public class WarehouseService extends GenericService<Warehouse> {

	@Autowired
	WarehouseRepos warehouseRepos;

	@Override
	public Warehouse findOne(Integer id) {
		Warehouse warehouse = super.findOne(id);
		Hibernate.initialize(warehouse.getCustomer());
		Hibernate.initialize(warehouse.getSupplier());
		Hibernate.initialize(warehouse.getFileList());
		Hibernate.initialize(warehouse.getHistoryList());
		Hibernate.initialize(warehouse.getLocationList());
		Hibernate.initialize(warehouse.getManagerList());
		return warehouse;
	}

	public List<Warehouse> findLight() {
		return warehouseRepos.findLight();
	}

	public Set<Integer> findNonEmptyWarehouseList(Integer projectId) {
		Set<Integer> result = new HashSet<>();
		result.add(-1);
		result.addAll(warehouseRepos.findNonEmptyWarehouseList(projectId));
		return result;
	}

	public List<Warehouse> find(Set<Integer> warehouseList) {
		return warehouseRepos.find(warehouseList);
	}

	public List<Warehouse> find(Integer projectId) {
		System.out.println("warehouse list " + findNonEmptyWarehouseList(projectId));
		return find(findNonEmptyWarehouseList(projectId));
	}

	public List<Integer> findIdListByManager(String username) {
		return warehouseRepos.findIdListByManager(username);
	}

}
