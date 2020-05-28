package ma.azdad.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Text;

@Repository
public interface TextRepos extends JpaRepository<Text, Integer> {

	@Query("select value from Text where app = ?1 and beanName = ?2")
	List<String> findValueByAppAndBeanName(String app, String beanName);

	@Query("select value from Text where app = ?1 and beanName = ?2 and type = ?3")
	List<String> findValueByAppAndBeanNameAndType(String app, String beanName, String type);

	@Query("from Text where app = ?1 and beanName = ?2 and type = ?3")
	List<Text> findByAppAndBeanNameAndType(String app, String beanName, String type);

	@Query("select distinct value from Text where app = ?1 and beanName = ?2 and type = ?3")
	List<String> findValueListByAppAndBeanNameAndType(String app, String beanName, String type);

}
