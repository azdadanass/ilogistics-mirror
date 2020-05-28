package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.JobRequestDeliveryDetail;
import ma.azdad.service.JobRequestDeliveryDetailService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class JobRequestDeliveryDetailView extends GenericView<JobRequestDeliveryDetail> {

	@Autowired
	private JobRequestDeliveryDetailService jobRequestDeliveryDetailService;

	@Autowired
	private CacheView cacheView;

	private JobRequestDeliveryDetail jobRequestDeliveryDetail = new JobRequestDeliveryDetail();

	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			jobRequestDeliveryDetail = jobRequestDeliveryDetailService.findOne(selectedId);
		else if (isViewPage)
			jobRequestDeliveryDetail = jobRequestDeliveryDetailService.findOne(selectedId);
	}

	protected void initParameters() {
		super.initParameters();
	}

	public void refreshList() {
		if (isListPage)
			list2 = list1 = jobRequestDeliveryDetailService.findAll();
	}

	public void refreshList(Integer projectId) {
		list2 = list1 = jobRequestDeliveryDetailService.findInstalledByProject(projectId);
	}

	public void flushJobRequestDeliveryDetail() {
		jobRequestDeliveryDetailService.flush();
	}

	public void refreshJobRequestDeliveryDetail() {
		jobRequestDeliveryDetail = jobRequestDeliveryDetailService.findOne(jobRequestDeliveryDetail.getId());
	}

	/*
	 * Redirection
	 */
	public void redirect() {
		if (!canViewJobRequestDeliveryDetail())
			cacheView.accessDenied();
	}

	public Boolean canViewJobRequestDeliveryDetail() {
		return true;
	}

	// SAVE JOBREQUESTDELIVERYDETAIL
	public Boolean canSaveJobRequestDeliveryDetail() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveJobRequestDeliveryDetail() {
		if (!canSaveJobRequestDeliveryDetail())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateJobRequestDeliveryDetail())
			return null;
		jobRequestDeliveryDetail = jobRequestDeliveryDetailService.save(jobRequestDeliveryDetail);

		return addParameters(viewPage, "faces-redirect=true", "id=" + jobRequestDeliveryDetail.getId());
	}

	public Boolean validateJobRequestDeliveryDetail() {
		return true;
	}

	// DELETE JOBREQUESTDELIVERYDETAIL
	public Boolean canDeleteJobRequestDeliveryDetail() {
		return true;
	}

	public String deleteJobRequestDeliveryDetail() {
		if (canDeleteJobRequestDeliveryDetail())
			jobRequestDeliveryDetailService.delete(jobRequestDeliveryDetail);
		return addParameters(listPage, "faces-redirect=true");
	}

	// GETTERS & SETTERS
	public JobRequestDeliveryDetail getJobRequestDeliveryDetail() {
		return jobRequestDeliveryDetail;
	}

	public void setJobRequestDeliveryDetail(JobRequestDeliveryDetail jobRequestDeliveryDetail) {
		this.jobRequestDeliveryDetail = jobRequestDeliveryDetail;
	}

}
