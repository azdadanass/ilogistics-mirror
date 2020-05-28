package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Tool;

@Repository
public interface ToolRepos extends JpaRepository<Tool, Integer> {

	@Query("select new Tool(id,matricule) from Tool a where a.tooltype.id = ?1 and status = ?2")
	public List<Tool> findLight(Integer toolTypeId, String status);

}
