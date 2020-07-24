package ma.azdad.view;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ma.azdad.model.GenericBean;
import ma.azdad.service.UtilsFunctions;

public class GenericViewOld<A extends GenericBean> {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected SessionView sessionView;

	@Autowired
	protected UtilsView utilsView;

	protected List<A> list1 = new ArrayList<>();
	protected List<A> list2 = new ArrayList<>();
	protected List<A> list3;
	protected List<A> list4;

	protected Integer selectedId;

	protected String currentPath;

	private String searchBean = "";

	protected String className1 = getParameterClassName();
	protected String className2 = className1.substring(0, 1).toLowerCase() + className1.substring(1);
	protected String listPage = className2 + "List.xhtml";
	protected String addEditPage = "addEdit" + className1 + ".xhtml";
	protected String viewPage = "view" + className1 + ".xhtml";

	protected Boolean isListPage = false;
	protected Boolean isAddPage = false;
	protected Boolean isEditPage = false;
	protected Boolean isViewPage = false;

	protected Integer pageIndex;

	public void init() {
		currentPath = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		initParameters();
		isListPage = ("/" + listPage).equals(currentPath);
		isAddPage = ("/" + addEditPage).equals(currentPath) && selectedId == null;
		isEditPage = ("/" + addEditPage).equals(currentPath) && selectedId != null;
		isViewPage = ("/" + viewPage).equals(currentPath);
	}

	protected void initParameters() {
		selectedId = UtilsFunctions.getIntegerParameter("id");
		pageIndex = UtilsFunctions.getIntegerParameter("pageIndex");
	}

	private void filterBean(String query) {
		list3 = null;
		List<A> list = new ArrayList<A>();
		query = query.toLowerCase().trim();
		for (A bean : list1) {
			if (bean.filter(query))
				list.add(bean);
		}
		list2 = list;
	}

	public Integer getRowsNumber() {
		if (list3 != null)
			return list3.size();
		else
			return list2.size();
	}

	public Integer getSelectedRowsNumber() {
		if (list4 != null)
			return list4.size();
		return 0;
	}

	public static String addParameters(String path, String... tab) {
		path += "?" + tab[0];
		if (tab.length > 1)
			for (int i = 1; i < tab.length; i++)
				path += "&" + tab[i];
		return path;
	}

	public void excelExportation(Object document) {
		utilsView.excelExportation(document);

		//		HSSFWorkbook wb = (HSSFWorkbook) document;
		//		HSSFSheet sheet = wb.getSheetAt(0);
		//		HSSFRow row;
		//		int rows; // No of rows
		//		rows = sheet.getPhysicalNumberOfRows();
		//		int cols = 0; // No of columns
		//		int tmp = 0;
		//		for (int i = 0; i < 10 || i < rows; i++) {
		//			row = sheet.getRow(i);
		//			if (row != null) {
		//				tmp = sheet.getRow(i).getPhysicalNumberOfCells();
		//				if (tmp > cols)
		//					cols = tmp;
		//			}
		//		}
		//
		//		for (int r = 0; r < rows; r++) {
		//			row = sheet.getRow(r);
		//			for (int c = 0; c < cols; c++)
		//				formatCell(row.getCell(c));
		//		}
		//
		//		HSSFCellStyle cellStyle = wb.createCellStyle();
		//		cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		//		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//		Font font = wb.createFont();
		//		font.setColor(HSSFColor.WHITE.index);
		//		cellStyle.setFont(font);
		//
		//		row = sheet.getRow(0);
		//		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++)
		//			row.getCell(i).setCellStyle(cellStyle);

	}

	private void formatCell(HSSFCell cell) {
		String str;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			str = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			str = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			str = String.valueOf(cell.getNumericCellValue());
			break;
		default:
			str = "";
			break;
		}
		// CLEAN FROM HTML TAGS
		str = str.replaceAll("<[^>]*>", "");
		// DATE ?
		Date date = UtilsFunctions.getDate(str);
		if (date == null)
			date = UtilsFunctions.getDateTime(str);
		if (date != null) {
			cell.setCellValue(date);
			return;
		}
		// NUMERIC ?
		if (!str.startsWith("0")) {
			Double d = null;
			try {
				d = Double.valueOf(str.replace(" ", "").replace(" ", "").replace(",", ".").replace("'", ""));
			} catch (Exception e) {
				d = null;
			}
			if (d != null) {
				cell.setCellValue(d);
				return;
			}
		}

		cell.setCellValue(str);
	}

	public void execJavascript(String script) {
		RequestContext.getCurrentInstance().execute(script);
	}

	@SuppressWarnings("unchecked")
	public String getParameterClassName() {
		return ((Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getSimpleName();
	}

	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public List<A> getList1() {
		return list1;
	}

	public void setList1(List<A> list1) {
		this.list1 = list1;
	}

	public List<A> getList2() {
		return list2;
	}

	public void setList2(List<A> list2) {
		this.list2 = list2;
	}

	public List<A> getList3() {
		return list3;
	}

	public void setList3(List<A> list3) {
		this.list3 = list3;
	}

	public List<A> getList4() {
		return list4;
	}

	public void setList4(List<A> list4) {
		this.list4 = list4;
	}

	public Integer getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(Integer selectedId) {
		this.selectedId = selectedId;
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void setCurrentPath(String currentPath) {
		this.currentPath = currentPath;
	}

	public String getSearchBean() {
		return searchBean;
	}

	public void setSearchBean(String searchBean) {
		this.searchBean = searchBean;
		filterBean(searchBean);
	}

	public Logger getLog() {
		return log;
	}

	public String getClassName1() {
		return className1;
	}

	public void setClassName1(String className1) {
		this.className1 = className1;
	}

	public String getClassName2() {
		return className2;
	}

	public void setClassName2(String className2) {
		this.className2 = className2;
	}

	public String getListPage() {
		return listPage;
	}

	public void setListPage(String listPage) {
		this.listPage = listPage;
	}

	public String getAddEditPage() {
		return addEditPage;
	}

	public void setAddEditPage(String addEditPage) {
		this.addEditPage = addEditPage;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

	public Boolean getIsListPage() {
		return isListPage;
	}

	public void setIsListPage(Boolean isListPage) {
		this.isListPage = isListPage;
	}

	public Boolean getIsAddPage() {
		return isAddPage;
	}

	public void setIsAddPage(Boolean isAddPage) {
		this.isAddPage = isAddPage;
	}

	public Boolean getIsEditPage() {
		return isEditPage;
	}

	public void setIsEditPage(Boolean isEditPage) {
		this.isEditPage = isEditPage;
	}

	public Boolean getIsViewPage() {
		return isViewPage;
	}

	public void setIsViewPage(Boolean isViewPage) {
		this.isViewPage = isViewPage;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

}
