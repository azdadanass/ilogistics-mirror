package ma.azdad.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.PartNumber;
import ma.azdad.model.PartNumberCategory;
import ma.azdad.model.PartNumberDetail;
import ma.azdad.model.PartNumberFile;
import ma.azdad.model.PartNumberIndustry;
import ma.azdad.repos.PartNumberRepos;
import ma.azdad.service.JsService;
import ma.azdad.service.PartNumberCategoryService;
import ma.azdad.service.PartNumberDetailService;
import ma.azdad.service.PartNumberFileService;
import ma.azdad.service.PartNumberHistoryService;
import ma.azdad.service.PartNumberIndustryService;
import ma.azdad.service.PartNumberService;
import ma.azdad.service.PartNumberTypeService;
import ma.azdad.service.TextService;
import ma.azdad.service.UtilsFunctions;
import ma.azdad.utils.FacesContextMessages;

@ManagedBean
@Component
@Scope("view")
public class PartNumberView extends GenericView<Integer, PartNumber, PartNumberRepos, PartNumberService> {

	@Autowired
	private SessionView sessionView;

	@Autowired
	protected PartNumberService partNumberService;

	@Autowired
	protected PartNumberHistoryService partNumberHistoryService;

	@Autowired
	protected PartNumberFileService partNumberFileService;

	@Autowired
	protected JsService jsService;

	@Autowired
	protected FileUploadView fileUploadView;

	@Autowired
	protected PartNumberDetailService partNumberDetailService;

	@Autowired
	protected TextService textService;

	@Autowired
	protected PartNumberTypeService partNumberTypeService;

	@Autowired
	protected PartNumberIndustryService partNumberIndustryService;

	@Autowired
	protected PartNumberCategoryService partNumberCategoryService;

	private PartNumber partNumber = new PartNumber();
	private PartNumber old = new PartNumber();
	private PartNumberFile partNumberFile;
	private PartNumberFile selectedPhoto;

	private Boolean isAddFromExcelPage = false;
	private Boolean listAll = true;
	private List<Integer> toDeleteDetailList = new ArrayList<>();

//	private List<PartNumber> source;
//	private List<PartNumber> target;
//	private DualListModel<PartNumber> relatedPartNumberDualList;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		isAddFromExcelPage = ("/addPartNumberFromExcel.xhtml").equals(currentPath);

