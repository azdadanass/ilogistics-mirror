package ma.azdad.view;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Base;
import ma.azdad.model.BaseFile;
import ma.azdad.service.BaseFileService;
import ma.azdad.service.BaseService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class BaseView extends GenericView<Base> {

	@Autowired
	protected BaseService baseService;

	@Autowired
	protected BaseFileService baseFileService;

	@Autowired
	protected FileView fileView;

	private Base base = new Base();
	private BaseFile baseFile;

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			base = baseService.findOne(selectedId);
		else if (isViewPage)
			base = baseService.findOne(selectedId);

	}

	public void refreshList() {
		if (isListPage)
			list2 = list1 = baseService.findAll();
	}

	public void refreshBase() {
		baseService.flush();
		base = baseService.findOne(base.getId());
	}

	// SAVE BASE
	public Boolean canSaveBase() {
		return true;
	}

	public String saveBase() {
		if (canSaveBase()) {
			base = baseService.save(base);
			return addParameters(viewPage, "faces-redirect=true", "id=" + base.getId());
		}
		return listPage;
	}

	// DELETE BASE
	public Boolean canDeleteBase() {
		return true;
	}

	public String deleteBase() {
		if (canDeleteBase())
			baseService.delete(base);
		return listPage;
	}

	// FILES MANAGEMENT
	private String baseFileType;
	private Integer baseFileId;

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		File file = fileView.handleFileUpload(event, getClassName2());
		BaseFile baseFile = new BaseFile(getClassName2(), file, baseFileType, event.getFile().getFileName(), sessionView.getUser());
		baseFileService.save(baseFile);
		synchronized (BaseView.class) {
			refreshBase();
		}
	}

	public void deleteBaseFile() {
		baseFileService.delete(baseFileId);
		refreshBase();
	}

	// GETTERS & SETTERS
	@Override
	public SessionView getSessionView() {
		return sessionView;
	}

	@Override
	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public Base getBase() {
		return base;
	}

	public void setBase(Base base) {
		this.base = base;
	}

	public FileView getFileView() {
		return fileView;
	}

	public void setFileView(FileView fileView) {
		this.fileView = fileView;
	}

	public BaseFileService getBaseFileService() {
		return baseFileService;
	}

	public void setBaseFileService(BaseFileService baseFileService) {
		this.baseFileService = baseFileService;
	}

	public String getBaseFileType() {
		return baseFileType;
	}

	public void setBaseFileType(String baseFileType) {
		this.baseFileType = baseFileType;
	}

	public Integer getBaseFileId() {
		return baseFileId;
	}

	public void setBaseFileId(Integer baseFileId) {
		this.baseFileId = baseFileId;
	}

	public BaseFile getBaseFile() {
		return baseFile;
	}

	public void setBaseFile(BaseFile baseFile) {
		this.baseFile = baseFile;
	}

}