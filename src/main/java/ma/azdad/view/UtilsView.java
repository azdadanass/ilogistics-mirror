package ma.azdad.view;

import java.util.Date;

import javax.faces.bean.ManagedBean;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.service.UtilsFunctions;

@ManagedBean(eager = true)
@Component
@Transactional
@Scope("application")
public class UtilsView {
	
	public String getBackground(Integer index) {
		return UtilsFunctions.getBackground(index);
	}

	public String getBadge(Integer index) {
		return UtilsFunctions.getBadge(index);
	}

	public String getMapIcon(Integer index) {
		return UtilsFunctions.getMapIcon(index);
	}

	public String getLetter(Integer index) {
		return UtilsFunctions.getLetter(index);
	}

	public Date getCurrentDate() {
		return new Date();
	}

	public void excelExportation(Object document) {
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		int rows; // No of rows
		rows = sheet.getPhysicalNumberOfRows();
		int cols = 0; // No of columns
		int tmp = 0;
		for (int i = 0; i < 10 || i < rows; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				tmp = sheet.getRow(i).getPhysicalNumberOfCells();
				if (tmp > cols)
					cols = tmp;
			}
		}

		for (int r = 0; r < rows; r++) {
			row = sheet.getRow(r);
			for (int c = 0; c < cols; c++)
				formatCell(row.getCell(c));
		}

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		Font font = wb.createFont();
		font.setColor(HSSFColor.WHITE.index);
		cellStyle.setFont(font);

		row = sheet.getRow(0);
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++)
			row.getCell(i).setCellStyle(cellStyle);

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
		str = str.replaceAll("<[^>]*>", "").trim();
		// DATE ?
		Date date = null;
		if (str.length() == 10)
			date = UtilsFunctions.getDate(str);
		else if (str.length() == 19)
			date = UtilsFunctions.getDateTime(str);
		else if (str.length() == 16)
			date = UtilsFunctions.getDateTimeWithoutSeconds(str);
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

}