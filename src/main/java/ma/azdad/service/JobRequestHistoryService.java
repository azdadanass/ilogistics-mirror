package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.JobRequestHistory;
import ma.azdad.repos.JobRequestHistoryRepos;

@Component
public class JobRequestHistoryService extends GenericService<Integer, JobRequestHistory, JobRequestHistoryRepos> {

	@Cacheable("jobRequestHistoryService.findAll")
	public List<JobRequestHistory> findAll() {
		return repos.findAll();
	}

	public JobRequestHistory findOne(Integer id) {
		JobRequestHistory jobRequestHistory = super.findOne(id);
		
		
		
		
		return jobRequestHistory;
	}

}
