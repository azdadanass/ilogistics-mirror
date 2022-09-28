package ma.azdad.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ma.azdad.model.CompanyProfile;
import ma.azdad.model.Profile;
import ma.azdad.repos.CompanyProfileRepos;

@Component
public class CompanyProfileService extends GenericService<Integer,CompanyProfile, CompanyProfileRepos> {

	@Value("${application}")
	private String application;

	public List<Integer> findCompanyIdList(String username, Profile profile) {
		return repos.findCompanyIdList(application, username, profile);
	}

	public List<CompanyProfile> findByErp() {
		return repos.findByErp(application);
	}
}
