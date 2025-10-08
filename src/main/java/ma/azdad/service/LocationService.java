package ma.azdad.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.CompanyType;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.Location;
import ma.azdad.model.StockRowState;
import ma.azdad.model.ZoneLine;
import ma.azdad.repos.LocationRepos;

@Component
public class LocationService extends GenericService<Integer, Location, LocationRepos> {

	@Autowired
	private LocationRepos locationRepos;
	@Autowired
	private DeliveryRequestService deliveryRequestService;

	@Override
	public Location findOne(Integer id) {
		Location location = super.findOne(id);
		initialize(location.getWarehouse());
		location.getDetailList().forEach(detail -> {
			initialize(detail.getCompany());
			initialize(detail.getCustomer());
			initialize(detail.getSupplier());
		});

		location.getLineList().forEach(l -> l.getColumnList().forEach(c -> initialize(c.getHeightList())));
		location.getIndustryList().forEach(i -> {
			initialize(i.getIndustry());
			i.getCategoryList().forEach(c -> {
				initialize(c.getCategory());
				c.getTypeList().forEach(t->initialize(t.getType()));
			});
		});
		
		location.initOptions();

		return location;
	}

	public Long countByWarehouseAndName(Integer warehouseId, String name, Integer id) {
		Long l = id == null ? locationRepos.countByWarehouseAndName(warehouseId, name) : locationRepos.countByWarehouseAndName(warehouseId, name, id);
		return l != null ? l : 0;
	}

	public Boolean isNameExists(Location location) {
		return countByWarehouseAndName(location.getWarehouse().getId(), location.getName(), location.getId()) > 0;
	}

	public List<Location> findByWarehouseAndStockRowStateAndOwner(Integer warehouseId, StockRowState stockRowState, CompanyType ownerType, Integer ownerId) {
		return repos.findByWarehouseAndStockRowStateAndOwner(warehouseId, stockRowState, ownerType, ownerId);
	}

	public List<Location> findByWarehouseAndStockRowStateAndOwner(DeliveryRequest deliveryRequest) {
		return findByWarehouseAndStockRowStateAndOwner(deliveryRequest.getWarehouseId(), deliveryRequest.getStockRowState(), deliveryRequest.getOwnerType(), deliveryRequest.getOwnerId());
	}

	public List<Location> findLightByWarehouse(Integer warehouseId) {
		return repos.findLightByWarehouse(warehouseId);
	}

	public List<ZoneLine> generateZoning(InputStream inputStream) throws IOException {
		List<ZoneLine> lines = new ArrayList<ZoneLine>();
		POIFSFileSystem fs;
		fs = new POIFSFileSystem(inputStream);
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		int rows = sheet.getPhysicalNumberOfRows();
		for (int i = 0; i < rows; i++) {
			row = sheet.getRow(i);
			if (row.getPhysicalNumberOfCells() != 3) {
				System.out.println("error : cols != 3");
				return null;
			}
			Integer numero = (int) row.getCell(0).getNumericCellValue();
			String columnsStr = row.getCell(1).getStringCellValue();
			String heightsStr = row.getCell(2).getStringCellValue();
			lines.add(new ZoneLine(numero, columnsStr, heightsStr));
		}
		Collections.sort(lines);
		return lines;
	}

	// mobile
	@SuppressWarnings("null")
	public List<ma.azdad.mobile.model.Location> findByWarehouseAndStockRowStateAndOwnerMobile(Integer id, String state) {
		DeliveryRequest deliveryRequest = deliveryRequestService.findOne(id);
		if (state.equals("Normal")) {
			deliveryRequest.setStockRowState(StockRowState.NORMAL);
		} else {
			deliveryRequest.setStockRowState(StockRowState.FAULTY);
		}
		List<Location> list = findByWarehouseAndStockRowStateAndOwner(deliveryRequest);

		List<ma.azdad.mobile.model.Location> mobileList = new ArrayList<>();
		for (Location location : list) {
			mobileList.add(new ma.azdad.mobile.model.Location(location.getId(), location.getName()));
		}
		System.out.print("lcation size :" + mobileList.size());
		return mobileList;
	}

}
