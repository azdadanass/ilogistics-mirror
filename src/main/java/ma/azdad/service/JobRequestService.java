package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.JobRequest;
import ma.azdad.repos.JobRequestRepos;

@Component
public class JobRequestService extends GenericService<Integer, JobRequest, JobRequestRepos> {

	@Cacheable("jobRequestService.findAll")
	public List<JobRequest> findAll() {
		return repos.findAll();
	}

	public JobRequest findOne(Integer id) {
		JobRequest jobRequest = super.findOne(id);
		
		
		
		
		return jobRequest;
	}

}
