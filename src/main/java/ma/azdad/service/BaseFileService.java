package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.BaseFile;

@Component
@Transactional
public class BaseFileService extends GenericServiceOld<BaseFile> {

}



