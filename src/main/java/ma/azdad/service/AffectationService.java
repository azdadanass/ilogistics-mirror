package ma.azdad.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.repos.AffectationRepos;
import ma.azdad.utils.DataTypes;

@Component
@Transactional
public class AffectationService {

	@Autowired
	AffectationRepos affectationRepos;

	public Map<String, String> getDatas(String type) {
		List<Object[]> objects = new ArrayList<Object[]>();
		if (DataTypes.LM.getValue().equals(type))
			objects = affectationRepos.getLineManagers();
		else if (DataTypes.SLM.getValue().equals(type))
			objects = affectationRepos.getSuperLineManagers();
		else if (DataTypes.LOM.getValue().equals(type))
			objects = affectationRepos.getLogisticManagers();
		else if (DataTypes.HR.getValue().equals(type))
			objects = affectationRepos.getHrManagers();
		Map<String, String> map = new HashMap<String, String>();
		for (Object[] o : objects) {
			map.put((String) o[0], (String) o[1]);
		}
		return map;
	}

}