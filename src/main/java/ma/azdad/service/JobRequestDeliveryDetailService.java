package ma.azdad.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.JobRequestDeliveryDetail;
import ma.azdad.model.SerialNumber;
import ma.azdad.repos.JobRequestDeliveryDetailRepos;
import ma.azdad.repos.SerialNumberRepos;

@Component
public class JobRequestDeliveryDetailService extends GenericService<Integer, JobRequestDeliveryDetail, JobRequestDeliveryDetailRepos> {

	@Autowired
	JobRequestDeliveryDetailRepos repos;

	@Autowired
	SerialNumberRepos serialNumberRepos;

	@Override
	public JobRequestDeliveryDetail findOne(Integer id) {
		JobRequestDeliveryDetail jobRequestDeliveryDetail = super.findOne(id);
		// Hibernate.initialize(jobRequestDeliveryDetail.get..);
		return jobRequestDeliveryDetail;
	}

	public List<JobRequestDeliveryDetail> findInstalledByProject(Integer projectId) {
		List<JobRequestDeliveryDetail> result = new ArrayList<>();
		List<JobRequestDeliveryDetail> data = repos.findInstalledByProject(projectId);
		for (JobRequestDeliveryDetail jrdd : data)
			if (!jrdd.getIsSerialNumberRequired())
				result.add(jrdd);
			else {
				List<SerialNumber> serialNumberList = serialNumberRepos.findByJobRequestAndDeliveryRequestDetail(jrdd.getTmpJobRequestId(), jrdd.getTmpDeliveryRequestDetailId());
				for (int i = 0; i < jrdd.getInstalledQuantity(); i++)
					result.add(new JobRequestDeliveryDetail(1.0, i < serialNumberList.size() ? serialNumberList.get(i).getName() : "", jrdd.getTmpPartNumberName(),
							jrdd.getTmpPartNumberDescription(), jrdd.getTmpDeliveryRequestReference(), jrdd.getTmpJobRequestId(), jrdd.getTmpJobRequestReference(),
							jrdd.getTmpSiteName(), jrdd.getTmpTeamName()));
			}
		return result;
	}

	public List<JobRequestDeliveryDetail> findInstalled(Integer companyId, Integer customerId, Collection<Integer> deliveryRequestIdList, Collection<Integer> partNumberIdList) {
		List<JobRequestDeliveryDetail> result = new ArrayList<>();
		if (deliveryRequestIdList.isEmpty() || partNumberIdList.isEmpty())
			return result;
		List<JobRequestDeliveryDetail> data = null;
		if (companyId != null)
			data = repos.findInstalledByCompanyOwner(companyId, deliveryRequestIdList, partNumberIdList);
		else if (customerId != null)
			data = repos.findInstalledByCustomerOwner(customerId, deliveryRequestIdList, partNumberIdList);
		for (JobRequestDeliveryDetail jrdd : data)
			if (!jrdd.getIsSerialNumberRequired())
				result.add(jrdd);
			else {
				List<SerialNumber> serialNumberList = serialNumberRepos.findByJobRequestAndDeliveryRequestDetail(jrdd.getTmpJobRequestId(), jrdd.getTmpDeliveryRequestDetailId());
				for (int i = 0; i < jrdd.getInstalledQuantity(); i++)
					result.add(new JobRequestDeliveryDetail(1.0, i < serialNumberList.size() ? serialNumberList.get(i).getName() : "", jrdd.getTmpPartNumberName(),
							jrdd.getTmpPartNumberDescription(), jrdd.getTmpDeliveryRequestReference(), jrdd.getTmpJobRequestId(), jrdd.getTmpJobRequestReference(),
							jrdd.getTmpSiteName(), jrdd.getTmpTeamName()));
			}
		return result;
	}

	public Long countByDeliveryRequest(Integer deliveryRequestId) {
		return repos.countByDeliveryRequest(deliveryRequestId);
	}

	public void deleteByDeliveryRequest(Integer deliveryRequestId) {
		repos.deleteByDeliveryRequest(deliveryRequestId);
		evictCache("deliveryRequestService");
		evictCache("jobRequestService");
	}
	
	public void deleteByDeliveryRequestAndNotStartedJobRequest(Integer deliveryRequestId){
		List<Integer> jobRequestIdList = repos.findNotStartedJobRequestIdListByDeliveryRequest(deliveryRequestId);
		repos.deleteByDeliveryRequestAndJobRequestList(deliveryRequestId, jobRequestIdList);
		evictCache("deliveryRequestService");
		evictCache("jobRequestService");
	}
}
