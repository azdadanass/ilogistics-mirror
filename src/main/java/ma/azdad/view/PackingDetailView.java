package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.PackingDetail;
import ma.azdad.repos.PackingDetailRepos;
import ma.azdad.service.PackingDetailService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class PackingDetailView extends GenericView<Integer, PackingDetail, PackingDetailRepos, PackingDetailService> {

	@Autowired
	private PackingDetailService packingDetailService;

	@Autowired
	private CacheView cacheView;

	private PackingDetail packingDetail = new PackingDetail();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			packingDetail = packingDetailService.findOne(id);
		else if (isViewPage)
			packingDetail = packingDetailService.findOne(id);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = packingDetailService.findAll();
	}

	public void flushPackingDetail() {
		packingDetailService.flush();
	}

	public void refreshPackingDetail() {
		if (packingDetail.getId() != null)
			packingDetail = packingDetailService.findOne(packingDetail.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewPackingDetail())
			cacheView.accessDenied();
	}

	public Boolean canViewPackingDetail() {
		return true;
	}

	// SAVE PACKINGDETAIL
	public Boolean canSavePackingDetail() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String savePackingDetail() {
		if (!canSavePackingDetail())
			return addParameters(listPage, "faces-redirect=true");
		if (!validatePackingDetail())
			return null;
		packingDetail = packingDetailService.save(packingDetail);

		return addParameters(viewPage, "faces-redirect=true", "id=" + packingDetail.getId());
	}

	public Boolean validatePackingDetail() {
		return true;
	}

	// DELETE PACKINGDETAIL
	public Boolean canDeletePackingDetail() {
		return true;
	}

	public String deletePackingDetail() {
		if (canDeletePackingDetail())
			try {
				packingDetailService.delete(packingDetail);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}
		return addParameters(listPage, "faces-redirect=true");
	}

	// GENERIC
	public List<PackingDetail> findByPartNumber(Integer partNumberId) {
		return packingDetailService.findByPartNumber(partNumberId);
	}

	// GETTERS & SETTERS
	public PackingDetail getPackingDetail() {
		return packingDetail;
	}

	public void setPackingDetail(PackingDetail packingDetail) {
		this.packingDetail = packingDetail;
	}

}
