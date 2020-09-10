package ma.azdad.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.PartNumber;
import ma.azdad.repos.PartNumberRepos;
import ma.azdad.utils.PartNumberExcelFileException;
import ma.azdad.view.FacesContextMessages;

@Component
@Transactional
public class PartNumberService extends GenericServiceOld<PartNumber> {

	@Autowired
	private PartNumberRepos partNumberRepos;

	@Autowired
	private TextService textService;

	@Autowired
	private PackingService packingService;

	@Autowired
	protected PartNumberTypeService partNumberTypeService;

	@Autowired
	protected PartNumberIndustryService partNumberIndustryService;

	@Autowired
	protected PartNumberCategoryService partNumberCategoryService;

	@Autowired
	BrandService brandService;

	public List<PartNumber> find(Boolean all, String username) {
		if (all)
			return findAll();
		else
			return partNumberRepos.find(username);
	}

	@Override
	public PartNumber findOne(Integer id) {
		PartNumber partNumber = super.findOne(id);
		Hibernate.initialize(partNumber.getBrand());
		Hibernate.initialize(partNumber.getFileList());
		Hibernate.initialize(partNumber.getHistoryList());
		Hibernate.initialize(partNumber.getDetailList());
		Hibernate.initialize(partNumber.getEquivalenceList());
		// Hibernate.initialize(partNumber.getRelatedPartNumberList());
		// Hibernate.initialize(partNumber.getPackingList());
		return partNumber;
	}

	@Override
	@CacheEvict(value = { "pns.cache1", "pns.cache2", "pns.cache3" }, allEntries = true)
	public PartNumber save(PartNumber a) {
		return super.save(a);
	}

	public String getAllNames() {
		List<String> names = partNumberRepos.getAllNames();
		return Arrays.toString(names.toArray());
	}

	@Cacheable(value = "pns.cache1")
	public List<PartNumber> findLight() {
		return partNumberRepos.findLight();
	}

	@Cacheable(value = "pns.cache2")
	public List<PartNumber> findLight(Boolean unit) {
		return partNumberRepos.findLight(unit);
	}

	public Long countByName(String name, Integer id) {
		Long l = id == null ? partNumberRepos.countByName(name) : partNumberRepos.countByName(name, id);
		return l != null ? l : 0;
	}

	public Boolean isNameExists(PartNumber partNumber) {
		return countByName(partNumber.getName(), partNumber.getId()) > 0;
	}

	@Cacheable(value = "pns.cache3")
	public List<PartNumber> findLikeNameOrDescription(String query) {
		return partNumberRepos.findLikeNameOrDescription(query);
	}

	public List<PartNumber> findLikeNameOrDescription(String query, Boolean stockItem) {
		return partNumberRepos.findLikeNameOrDescription(query, stockItem);
	}

	public List<PartNumber> readFile(InputStream inputStream) {
		List<PartNumber> result = new ArrayList<>();
		try {
			// POIFSFileSystem fs = new POIFSFileSystem(new
			// FileInputStream("/home/anass/Bureau/template.xls"));
			POIFSFileSystem fs = new POIFSFileSystem(inputStream);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
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
			if (cols != 9)
				FacesContextMessages.ErrorMessages("number of columns should be 9 with this order (PN, description, industry, category, type, brand, partialDelivery, unitType, stockItem)");
			for (int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				try {
					String name = getString("Name", row.getCell(0));
					String description = getString("Description", row.getCell(1));
					String industry = getString("Industry", row.getCell(2), partNumberIndustryService.findNameList());
					String category = getString("Category", row.getCell(3), partNumberCategoryService.findNameList(partNumberIndustryService.findByName(industry).getId()));
					String type = getString("Type", row.getCell(4), partNumberTypeService.findNameList(partNumberCategoryService.findByName(partNumberIndustryService.findByName(industry).getId(), category).getId()));
					String brand = getString("Brand", row.getCell(5), brandService.findNameList());
					String partialDelivery = getString("Partial Delivery", row.getCell(6), Arrays.asList("Yes", "No"));
					String unitType = getString("Unit Type", row.getCell(7), textService.findValueListByBeanNameAndType("partNumber", "unitType"));
					String stockItem = getString("Partial Delivery", row.getCell(6), Arrays.asList("Yes", "No"));

					PartNumber partNumber = new PartNumber();
					partNumber.setName(name);
					partNumber.setDescription(description);
					partNumber.setPartNumberType(partNumberTypeService.findByName(partNumberCategoryService.findByName(partNumberIndustryService.findByName(industry).getId(), category).getId(), type));
					partNumber.setBrand(brandService.findByName(brand));
					partNumber.setPartialDelivery("yes".equalsIgnoreCase(partialDelivery));
					partNumber.setUnitType(unitType);
					partNumber.setStockItem("yes".equalsIgnoreCase(stockItem));
					result.add(partNumber);
				} catch (PartNumberExcelFileException e) {
					FacesContextMessages.WarningMessages("ignore row :" + (r + 1) + ", " + e.getMessage());
				}
			}
		} catch (IOException e1) {
			log.error(e1.getMessage());
		}
		return result;
	}

	private String getString(String field, HSSFCell cell) throws PartNumberExcelFileException {
		return getString(field, cell, null);
	}

	private String getString(String field, HSSFCell cell, List<String> valueList) throws PartNumberExcelFileException {
		String string = UtilsFunctions.cleanString(UtilsFunctions.convertToString(cell));
		if (string == null || string.isEmpty())
			throw new PartNumberExcelFileException(field + " should not be null");
		if (valueList == null)
			return string;
		return valueList.stream().distinct().filter(i -> i.equalsIgnoreCase(string)).findFirst().orElseThrow(() -> new PartNumberExcelFileException(field + " : " + string + " should be one of these : " + valueList));
	}

	@Async
	public void updateImage() {
		partNumberRepos.updateImage();
		partNumberRepos.updateNullImage();
	}

	@Async
	public void updateImage(Integer id) {
		partNumberRepos.updateImage(id);
	}

	public void updateHasPacking(Integer id) {
		partNumberRepos.updateHasPacking(id, packingService.countByPartNumber(id) > 0);
	}

	public void updateHasPacking() {
		findLight().forEach(i -> updateHasPacking(i.getId()));
	}

	public Boolean getHasPacking(Integer id) {
		return partNumberRepos.getHasPacking(id);
	}

}
