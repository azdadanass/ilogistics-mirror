package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.repos.UserDataRepos;

@Component
@Transactional
public class UserDataService {

	@Autowired
	UserDataRepos repos;

//	public Map<String, String> getDatas(String type) {
//		List<Object[]> objects = new ArrayList<Object[]>();
//		if (DataTypes.PHOTO.getValue().equals(type))
//			objects = userDataRepos.getPhotos();
//		else if (DataTypes.EMAIL.getValue().equals(type))
//			objects = userDataRepos.getMailAdresses();
//		else if (DataTypes.NAME.getValue().equals(type))
//			objects = userDataRepos.getFullNames();
//		else if (DataTypes.PHONE.getValue().equals(type))
//			objects = userDataRepos.getPhones();
//		else if (DataTypes.JOB.getValue().equals(type))
//			objects = userDataRepos.getJobs();
//		else if (DataTypes.CIN.getValue().equals(type))
//			objects = userDataRepos.getCins();
//
//		Map<String, String> map = new HashMap<String, String>();
//		for (Object[] o : objects) {
//			map.put((String) o[0], (String) o[1]);
//		}
//		return map;
//	}

}