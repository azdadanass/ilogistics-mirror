package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.Stop;
import ma.azdad.repos.StopRepos;

@Component
public class StopService extends GenericService<Integer, Stop, StopRepos> {

	@Autowired
	StopRepos stopRepos;

	@Override
	public Stop findOne(Integer id) {
		Stop stop = super.findOne(id);
		return stop;
	}

}
