package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Acceptance;
import ma.azdad.repos.AcceptanceRepos;

@Component
@Transactional
public class AcceptanceService {

	@Autowired
	private AcceptanceRepos acceptanceRepos;

	public Acceptance findOne(Integer id) {
		return acceptanceRepos.findById(id).get();
	}

	public Long countPendingAcceptances(Integer transportationRequestId) {
		Long l = acceptanceRepos.countPendingAcceptances(transportationRequestId);
		return l != null ? l : 0;
	}

}
