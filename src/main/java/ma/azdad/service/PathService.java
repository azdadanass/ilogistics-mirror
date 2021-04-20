package ma.azdad.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Path;
import ma.azdad.repos.PathRepos;

@Component
@Transactional
public class PathService extends GenericService<Integer, Path, PathRepos> {

	@Autowired
	PathRepos pathRepos;

	@Override
	public Path findOne(Integer id) {
		Path path = super.findOne(id);
		return path;
	}

}
