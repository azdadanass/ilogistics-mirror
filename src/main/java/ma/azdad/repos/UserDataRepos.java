package ma.azdad.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.azdad.model.UserData;

@Repository
public abstract interface UserDataRepos extends JpaRepository<UserData, String> {

//	@Query("SELECT username,photo FROM UserData")
//	public List<Object[]> getPhotos();
//
//	@Query("SELECT username,fullName FROM UserData")
//	public List<Object[]> getFullNames();
//
//	@Query("SELECT username,job FROM UserData")
//	public List<Object[]> getJobs();
//
//	@Query("SELECT username,phone FROM UserData")
//	public List<Object[]> getPhones();
//
//	@Query("SELECT username,email FROM UserData")
//	public List<Object[]> getMailAdresses();
//	
//	@Query("SELECT username,cin FROM UserData")
//	public List<Object[]> getCins();

}