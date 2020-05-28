package ma.azdad.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.JobRequestDeliveryDetail;
import ma.azdad.model.SerialNumber;
import ma.azdad.repos.JobRequestDeliveryDetailRepos;
import ma.azdad.repos.SerialNumberRepos;

@Component
@Transactional
public class JobRequestDeliveryDetailService extends GenericService<JobRequestDeliveryDetail> {

	@Autowired
	JobRequestDeliveryDetailRepos jobRequestDeliveryDetailRepos;

	@Autowired
	SerialNumberRepos serialNumberRepos;

	@Override
	public JobRequestDeliveryDetail findOne(Integer id) {
		JobRequestDeliveryDetail jobRequestDeliveryDetail = super.findOne(id);
		//		Hibernate.initialize(jobRequestDeliveryDetail.get..);
		return jobRequestDeliveryDetail;
	}

	public List<JobRequestDeliveryDetail> findInstalledByProject(Integer projectId) {
		List<JobRequestDeliveryDetail> result = new ArrayList<>();
		List<JobRequestDeliveryDetail> data = jobRequestDeliveryDetailRepos.findInstalledByProject(projectId);
		for (JobRequestDeliveryDetail jrdd : data)
			if (!jrdd.getIsSerialNumberRequired())
				result.add(jrdd);
			else {
				List<SerialNumber> serialNumberList = serialNumberRepos.findByJobRequestAndDeliveryRequestDetail(jrdd.getTmpJobRequestId(), jrdd.getTmpDeliveryRequestDetailId());
				for (int i = 0; i < jrdd.getInstalledQuantity(); i++)
					result.add(new JobRequestDeliveryDetail(1.0, i < serialNumberList.size() ? serialNumberList.get(i).getName() : "", jrdd.getTmpPartNumberName(), jrdd.getTmpPartNumberDescription(),
							jrdd.getTmpDeliveryRequestReference(), jrdd.getTmpJobRequestId(), jrdd.getTmpJobRequestReference(), jrdd.getTmpSiteName(), jrdd.getTmpTeamName()));
			}
		return result;
	}

}
