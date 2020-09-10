package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DocType;
import ma.azdad.repos.DocTypeRepos;

@Component
@Transactional
public class DocTypeService extends GenericServiceOld<DocType> {

	@Value("${application}")
	private String application;

	@Autowired
	DocTypeRepos docTypeRepos;

	public List<String> findByType(String type) {
		return docTypeRepos.findByAppAndType(application, type);
	}

	public List<String> findByType(String type, Integer filter) {
		return docTypeRepos.findByAppAndType(application, type, filter);
	}

}