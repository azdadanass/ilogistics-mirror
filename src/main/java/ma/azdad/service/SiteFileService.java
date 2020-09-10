package ma.azdad.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.SiteFile;

@Component
@Transactional
public class SiteFileService extends GenericServiceOld<SiteFile> {

}

