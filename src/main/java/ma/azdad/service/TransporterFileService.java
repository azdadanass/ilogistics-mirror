package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.TransporterFile;
import ma.azdad.repos.TransporterFileRepos;

@Component
@Transactional
public class TransporterFileService extends GenericService<Integer, TransporterFile, TransporterFileRepos> {

}
