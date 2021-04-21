package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumberFile;
import ma.azdad.repos.PartNumberFileRepos;

@Component
public class PartNumberFileService extends GenericService<Integer, PartNumberFile, PartNumberFileRepos> {

}
