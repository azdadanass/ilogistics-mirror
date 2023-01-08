package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Acceptance;
import ma.azdad.repos.AcceptanceRepos;

@Component
@Transactional
public class AcceptanceService {

	@Autowired
	private AcceptanceRepos repos;

	public Acceptance findOne(Integer id) {
		return repos.findById(id).get();
	}

	public Long countPendingAcceptances(Integer transportationRequestId) {
		Long l = repos.countPendingAcceptances(transportationRequestId);
		return l != null ? l : 0;
	}
	
	public List<Acceptance> findByPo(Integer poId) {
		return repos.findByPo(poId);
	}

}
