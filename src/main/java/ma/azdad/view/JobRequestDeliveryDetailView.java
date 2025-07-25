package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.JobRequestDeliveryDetail;
import ma.azdad.repos.JobRequestDeliveryDetailRepos;
import ma.azdad.service.JobRequestDeliveryDetailService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class JobRequestDeliveryDetailView extends GenericView<Integer, JobRequestDeliveryDetail, JobRequestDeliveryDetailRepos, JobRequestDeliveryDetailService> {

	@Autowired
	private CacheView cacheView;

	private JobRequestDeliveryDetail jobRequestDeliveryDetail = new JobRequestDeliveryDetail();
	
	
	private DatatableList<JobRequestDeliveryDetail> datatable1;
	private DatatableList<JobRequestDeliveryDetail> datatable2;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			jobRequestDeliveryDetail = service.findOne(id);
		else if (isViewPage)
			jobRequestDeliveryDetail = service.findOne(id);
		else if(isPage("viewDeliveryRequest")) {
			datatable1 = new DatatableList<JobRequestDeliveryDetail>(service.findByDeliveryRequest(id));
			datatable2 = new DatatableList<JobRequestDeliveryDetail>(service.findSummaryByDeliveryRequest(id));
		}
			
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = service.findAll();
		if(isPage("viewDeliveryRequest"))
			initLists(service.findInstalledByDeliveryRequest(id));
	}

	public void refreshListByProject(Integer projectId) {
		list2 = list1 = service.findInstalledByProject(projectId);
	}

	public void flushJobRequestDeliveryDetail() {
		service.flush();
	}

	public void refreshJobRequestDeliveryDetail() {
		jobRequestDeliveryDetail = service.findOne(jobRequestDeliveryDetail.getId());
	}

	/*
	 * Redirection
	 */
	@Override
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
		jobRequestDeliveryDetail = service.save(jobRequestDeliveryDetail);

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
			try {
				service.delete(jobRequestDeliveryDetail);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return addParameters(listPage, "faces-redirect=true");
	}

	// GETTERS & SETTERS
	public JobRequestDeliveryDetail getJobRequestDeliveryDetail() {
		return jobRequestDeliveryDetail;
	}

	public void setJobRequestDeliveryDetail(JobRequestDeliveryDetail jobRequestDeliveryDetail) {
		this.jobRequestDeliveryDetail = jobRequestDeliveryDetail;
	}

	public DatatableList<JobRequestDeliveryDetail> getDatatable1() {
		return datatable1;
	}

	public void setDatatable1(DatatableList<JobRequestDeliveryDetail> datatable1) {
		this.datatable1 = datatable1;
	}

	public DatatableList<JobRequestDeliveryDetail> getDatatable2() {
		return datatable2;
	}

	public void setDatatable2(DatatableList<JobRequestDeliveryDetail> datatable2) {
		this.datatable2 = datatable2;
	}
	
	
	

}
