package ma.azdad.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Acceptance;
import ma.azdad.repos.AcceptanceRepos;

@Component
public class AcceptanceService extends GenericService<Integer, Acceptance, AcceptanceRepos> {

	public Acceptance findOne(Integer id) {
		return repos.findById(id).get();
	}

	public Long countPendingAcceptances(Integer transportationRequestId) {
		Long l = repos.countPendingAcceptances(transportationRequestId);
		return l != null ? l : 0;
	}

	@Cacheable("acceptanceService.findByPo")
	public List<Acceptance> findByPo(Integer poId) {
		return repos.findByPo(poId);
	}

}
