package ma.azdad.view;

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
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Brand;
import ma.azdad.model.Supplier;
import ma.azdad.service.BrandService;
import ma.azdad.service.SupplierService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class BrandView extends GenericViewOld<Brand> {

	@Autowired
	private BrandService brandService;

	@Autowired
	private CacheView cacheView;

	@Autowired
	private FileView fileView;

	@Autowired
	private SupplierService supplierService;

	private Brand brand = new Brand();

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
			brand = brandService.findOne(selectedId);
			source = supplierService.findLight();
			target = new ArrayList<Supplier>(brand.getSupplierList());
			source.removeAll(target);
			supplierDualList = new DualListModel<>(source, target);
		} else if (isViewPage)
			brand = brandService.findOne(selectedId);
	}

	@Override
	protected void initParameters() {
		super.initParameters();
	}

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

		Set<Integer> existingSupplierIdSet = brand.getSupplierList().stream().map(i -> i.getId()).collect(Collectors.toSet());
		supplierDualList.getTarget().stream().filter(i -> !existingSupplierIdSet.contains(i.getId())).forEach(i -> brand.getSupplierList().add(supplierService.findOne(i.getId())));

		brand = brandService.save(brand);

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
			brandService.delete(brand);
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
		String link = fileView.uploadFileOld(event, fileName, "photos/il_brand");
		brand.setImage(link);
		brandService.save(brand);
		refreshBrand();
	}

	// GENERIC
	public List<Brand> findAll() {
		return brandService.findAll();
	}

	// GETTERS & SETTERS
	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	public DualListModel<Supplier> getSupplierDualList() {
		return supplierDualList;
	}

	public void setSupplierDualList(DualListModel<Supplier> supplierDualList) {
		this.supplierDualList = supplierDualList;
	}

}
