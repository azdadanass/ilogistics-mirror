package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.JobRequestSerialNumber;
import ma.azdad.repos.JobRequestSerialNumberRepos;

@Component
public class JobRequestSerialNumberService extends GenericService<Integer, JobRequestSerialNumber, JobRequestSerialNumberRepos> {

	@Cacheable("jobRequestSerialNumberService.findAll")
	public List<JobRequestSerialNumber> findAll() {
		return repos.findAll();
	}

	public JobRequestSerialNumber findOne(Integer id) {
		JobRequestSerialNumber jobRequestSerialNumber = super.findOne(id);
		return jobRequestSerialNumber;
	}

	public List<JobRequestSerialNumber> findDeliveryListsByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId){
		return repos.findDeliveryListsByCompanyOwner(username, warehouseList, assignedProjectList, companyId);
	}
}
