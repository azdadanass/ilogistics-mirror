package ma.azdad.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import ma.azdad.model.Site;
import ma.azdad.model.TransportationJob;
import ma.azdad.model.TransportationRequest;
import ma.azdad.repos.SiteRepos;
import ma.azdad.repos.TransportationJobRepos;
import ma.azdad.repos.TransportationRequestRepos;
import ma.azdad.utils.SiteExcelFileException;

@Component
public class SiteService extends GenericService<Integer, Site, SiteRepos> {

	@Autowired
	SiteRepos repos;

	@Autowired
	TransportationRequestRepos transportationRequestRepos;

	@Autowired
	TransportationJobRepos transportationJobRepos;

	@Autowired
	TransportationRequestService transportationRequestService;

	@Autowired
	TransportationJobService transportationJobService;

	@Autowired
	GoogleGeocodeService googleGeocodeService;

	@Override
	public Site findOne(Integer id) {
		Site site = super.findOne(id);
		Hibernate.initialize(site.getFileList());
		Hibernate.initialize(site.getHistoryList());
		Hibernate.initialize(site.getContact());
		Hibernate.initialize(site.getCustomer());
		Hibernate.initialize(site.getSupplier());
		return site;
	}

	public Site findOneLazy(Integer id) {
		return super.findOne(id);
	}

	@Override
	@CacheEvict(value = { "siteService.cache1", "siteService.cache2", "siteService.cache3", "siteService.cache4" }, allEntries = true)
	public Site save(Site a) {
		Site site = super.save(a);
		googleGeocodeService.updateGoogleGeocodeDataAsync(site);
		return site;
	}

	public List<Site> findLight() {
		return repos.findLight();
	}
	
	public List<Site> findByCategoryAndGoogleRegion(Integer categoryId,String googleRegion){
		return repos.findByCategoryAndGoogleRegion(categoryId, googleRegion);
	}
	

	@Cacheable(value = "siteService.cache2")
	public List<Site> findLight(Integer typeId) {
		return repos.findLight(typeId);
	}

	@Cacheable(value = "siteService.cache3")
	public Map<String, List<Site>> findLightMap(Integer typeId) {
		Map<String, List<Site>> result = new HashMap<String, List<Site>>();
		for (Site site : repos.findAllCoordinates(typeId)) {
			String key = site.getCustomerId() + ";" + site.getSupplierId() + ";" + site.getOwner();

			result.putIfAbsent(key, new ArrayList<Site>());
			result.get(key).add(site);
		}
		return result;
	}

	@Cacheable(value = "siteService.cache4")
	public List<String> getNearbySites(Site site) {
		List<String> result = new ArrayList<>();
		Map<String, List<Site>> map = findLightMap(site.getType().getId());
		String key = site.getCustomerId() + ";" + site.getSupplierId() + ";" + site.getOwner();

		System.out.println("KEY !! : " + key);

		if (map.get(key) != null)
			for (Site s : map.get(key))
				if (UtilsFunctions.distFrom(site, s) < 100)
					result.add(s.getName());
		return result;
	}

	public List<Site> findLight(Integer typeId, String username) {
		return repos.findLight(typeId, username);
	}

	public List<Site> findAllCoordinates(Integer typeId) {
		return repos.findAllCoordinates(typeId);
	}

	public List<Site> findLight(String username) {
		return repos.findLight(username);
	}

	public Long countByName(String name, Integer id) {
		Long l = id == null ? repos.countByName(name) : repos.countByName(name, id);
		return l != null ? l : 0;
	}

	public Boolean isNameExists(Site site) {
		return countByName(site.getName(), site.getId()) > 0;
	}

	public Boolean isCodeExists(Site site) {
		return repos.countByCode(site.getCode(), site.getId()) > 0;
	}

	@Cacheable(value = "siteService.cache1")
	public List<Site> findAllCoordinates() {
		return repos.findAllCoordinates();
	}

