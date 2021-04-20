package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.ExternalResourceFile;
import ma.azdad.repos.ExternalResourceFileRepos;

@Component
@Transactional
public class ExternalResourceFileService extends GenericService<Integer, ExternalResourceFile, ExternalResourceFileRepos> {

}
