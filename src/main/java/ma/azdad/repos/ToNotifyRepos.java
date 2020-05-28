package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.ToNotify;

@Repository
public interface ToNotifyRepos extends JpaRepository<ToNotify, Integer> {

	@Query("from ToNotify where user.username = ?1")
	public List<ToNotify> findByUser(String username);

	@Query("select count(*) from ToNotify where user.username = ?1 and internalResource.username = ?2")
	public Long countByUserAndInternalResource(String username, String internalResourceUsername);

}