		if (isListPage)
			refreshList();
		else if (isEditPage) {
			partNumber = partNumberService.findOne(id);
			partNumber.init();
			old = partNumber.copy();
			partNumber.initDetailList();
		} else if (isViewPage || "/partNumberReporting.xhtml".equals(currentPath))
			partNumber = partNumberService.findOne(id);
//		else if ("/relatedPartNumber.xhtml".equals(currentPath)) {
//			partNumber = partNumberService.findOne(id);
//			source = partNumberService.findLight();
//			target = partNumber.getRelatedPartNumberList();
//			source.removeAll(target);
//			relatedPartNumberDualList = new DualListModel<>(source, target);
//		}

	}

	@Override
	public void refreshList() {
		if (isListPage)
			list2 = list1 = partNumberService.find(listAll, sessionView.getUsername());
	}

	public void refreshPartNumber() {
		partNumberService.flush();
		partNumber = partNumberService.findOne(partNumber.getId());
		System.out.println("partNumber.getPhotoList().size() " + partNumber.getPhotoList().size());
	}

	// SAVE PARTNUMBER
	public Boolean canSavePartNumber() {
		if (isListPage || isAddPage || isAddFromExcelPage)
			return sessionView.isSE();
		else if (isViewPage || isEditPage)
			return sessionView.isSE() && partNumber.getUnit();

		return false;
	}

	public String savePartNumber(PartNumber partNumber) {
		if (canSavePartNumber()) {
			if (!validate(partNumber))
				return null;
			try {
				partNumber.setUser(sessionView.getUser());
				if (!partNumber.getUnit()) {
					for (Integer detailId : toDeleteDetailList)
						partNumberDetailService.delete(detailId);
					for (PartNumberDetail detail : partNumber.getDetailList())
						detail.setPartNumber(partNumberService.findOne(detail.getPartNumberId()));
				}

				if (!partNumber.getExpirable())
					partNumber.setExpiryDuration(null);

				partNumber.calculateState();
				partNumber = partNumberService.save(partNumber);
				if (isAddPage || isAddFromExcelPage)
					partNumberHistoryService.created(partNumber);
				else
					partNumberHistoryService.edited(partNumber, partNumber.getChanges(old));
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

			return addParameters(viewPage, "faces-redirect=true", "id=" + partNumber.getId());
		}
		return addParameters(listPage, "faces-redirect=true");
	}

	public String savePartNumber() {
		return savePartNumber(partNumber);
	}

	public Boolean validate(PartNumber partNumber) {
		partNumber.setName(UtilsFunctions.cleanString(partNumber.getName()));
		if (partNumberService.isNameExists(partNumber)) {
			FacesContextMessages.ErrorMessages("PN already exists in database : " + partNumber.getName());
			return false;
		}
		if (!partNumber.getUnit() && partNumber.getDetailList().isEmpty())
			return FacesContextMessages.ErrorMessages("Detail List should not be null");
		return true;
	}

	// ADD FROM EXCEL
	public void uploadExcelFile(FileUploadEvent event) throws IOException {
		list1 = partNumberService.readFile(event.getFile().getInputstream());
		System.out.println(list1.size());
	}

	public Boolean canSaveFromExcel() {
		return sessionView.isSE();
	}

	public void saveFromExcel() {
		if (list1 != null) {
			for (PartNumber partNumber : list1) {
				if (partNumber.getName().isEmpty()) {
					FacesContextMessages.ErrorMessages("PN should not be empty");
					continue;
				}
				if (partNumber.getDescription().isEmpty()) {
					FacesContextMessages.ErrorMessages("Description should not be empty");
					continue;
				}

				// check industry
				if (!partNumberIndustryService.findNameList().stream().anyMatch(i -> i.equals(partNumber.getIndustryName()))) {
					FacesContextMessages.ErrorMessages("Industry must be equal to one of these values  " + partNumberIndustryService.findNameList() + " for PN : " + partNumber.getName());
					continue;
				}
				PartNumberIndustry industry = partNumberIndustryService.findByName(partNumber.getIndustryName());

				// check category
				if (!partNumberCategoryService.findNameList(industry.getId()).stream().anyMatch(i -> i.equals(partNumber.getCategoryName()))) {
					FacesContextMessages.ErrorMessages("Category must be equal to one of these values  " + partNumberCategoryService.findNameList(industry.getId()) + " for PN : " + partNumber.getName());
					continue;
				}
				PartNumberCategory category = partNumberCategoryService.findByName(industry.getId(), partNumber.getCategoryName());
				// check type

				if (!partNumberTypeService.findNameList(category.getId()).stream().anyMatch(i -> i.equals(partNumber.getTypeName()))) {
					FacesContextMessages.ErrorMessages("Type must be equal to one of these values  " + partNumberTypeService.findNameList(category.getId()) + " for PN : " + partNumber.getName());
					continue;
				}
				partNumber.setPartNumberType(partNumberTypeService.findByName(category.getId(), partNumber.getTypeName()));
				if (savePartNumber(partNumber) != null)
					FacesContextMessages.InfoMessages("PN : " + partNumber.getName() + " Added with success.");
			}
			list1.clear();
		}
	}

	// DELETE PARTNUMBER
	public Boolean canDeletePartNumber() {
		// TODO and doesnt have elements
		return sessionView.isSE() && sessionView.isTheConnectedUser(partNumber.getUser());
	}

	public String deletePartNumber() {
		if (canDeletePartNumber())
			try {
				partNumberService.delete(partNumber);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return listPage;
	}

	// FILES MANAGEMENT
	private String partNumberFileType;
	private Integer partNumberFileId;

	public Boolean canAddFile() {
		return sessionView.isTheConnectedUser(partNumber.getUser());
	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		if (!canAddFile())
			return;
		File file = fileUploadView.handleFileUpload(event, getClassName2());
		PartNumberFile partNumberFile = new PartNumberFile(file, partNumberFileType, event.getFile().getFileName(), sessionView.getUser(), partNumber);
		partNumberFileService.save(partNumberFile);
		synchronized (PartNumberView.class) {
			refreshPartNumber();
		}
		partNumberService.updateImage(partNumber.getId());
	}

	public Boolean canDeletePartNumberFile() {
		return sessionView.isTheConnectedUser(partNumber.getUser());
	}

	public void deletePartNumberFile() {
		if (!canDeletePartNumberFile())
			return;
		try {
			partNumberFileService.delete(partNumberFileId);
			refreshPartNumber();
		} catch (Exception e) {
			FacesContextMessages.ErrorMessages(e.getMessage());
		}

	}

	// Details Management
	public Boolean canAddDetail() {
		return isAddPage;
	}

	public void addDetail() {
		if (canAddDetail())
			partNumber.getDetailList().add(new PartNumberDetail(partNumber.getMaxNumberOfDetailList() + 1, partNumber));
	}

	public Boolean canRemoveDetail() {
		return true;
	}

	public void removeDetail(int index) {
		if (canRemoveDetail()) {
			Integer id = partNumber.getDetailList().get(index).getId();
			if (id != null)
				toDeleteDetailList.add(id);
			partNumber.getDetailList().remove(index);
		}

	}

	// GENERIC FUNCTIONS
	public List<PartNumber> findLight(Boolean unit) {
		return partNumberService.findLight(unit);
	}

	public List<PartNumber> findLight() {
		return partNumberService.findLight();
	}

	public List<PartNumber> completePartNumber1(String query) {
		query = "%" + query + "%";
		return partNumberService.findLikeNameOrDescription(query);
	}

	public List<PartNumber> completePartNumber2(String query) {
		query = "%" + query + "%";
		return partNumberService.findLikeNameOrDescription(query, true);
	}

	// not used anymore
//	public String generateOverlayPanelContent(PartNumber partnumber) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("<div class=\"col-xs-4 center\">");
//		sb.append("		<img src=\"" + partnumber.getImage() + "\" class=\"grid_images\" />");
//		sb.append("		<br />");
//		sb.append("		<span class=\"bolder green smaller-80\" >");
//		sb.append("			" + partnumber.getName());
//		sb.append("		</span>");
//		sb.append("</div>");
//		sb.append("<div class=\"col-xs-8\">");
//		sb.append("		<i class=\"ace-icon fa fa-caret-right middle bigger-120 blue\"></i>");
//		sb.append("		<span class=\"bolder blue\" >");
//		sb.append("			Type : " + partnumber.getType());
//		sb.append("		</span>");
//		sb.append("		<br />");
//		sb.append("		<i class=\"ace-icon fa fa-caret-right middle bigger-120 pink\"></i>");
//		sb.append("		<span class=\"pink\" >");
//		sb.append("			Industry : " + partnumber.getIndustry());
//		sb.append("		</span>");
//		sb.append("		<br />");
//		sb.append("		<i class=\"ace-icon fa fa-caret-right middle bigger-120 green\"></i>");
//		sb.append("		<span class=\"green\" >");
//		sb.append("			Manufacture : " + partnumber.getManufacture());
//		sb.append("		</span>");
//		sb.append("		<br />");
//		sb.append("		<i class=\"ace-icon fa fa-caret-right middle bigger-120 orange\"></i>");
//		sb.append("		<span class=\"orange\" >");
//		sb.append("			Technology : " + partnumber.getTechnology());
//		sb.append("		</span>");
//		sb.append("		<br />");
//		sb.append("		<i class=\"ace-icon fa fa-caret-right middle bigger-120 light-blue\"></i>");
//		sb.append("		<span class=\"light-blue\" >");
//		sb.append("			Unit/Kit : " + (partnumber.getUnit() ? "Unit" : "Kit"));
//		sb.append("		</span>");
//		sb.append("		<br />");
//		sb.append("		<i class=\"ace-icon fa fa-caret-right middle bigger-120 purple\"></i>");
//		sb.append("		<span class=\"purple\" >");
//		sb.append("			Description : " + partnumber.getDescription());
//		sb.append("		</span>");
//		sb.append("		<hr/>");
//		sb.append("</div>");
//		
//		System.out.println(sb.toString());
//
//		return sb.toString();
//
//	}

	// JS
	public String autocompleteScript(String id) {
		return jsService.typeahead(id, partNumberService.getAllNames());
	}

	// Equivalence
	public Boolean canAddEquivalence() {
		return sessionView.isAdmin();
	}

	// PACKING
	public Boolean canAddPacking() {
		return PackingView.canAddPacking(partNumber, sessionView);
	}

//	// RELATED PART NUMBER
//	private Boolean relatedPartNumberEditMode = false;
//
//	public Boolean canEditRelatedPartNumberList() {
//		return !relatedPartNumberEditMode && sessionView.isAdmin();
//	}
//
//	public void initEditRelatedPartNumberList() {
//		partNumber.getRelatedPartNumberList().forEach(i -> i.setRelatedPartNumber(i));
//	}
//
//	public Boolean canAddRelatedPartNumber() {
//		return relatedPartNumberEditMode && sessionView.isAdmin();
//	}
//
//	public void addRelatedPartNumber() {
//		if (!canAddRelatedPartNumber())
//			return;
//		partNumber.addRelatedPartNumber(new PartNumber());
//	}
//
//	public void initRelatedPartNumber(PartNumber partNumber) {
//		partNumber.setId(partNumber.getRelatedPartNumber().getId());
//		partNumber.setName(partNumber.getRelatedPartNumber().getName());
//		partNumber.setDescription(partNumber.getRelatedPartNumber().getDescription());
//	}
//
//	public Boolean canDeleteRelatedPartNumber() {
//		return relatedPartNumberEditMode && sessionView.isAdmin();
//	}
//
//	public void deleteRelatedPartNumber(int index) {
//		if (!canDeleteRelatedPartNumber())
//			return;
//		partNumber.removeRelatedPartNumber(partNumber.getRelatedPartNumberList().get(index));
//	}
//
//	public Boolean canSaveRelatedPartNumberList() {
//		return relatedPartNumberEditMode && sessionView.isAdmin();
//	}
//
//	public void saveRelatedPartNumberList() {
//		if (!canSaveRelatedPartNumberList())
//			return;
//		if (!validatesSaveRelatedPartNumberList())
//			return;
//
//		List<PartNumber> tmpList = new ArrayList<PartNumber>(partNumber.getRelatedPartNumberList());
//		partNumber.removeAllRelatedPartNumber();
//		tmpList.forEach(i -> partNumber.getRelatedPartNumberList().add(partNumberService.findOne(i.getId())));
//		partNumberService.save(partNumber);
//		partNumber = partNumberService.findOne(partNumber.getId());
//
//		relatedPartNumberEditMode = false;
//
//	}
//
//	private Boolean validatesSaveRelatedPartNumberList() {
//		if(partNumber.getRelatedPartNumberList().stream().filter(i->i.getRelatedPartNumber()==null).count()>0)
//			return FacesContextMessages.ErrorMessages("Part Numbrt should not be null");
//		
//		if (partNumber.getRelatedPartNumberList().size() > partNumber.getRelatedPartNumberList().stream().map(i -> i.getId()).distinct().count())
//			return FacesContextMessages.ErrorMessages("Duplicate Part Number");
//
//		return true;
//	}

	// GETTERS & SETTERS




	public PartNumberService getPartNumberService() {
		return partNumberService;
	}

	public void setPartNumberService(PartNumberService partNumberService) {
		this.partNumberService = partNumberService;
	}

	public PartNumber getPartNumber() {
		return partNumber;
	}

	public void setPartNumber(PartNumber partNumber) {
		this.partNumber = partNumber;
	}

	public FileUploadView getFileUploadView() {
		return fileUploadView;
	}

	public void setFileUploadView(FileUploadView fileUploadView) {
		this.fileUploadView = fileUploadView;
	}

	public PartNumberFileService getPartNumberFileService() {
		return partNumberFileService;
	}

	public void setPartNumberFileService(PartNumberFileService partNumberFileService) {
		this.partNumberFileService = partNumberFileService;
	}

	public String getPartNumberFileType() {
		return partNumberFileType;
	}

	public void setPartNumberFileType(String partNumberFileType) {
		this.partNumberFileType = partNumberFileType;
	}

	public Integer getPartNumberFileId() {
		return partNumberFileId;
	}

	public void setPartNumberFileId(Integer partNumberFileId) {
		this.partNumberFileId = partNumberFileId;
	}

	public PartNumberFile getPartNumberFile() {
		return partNumberFile;
	}

	public void setPartNumberFile(PartNumberFile partNumberFile) {
		this.partNumberFile = partNumberFile;
	}

	public PartNumberFile getSelectedPhoto() {
		return selectedPhoto;
	}

	public void setSelectedPhoto(PartNumberFile selectedPhoto) {
		this.selectedPhoto = selectedPhoto;
	}

	public Boolean getListAll() {
		return listAll;
	}

	public void setListAll(Boolean listAll) {
		this.listAll = listAll;
	}

//	public DualListModel<PartNumber> getRelatedPartNumberDualList() {
//		return relatedPartNumberDualList;
//	}
//
//	public void setRelatedPartNumberDualList(DualListModel<PartNumber> relatedPartNumberDualList) {
//		this.relatedPartNumberDualList = relatedPartNumberDualList;
//	}

//	public Boolean getRelatedPartNumberEditMode() {
//		return relatedPartNumberEditMode;
//	}
//
//	public void setRelatedPartNumberEditMode(Boolean relatedPartNumberEditMode) {
//		this.relatedPartNumberEditMode = relatedPartNumberEditMode;
//	}

}
