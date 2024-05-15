package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.IssueFile;
import ma.azdad.repos.IssueFileRepos;

@Component
public class IssueFileService extends GenericService<Integer, IssueFile, IssueFileRepos> {

}
