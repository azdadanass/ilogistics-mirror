package ma.azdad.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.mobile.model.DnMaterials;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.InboundType;
import ma.azdad.model.JobRequestStatus;
import ma.azdad.model.PartNumber;
import ma.azdad.model.StockRow;
import ma.azdad.model.StockRowStatus;
import ma.azdad.repos.DeliveryRequestDetailRepos;
import ma.azdad.repos.DeliveryRequestRepos;
import ma.azdad.repos.PartNumberRepos;
import ma.azdad.repos.StockRowRepos;
import ma.azdad.utils.DeliveryRequestExcelFileException;
import ma.azdad.utils.LabelValue;

@Component
public class DeliveryRequestDetailService extends GenericService<Integer, DeliveryRequestDetail, DeliveryRequestDetailRepos> {

	@Autowired
	DeliveryRequestDetailRepos repos;

	@Autowired
	PartNumberRepos partNumberRepos;

	@Autowired
	StockRowRepos stockRowRepos;

	@Autowired
	StockRowService stockRowService;
	
	@Autowired
	DeliveryRequestSerialNumberService deliveryRequestSerialNumberService;

	@Autowired
	DeliveryRequestRepos deliveryRequestRepos;
	
	@Autowired
	DeliveryRequestService deliveryRequestService;

	@Autowired
	JobRequestDeliveryDetailService jobRequestDeliveryDetailService;

	@Autowired
	PartNumberService partNumberService;

