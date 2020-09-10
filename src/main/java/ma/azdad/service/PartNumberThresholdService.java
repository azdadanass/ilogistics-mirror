package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberThreshold;
import ma.azdad.repos.PartNumberThresholdRepos;

@Component
@Transactional
public class PartNumberThresholdService extends GenericServiceOld<PartNumberThreshold> {

	@Autowired
	PartNumberThresholdRepos partNumberThresholdRepos;

	@Override
	public PartNumberThreshold findOne(Integer id) {
		PartNumberThreshold partNumberThreshold = super.findOne(id);
		//		Hibernate.initialize(partNumberThreshold.get..);
		return partNumberThreshold;
	}
	
	public List<PartNumberThreshold> findByProject(Integer projectId){
		return partNumberThresholdRepos.findByProject(projectId);
	}
	
	public Long countByProjectAndPartNumber(Integer projectId,Integer partNumberId) {
		return ObjectUtils.firstNonNull(partNumberThresholdRepos.countByProjectAndPartNumber(projectId, partNumberId),0l);
	}

}

