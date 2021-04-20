package ma.azdad.service;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.BoqMapping;
import ma.azdad.repos.BoqMappingRepos;

@Component
@Transactional
public class BoqMappingService extends GenericService<Integer, BoqMapping, BoqMappingRepos> {

	@Autowired
	BoqMappingRepos boqMappingRepos;

	@Override
	public BoqMapping findOne(Integer id) {
		BoqMapping boqMapping = super.findOne(id);
		// Hibernate.initialize(boqMapping.get..);
		return boqMapping;
	}

	public List<BoqMapping> findRemaining(Integer poId) {
		return boqMappingRepos.findRemaining(poId);
	}

	public Long countByPo(Integer poId) {
		return ObjectUtils.firstNonNull(boqMappingRepos.countByPo(poId), 0l);
	}
}
