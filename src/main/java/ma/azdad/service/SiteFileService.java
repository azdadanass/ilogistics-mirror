package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.SiteFile;
import ma.azdad.repos.SiteFileRepos;

@Component
@Transactional
public class SiteFileService extends GenericService<Integer, SiteFile, SiteFileRepos> {

}
