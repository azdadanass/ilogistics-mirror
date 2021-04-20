package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberFile;
import ma.azdad.repos.PartNumberFileRepos;

@Component
@Transactional
public class PartNumberFileService extends GenericService<Integer, PartNumberFile, PartNumberFileRepos> {

}
