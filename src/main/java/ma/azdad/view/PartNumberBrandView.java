package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumberBrand;
import ma.azdad.model.Supplier;
import ma.azdad.repos.PartNumberBrandRepos;
import ma.azdad.service.PartNumberBrandService;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.SupplierService;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class PartNumberBrandView extends GenericView<Integer, PartNumberBrand, PartNumberBrandRepos, PartNumberBrandService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private PartNumberBrandService brandService;

	@Autowired
	private CacheView cacheView;

	@Autowired
	private FileUploadView fileUploadView;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private PartNumberService partNumberService;

	private PartNumberBrand brand = new PartNumberBrand();

	List<Supplier> source;
	List<Supplier> target;
	private DualListModel<Supplier> supplierDualList;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isAddPage) {
			source = supplierService.findLight();
			target = new ArrayList<Supplier>();
			supplierDualList = new DualListModel<>(source, target);
		} else if (isEditPage) {
			brand = brandService.findOne(id);
			source = supplierService.findLight();
			target = new ArrayList<Supplier>(brand.getSupplierList());
			source.removeAll(target);
			supplierDualList = new DualListModel<>(source, target);
		} else if (isViewPage)
			brand = brandService.findOne(id);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = brandService.findAll();
	}

	public void flushBrand() {
		brandService.flush();
	}

	public void refreshBrand() {
		if (brand.getId() != null)
			brand = brandService.findOne(brand.getId());
	}

	/*
	 * Redirection
	 */
	@Override
	public void redirect() {
		if (!canViewBrand())
			cacheView.accessDenied();
	}

	public Boolean canViewBrand() {
		return sessionView.getIsAdmin();
	}

	// SAVE BRAND
	public Boolean canSaveBrand() {
		if (isListPage || isAddPage)
			return sessionView.getIsAdmin();
		else if (isViewPage || isEditPage)
			return sessionView.getIsAdmin();
		;
		return false;
	}

	public String saveBrand() {
		if (!canSaveBrand())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateBrand())
			return null;

		System.out.println("saveBrand");
		
		Set<Integer> existingSupplierIdSet = brand.getSupplierList().stream().map(i -> i.getId()).collect(Collectors.toSet());
		supplierDualList.getTarget().stream().filter(i -> !existingSupplierIdSet.contains(i.getId())).forEach(i -> brand.getSupplierList().add(supplierService.findOne(i.getId())));

		brand = brandService.save(brand);

		if (isEditPage)
			partNumberService.updateBrandName(brand.getId(), brand.getName());

		return addParameters(viewPage, "faces-redirect=true", "id=" + brand.getId());
	}

	public Boolean validateBrand() {
		if (brandService.isNameExists(brand.getName(), brand.getId()))
			return FacesContextMessages.ErrorMessages("Name already exists");

		return true;
	}

	// DELETE BRAND
	public Boolean canDeleteBrand() {
		return sessionView.getIsAdmin();
	}

	public String deleteBrand() {
		if (canDeleteBrand())
			try {
				brandService.delete(brand);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return addParameters(listPage, "faces-redirect=true");
	}

	// PHOTO MANAGEMENT
	public Boolean canUploadImage() {
		return sessionView.isAdmin();
	}

	public void uploadImage(FileUploadEvent event) {
		if (!canUploadImage())
			return;
		String fileName = "il_brand_" + brand.getId();
		String link = fileUploadView.uploadFileOld(event, fileName, "photos/il_brand");
		brand.setImage(link);
		brandService.save(brand);
		refreshBrand();
	}

	// photos
	public Boolean canUploadPhoto() {
		return sessionView.isAdmin();
	}

	public void handlePhotoUpload(FileUploadEvent event) throws IOException {
		System.out.println("canUploadPhoto");
		File file = fileUploadView.handlePhotoUpload(event, getClassName2(), 400 * 1024);
		brand.setImage("files/" + getClassName2() + "/" + file.getName());
		synchronized (PartNumberBrandView.class) {
			brand = service.saveAndRefresh(brand);
		}
	}

	// generic
	public List<PartNumberBrand> findAll() {
		return brandService.findAll();
	}

	// GETTERS & SETTERS
	public PartNumberBrand getBrand() {
		return brand;
	}

	public void setBrand(PartNumberBrand brand) {
		this.brand = brand;
	}

	public DualListModel<Supplier> getSupplierDualList() {
		return supplierDualList;
	}

	public void setSupplierDualList(DualListModel<Supplier> supplierDualList) {
		this.supplierDualList = supplierDualList;
	}

}
