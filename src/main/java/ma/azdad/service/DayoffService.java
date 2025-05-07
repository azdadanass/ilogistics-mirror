package ma.azdad.service;

import java.util.Date;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.repos.DayoffRepos;

@Component
@Transactional
public class DayoffService {

	@Autowired
	private DayoffRepos repos;
	
	public Long countByDate(Date date){
		return ObjectUtils.firstNonNull(repos.countByDate(date),0l);
	}
	
	public Long countBetweenDates(Date startDate,Date endDate) {
		return repos.countBetweenDates(startDate, endDate);
	}
	
	public Boolean isDayoff(Date date) {
		return countByDate(date) > 0;
	}

}