	public List<Site> readFile(InputStream inputStream, Site template) throws SiteExcelFileException {
		List<Site> result = new ArrayList<>();
		POIFSFileSystem fs;
		try {
			fs = new POIFSFileSystem(inputStream);
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

			if (cols != 8)
				throw new SiteExcelFileException("number of columns should be 2 with this order (Name[TEXT],Phone[TEXT],FAX[TEXT],ADDRESS1[TEXT],ADDRESS2[TEXT],ADDRESS3[TEXT],LATITUDE[NUMBER],LONGITUDE[Number])");

			for (int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				if (row != null) {
					String name, phone, fax, address1, address2, address3;
					Double latitude, longitude;

					HSSFCell nameCell = row.getCell(0);
					HSSFCell phoneCell = row.getCell(1);
					HSSFCell faxCell = row.getCell(2);
					HSSFCell address1Cell = row.getCell(3);
					HSSFCell address2Cell = row.getCell(4);
					HSSFCell address3Cell = row.getCell(5);
					HSSFCell latitudeCell = row.getCell(6);
					HSSFCell longitudeCell = row.getCell(7);
					if (nameCell == null || latitudeCell == null || longitudeCell == null || nameCell.getCellType() == HSSFCell.CELL_TYPE_BLANK || latitudeCell.getCellType() == HSSFCell.CELL_TYPE_BLANK || longitudeCell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
						continue;

					name = getStringValue(nameCell, r);
					phone = getStringValue(phoneCell, r);
					fax = getStringValue(faxCell, r);
					address1 = getStringValue(address1Cell, r);
					address2 = getStringValue(address2Cell, r);
					address3 = getStringValue(address3Cell, r);
					latitude = getNumericValue(latitudeCell, r);
					longitude = getNumericValue(longitudeCell, r);

					result.add(new Site(name, latitude, longitude, address1, address2, address3, phone, fax, template.getType(), template.getOwnerType(), template.getCustomer(), template.getSupplier(), template.getOwner(), template.getUser()));

				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new SiteExcelFileException("not valid file !");
		}

		return result;
	}

	private String getStringValue(HSSFCell cell, int r) throws SiteExcelFileException {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case HSSFCell.CELL_TYPE_NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		case HSSFCell.CELL_TYPE_BLANK:
			return null;
		default:
			throw new SiteExcelFileException("error at row : " + (r + 1));
		}
	}

	private Double getNumericValue(HSSFCell cell, int r) throws SiteExcelFileException {
		try {
			return cell.getNumericCellValue();
		} catch (Exception e) {
			try {
				return Double.valueOf(cell.getStringCellValue().replaceAll(",", ".").replaceAll(" ", ""));
			} catch (Exception e2) {
				throw new SiteExcelFileException("error  at row : " + (r + 1));
			}
		}

	}

	// EDIT SITE Coordinates
	public void editSiteCoordinates(Integer siteId, Double latitude, Double longitude) {
		repos.updateLatitude(siteId, latitude);
		repos.updateLongitude(siteId, longitude);
		googleGeocodeService.updateGoogleGeocodeDataAsync(findOne(siteId));

		// update Associated TR
		List<TransportationRequest> transportationRequestList = transportationRequestRepos.findAssociatedWithSite(siteId);
		List<Integer> transportationRequestIdList = new ArrayList<>();
		for (TransportationRequest transportationRequest : transportationRequestList) {
			transportationRequestService.calculateEstimatedDistanceAndDuration(transportationRequest);
			transportationRequestService.save(transportationRequest);
			transportationRequestIdList.add(transportationRequest.getId());
		}

		// update Associated TR jobs
		if (transportationRequestIdList.isEmpty())
			return;
		List<TransportationJob> transportationJobList = transportationJobRepos.findByTransportationRequestList(transportationRequestIdList);
		for (TransportationJob transportationJob : transportationJobList)
			transportationJobService.updateCalculableFields(transportationJob, false);
	}

	public void updateGoogleGeocodeData() {
		System.out.println("updateGoogleGeocodeData!");
		List<Site> list = repos.findByNotHavingGoogleAddress();
		for (Site site : list)
			googleGeocodeService.updateGoogleGeocodeData(site);
	}

	public List<Site> findLightAndHavingWarehouse() {
		return repos.findLightAndHavingWarehouse();
	}
	
	public List<String> findGoogleRegionList(){
		return repos.findGoogleRegionList();
	}
}
