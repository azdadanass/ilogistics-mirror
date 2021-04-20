package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumberDetail;
import ma.azdad.repos.PartNumberDetailRepos;

@Component
@Transactional
public class PartNumberDetailService extends GenericService<Integer, PartNumberDetail, PartNumberDetailRepos> {

}
