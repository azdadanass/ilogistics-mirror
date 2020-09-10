package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Stop;
import ma.azdad.repos.StopRepos;

@Component
@Transactional
public class StopService extends GenericServiceOld<Stop> {

	@Autowired
	StopRepos stopRepos;

	@Override
	public Stop findOne(Integer id) {
		Stop stop = super.findOne(id);
		return stop;
	}

}
