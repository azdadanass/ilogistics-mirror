package ma.azdad.service;

import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumberDetail;
import ma.azdad.repos.PartNumberDetailRepos;

@Component
public class PartNumberDetailService extends GenericService<Integer, PartNumberDetail, PartNumberDetailRepos> {

}
