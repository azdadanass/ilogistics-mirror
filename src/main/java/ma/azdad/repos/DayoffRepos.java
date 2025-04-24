package ma.azdad.repos;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ma.azdad.model.Dayoff;

@Repository
public interface DayoffRepos extends JpaRepository<Dayoff, Integer> {

	
	@Query("select count(*) from Dayoff where date = ?1")
	Long countByDate(Date date);
	
	
	@Query("select count(*) from Dayoff where date between ?1 and ?2")
	Long countBetweenDates(Date startDate,Date endDate);

}
