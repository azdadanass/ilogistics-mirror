package ma.azdad.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.JobRequest;
import ma.azdad.model.JobRequestDeliveryDetail;
import ma.azdad.model.JobRequestHistory;
import ma.azdad.model.JobRequestStatus;
import ma.azdad.model.SerialNumber;
import ma.azdad.model.User;
import ma.azdad.repos.JobRequestDeliveryDetailRepos;
import ma.azdad.repos.SerialNumberRepos;

@Component
public class JobRequestDeliveryDetailService extends GenericService<Integer, JobRequestDeliveryDetail, JobRequestDeliveryDetailRepos> {

	@Autowired
	JobRequestDeliveryDetailRepos repos;

	@Autowired
	SerialNumberRepos serialNumberRepos;

	@Autowired
	JobRequestService jobRequestService;

	@Autowired
	JobRequestHistoryService jobRequestHistoryService;
	
	@Autowired
	StockRowService stockRowService;

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
				List<SerialNumber> serialNumberList = serialNumberRepos.findByJobRequestAndDeliveryRequestAndPartNumber(jrdd.getJobRequestId(), jrdd.getDeliveryRequestId(),
						jrdd.getPartNumberId());
				for (int i = 0; i < jrdd.getInstalledQuantity(); i++)
					result.add(new JobRequestDeliveryDetail(1.0, i < serialNumberList.size() ? serialNumberList.get(i).getName() : "", jrdd.getPartNumberName(),
							jrdd.getPartNumberDescription(), jrdd.getDeliveryRequestReference(), jrdd.getJobRequestId(), jrdd.getJobRequestReference(), jrdd.getSiteName(),
							jrdd.getTeamName()));
			}
		return result;
	}

	public List<JobRequestDeliveryDetail> findByDeliveryRequest(Integer deliveryRequestId) {
		return repos.findByDeliveryRequest(deliveryRequestId);
	}

	public List<JobRequestDeliveryDetail> findSummaryByDeliveryRequest(Integer deliveryRequestId) {
		List<JobRequestDeliveryDetail> data = repos.findByDeliveryRequest(deliveryRequestId);
		
		Map<Integer,Double> dnQtyMap = stockRowService.findQuantityPartNumberMapByDeliveryRequest(deliveryRequestId);
		
		Map<Integer, JobRequestDeliveryDetail> map = new HashMap<Integer, JobRequestDeliveryDetail>();
		for (JobRequestDeliveryDetail jrdd : data) {
			JobRequestDeliveryDetail jobRequestDeliveryDetail;
			if (!map.containsKey(jrdd.getPartNumberId())) {
				jobRequestDeliveryDetail = new JobRequestDeliveryDetail();
				jobRequestDeliveryDetail.setPartNumberName(jrdd.getPartNumberName());
				jobRequestDeliveryDetail.setPartNumberId(jrdd.getPartNumberId());
				jobRequestDeliveryDetail.setPartNumberDescription(jrdd.getPartNumberDescription());
				jobRequestDeliveryDetail.setPartNumberImage(jrdd.getPartNumberImage());
				map.put(jrdd.getPartNumberId(), jobRequestDeliveryDetail);
			}
			jobRequestDeliveryDetail = map.get(jrdd.getPartNumberId());
			if (Arrays.asList(JobRequestStatus.COMPLETED, JobRequestStatus.VALIDATED).contains(jrdd.getJobRequestStatus()))
				jobRequestDeliveryDetail.setQuantity(jobRequestDeliveryDetail.getQuantity()+ jrdd.getInstalledQuantity());
			else
				jobRequestDeliveryDetail.setQuantity(jobRequestDeliveryDetail.getQuantity()+jrdd.getQuantity());
			jobRequestDeliveryDetail.setDnQuantity(dnQtyMap.get(jrdd.getPartNumberId()));
		}
		return new ArrayList<JobRequestDeliveryDetail>(map.values());
	}

	public List<JobRequestDeliveryDetail> findInstalledByDeliveryRequest(Integer deliveryRequestId) {
		List<JobRequestDeliveryDetail> result = new ArrayList<>();
		List<JobRequestDeliveryDetail> data = repos.findInstalledByDeliveryRequest(deliveryRequestId);
		for (JobRequestDeliveryDetail jrdd : data)
			if (!jrdd.getIsSerialNumberRequired())
				result.add(jrdd);
			else {
				List<SerialNumber> serialNumberList = serialNumberRepos.findByJobRequestAndDeliveryRequestAndPartNumber(jrdd.getJobRequestId(), jrdd.getDeliveryRequestId(),
						jrdd.getPartNumberId());
				for (int i = 0; i < jrdd.getInstalledQuantity(); i++)
					result.add(new JobRequestDeliveryDetail(1.0, i < serialNumberList.size() ? serialNumberList.get(i).getName() : "", jrdd.getPartNumberName(),
							jrdd.getPartNumberDescription(), jrdd.getDeliveryRequestReference(), jrdd.getJobRequestId(), jrdd.getJobRequestReference(), jrdd.getSiteName(),
							jrdd.getTeamName()));
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
				List<SerialNumber> serialNumberList = serialNumberRepos.findByJobRequestAndDeliveryRequestAndPartNumber(jrdd.getJobRequestId(), jrdd.getDeliveryRequestId(),
						jrdd.getPartNumberId());
				for (int i = 0; i < jrdd.getInstalledQuantity(); i++)
					result.add(new JobRequestDeliveryDetail(1.0, i < serialNumberList.size() ? serialNumberList.get(i).getName() : "", jrdd.getPartNumberName(),
							jrdd.getPartNumberDescription(), jrdd.getDeliveryRequestReference(), jrdd.getJobRequestId(), jrdd.getJobRequestReference(), jrdd.getSiteName(),
							jrdd.getTeamName()));
			}
		return result;
	}

	public Long countByDeliveryRequest(Integer deliveryRequestId) {
		return ObjectUtils.firstNonNull(repos.countByDeliveryRequest(deliveryRequestId), 0l);
	}

	public void deleteByDeliveryRequest(Integer deliveryRequestId) {
		repos.deleteByDeliveryRequest(deliveryRequestId);
		evictCache("deliveryRequestService");
		evictCache("jobRequestService");
	}

	public void deleteByDeliveryRequestAndNotStartedJobRequest(DeliveryRequest OutboundDeliveryRequestReturn, DeliveryRequest inboundDeliveryRequest, User connectedUser) {
		List<Integer> jobRequestIdList = repos.findNotStartedJobRequestIdListByDeliveryRequest(OutboundDeliveryRequestReturn.getId());
		if (jobRequestIdList.isEmpty())
			return;

		repos.deleteByDeliveryRequestAndJobRequestList(OutboundDeliveryRequestReturn.getId(), jobRequestIdList);

		jobRequestIdList.stream().distinct().forEach(i -> {
			JobRequest jobRequest = jobRequestService.findOneLight(i);
			jobRequestHistoryService.save(new JobRequestHistory("Edited", connectedUser, "DN Mapping Cleared for outbond DN " + OutboundDeliveryRequestReturn.getReference()
					+ " after return DN " + inboundDeliveryRequest.getReference() + " delivered / partially delivered to the warehouse", jobRequest));
		});

		evictCache("deliveryRequestService");
		evictCache("jobRequestService");
	}
}