	// reporting
	public List<DeliveryRequestDetail> teamInventory(Integer projectId) {
		return repos.findByDestinationProjectAndTypeAndStatus(projectId, Arrays.asList(DeliveryRequestType.OUTBOUND, DeliveryRequestType.XBOUND),
				Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED), Arrays.asList(JobRequestStatus.REJECTED, JobRequestStatus.CANCELED));
	}

	public Map<Integer, Double> findQuantityPartNumberMapByDeliveryRequest(Integer deliveryRequest) {
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		List<Object[]> data = repos.findQuantityByDeliveryRequestGroupByPartNumber(deliveryRequest);
		for (Object[] row : data)
			result.put((Integer) row[0], (Double) row[1]);
		return result;
	}

	@Override
	public DeliveryRequestDetail findOne(Integer id) {
		DeliveryRequestDetail deliveryRequestDetail = super.findOne(id);
		Hibernate.initialize(deliveryRequestDetail.getDeliveryRequest());
		return deliveryRequestDetail;
	}

	public List<DeliveryRequestDetail> findByOutboundAndWaiting(Integer projectId, Integer warehouseId, Integer id) {
		return id == null
				? repos.findByProjectAndWarehouseAndTypeAndStatus(projectId, warehouseId, DeliveryRequestType.OUTBOUND,
						Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2))
				: repos.findByProjectAndWarehouseAndTypeAndStatus(projectId, warehouseId, DeliveryRequestType.OUTBOUND,
						Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2), id);
	}

	public List<DeliveryRequestDetail> findRemainingByPo(Integer poId) {
		return repos.findRemainingByPo(poId);
	}

	public List<DeliveryRequestDetail> findMappingSummary(Integer deliveryRequestId) {
		List<DeliveryRequestDetail> result = new ArrayList<DeliveryRequestDetail>();
		Map<Integer, Double> dnQtyMap = findQuantityPartNumberMapByDeliveryRequest(deliveryRequestId);
		Map<Integer, Double> returnQtyMap = stockRowService.findReturnedQuantityPartNumberMapByOutboundDeliveryRequest(deliveryRequestId);
		Map<Integer, Double> mappedQtyMap = jobRequestDeliveryDetailService.findMappedQuantityMap(deliveryRequestId);
		DeliveryRequestType dnType = deliveryRequestRepos.findType(deliveryRequestId);

		switch (dnType) {
		case OUTBOUND:
			for (Integer key : dnQtyMap.keySet()) {
				Double dnQty = dnQtyMap.get(key);
				Double returnQty = returnQtyMap.getOrDefault(key, 0.0);
				Double mappedQty = mappedQtyMap.getOrDefault(key, 0.0);
				Double remainingQty = dnQty - returnQty - mappedQty;

				DeliveryRequestDetail deliveryRequestDetail = new DeliveryRequestDetail();
				deliveryRequestDetail.setPartNumber(partNumberService.findOneLight(key));
				deliveryRequestDetail.setQuantity(dnQty);
				deliveryRequestDetail.setTmpReturnedQuantity(returnQty);
				deliveryRequestDetail.setTmpMappedQuantity(mappedQty);
				deliveryRequestDetail.setTmpRemainingQuantity(remainingQty);
				result.add(deliveryRequestDetail);

			}
			break;
		case INBOUND:
			for (Integer key : mappedQtyMap.keySet()) {
				Double mappedQty = mappedQtyMap.get(key);
				Double dnQty = dnQtyMap.getOrDefault(key, 0.0);
				Double remainingQty = mappedQty - dnQty;
				DeliveryRequestDetail deliveryRequestDetail = new DeliveryRequestDetail();
				deliveryRequestDetail.setPartNumber(partNumberService.findOneLight(key));
				deliveryRequestDetail.setQuantity(dnQty);
				deliveryRequestDetail.setTmpMappedQuantity(mappedQty);
				deliveryRequestDetail.setTmpRemainingQuantity(remainingQty);
				result.add(deliveryRequestDetail);
			}
			break;
		default:
			break;
		}

		return result;
	}

	public List<DeliveryRequestDetail> findRemainingByProjectAndWarehouse(Integer projectId, Integer warehouseId, DeliveryRequest deliveryRequest) {
		List<DeliveryRequestDetail> stockRowList = stockRowRepos.findRemainingByProjectAndWarehouse(projectId, warehouseId);
		if (stockRowList == null)
			return new ArrayList<>();

		// Initialize packing information
		stockRowList.forEach(i -> Hibernate.initialize(i.getPacking()));

		List<DeliveryRequestDetail> waitingList = findByOutboundAndWaiting(projectId, warehouseId, deliveryRequest.getId());

		Map<String, DeliveryRequestDetail> stockRowMap = new HashMap<>();
		for (DeliveryRequestDetail deliveryRequestDetail : stockRowList) {
			stockRowMap.put(deliveryRequestDetail.getKey(), deliveryRequestDetail);
		}

		if (waitingList != null)
			for (DeliveryRequestDetail deliveryRequestDetail : waitingList)
				if (stockRowMap.containsKey(deliveryRequestDetail.getKey())) {
					DeliveryRequestDetail drd = stockRowMap.get(deliveryRequestDetail.getKey());
					drd.setRemainingQuantity(drd.getRemainingQuantity() - deliveryRequestDetail.getRemainingQuantity());
				}

		List<DeliveryRequestDetail> result = new ArrayList<>();
		for (DeliveryRequestDetail detail : stockRowList)
			if (UtilsFunctions.compareDoubles(detail.getRemainingQuantity(), 0.0, 4) > 0)
				result.add(detail);

		// EDIT CASE --> update selection quantities
		if (deliveryRequest.getId() != null) {
			Map<String, Double> oldQuantities = new HashMap<>();
			for (DeliveryRequestDetail detail : deliveryRequest.getDetailList())
				oldQuantities.put(detail.getKey(), detail.getQuantity());
			for (DeliveryRequestDetail detail : result)
				if (oldQuantities.containsKey(detail.getKey()))
					detail.setQuantity(oldQuantities.get(detail.getKey()));
		}

		return result;
	}

	public List<DeliveryRequestDetail> findRemainingByOutboundDeliveryRequestReturn(DeliveryRequest deliveryRequest, DeliveryRequest outboundDeliveryRequest) {
		List<DeliveryRequestDetail> stockRowList = stockRowRepos.findRemainingByOutboundDeliveryRequestReturn(outboundDeliveryRequest.getId());
		if (stockRowList == null)
			return new ArrayList<>();

		// Initialize packing information
		stockRowList.forEach(i -> Hibernate.initialize(i.getPacking()));

		List<DeliveryRequestStatus> waitingStatus = Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2);
		List<DeliveryRequestDetail> waitingList = deliveryRequest == null || deliveryRequest.getId() == null
				? repos.findByOutboundDeliveryRequestReturnAndStatus(outboundDeliveryRequest.getId(), waitingStatus)
				: repos.findByOutboundDeliveryRequestReturnAndStatus(outboundDeliveryRequest.getId(), waitingStatus, deliveryRequest.getId());

		Map<String, DeliveryRequestDetail> stockRowMap = new HashMap<>();
		for (DeliveryRequestDetail deliveryRequestDetail : stockRowList) {
			stockRowMap.put(deliveryRequestDetail.getKey(), deliveryRequestDetail);
		}

		if (waitingList != null)
			for (DeliveryRequestDetail deliveryRequestDetail : waitingList)
				if (stockRowMap.containsKey(deliveryRequestDetail.getKey())) {
					DeliveryRequestDetail drd = stockRowMap.get(deliveryRequestDetail.getKey());
					drd.setRemainingQuantity(drd.getRemainingQuantity() - deliveryRequestDetail.getRemainingQuantity());
				}

		List<DeliveryRequestDetail> result = new ArrayList<>();
		for (DeliveryRequestDetail detail : stockRowList)
			if (UtilsFunctions.compareDoubles(detail.getRemainingQuantity(), 0.0, 4) > 0)
				result.add(detail);

		// EDIT CASE --> update selection quantities
		if (deliveryRequest != null && deliveryRequest.getId() != null) {
			Map<String, Double> oldQuantities = new HashMap<>();
			for (DeliveryRequestDetail detail : deliveryRequest.getDetailList())
				oldQuantities.put(detail.getKey(), detail.getQuantity());
			for (DeliveryRequestDetail detail : result)
				if (oldQuantities.containsKey(detail.getKey()))
					detail.setQuantity(oldQuantities.get(detail.getKey()));
		}

		return result;
	}

	public List<DeliveryRequestDetail> findRemainingByOutboundDeliveryRequestTransfer(DeliveryRequest deliveryRequest, DeliveryRequest outboundDeliveryRequest) {
		List<DeliveryRequestDetail> stockRowList = stockRowRepos.findRemainingByOutboundDeliveryRequestTransfer(outboundDeliveryRequest.getId());
		if (stockRowList == null)
			return new ArrayList<>();

		// Initialize packing information
		stockRowList.forEach(i -> Hibernate.initialize(i.getPacking()));

		List<DeliveryRequestStatus> waitingStatus = Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2);
		List<DeliveryRequestDetail> waitingList = deliveryRequest == null || deliveryRequest.getId() == null
				? repos.findByOutboundDeliveryRequestTransferAndStatus(outboundDeliveryRequest.getId(), waitingStatus)
				: repos.findByOutboundDeliveryRequestTransferAndStatus(outboundDeliveryRequest.getId(), waitingStatus, deliveryRequest.getId());

		Map<String, DeliveryRequestDetail> stockRowMap = new HashMap<>();
		for (DeliveryRequestDetail deliveryRequestDetail : stockRowList) {
			stockRowMap.put(deliveryRequestDetail.getKey(), deliveryRequestDetail);
		}

		if (waitingList != null)
			for (DeliveryRequestDetail deliveryRequestDetail : waitingList)
				if (stockRowMap.containsKey(deliveryRequestDetail.getKey())) {
					DeliveryRequestDetail drd = stockRowMap.get(deliveryRequestDetail.getKey());
					drd.setRemainingQuantity(drd.getRemainingQuantity() - deliveryRequestDetail.getRemainingQuantity());
				}

		List<DeliveryRequestDetail> result = new ArrayList<>();
		for (DeliveryRequestDetail detail : stockRowList)
			if (UtilsFunctions.compareDoubles(detail.getRemainingQuantity(), 0.0, 4) > 0)
				result.add(detail);

		// EDIT CASE --> update selection quantities
		if (deliveryRequest != null && deliveryRequest.getId() != null) {
			Map<String, Double> oldQuantities = new HashMap<>();
			for (DeliveryRequestDetail detail : deliveryRequest.getDetailList())
				oldQuantities.put(detail.getKey(), detail.getQuantity());
			for (DeliveryRequestDetail detail : result)
				if (oldQuantities.containsKey(detail.getKey()))
					detail.setQuantity(oldQuantities.get(detail.getKey()));
		}

		if (deliveryRequest != null)
			for (DeliveryRequestDetail drd : result) {
				drd.setUnitCost(drd.getUnitPrice());
				drd.setUnitPrice(null);
			}

		return result;
	}

	public List<DeliveryRequestDetail> readInboundFile(InputStream inputStream, DeliveryRequest deliveryRequest) throws DeliveryRequestExcelFileException {
		List<DeliveryRequestDetail> result = new ArrayList<>();

		Map<PartNumber, Double> map = new HashMap<>();

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

			if (cols != 2)
				throw new DeliveryRequestExcelFileException("number of columns should be 2 with this order (PN[TEXT],Quantity[Numeric])");

			for (int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				if (row != null) {
					String pn;
					Double quantity;
					HSSFCell pnCell = row.getCell(0);
					HSSFCell quantityCell = row.getCell(1);
					if (pnCell == null || quantityCell == null || pnCell.getCellType() == HSSFCell.CELL_TYPE_BLANK || quantityCell.getCellType() == HSSFCell.CELL_TYPE_BLANK)
						continue;

					pn = getPnFromCell(pnCell, r);
					quantity = getQuantityFromCell(quantityCell, r);

					PartNumber partNumber = partNumberRepos.findByName(pn);
					if (partNumber == null)
						throw new DeliveryRequestExcelFileException("PN " + pn + " dosen't exist in DB !");

					if (!map.containsKey(partNumber))
						map.put(partNumber, quantity);
					else
						map.put(partNumber, map.get(partNumber) + quantity);

				}
			}

			for (PartNumber pn : map.keySet())
				result.add(new DeliveryRequestDetail(map.get(pn), pn, deliveryRequest));

		} catch (IOException e) {
			e.printStackTrace();
			throw new DeliveryRequestExcelFileException("not valid file !");
		}

		return result;
	}

	public Map<String, Double> readOutboundFile(InputStream inputStream, DeliveryRequest deliveryRequest, List<DeliveryRequestDetail> sourceList) throws DeliveryRequestExcelFileException {
		Map<String, Double> srcMap = new HashMap<>();
		for (DeliveryRequestDetail drd : sourceList)
			srcMap.put(drd.getKey(), drd.getRemainingQuantity());

		Map<String, Double> resultMap = new HashMap<>();

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

			if (cols != 5)
				throw new DeliveryRequestExcelFileException("number of columns should be 5 with this order (PN[TEXT],Origin DN Number[TEXT],Inbound Request[TEXT],STATUS[INTEGER],Quantity[Numeric])");

			for (int r = 1; r < rows; r++) {
				row = sheet.getRow(r);
				if (row != null) {
					String pn; // part number
					String on; // origin number
					String idr; // inboundDeliveryReference
					Integer status;
					Double quantity;
					HSSFCell pnCell = row.getCell(0);
					HSSFCell onCell = row.getCell(1);
					HSSFCell idrCell = row.getCell(2);
					HSSFCell statusCell = row.getCell(3);
					HSSFCell quantityCell = row.getCell(4);

					if (Arrays.asList(pnCell, onCell, idrCell, statusCell, quantityCell).contains(null))
						continue;
					if (Arrays.asList(pnCell.getCellType(), onCell.getCellType(), idrCell.getCellType(), statusCell.getCellType(), quantityCell.getCellType()).contains(HSSFCell.CELL_TYPE_BLANK))
						continue;

					pn = getPnFromCell(pnCell, r);
					on = getOnFromCell(onCell, r);
					idr = getIdrFromCell(idrCell, r);
					status = getStatusFromCell(statusCell, r);
					quantity = getQuantityFromCell(quantityCell, r);

					PartNumber partNumber = partNumberRepos.findByName(pn);
					if (partNumber == null)
						throw new DeliveryRequestExcelFileException("PN " + pn + " dosen't exist in DB !");

					if (deliveryRequestRepos.countByOriginNumber(on) == 0)
						throw new DeliveryRequestExcelFileException("Origin Number :  " + on + " dosen't exist in DB !");

					Integer inboundReferenceNumber;

					try {
						inboundReferenceNumber = Integer.valueOf(idr.replace("DN" + (DeliveryRequestType.INBOUND.ordinal() + 1), ""));
					} catch (Exception e) {
						throw new DeliveryRequestExcelFileException("Invalid Format for Inbound DN : " + idr);
					}
					DeliveryRequest inboundDeliveryRequest = deliveryRequestRepos.findByTypeAndReferenceNumber(DeliveryRequestType.INBOUND, inboundReferenceNumber);
					if (inboundDeliveryRequest == null)
						throw new DeliveryRequestExcelFileException("Inbound DN " + idr + " dosen't exist in DB !");

					String key = StockRow.getKey(status, on, partNumber.getId(), inboundDeliveryRequest.getId());

					if (!srcMap.containsKey(key))
						throw new DeliveryRequestExcelFileException("one or more rows dont match with actual remaining list, check columns (PN,OriginDN,status,InboundDN) : " + partNumber.getName());
					if (!resultMap.containsKey(key))
						// resultMap.put(key, new DeliveryRequestDetail(0.0,
						// partNumber, StockRowStatus.values()[status], on,
						// inboundDeliveryRequest));
						resultMap.put(key, 0.0);

					// resultMap.get(key).setQuantity(resultMap.get(key).getQuantity()
					// + quantity);
					resultMap.put(key, resultMap.get(key) + quantity);

					if (resultMap.get(key).compareTo(srcMap.get(key)) > 0)
						throw new DeliveryRequestExcelFileException("Error MAX quantity for (pn:" + pn + ",originNumber:" + on + ",inboundDn:" + idr + ",status:" + status + ")");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new DeliveryRequestExcelFileException("not valid file !");
		}

		return resultMap;
	}

	private String getPnFromCell(HSSFCell cell, int r) throws DeliveryRequestExcelFileException {
		String result = null;
		try {
			result = cell.getStringCellValue();
		} catch (Exception e) {
			try {
				result = String.valueOf((int) cell.getNumericCellValue());
			} catch (Exception e2) {
				throw new DeliveryRequestExcelFileException("error PN at row : " + (r + 1));
			}
		}
		return UtilsFunctions.cleanString(result);
	}

	private String getOnFromCell(HSSFCell cell, int r) throws DeliveryRequestExcelFileException {
		String result = null;
		try {
			result = cell.getStringCellValue();
		} catch (Exception e) {
			try {
				result = String.valueOf((int) cell.getNumericCellValue());
			} catch (Exception e2) {
				throw new DeliveryRequestExcelFileException("error OriginNumber at row : " + (r + 1));
			}
		}
		return UtilsFunctions.cleanString(result);
	}

	private String getIdrFromCell(HSSFCell cell, int r) throws DeliveryRequestExcelFileException {
		String result = null;
		try {
			result = cell.getStringCellValue();
			if (!result.startsWith("DN" + (DeliveryRequestType.INBOUND.ordinal() + 1)))
				throw new DeliveryRequestExcelFileException("Invalid format for Inbound DN at row : " + (r + 1));
		} catch (Exception e) {
			throw new DeliveryRequestExcelFileException("error Inbound DN at row : " + (r + 1));
		}
		return UtilsFunctions.cleanString(result);
	}

	private Double getQuantityFromCell(HSSFCell cell, int r) throws DeliveryRequestExcelFileException {
		try {
			return cell.getNumericCellValue();
		} catch (Exception e) {
			try {
				return Double.valueOf(cell.getStringCellValue().replaceAll(",", ".").replaceAll(" ", ""));
			} catch (Exception e2) {
				throw new DeliveryRequestExcelFileException("error QUANTITY at row : " + (r + 1));
			}
		}

	}

	private Integer getStatusFromCell(HSSFCell cell, int r) throws DeliveryRequestExcelFileException {
		try {
			int status = (int) cell.getNumericCellValue();
			if (status < 0 || status >= StockRowStatus.values().length)
				throw new DeliveryRequestExcelFileException("error Status at row : " + (r + 1) + " --> Status should be between 0 and " + (StockRowStatus.values().length - 1));
			return status;
		} catch (Exception e) {
			throw new DeliveryRequestExcelFileException("error Status at row : " + (r + 1));
		}
	}

	public void updateUnitCost(Integer id, Double unitCost) {
		DeliveryRequestDetail deliveryRequestDetail = findOne(id);

		if (deliveryRequestDetail.getDeliveryRequest().getIsOutbound())
			return;
		// update inbound detail
		repos.updateUnitCost(deliveryRequestDetail.getId(), unitCost);
		List<Integer> idList;
		if (deliveryRequestDetail.getDeliveryRequest().getIsInbound()) {
			// update related StockRow
//			idList = stockRowRepos.findIdListByPartNumberAndInboundDeliveryRequest(deliveryRequestDetail.getPartNumber().getId(),
//					deliveryRequestDetail.getDeliveryRequest().getId());
//			if (idList != null && !idList.isEmpty())
//				stockRowRepos.updateUnitCost(idList, unitCost);

			// update related outbound details
			idList = repos.findIdList(DeliveryRequestType.OUTBOUND, deliveryRequestDetail.getPartNumber().getId(), deliveryRequestDetail.getDeliveryRequest().getId());
			if (idList != null && !idList.isEmpty()) {
				repos.updateUnitCost(idList, unitCost);
				// update related return from outbound
				for (Integer outboundDeliveryRequestDetailId : idList) {
					List<Integer> idList2 = repos.findIdListByPartNumberAndOutboundDeliveryRequestReturn(deliveryRequestDetail.getPartNumber().getId(), outboundDeliveryRequestDetailId);
					for (Integer integer : idList2)
						updateUnitCost(integer, unitCost);
				}
			}

		}
//		else if (deliveryRequestDetail.getDeliveryRequest().getIsXbound()) {
//			// update related StockRow
//			idList = stockRowRepos.findIdList(DeliveryRequestType.XBOUND, deliveryRequestDetail.getDeliveryRequest().getId(), deliveryRequestDetail.getPartNumber().getId());
//			if (idList != null && !idList.isEmpty())
//				stockRowRepos.updateUnitCost(idList, unitCost);
//		}

	}

	public void updateUnitPrice(Integer id, Double unitPrice) {
		DeliveryRequestDetail deliveryRequestDetail = findOne(id);

		if (!deliveryRequestDetail.getDeliveryRequest().getIsOutbound()) // maybe xbound too
			return;

		repos.updateUnitPrice(deliveryRequestDetail.getId(), unitPrice);

//		List<Integer> idList;
//		if (deliveryRequestDetail.getDeliveryRequest().getIsOutbound()) {
//			// update related StockRow
//			idList = stockRowRepos.findIdListByPartNumberAndDeliveryRequest(deliveryRequestDetail.getPartNumber().getId(), deliveryRequestDetail.getDeliveryRequest().getId());
//			if (idList != null && !idList.isEmpty())
//				stockRowRepos.updateUnitPrice(idList, unitPrice);
//		}

	}

	public List<LabelValue> findOwnerList(Integer outboundDeliveryRequestId) {
		List<Integer> customerList = repos.findOwnerCustomerList(outboundDeliveryRequestId);
		List<Integer> supplierList = repos.findOwnerSupplierList(outboundDeliveryRequestId);
		List<Integer> companyList = repos.findOwnerCompanyList(outboundDeliveryRequestId);

		List<LabelValue> result = new ArrayList<>();

		result.addAll(customerList.stream().map(item -> new LabelValue("", item, "customer")).collect(Collectors.toList()));
		result.addAll(supplierList.stream().map(item -> new LabelValue("", item, "supplier")).collect(Collectors.toList()));
		result.addAll(companyList.stream().map(item -> new LabelValue("", item, "company")).collect(Collectors.toList()));

		return result;
	}

	public List<DeliveryRequestDetail> findByPartNumberAndDeliveryRequestTypeAndCompany(Integer partNumberId, DeliveryRequestType deliveryRequestType, InboundType inboundType, Integer companyId,
			List<DeliveryRequestStatus> deliveryRequestStatus) {
		return repos.findByPartNumberAndDeliveryRequestTypeAndCompany(partNumberId, deliveryRequestType, inboundType, companyId, deliveryRequestStatus);
	}

//	public Double findPendingQuantityByCompanyOwnerAnPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId) {
//		Double d = repos.findPendingQuantityByCompanyOwnerAnPartNumber(username, warehouseList, assignedProjectList, companyId, partNumberId, DeliveryRequestType.OUTBOUND, Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
//		return d != null ? d : 0.0;
//	}

//	public Double findPendingQuantityByCustomerOwnerAnPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId) {
//		Double d = repos.findPendingQuantityByCustomerOwnerAnPartNumber(username, warehouseList, assignedProjectList, customerId, partNumberId, DeliveryRequestType.OUTBOUND, Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
//		return d != null ? d : 0.0;
//	}

	public Map<Integer, Double> findPendingQuantityByCompanyOwnerAndProjectSubTypeStockGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList,
			Integer companyId) {
		List<Object[]> data = repos.findPendingQuantityByCompanyOwnerAndProjectSubTypeStockGroupByPartNumber(username, warehouseList, assignedProjectList, companyId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		data.forEach(i -> result.put((Integer) i[0], (Double) i[1]));
		return result;
	}

	public Map<Integer, Double> findForecastQuantityGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		List<Object[]> data = repos.findForecastQuantityGroupByPartNumber(username, warehouseList, assignedProjectList, companyId);
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		data.forEach(i -> result.put((Integer) i[0], (Double) i[1]));
		return result;
	}

	public Map<Integer, Double> findPendingQuantityByCustomerOwnerGroupByPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId) {
		List<Object[]> data = repos.findPendingQuantityByCustomerOwnerGroupByPartNumber(username, warehouseList, assignedProjectList, customerId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		data.forEach(i -> result.put((Integer) i[0], (Double) i[1]));
		return result;
	}

	public Map<Integer, Double> findPendingQuantityByCustomerOwnerGroupByPartNumber(Integer customerId, List<Integer> projectIdList) {
		List<Object[]> data = repos.findPendingQuantityByCustomerOwnerGroupByPartNumber(customerId, projectIdList, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		data.forEach(i -> result.put((Integer) i[0], (Double) i[1]));
		return result;
	}

	public Map<Integer, Double> findPendingQuantityBySupplierOwnerGroupByPartNumber(Integer supplierId, List<Integer> projectIdList) {
		List<Object[]> data = repos.findPendingQuantityBySupplierOwnerGroupByPartNumber(supplierId, projectIdList, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		data.forEach(i -> result.put((Integer) i[0], (Double) i[1]));
		return result;
	}

	public Double findPendingQuantityByCompanyOwnerAnPartNumberAndProject(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId,
			Integer projectId) {
		Double d = repos.findPendingQuantityByCompanyOwnerAnPartNumberAndProject(username, warehouseList, assignedProjectList, companyId, partNumberId, projectId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		return d != null ? d : 0.0;
	}

	public Double findPendingQuantityByCustomerOwnerAnPartNumberAndProject(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId,
			Integer projectId) {
		Double d = repos.findPendingQuantityByCustomerOwnerAnPartNumberAndProject(username, warehouseList, assignedProjectList, customerId, partNumberId, projectId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		return d != null ? d : 0.0;
	}

	public Double findPendingQuantityByCustomerOwnerAnPartNumberAndProject(Integer customerId, Integer partNumberId, Integer projectId) {
		Double d = repos.findPendingQuantityByCustomerOwnerAnPartNumberAndProject(customerId, partNumberId, projectId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		return d != null ? d : 0.0;
	}

	public Double findPendingQuantityBySupplierOwnerAnPartNumberAndProject(Integer supplierId, Integer partNumberId, Integer projectId) {
		Double d = repos.findPendingQuantityBySupplierOwnerAnPartNumberAndProject(supplierId, partNumberId, projectId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		return d != null ? d : 0.0;
	}

	public Double findPendingQuantityByCompanyOwnerAnPartNumberAndWarehouse(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId,
			Integer warehouseId) {
		Double d = repos.findPendingQuantityByCompanyOwnerAnPartNumberAndWarehouse(username, warehouseList, assignedProjectList, companyId, partNumberId, warehouseId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		return d != null ? d : 0.0;
	}

	public Double findPendingQuantityByCustomerOwnerAnPartNumberAndWarehouse(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId,
			Integer warehouseId) {
		Double d = repos.findPendingQuantityByCustomerOwnerAnPartNumberAndWarehouse(username, warehouseList, assignedProjectList, customerId, partNumberId, warehouseId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		return d != null ? d : 0.0;
	}

	public Double findPendingQuantityByCustomerOwnerAnPartNumberAndWarehouse(Integer customerId, List<Integer> projectIdList, Integer partNumberId, Integer warehouseId) {
		Double d = repos.findPendingQuantityByCustomerOwnerAnPartNumberAndWarehouse(customerId, projectIdList, partNumberId, warehouseId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		return d != null ? d : 0.0;
	}

	public Double findPendingQuantityBySupplierOwnerAnPartNumberAndWarehouse(Integer supplierId, List<Integer> projectIdList, Integer partNumberId, Integer warehouseId) {
		Double d = repos.findPendingQuantityBySupplierOwnerAnPartNumberAndWarehouse(supplierId, projectIdList, partNumberId, warehouseId, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2));
		return d != null ? d : 0.0;
	}

	public Boolean isOutboundDeliveryRequestFullyReturned(DeliveryRequest outboundDeliveryRequest) {
		return findRemainingByOutboundDeliveryRequestReturn(null, outboundDeliveryRequest).size() == 0;
	}

//	public List<DeliveryRequestDetail> findReturnedDetailList(Integer outboundDeliveryRequestId) {
//		List<DeliveryRequestDetail> result = repos.findReturnedDetailList(outboundDeliveryRequestId,
//				Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
//		result.forEach(i -> {
//			Hibernate.initialize(i.getDeliveryRequest());
//			Hibernate.initialize(i.getPacking());
//		});
//		return result;
//	}

//	public Long countReturnedDetailList(Integer outboundDeliveryRequestId) {
//		return repos.countReturnedDetailList(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
//	}

	public List<DeliveryRequestDetail> findReturnedDetailListGroupByPartNumber(Integer outboundDeliveryRequestId) {
		return repos.findReturnedDetailListGroupByPartNumber(outboundDeliveryRequestId, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	public Map<PartNumber, Double> findReturnedQuantityMap(Integer outboundDeliveryRequestId) {
		return findReturnedDetailListGroupByPartNumber(outboundDeliveryRequestId).stream()
				.collect(Collectors.groupingBy(DeliveryRequestDetail::getPartNumber, Collectors.summingDouble(DeliveryRequestDetail::getQuantity)));
	}

	public List<DeliveryRequestDetail> findByPartNumberAndTypeAndProjectTypeStockAndProjectCompanyAndDeliveryRequestStatus(Integer partNumberId, DeliveryRequestType deliveryRequestType,
			Integer companyId, List<DeliveryRequestStatus> deliveryRequestStatus) {
		return repos.findByPartNumberAndTypeAndProjectTypeStockAndProjectCompanyAndDeliveryRequestStatus(partNumberId, deliveryRequestType, companyId, deliveryRequestStatus);
	}

	// size <= 1 if inbound
	public List<DeliveryRequestDetail> findByDeliveryRequestAndPartNumber(Integer deliveryRequestId, Integer partNumberId) {
		return repos.findByDeliveryRequestAndPartNumber(deliveryRequestId, partNumberId);
	}

	public void clearPurchaseCostByDeliveryRequest(Integer deliveryRequestId) {
		repos.clearPurchaseCostByDeliveryRequest(deliveryRequestId);
	}

	public List<DeliveryRequestDetail> findByCompanyOwnerAndPartNumberAndNotDelivered(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId,
			DeliveryRequestType type, Integer partNumberId) {
		return repos.findByCompanyOwnerAndPartNumberAndNotDelivered(username, warehouseList, assignedProjectList, companyId, type, partNumberId);
	}

	public List<DeliveryRequestDetail> findTransferredAndPendingDetailList(Integer outboundDeliveryRequestId) {
		return repos.findTransferredAndPendingDetailList(outboundDeliveryRequestId);
	}

	public List<DeliveryRequestDetail> findReturnedAndPendingDetailList(Integer outboundDeliveryRequestId) {
		return repos.findReturnedAndPendingDetailList(outboundDeliveryRequestId);
	}

	// PN Reporting quantities
	public Double findPendingStockByCompanyOwnerAndPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId,
			DeliveryRequestType deliveryRequestType) {
		return ObjectUtils.firstNonNull(repos.findPendingStockByCompanyOwnerAndPartNumber(username, warehouseList, assignedProjectList, companyId, partNumberId, deliveryRequestType,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2)), 0.0);
	}

	public Double findPendingByCompanyOwnerAndPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId, Integer partNumberId,
			DeliveryRequestType deliveryRequestType) {
		return ObjectUtils.firstNonNull(repos.findPendingByCompanyOwnerAndPartNumber(username, warehouseList, assignedProjectList, companyId, partNumberId, deliveryRequestType,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2)), 0.0);
	}

	public Double findPendingStockByCustomerOwnerAndPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId,
			DeliveryRequestType deliveryRequestType) {
		return ObjectUtils.firstNonNull(repos.findPendingStockByCustomerOwnerAndPartNumber(username, warehouseList, assignedProjectList, customerId, partNumberId, deliveryRequestType,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2)), 0.0);
	}

	public Double findPendingByCustomerOwnerAndPartNumber(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer customerId, Integer partNumberId,
			DeliveryRequestType deliveryRequestType) {
		return ObjectUtils.firstNonNull(repos.findPendingByCustomerOwnerAndPartNumber(username, warehouseList, assignedProjectList, customerId, partNumberId, deliveryRequestType,
				Arrays.asList(DeliveryRequestStatus.EDITED, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.APPROVED2)), 0.0);
	}

	public Double findTotalExpirableItems(Integer deliveryRequestId) {
		return ObjectUtils.firstNonNull(repos.findTotalExpirableItems(deliveryRequestId), 0.0);
	}
	
	public Double findQuantityByDeliveryRequestAndPartNumber(Integer deliveryRequestId,Integer partNumberId) {
		return repos.findQuantityByDeliveryRequestAndPartNumber(deliveryRequestId, partNumberId);
	}

	// mobile

	public List<ma.azdad.mobile.model.HardwareStatusData> initStockRowList(ma.azdad.mobile.model.DeliveryRequest deliveryRequest, String countOk) {
		System.out.println("details size : " + deliveryRequest.getDetailList().get(0).getTmpQuantity());
		for (ma.azdad.mobile.model.DeliveryRequestDetail detail : deliveryRequest.getDetailList())
			if (detail.getTmpQuantity() > 0.0) {
				// detail.setRemainingQuantity(detail.getRemainingQuantity() -
				// detail.getTmpQuantity());
				if (countOk.equals("Yes"))
					deliveryRequest.getStockRowList().add(new ma.azdad.mobile.model.HardwareStatusData(detail.getId(), detail.getPartNumberName(), detail.getRemainingQuantity(),
							detail.getPackingName(), detail.getPackingQuantity(), detail.getPackingId(), detail.getId(), detail.getPartNumberImage()));
				else
					deliveryRequest.getStockRowList().add(new ma.azdad.mobile.model.HardwareStatusData(detail.getId(), detail.getPartNumberName(), detail.getTmpQuantity(), detail.getPackingName(),
							detail.getTmpQuantity(), detail.getPackingId(), detail.getId(), detail.getPartNumberImage()));

			}
		System.out.println("stocks size : " + deliveryRequest.getStockRowList().get(0).getQuantity());
		return deliveryRequest.getStockRowList();
	}

	public List<ma.azdad.mobile.model.DnMaterials> initStockRowList(ma.azdad.mobile.model.DeliveryRequest deliveryRequest) {
		DeliveryRequest dn = deliveryRequestService.findOne(deliveryRequest.getId());
		List<StockRow> rows = stockRowService.generateStockRowFromOutboundDeliveryRequest(dn);
		List<ma.azdad.mobile.model.DnMaterials> list = new ArrayList<>();
		for (StockRow row : rows) {
			list.add(new DnMaterials(0, row.getPartNumber().getName(), row.getStatus().getValue(), row.getLocation().getName(), row.getOriginNumber(), -row.getQuantity(),
					row.getInboundDeliveryRequest().getReference(), row.getDeliveryRequestDetail().getPartNumberImage()));
		}
		return list;
	}
	
	public List<ma.azdad.mobile.model.DeliveryRequestSerialNumber> initSNList(ma.azdad.mobile.model.DeliveryRequest deliveryRequest){
		DeliveryRequest dn = deliveryRequestService.findOne(deliveryRequest.getId());
		List<StockRow> rows = stockRowService.generateStockRowFromOutboundDeliveryRequest(dn);
		List<DeliveryRequestSerialNumber> potentialSerialNumberList = new ArrayList<DeliveryRequestSerialNumber>();
		List<ma.azdad.mobile.model.DeliveryRequestSerialNumber> potentialSerialNumberListMobile = new ArrayList<ma.azdad.mobile.model.DeliveryRequestSerialNumber>();
		List<StockRow> potentialStockRowlist = new ArrayList<StockRow>();
		potentialStockRowlist = stockRowService.generateStockRowFromOutboundDeliveryRequest(dn);
		potentialStockRowlist.stream().filter(i -> i.getPacking().getHasSerialnumber()).forEach(i -> {
			System.out.println(i.getPartNumberName());
			i.getPacking().getDetailList().stream().filter(j -> j.getHasSerialnumber()).forEach(j -> {
				Integer quantity = -((int) (double) i.getQuantity()) * j.getQuantity() / i.getPacking().getQuantity();
				List<DeliveryRequestSerialNumber> snList = deliveryRequestSerialNumberService.findHavingSerialNumberAndNoOutbound(i.getInboundDeliveryRequestDetail().getId(), i.getLocationId(),
						j.getId(), quantity);
				potentialSerialNumberList.addAll(snList);
			});
		});
		for (DeliveryRequestSerialNumber sn : potentialSerialNumberList) {
			potentialSerialNumberListMobile.add(new ma.azdad.mobile.model.DeliveryRequestSerialNumber(sn.getId(), sn.getPackingDetail().getType(), sn.getInboundStockRow().getPartNumberName(), 
					sn.getSerialNumber(), sn.getInboundStockRow().getPartNumberImage(), null, sn.getPackingNumero(),sn.getInboundStockRow().getStatusValue() , sn.getInboundStockRow().getLocationName(),
					sn.getLongitude(),sn.getLatitude(),sn.getEntryMode()));
			
		}
		
		return potentialSerialNumberListMobile;
	}
	
	
}
