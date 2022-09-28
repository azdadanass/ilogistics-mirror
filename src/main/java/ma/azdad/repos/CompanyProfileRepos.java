package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.CompanyProfile;
import ma.azdad.model.Profile;

@Repository
public interface CompanyProfileRepos extends JpaRepository<CompanyProfile, Integer> {

	@Query("select company.id from CompanyProfile where erp = ?1 and user.username = ?2 and profile = ?3")
	public List<Integer> findCompanyIdList(String erp, String username, Profile profile);

	@Query("select company.id from CompanyProfile where erp = ?1 and user.username = ?2 and profile in (?3)")
	public List<Integer> findCompanyIdList(String erp, String username, List<Profile> profileList);

	public List<CompanyProfile> findByErp(String erp);

}
