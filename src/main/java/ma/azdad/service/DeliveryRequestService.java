package ma.azdad.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.ecs.html.A;
import org.apache.ecs.html.Body;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.H4;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.Html;
import org.apache.ecs.html.Span;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.apache.ecs.wml.Img;
import org.apache.ecs.xhtml.br;
import org.hibernate.Hibernate;
import org.primefaces.event.FileUploadEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.BarcodeQRCode;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ma.azdad.mobile.model.Dashboard;
import ma.azdad.mobile.model.HardwareStatusData;
import ma.azdad.model.BoqMapping;
import ma.azdad.model.CompanyType;
import ma.azdad.model.Currency;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestExpiryDate;
import ma.azdad.model.DeliveryRequestFile;
import ma.azdad.model.DeliveryRequestHistory;
import ma.azdad.model.DeliveryRequestSerialNumber;
import ma.azdad.model.DeliveryRequestState;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.InboundType;
import ma.azdad.model.IssueStatus;
import ma.azdad.model.PackingDetail;
import ma.azdad.model.PartNumber;
import ma.azdad.model.Po;
import ma.azdad.model.Project;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.StockRow;
import ma.azdad.model.StockRowState;
import ma.azdad.model.StockRowStatus;
import ma.azdad.model.User;
import ma.azdad.repos.AppLinkRepos;
import ma.azdad.repos.DeliveryRequestDetailRepos;
import ma.azdad.repos.DeliveryRequestFileRepos;
import ma.azdad.repos.DeliveryRequestRepos;
import ma.azdad.repos.DeliveryRequestSerialNumberRepos;
import ma.azdad.repos.StockRowRepos;
import ma.azdad.utils.App;
import ma.azdad.utils.ImageUtil;
import ma.azdad.utils.PdfHelper;
import ma.azdad.utils.Public;

@Component
public class DeliveryRequestService extends GenericService<Integer, DeliveryRequest, DeliveryRequestRepos> {

	@Value("#{'${spring.profiles.active}'.replaceAll('-dev','')}")
	private String erp;

	@Autowired
	DeliveryRequestRepos deliveryRequestRepos;

	@Autowired
	DeliveryRequestExpiryDateService deliveryRequestExpiryDateService;

	@Autowired
	DeliveryRequestFileRepos deliveryRequestFileRepos;

	@Autowired
	AppLinkRepos appLinkRepos;

	@Autowired
	ProjectService projectService;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	PoService poService;

	@Autowired
	DeliveryRequestDetailRepos deliveryRequestDetailRepos;

	@Autowired
	StockRowRepos stockRowRepos;

	@Autowired
	StockRowService stockRowService;

	@Autowired
	ProjectCrossService projectCrossService;
	@Autowired
	OldEmailService emailService;
	@Autowired
	SmsService smsService;

	@Autowired
	LocationService locationService;

	@Autowired
	protected DeliveryRequestSerialNumberService deliveryRequestSerialNumberService;

	@Autowired
	UserService userService;

	@Autowired
	DeliveryRequestDetailService deliveryRequestDetailService;

	@Autowired
	DeliveryRequestSerialNumberRepos deliveryRequestSerialNumberRepos;

	@Autowired
	JobRequestDeliveryDetailService jobRequestDeliveryDetailService;

	@Autowired
	TransportationRequestService transportationRequestService;

	@Autowired
	BoqService boqService;

	@Override
	public DeliveryRequest findOne(Integer id) {
		DeliveryRequest deliveryRequest = super.findOne(id);
		Hibernate.initialize(deliveryRequest.getStockRowList());
		Hibernate.initialize(deliveryRequest.getCommentList());
		Hibernate.initialize(deliveryRequest.getDetailList());
		deliveryRequest.getDetailList().forEach(i -> Hibernate.initialize(i.getPacking()));
		deliveryRequest.getDetailList().forEach(i -> i.getPacking().getDetailList().forEach(j -> Hibernate.initialize(j)));
		Hibernate.initialize(deliveryRequest.getFileList());
		Hibernate.initialize(deliveryRequest.getHistoryList());
		Hibernate.initialize(deliveryRequest.getIssueList());
		Hibernate.initialize(deliveryRequest.getOrigin());
		Hibernate.initialize(deliveryRequest.getDestination());
		Hibernate.initialize(deliveryRequest.getCompany());
		Hibernate.initialize(deliveryRequest.getCustomer());
		Hibernate.initialize(deliveryRequest.getSupplier());
		Hibernate.initialize(deliveryRequest.getTransporter());
		if (deliveryRequest.getTransporter() != null)
			Hibernate.initialize(deliveryRequest.getTransporter().getSupplier());
		Hibernate.initialize(deliveryRequest.getWarehouse());
		Hibernate.initialize(deliveryRequest.getEndCustomer());
//		Hibernate.initialize(deliveryRequest.getToSupplier());
		Hibernate.initialize(deliveryRequest.getDeliverToCompany());
		Hibernate.initialize(deliveryRequest.getDeliverToCustomer());
		Hibernate.initialize(deliveryRequest.getDeliverToSupplier());
		Hibernate.initialize(deliveryRequest.getPo());
		if (deliveryRequest.getToUser() != null) {
			Hibernate.initialize(deliveryRequest.getToUser().getCustomer());
			Hibernate.initialize(deliveryRequest.getToUser().getSupplier());
		}

		if (deliveryRequest.getIsInbound() || deliveryRequest.getIsOutbound())
			Hibernate.initialize(deliveryRequest.getWarehouse().getLocationList());
		Hibernate.initialize(deliveryRequest.getTransportationRequest());
		if (deliveryRequest.getProject() != null) {
			Hibernate.initialize(deliveryRequest.getProject().getManager());
			Hibernate.initialize(deliveryRequest.getProject().getCostcenter().getLob().getManager());
			Hibernate.initialize(deliveryRequest.getProject().getCostcenter().getLob().getBu().getDirector());
		}
		if (deliveryRequest.getDestinationProject() != null)
			Hibernate.initialize(deliveryRequest.getDestinationProject().getManager());
		Hibernate.initialize(deliveryRequest.getToNotifyList());
		if (deliveryRequest.getIsInboundReturn())
			Hibernate.initialize(deliveryRequest.getOutboundDeliveryRequestReturn());
		if (deliveryRequest.getIsInboundTransfer())
			Hibernate.initialize(deliveryRequest.getOutboundDeliveryRequestTransfer());
		Hibernate.initialize(deliveryRequest.getExternalRequester());
		Hibernate.initialize(deliveryRequest.getBoqMappingList());
		if (deliveryRequest.getWarehouse() != null)
			Hibernate.initialize(deliveryRequest.getWarehouse().getManagerList());
		return deliveryRequest;
	}

	@Cacheable(value = "deliveryRequestService.findLight")
	public List<DeliveryRequest> findLight(String username, DeliveryRequestType type, DeliveryRequestState state, List<Integer> warehouseList, List<Integer> projectList) {
		if (state == null)
			if (type != null)
				return deliveryRequestRepos.findLight(username, warehouseList, projectList, type);
			else
				return deliveryRequestRepos.findLight(username, warehouseList, projectList);
		else if (type != null)
			return deliveryRequestRepos.findLight(username, type, state.getStatusList(), warehouseList, projectList);
		else
			return deliveryRequestRepos.findLight(username, state.getStatusList(), warehouseList, projectList);

//		if (DeliveryRequestState.WAITING.equals(state))
//			if (type != null)
//				return deliveryRequestRepos.findLight(username, type, state.getStatusList(), warehouseList, projectList);
//			else
//				return deliveryRequestRepos.findLight(username, state.getStatusList(), warehouseList, projectList);
//		else if (DeliveryRequestState.PARTIALLY_DELIVRED.equals(state))
//			if (type != null)
//				return deliveryRequestRepos.findLight(username, type, DeliveryRequestStatus.PARTIALLY_DELIVRED, warehouseList, projectList);
//			else
//				return deliveryRequestRepos.findLight(username, DeliveryRequestStatus.PARTIALLY_DELIVRED, warehouseList, projectList);
//		else if (DeliveryRequestState.DELIVRED.equals(state))
//			if (type != null)
//				return deliveryRequestRepos.findLight(username, type, state.getStatusList(), warehouseList, projectList);
//			else
//				return deliveryRequestRepos.findLight(username, state.getStatusList(), warehouseList, projectList);
//		else if (DeliveryRequestState.REJECTED.equals(state))
//			if (type != null)
//				return deliveryRequestRepos.findLight(username, type, state.getStatusList(), warehouseList, projectList);
//			else
//				return deliveryRequestRepos.findLight(username, state.getStatusList(), warehouseList, projectList);
//		return null;

	}

	public List<DeliveryRequest> findLightBySupplierUser(DeliveryRequestType type, DeliveryRequestState state, Integer deliverToSupplierId, List<Integer> destinationProjectList,
			List<Integer> warehouseList) {
		if (state == null)
			return repos.findLightBySupplierUser(deliverToSupplierId, destinationProjectList, type, warehouseList);
		else
			return repos.findLightBySupplierUser(deliverToSupplierId, destinationProjectList, type, warehouseList, state.getStatusList());
	}

	public List<DeliveryRequest> findByMissingPo(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, DeliveryRequestType type) {
		List<DeliveryRequest> result = deliveryRequestRepos.findByMissingPo(username, warehouseList, assignedProjectList, type);
		result.forEach(i -> {
			i.setTotalCost(deliveryRequestRepos.getTotalCost(i.getId()));
			i.setTotalPrice(deliveryRequestRepos.getTotalPrice(i.getId()));
			i.setEndCustomerName(deliveryRequestRepos.getEndCustomerName(i.getId()));
		});
		return result;
	}

	@Cacheable(value = "deliveryRequestService.countByMissingPo")
	public Long countByMissingPo(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, DeliveryRequestType type) {
		return deliveryRequestRepos.countByMissingPo(username, warehouseList, assignedProjectList, type);
	}

	public List<DeliveryRequest> findByMissingBoqMapping(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, DeliveryRequestType type) {
		return deliveryRequestRepos.findByMissingBoqMapping(username, warehouseList, assignedProjectList, type);
	}

	@Cacheable(value = "deliveryRequestService.countByMissingBoqMapping")
	public Long countByMissingBoqMapping(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, DeliveryRequestType type) {
		return deliveryRequestRepos.countByMissingBoqMapping(username, warehouseList, assignedProjectList, type);
	}

	@Cacheable(value = "deliveryRequestService.findLightByRequester")
	public List<DeliveryRequest> findLightByRequester(String username, DeliveryRequestType type, DeliveryRequestStatus status) {
		return deliveryRequestRepos.findLightByRequester(type, username, status);
	}

	public List<DeliveryRequest> findToAcknowledgeInternal(String username, List<Integer> warehouseList) {
		return deliveryRequestRepos.findToAcknowledgeInternal(username, warehouseList);
	}

	@Cacheable(value = "deliveryRequestService.countToAcknowledgeInternal")
	public Long countToAcknowledgeInternal(String username, List<Integer> warehouseList) {
		return deliveryRequestRepos.countToAcknowledgeInternal(username, warehouseList);
	}

	public List<DeliveryRequest> findToAcknowledgeExternalSupplierUser(String username, Integer supplierId, List<Integer> projectIdList) {
		return deliveryRequestRepos.findToAcknowledgeExternalSupplierUser(username, supplierId, projectIdList);
	}

	@Cacheable(value = "deliveryRequestService.countToAcknowledgeExternalSupplierUser")
	public Long countToAcknowledgeExternalSupplierUser(String username, Integer supplierId, List<Integer> projectIdList) {
		return deliveryRequestRepos.countToAcknowledgeExternalSupplierUser(username, supplierId, projectIdList);
	}

	public List<DeliveryRequest> findToAcknowledgeExternalCustomerUser(String username, Integer customerId, List<Integer> projectIdList) {
		return deliveryRequestRepos.findToAcknowledgeExternalCustomerUser(username, customerId, projectIdList);
	}

	@Cacheable(value = "deliveryRequestService.countToAcknowledgeExternalCustomerUser")
	public Long countToAcknowledgeExternalCustomerUser(String username, Integer customerId, List<Integer> projectIdList) {
		return deliveryRequestRepos.countToAcknowledgeExternalCustomerUser(username, customerId, projectIdList);
	}

	// @Cacheable(value = "deliveryRequestService.cache4")
	// public List<DeliveryRequest> findLightByRequester(String username,
	// DeliveryRequestStatus status) {
	// return deliveryRequestRepos.findLightByRequester(username, status);
	// }

	@Cacheable(value = "deliveryRequestService.countByRequester")
	public Long countByRequester(String username, DeliveryRequestType type, DeliveryRequestStatus status) {
		return deliveryRequestRepos.countByRequester(type, username, status);
	}

	public List<DeliveryRequest> findLightByIsForTransferAndDestinationProjectAndNotTransferred(String username, List<Integer> assignedProjectList) {
		return deliveryRequestRepos.findLightByIsForTransferAndDestinationProjectAndNotTransferredAndStatus(DeliveryRequestType.OUTBOUND, username, assignedProjectList,
				Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED));
	}

	public Long countByIsForTransferAndDestinationProjectAndNotTransferred(String username, List<Integer> assignedProjectList) {
		return deliveryRequestRepos.countByIsForTransferAndDestinationProjectAndNotTransferredAndStatus(DeliveryRequestType.OUTBOUND, username, assignedProjectList,
				Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED));
	}

	public List<DeliveryRequest> findLightByIsForReturnAndNotFullyReturned(String username) {
		return deliveryRequestRepos.findLightByIsForReturnAndNotFullyReturned(DeliveryRequestType.OUTBOUND, username,
				Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED));
	}

	public Long countByIsForReturnAndNotFullyReturned(String username) {
		return deliveryRequestRepos.countByIsForReturnAndNotFullyReturned(DeliveryRequestType.OUTBOUND, username, Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED));
	}

	public List<DeliveryRequest> findLightByPendingTransportation(String username) {
		return deliveryRequestRepos.findLightByPendingTransportation(username, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	public Long countByPendingTransportation(String username) {
		return deliveryRequestRepos.countByPendingTransportation(username, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	@Cacheable(value = "deliveryRequestService.findLightToApprove")
	public List<DeliveryRequest> findLightToApprove(String username, List<Integer> delegatedProjectList) {
		List<DeliveryRequest> result = new ArrayList<DeliveryRequest>();
		result.addAll(deliveryRequestRepos.findLightToApprovePm(username, delegatedProjectList, DeliveryRequestStatus.REQUESTED));
		result.addAll(deliveryRequestRepos.findLightToApproveHm(username, DeliveryRequestStatus.APPROVED1));
		return result;
	}

	@Cacheable(value = "deliveryRequestService.countToApprove")
	public Long countToApprove(String username, List<Integer> delegatedProjectList) {
		return deliveryRequestRepos.countToApprovePm(username, delegatedProjectList, DeliveryRequestStatus.REQUESTED)
				+ deliveryRequestRepos.countToApproveHm(username, DeliveryRequestStatus.APPROVED1);
	}

	@Cacheable(value = "deliveryRequestService.findLightByWarehouseList")
	public List<DeliveryRequest> findLightByWarehouseList(List<Integer> warehouseList) {
		return deliveryRequestRepos.findLightByWarehouseList(warehouseList, Arrays.asList(DeliveryRequestStatus.APPROVED2, DeliveryRequestStatus.PARTIALLY_DELIVRED), DeliveryRequestType.XBOUND);
	}

	@Cacheable(value = "deliveryRequestService.countByWarehouseList")
	public Long countByWarehouseList(List<Integer> warehouseList, List<DeliveryRequestStatus> statusList) {
		return deliveryRequestRepos.countByWarehouseList(warehouseList, statusList, DeliveryRequestType.XBOUND);
	}

	public Integer getMaxReferenceNumber(DeliveryRequestType type) {
		Integer max = deliveryRequestRepos.getMaxReferenceNumber(type);
		return max != null ? max : 0;
	}

	@Cacheable(value = "deliveryRequestService.findLightByRequester")
	public List<DeliveryRequest> findLightByRequester(String username) {
		return deliveryRequestRepos.findLightByRequester(username);
	}

	public List<DeliveryRequest> findByCanBeTransported(String username) {
		return deliveryRequestRepos.findByCanBeTransported(username, DeliveryRequestStatus.APPROVED2);
	}

	public String generateEmailNotification(DeliveryRequest deliveryRequest, String dearFullName, Boolean showMessage) {
		DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
		Html html = new Html();
		Body body = new Body();
		body.setStyle("padding-top: 40px; background-color: #fff; font-family: 'Arial';font-size: 12px;");

		String kindly;
		switch (deliveryRequest.getStatus()) {
		case REQUESTED:
			kindly = "Kindly be informed that the resource " + deliveryRequest.getRequester().getFullName() + " has raised a new delivery request on Ilogistics pending your approval";
			break;
		case APPROVED2:
			kindly = "Kindly be informed that the delivery request below has been <b style='color:green'>Approved</b> for delivery";
			break;
		case DELIVRED:
			kindly = "Kindly be informed that the delivery request below has been delivred "
					+ (deliveryRequest.getIsInbound() ? " to warehouse" : (deliveryRequest.getDestination() != null ? " to " + deliveryRequest.getDestination().getName() : " to site"));
		default:
			kindly = "Kindly be informed that the delivery request below has been <b style='color:" + deliveryRequest.getStatus().getColorCode() + "'>" + deliveryRequest.getStatus().getValue();
			break;
		}
		kindly += "</b> <br/><br/>";

//		String kindly = (DeliveryRequestStatus.REQUESTED.equals(deliveryRequest.getStatus())
//				? "Kindly be informed that the resource " + deliveryRequest.getRequester().getFullName() + " has raised a new delivery request on Ilogistics pending your approval"
//				: "Kindly be informed that the delivery request below has been <b style='color:" + deliveryRequest.getStatus().getColorCode() + "'>" + deliveryRequest.getStatus().getValue())
//				+ (DeliveryRequestStatus.DELIVRED.equals(deliveryRequest.getStatus())
//						? deliveryRequest.getIsInbound() ? " to warehouse" : deliveryRequest.getDestination() != null ? " to " + deliveryRequest.getDestination().getName() : " to site"
//						: "")
//				+ "</b> <br/><br/>";

		Div div = (Div) new Div("Dear " + dearFullName + ",<br/> " + kindly).setStyle("width: 90%; margin: auto;font-family: 'Arial';  font-size: 12px;clear: right;");
		if (showMessage) {
			body.addElement(div);
			body.addElement(new br());
		}

		Div qrDiv = (Div) new Div().setStyle("text-align:center;margin:20px 0px 10px 0px");
		A qrA = (A) new A(App.QR.getLink() + "//dn/" + deliveryRequest.getId() + "/" + deliveryRequest.getQrKey()).setStyle("text-align:right;margin:20px 0px 10px 0px;");
		Img qrImg = (Img) new Img(App.QR.getLink() + "//img/dn/" + deliveryRequest.getId() + "/" + deliveryRequest.getQrKey()).setStyle("width:100px;height:100px");
		qrImg.setWidth("100");
		qrImg.setHeight("100");
		qrA.addElement(qrImg);
		qrDiv.addElement(qrA);
		body.addElement(qrDiv);

		Div timeLineDiv = (Div) new Div().setStyle("text-align:center;margin:20px 0px 10px 0px");
		timeLineDiv.addElement(new Img(getTimeLineImage(deliveryRequest)));
		body.addElement(timeLineDiv);
		body.addElement(new br());

		body.addElement(new HR().setStyle("margin-top: 20px; margin-bottom: 20px; border: 0; border-top: 1px solid #eee;"));

		Div widgetDiv = (Div) new Div().setStyle("text-align: center;margin: 10px 0px 30px 0px");
		Table widgetTable = new Table();
		widgetTable.setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 60%; margin: auto; font-size: 12px;");
		TR widgetTableRow1 = new TR();
		TD td = new TD();
		td.setStyle("padding:10px");
		td.addElement(new Img("http://www.3gcom.ma/erp/ilogistics/widgets/1.png").setWidth("42px"));
		widgetTableRow1.addElement(td);
		td = new TD().setWidth("200px");
		td.addElement(new Span("Number Of Items").setStyle("color:#6fb3e0;font-size: 12px"));
		td.addElement(new br());
		td.addElement(new Span(deliveryRequest.getNumberOfItems() + "").setStyle("color:#555"));
		widgetTableRow1.addElement(td);
		td = new TD();
		td.setStyle("border-left:1px solid #ddd;padding:10px");
		td.addElement(new Img("http://www.3gcom.ma/erp/ilogistics/widgets/2.png").setWidth("42px"));
		widgetTableRow1.addElement(td);

		td = new TD().setWidth("200px");
		td.addElement(new Span("Net Weight").setStyle("color:#9abc32;font-size: 12px"));
		td.addElement(new br());
		td.addElement(new Span(UtilsFunctions.formatDouble(deliveryRequest.getNetWeight()) + " Kg").setStyle("color:#555"));
		widgetTableRow1.addElement(td);

		td = new TD();
		td.setStyle("border-left:1px solid #ddd;padding:10px");
		td.addElement(new Img("http://www.3gcom.ma/erp/ilogistics/widgets/3.png").setWidth("42px"));
		widgetTableRow1.addElement(td);

		td = new TD().setWidth("200px");
		td.addElement(new Span("Gross Weight").setStyle("color:#6f3cc4;font-size: 12px"));
		td.addElement(new br());
		td.addElement(new Span(UtilsFunctions.formatDouble(deliveryRequest.getGrossWeight()) + " Kg").setStyle("color:#555"));
		widgetTableRow1.addElement(td);

		td = new TD();
		td.setStyle("border-left:1px solid #ddd;padding:10px");
		td.addElement(new Img("http://www.3gcom.ma/erp/ilogistics/widgets/4.png").setWidth("42px"));
		widgetTableRow1.addElement(td);

		td = new TD().setWidth("200px");
		td.addElement(new Span("Volume").setStyle("color:#e8b110;font-size: 12px"));
		td.addElement(new br());
		td.addElement(new Span(UtilsFunctions.formatDouble(deliveryRequest.getVolume()) + " m3").setStyle("color:#555"));
		widgetTableRow1.addElement(td);

		widgetTable.addElement(widgetTableRow1);
		widgetDiv.addElement(widgetTable);
		body.addElement(widgetDiv);

		Table table = (Table) new Table().setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto;font-family: 'Arial'; font-size: 12px;");
		table.setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto; font-size: 12px;");

		String tdStyle1 = "background-color: #edf3f4; color: #336199; border: 1px solid #ddd; padding: 6px; text-align: right;";
		String tdStyle2 = "border: 1px solid #ddd; border-bottom: 1px dotted #d5e4f1; border-top: 1px dotted #d5e4f1; padding: 6px;";
		String tdStyle3 = "border-top: 1px solid #fff; border-bottom: 1px solid #fff;";
		String tdWidth1 = "12%";
		String tdWidth2 = "36%";
		String tdWidth3 = "4%";

		TR row = new TR();
		row.addElement(new TD("Reference").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getFullType()).setWidth(tdWidth2)
				.setStyle(tdStyle2 + "color: " + (deliveryRequest.getIsInbound() ? "#69aa46" : deliveryRequest.getIsOutbound() ? "#dd5a43" : "#478fca")));
		row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
		row.addElement(new TD("Type").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getType().getValue()).setWidth(tdWidth2)
				.setStyle(tdStyle2 + "color: " + (deliveryRequest.getIsInbound() ? "#69aa46" : deliveryRequest.getIsOutbound() ? "#dd5a43" : "#478fca")));
		table.addElement(row);

		if (deliveryRequest.getIsInbound() || deliveryRequest.getIsOutbound()) {
			row = new TR();
			row.addElement(new TD("Project").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getProject().getName()).setWidth(tdWidth2).setStyle(tdStyle2 + "color: #69aa46"));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Warehouse").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getWarehouse().getName()).setWidth(tdWidth2).setStyle(tdStyle2 + ("color: #c6699f")));
			table.addElement(row);
		}

		if (deliveryRequest.getIsInbound()) {
			row = new TR();
			row.addElement(new TD("Pref Storage Location").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getPreferedLocation() != null ? deliveryRequest.getPreferedLocation().getName() : "").setWidth(tdWidth2).setStyle(tdStyle2));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Appr Storage Period").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getApproximativeStoragePeriod() + "").setWidth(tdWidth2).setStyle(tdStyle2));
			table.addElement(row);
		} else if (deliveryRequest.getIsOutbound()) {
			row = new TR();
			row.addElement(new TD("Destination Project").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getDestinationProject() != null ? deliveryRequest.getDestinationProject().getName() : "").setWidth(tdWidth2).setStyle(tdStyle2 + ("color: #c6699f")));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Destination Project Manager").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getDestinationProject() != null ? deliveryRequest.getDestinationProject().getManager().getFullName() : "").setWidth(tdWidth2)
					.setStyle(tdStyle2 + ("color: #69aa46")));
			table.addElement(row);
		}

		row = new TR();
		row.addElement(new TD("Priority").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getPriority().getValue()).setWidth(tdWidth2).setStyle(tdStyle2));
		row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
		row.addElement(new TD("Important").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getImportant() ? "Yes" : "No").setWidth(tdWidth2).setStyle(tdStyle2));
		table.addElement(row);

		row = new TR();
		row.addElement(new TD("Is SN Required").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getIsSnRequired() ? "Yes" : "No").setWidth(tdWidth2).setStyle(tdStyle2));
		row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
		row.addElement(new TD("Is Packing List Required").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getIsPackingListRequired() ? "Yes" : "No").setWidth(tdWidth2).setStyle(tdStyle2));
		table.addElement(row);

		row = new TR();
		row.addElement(new TD("Requester").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getRequester().getFullName()).setWidth(tdWidth2).setStyle(tdStyle2 + "color:#ff892a"));
		row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
		row.addElement(new TD("REF").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getSmsRef() != null ? deliveryRequest.getSmsRef() : "").setWidth(tdWidth2).setStyle(tdStyle2 + "color:#a069c3"));
		table.addElement(row);

		row = new TR();
		row.addElement(new TD("Transportation Needed").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getTransportationNeeded() != null && deliveryRequest.getTransportationNeeded() ? "Yes" : "No").setWidth(tdWidth2).setStyle(tdStyle2));
		row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
		row.addElement(new TD("Transporter").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getTransporter() != null ? deliveryRequest.getTransporter().getFullName() : "").setWidth(tdWidth2).setStyle(tdStyle2));
		table.addElement(row);

		row = new TR();
		row.addElement(new TD("Transportation Request").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(
				new TD(deliveryRequest.getTransportationRequest() != null ? deliveryRequest.getTransportationRequest().getReference() : "").setWidth(tdWidth2).setStyle(tdStyle2 + "color:#478fca"));
		row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
		row.addElement(new TD("TR Status").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getTransportationRequest() != null ? deliveryRequest.getTransportationRequest().getStatus().getValue() : "").setWidth(tdWidth2).setStyle(tdStyle2));
		table.addElement(row);

		body.addElement(table);

		body.addElement(new H4("Delivery Details").setStyle("color:#478fca;font-size:17px;width:90%;margin:auto;margin-top:30px;margin-bottom:10px"));

		if (deliveryRequest.getIsInbound() || deliveryRequest.getIsXbound()) {
			table = (Table) new Table().setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto;font-family: 'Arial'; font-size: 12px;");
			table.setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto; font-size: 12px;");

			row = new TR();
			row.addElement(new TD("Owner").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getOrigin() != null ? deliveryRequest.getOwnerName() : "").setWidth(tdWidth2).setStyle(tdStyle2));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Needed Delivery Date").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getNeededDeliveryDate() != null ? UtilsFunctions.getFormattedDateTime(deliveryRequest.getNeededDeliveryDate()) : "").setWidth(tdWidth2)
					.setStyle(tdStyle2 + "color:#a069c3"));
			table.addElement(row);

			row = new TR();
			row.addElement(new TD("Origin DN N°").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getOriginNumber()).setWidth(tdWidth2).setStyle(tdStyle2));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Delivery Date").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getDate4() != null ? UtilsFunctions.getFormattedDateTime(deliveryRequest.getDate4()) : "").setWidth(tdWidth2).setStyle(tdStyle2));
			table.addElement(row);

			row = new TR();
			row.addElement(new TD("Origin Site").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getOrigin() != null ? deliveryRequest.getOrigin().getName() : "None").setWidth(tdWidth2).setStyle(tdStyle2 + "color:#ff851d"));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Address").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getOrigin() != null ? deliveryRequest.getOrigin().getGoogleAddress() : "").setWidth(tdWidth2).setStyle(tdStyle2));
			table.addElement(row);

			body.addElement(table);

		}

		body.addElement(new br());
		body.addElement(new br());

		if (deliveryRequest.getIsOutbound() || deliveryRequest.getIsXbound()) {
			table = (Table) new Table().setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto;font-family: 'Arial'; font-size: 12px;");
			table.setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto; font-size: 12px;");

			row = new TR();
			row.addElement(new TD("End Customer").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getEndCustomer() != null ? deliveryRequest.getEndCustomer().getName() : "").setWidth(tdWidth2).setStyle(tdStyle2 + "color:#ff851d"));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Needed Delivery Date").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getNeededDeliveryDate() != null ? UtilsFunctions.getFormattedDateTime(deliveryRequest.getNeededDeliveryDate()) : "").setWidth(tdWidth2)
					.setStyle(tdStyle2 + "color:#a069c3"));

			table.addElement(row);

			row = new TR();
			row.addElement(new TD("Destination Site").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getDestination() != null ? deliveryRequest.getDestination().getName() : "").setWidth(tdWidth2).setStyle(tdStyle2));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Destination Address").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getDestination() != null ? deliveryRequest.getDestination().getGoogleAddress() : "").setWidth(tdWidth2).setStyle(tdStyle2));
			table.addElement(row);

			row = new TR();
			row.addElement(new TD("Deliver To ").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getDeliverToEntityName()).setWidth(tdWidth2).setStyle(tdStyle2));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Deliver to").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(
					deliveryRequest.getToUser() != null ? deliveryRequest.getToUser().getFullName() + ", " + deliveryRequest.getToUser().getEmail() + ", " + deliveryRequest.getToUser().getPhone()
							: "")
					.setWidth(tdWidth2).setStyle(tdStyle2));
			table.addElement(row);

			body.addElement(table);

		}

		body.addElement(new HR().setStyle("margin-top: 20px; margin-bottom: 20px; border: 0; border-top: 1px solid #eee;"));

		// Details Table
		String tdStyle4 = "border: 1px solid #ddd; padding: 8px;";
		String tableStyle1 = "border: 2px solid #307ecc; border-spacing: 0; border-collapse: collapse; width: 80%; margin: auto; margin-top: 20px; text-align: center;font-family: 'Arial';  font-size: 12px;";
		String thStyle1 = "background-color: #307ecc; color: #fff; padding: 10px";
		table = (Table) new Table().setStyle(tableStyle1);
		table.addElement(new TR().addElement(new TH("Details").setColSpan(4).setStyle(thStyle1)));

		String trStyle1 = "background-image: -webkit-linear-gradient(top, #ffffff, #e6e6e6); font-weight: bold; color: #333333";
		TR table2Row2 = (TR) new TR().setStyle(trStyle1);
		table2Row2.addElement(new TH("PN").setWidth("10%").setStyle(tdStyle4));
		table2Row2.addElement(new TH("Description").setWidth("30%").setStyle(tdStyle4));
		table2Row2.addElement(new TH("Unit/Kit").setStyle(tdStyle4));
		table2Row2.addElement(new TH("Quantity").setStyle(tdStyle4));
		table.addElement(table2Row2);

		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			TR tr = (TR) new TR().setStyle("background-color: #F9F9F9;");
			tr.addElement(new TD(detail.getPartNumber().getName()).setStyle(tdStyle4 + "color:#69aa46"));
			tr.addElement(new TD(detail.getPartNumber().getDescription()).setStyle(tdStyle4 + "color:#478fca"));
			tr.addElement(new TD(detail.getPartNumber().getUnit() ? "Unit" : "Kit").setStyle(tdStyle4));
			tr.addElement(new TD(decimalFormat.format(detail.getQuantity())).setStyle(tdStyle4));
			table.addElement(tr);
		}

		body.addElement(table);
		html.addElement(body);
		return html.toString();
	}

	private String getTimeLineImage(DeliveryRequest deliveryRequest) {
		String path = "http://www.3gcom.ma/erp/ilogistics/dn/";
		switch (deliveryRequest.getStatus()) {
		case EDITED:
			return deliveryRequest.getIsInbound() ? path + "1.png" : path + "a1.png";
		case REQUESTED:
			return deliveryRequest.getIsInbound() ? path + "2.png" : path + "a2.png";
		case APPROVED1:
			return deliveryRequest.getIsInbound() ? path + "3.png" : path + "a3.png";
		case APPROVED2:
			return deliveryRequest.getIsInbound() ? path + "3.png" : path + "a3.png";
		case PARTIALLY_DELIVRED:
			return path + "4.png";
		case DELIVRED:
			return deliveryRequest.getIsInbound() ? path + "5.png" : path + "a4.png";
		case ACKNOWLEDGED:
			return path + "a5.png";
		case REJECTED:
			return path + "r.png";
		case CANCELED:
			return path + (deliveryRequest.getDate2() == null ? "c1.png" : deliveryRequest.getDate3() == null ? "c2.png" : "c3.png");
		default:
			break;
		}
		return null;
	}

	public String generatePdf(DeliveryRequest deliveryRequest) {
		return DeliveryRequestPdfGenerator.generatePdf(deliveryRequest);
	}

	public String generatePdfLink(DeliveryRequest deliveryRequest) {
		try {
			byte[] pdfData = generatePdf2(deliveryRequest);
			if (pdfData == null) {
				return null;
			}
			String downloadPath = "temp/" + deliveryRequest.getReference() + ".pdf";
			String fullPath = UtilsFunctions.path() + downloadPath;

			FileOutputStream fos = new FileOutputStream(fullPath);
			fos.write(pdfData);
			fos.close();
			return "/" + downloadPath;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] generatePdf2(DeliveryRequest deliveryRequest) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Document document = new Document();
			document.setMargins(36, 36, 100, 36);
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			// Load images
			Image logo = Image.getInstance("http://ilogistics.3gcominside.com/resources/pdf/gcom.png");
			logo.scaleToFit(70, 70);

			String qrImageLink = App.QR.getLink() + "//img/dn/" + deliveryRequest.getId() + "/" + deliveryRequest.getQrKey();
			Image qrImage = Image.getInstance(qrImageLink);
			qrImage.scaleToFit(70, 70);
			PdfPTable headerTable = createHeaderTable(logo, qrImage, deliveryRequest.getReference());
			writer.setPageEvent(new PdfHelper(headerTable));
			document.open();

			addMainContent(document, deliveryRequest);

			document.close();
			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private PdfPTable createHeaderTable(Image logo, Image qrImage, String reference) throws DocumentException {
		PdfPTable headerTable = new PdfPTable(3);
		headerTable.setWidthPercentage(100);
		headerTable.setWidths(new float[] { 1f, 1f, 1f });

		// Add logo
		PdfPCell logoCell = new PdfPCell(logo);
		logoCell.setBorder(Rectangle.NO_BORDER);
		logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		headerTable.addCell(logoCell);

		// Add title
		Font titleFont = new Font(Font.FontFamily.HELVETICA, 15, Font.NORMAL | Font.UNDERLINE, new BaseColor(70, 73, 74));
		Paragraph title = new Paragraph(reference, titleFont);
		title.setAlignment(Element.ALIGN_CENTER);
		PdfPCell titleCell = new PdfPCell(title);
		titleCell.setBorder(Rectangle.NO_BORDER);
		titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		headerTable.addCell(titleCell);

		// Add QR code
		PdfPCell qrCell = new PdfPCell(qrImage);
		qrCell.setBorder(Rectangle.NO_BORDER);
		qrCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		headerTable.addCell(qrCell);

		return headerTable;
	}

	private void addMainContent(Document document, DeliveryRequest deliveryRequest) throws DocumentException, IOException {
		PdfPTable widgetTable = new PdfPTable(5);
		widgetTable.setWidthPercentage(100);
		widgetTable.setHorizontalAlignment(Element.ALIGN_CENTER);
		widgetTable.setSpacingBefore(0);
		float[] columnWidths = { 0.5f, 1f, 1f, 1f, 1f };
		widgetTable.setWidths(columnWidths);

		Font widgetFont = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);

		// First column - empty cell
		PdfPCell cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		widgetTable.addCell(cell);

		// Column 2 - Number of Items
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		Image image1 = ImageUtil.getImageFromUrl("http://www.3gcom.ma/erp/ilogistics/widgets/1.png");
		image1.scaleToFit(42, 42);
		cell.addElement(image1);
		cell.addElement(new Phrase("Number Of Items", widgetFont));
		cell.addElement(new Phrase(String.valueOf(deliveryRequest.getNumberOfItems()), widgetFont));
		widgetTable.addCell(cell);

		// Column 3 - Net Weight
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		Image image2 = ImageUtil.getImageFromUrl("http://www.3gcom.ma/erp/ilogistics/widgets/2.png");
		image2.scaleToFit(42, 42);
		cell.addElement(image2);
		cell.addElement(new Phrase("Net Weight", widgetFont));
		cell.addElement(new Phrase(UtilsFunctions.formatDouble(deliveryRequest.getNetWeight()) + " Kg", widgetFont));
		widgetTable.addCell(cell);

		// Column 4 - Gross Weight
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		Image image3 = ImageUtil.getImageFromUrl("http://www.3gcom.ma/erp/ilogistics/widgets/3.png");
		image3.scaleToFit(42, 42);
		cell.addElement(image3);
		cell.addElement(new Phrase("Gross Weight", widgetFont));
		cell.addElement(new Phrase(UtilsFunctions.formatDouble(deliveryRequest.getGrossWeight()) + " Kg", widgetFont));
		widgetTable.addCell(cell);

		// Column 5 - Volume
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		Image image4 = ImageUtil.getImageFromUrl("http://www.3gcom.ma/erp/ilogistics/widgets/4.png");
		image4.scaleToFit(42, 42);
		cell.addElement(image4);
		cell.addElement(new Phrase("Volume", widgetFont));
		cell.addElement(new Phrase(UtilsFunctions.formatDouble(deliveryRequest.getVolume()) + " m3", widgetFont));
		widgetTable.addCell(cell);

		document.add(widgetTable);

		document.add(new Paragraph("\n"));

		Image timelineImage = ImageUtil.getImageFromUrl(getTimeLineImage(deliveryRequest));
		timelineImage.scaleToFit(500, 300);
		timelineImage.setAlignment(Image.ALIGN_CENTER);
		document.add(timelineImage);

		Font titleFontBlue = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, new BaseColor(10, 128, 191));
		Paragraph infoTitle = new Paragraph("General Informations", titleFontBlue);
		infoTitle.setAlignment(Element.ALIGN_LEFT);
		infoTitle.setSpacingBefore(10f);
		document.add(infoTitle);

		PdfPTable table1 = new PdfPTable(2);
		table1.setWidthPercentage(100);
		table1.setSpacingBefore(10f);
		table1.setSpacingAfter(10f);
		table1.setWidths(new float[] { 1f, 2f });

		addTableRow(table1, "DN Number", safeValue(deliveryRequest.getReference()));
		addTableRow(table1, "Type", safeValue(deliveryRequest.getType() != null ? deliveryRequest.getType().getValue() : null));
		addTableRow(table1, "REF", safeValue(deliveryRequest.getSmsRef()));
		addTableRow(table1, "Project", safeValue(deliveryRequest.getProjectName()));
		addTableRow(table1, "Owner", deliveryRequest.getOrigin() != null ? deliveryRequest.getOwnerName() : "");
		addTableRow(table1, "Warehouse", safeValue(deliveryRequest.getWarehouseName()));
		addTableRow(table1, "Appr Storage Period", deliveryRequest.getApproximativeStoragePeriod() != null ? String.valueOf(deliveryRequest.getApproximativeStoragePeriod()) : "");
		addTableRow(table1, "Requester", safeValue(deliveryRequest.getRequesterFullName()));
		addTableRow(table1, "Priority", safeValue(deliveryRequest.getPriority() != null ? deliveryRequest.getPriority().getValue() : null));
		addTableRow(table1, "Is SN Required", deliveryRequest.getIsSnRequired() != null ? (deliveryRequest.getIsSnRequired() ? "Yes" : "No") : "");

		document.add(table1);

		Paragraph deliveryTitle = new Paragraph("Delivery Details", titleFontBlue);
		deliveryTitle.setAlignment(Element.ALIGN_LEFT);
		deliveryTitle.setSpacingBefore(10f);
		document.add(deliveryTitle);
		;
		PdfPTable table3 = new PdfPTable(2);
		table3.setWidthPercentage(100);
		table3.setSpacingBefore(10f);
		table3.setSpacingAfter(10f);
		table3.setWidths(new float[] { 1f, 2f });

		addTableRow(table3, "Needed Delivery Date", deliveryRequest.getNeededDeliveryDate() != null ? UtilsFunctions.getFormattedDateTime(deliveryRequest.getNeededDeliveryDate()) : "");
		addTableRow(table3, "Delivery Date", deliveryRequest.getDate4() != null ? UtilsFunctions.getFormattedDateTime(deliveryRequest.getDate4()) : "");
		addTableRow(table3, "Origin Site", safeValue(deliveryRequest.getOriginName()));
		addTableRow(table3, "Origin Site Address", safeValue(deliveryRequest.getOrigin() != null ? deliveryRequest.getOrigin().getGoogleAddress() : ""));
		addTableRow(table3, "Transportation required", deliveryRequest.getTransportationNeeded() != null ? (deliveryRequest.getTransportationNeeded() ? "Yes" : "No") : "");
		addTableRow(table3, "TR Number", safeValue(deliveryRequest.getTransportationRequest() != null ? deliveryRequest.getTransportationRequest().getReference() : ""));

		table3.setSpacingAfter(40);
		document.add(table3);

		Paragraph eqDetails = new Paragraph("Equipements Details", titleFontBlue);
		infoTitle.setAlignment(Element.ALIGN_LEFT);
		infoTitle.setSpacingBefore(10f);
		document.add(eqDetails);

		PdfPTable detailsTable = new PdfPTable(4);
		detailsTable.setWidthPercentage(100); // Full width
		detailsTable.setSpacingBefore(15f); // Space before the table
		detailsTable.setWidths(new float[] { 1f, 3f, 5f, 1f }); // Adjust column widths
		Font headerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.WHITE);
		BaseColor headerBgColor = new BaseColor(50, 130, 200); // Light blue color for headers
		Font bodyFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, new BaseColor(70, 73, 74));
		BaseColor headerColor = new BaseColor(200, 200, 200);
		float headerBorderWidth = 0.5f;
		String[] detailHeaders = { "Image", "PN", "Description", "Qty" };
		for (String header : detailHeaders) {
			PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
			headerCell.setBorderColor(headerColor);
			headerCell.setBorderWidth(headerBorderWidth);
			headerCell.setBackgroundColor(headerBgColor); // Light blue background
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setPadding(8);
			detailsTable.addCell(headerCell);
		}
		DecimalFormat decimalFormat = new DecimalFormat("###,###.##");
		// Loop through the details list and add rows
		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			try {
				String imageUrl = detail.getPartNumber().getImage(); // Get image URL
				if (imageUrl != null && !imageUrl.isEmpty()) {
					Image partImage = ImageUtil.getImageFromUrl(Public.getPublicUrl(imageUrl));
					partImage.scaleToFit(40, 25); // Scale image size
					PdfPCell imageCell = new PdfPCell(partImage);
					imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					imageCell.setBorder(Rectangle.BOX);
					detailsTable.addCell(imageCell);
				} else {
					// If no image, add an empty cell
					detailsTable.addCell(createTableCell("No Image", bodyFont));
				}
			} catch (Exception e) {
				detailsTable.addCell(createTableCell("Error Loading Image", bodyFont));
			}
			detailsTable.addCell(createTableCell(detail.getPartNumber().getName(), bodyFont));
			detailsTable.addCell(createTableCell(detail.getPartNumber().getDescription(), bodyFont));
			detailsTable.addCell(createTableCell(String.valueOf(decimalFormat.format(detail.getQuantity())), bodyFont));
		}
		detailsTable.setSpacingAfter(30);

		document.add(detailsTable);

		Paragraph packingTitle = new Paragraph("Packing List", titleFontBlue);
		packingTitle.setAlignment(Element.ALIGN_LEFT);
		document.add(packingTitle);

		PdfPTable packingTable = new PdfPTable(5);
		packingTable.setWidthPercentage(100); // Full width
		packingTable.setSpacingBefore(15f); // Space before the table
		packingTable.setWidths(new float[] { 1f, 4f, 2f, 2f, 1f });// Adjust column widths

		String[] packingHeaders = { "Type", "Dimension", "GW", "Volume", "Qty" };
		for (String header : packingHeaders) {
			PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
			headerCell.setBackgroundColor(headerBgColor); // Light blue background
			headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			headerCell.setBorderColor(headerColor);
			headerCell.setBorderWidth(headerBorderWidth);
			headerCell.setPadding(8);
			packingTable.addCell(headerCell);
		}

		for (PackingDetail detail : deliveryRequest.getPackingDetailSummaryList()) {
			try {
				String imageUrl = detail.getTypeImage(); // Get image URL
				if (imageUrl != null && !imageUrl.isEmpty()) {
					Image partImage = ImageUtil.getImageFromUrl("http://ilogistics.3gcominside.com" + imageUrl);
					partImage.scaleToFit(40, 25); // Scale image size
					PdfPCell imageCell = new PdfPCell(partImage);
					imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					imageCell.setBorder(Rectangle.BOX);
					packingTable.addCell(imageCell);
				} else {
					// If no image, add an empty cell
					packingTable.addCell(createTableCell("No Image", bodyFont));
				}
			} catch (Exception e) {
				packingTable.addCell(createTableCell("Error Loading Image", bodyFont));
			}
			packingTable.addCell(createTableCell(safeValue(detail.getLength()) + " m / " + safeValue(detail.getWidth()) + " m / " + safeValue(detail.getHeight()) + " m", bodyFont));
			packingTable.addCell(createTableCell(safeValue(detail.getGrossWeight()) + " Kg", bodyFont));
			packingTable.addCell(createTableCell(safeValue(detail.getVolume()) + " m3", bodyFont));
			packingTable.addCell(createTableCell(String.valueOf(decimalFormat.format(detail.getQuantity())), bodyFont));
		}

		document.add(packingTable);

		PdfPTable signatureTable = new PdfPTable(2);
		signatureTable.setWidthPercentage(100);
		signatureTable.setSpacingBefore(10f);
		signatureTable.setSpacingAfter(10f);
		signatureTable.setWidths(new float[] { 1f, 2f });

		Font labelFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.DARK_GRAY);
		Font companyFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, new BaseColor(0, 102, 204)); // Blue
		Font resourceFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, new BaseColor(0, 153, 76)); // Green
		Font emailFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, new BaseColor(204, 0, 0)); // Red
		Font phoneFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, new BaseColor(153, 51, 255)); // Purple
		Font cinFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, new BaseColor(255, 102, 0)); // Orange

		// Create an empty cell for the actual signature content
		PdfPTable infoTable = new PdfPTable(1);
		infoTable.setWidthPercentage(100);
		PdfPCell titleCell = new PdfPCell(new Phrase("Stamp & Signature", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.GRAY)));
		titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		titleCell.setPadding(5);
		titleCell.setBorder(Rectangle.NO_BORDER);
		infoTable.addCell(titleCell);
		infoTable.addCell(createMixedCell("Company: ", deliveryRequest.getDeliverToEntityName(), labelFont, companyFont));
		infoTable.addCell(createMixedCell("Ressource: ", safeValue(deliveryRequest.getToUserFullName()), labelFont, resourceFont));
		infoTable.addCell(createMixedCell("Email: ", safeValue(deliveryRequest.getToUserEmail()), labelFont, emailFont));
		infoTable.addCell(createMixedCell("Tel: ", safeValue(deliveryRequest.getToUserPhone()), labelFont, phoneFont));
		infoTable.addCell(createMixedCell("CIN: ", safeValue(deliveryRequest.getToUserCin()), labelFont, cinFont));

		PdfPCell infoCell = new PdfPCell(infoTable);
		infoCell.setBackgroundColor(new BaseColor(222, 248, 252));
		infoCell.setBorder(Rectangle.BOX);
		infoCell.setPadding(5);

		PdfPCell signatureContentCell = new PdfPCell();
		signatureContentCell.setFixedHeight(100);
		signatureContentCell.setBorder(Rectangle.BOX);
		signatureContentCell.setPadding(5);
		signatureContentCell.setPhrase(new Phrase(""));

		signatureTable.addCell(infoCell);
		signatureTable.addCell(signatureContentCell);

		document.add(signatureTable);

	}

	private static PdfPCell createMixedCell(String label, String value, Font labelFont, Font valueFont) {
		Phrase phrase = new Phrase();
		phrase.add(new Chunk(label, labelFont));
		phrase.add(new Chunk(value, valueFont));

		PdfPCell cell = new PdfPCell(phrase);
		cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}

	private String safeValue(String value) {
		return value != null ? value : "";
	}

	private String safeValue(Double value) {
		return value != null ? String.valueOf(value) : "";
	}

	private static PdfPCell createTableCell(String text, Font font) {
		BaseColor lightGrey = new BaseColor(200, 200, 200);
		float borderWidth = 0.5f;
		PdfPCell cell = new PdfPCell(new Phrase(text, font));
		cell.setPadding(6);
		cell.setBorderWidth(borderWidth);
		cell.setBorderColor(lightGrey);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		return cell;
	}

	private void addTableRow(PdfPTable table, String key, String value) {
		BaseColor lightGrey = new BaseColor(200, 200, 200);
		BaseColor extraGray = new BaseColor(230, 230, 230); // Extra gray background
		BaseColor blueText = new BaseColor(16, 65, 117); // Blue text color
		float borderWidth = 0.5f;

		PdfPCell cell1 = new PdfPCell(new Phrase(key, new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, blueText)));
		cell1.setBackgroundColor(new BaseColor(222, 248, 252));
		cell1.setBorderColor(lightGrey);
		cell1.setBorderWidth(borderWidth);
		table.addCell(cell1);

		Font valueFont = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK); // Default black text
		BaseColor bgColor = BaseColor.WHITE; //

		if (key.equals("DN Number") || key.equals("Type") || key.equals("Transportation required") || key.equals("Is SN Required") || key.equals(" Needed Delivery Date")) {
			bgColor = extraGray;
			valueFont.setColor(blueText);
		}

		PdfPCell cell2 = new PdfPCell(new Phrase(value, valueFont));
		cell2.setBackgroundColor(bgColor);
		cell2.setBorderColor(lightGrey);
		cell2.setBorderWidth(borderWidth);
		table.addCell(cell2);
	}

	public String generateStamp(DeliveryRequest deliveryRequest) {
		String downloadPath = "temp/stamp_" + deliveryRequest.getReference() + ".pdf";
		try {
			Document document = new Document(new RectangleReadOnly(284, 171), 5, 5, 5, 5); // 100mm * 60mm
			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
			Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
			float[] pointColumnWidths = { 158F, 300F };
			PdfPTable table1 = new PdfPTable(pointColumnWidths);
			table1.setTotalWidth(290);
			table1.setLockedWidth(true);
			PdfPCell cell1, cell2;
			Phrase phrase;
			Paragraph paragraph;
			PdfPTable table2 = new PdfPTable(2);

			PdfWriter.getInstance(document, new FileOutputStream(UtilsFunctions.path() + downloadPath));

			document.open();

			paragraph = new Paragraph(deliveryRequest.getReference() + " | Delivery Date : " + (deliveryRequest.getDate4() != null ? UtilsFunctions.getFormattedDate(deliveryRequest.getDate4()) : ""),
					titleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(10f);
			document.add(paragraph);

			// qrcode Cell
			BarcodeQRCode barcodeQrcode = new BarcodeQRCode(App.QR.getLink() + "/dn/" + deliveryRequest.getId() + "/" + deliveryRequest.getQrKey(), 100, 100, null);
			Image qrcodeImage = barcodeQrcode.getImage();
			qrcodeImage.scaleToFit(95, 95);
			// qrcodeImage.scalePercent(100);
			Image logo = null;
			if ("gcom".equals(erp))
				logo = Image.getInstance(UtilsFunctions.path() + "resources/pdf/gcom.png");
			else if ("orange".equals(erp))
				logo = Image.getInstance(UtilsFunctions.path() + "resources/pdf/orange.png");
			logo.scaleToFit(50, 60);
			logo.setAlignment(Element.ALIGN_CENTER);
			cell1 = new PdfPCell();
			cell1.setBorder(Rectangle.NO_BORDER);
			cell1.addElement(qrcodeImage);
			cell1.addElement(logo);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell1);

			// owner/project/ref/g weight/volume cell
			phrase = new Phrase();
			phrase.add(new Chunk("# Of Items : " + deliveryRequest.getNumberOfItems(), boldFont));
			phrase.add(new Chunk("\nOwner : ", boldFont));
			phrase.add(new Chunk(deliveryRequest.getOwnerName() != null ? UtilsFunctions.cutText(deliveryRequest.getOwnerName(), 70) : "", normalFont));
			phrase.add(new Chunk("\nProject : ", boldFont));
			phrase.add(new Chunk(UtilsFunctions.cutText(deliveryRequest.getProject().getName(), 25), normalFont));
			phrase.add(new Chunk("\nRef : ", boldFont));
			phrase.add(new Chunk(UtilsFunctions.cutText(deliveryRequest.getSmsRef(), 25), normalFont));
			cell1 = new PdfPCell();
			cell1.setPadding(3f);
			cell1.setLeading(0, 1f);
			cell1.addElement(phrase);

			phrase = new Phrase();
			phrase.add(new Chunk("Gross Weight\n", boldFont));
			phrase.add(new Chunk(UtilsFunctions.formatDouble(deliveryRequest.getGrossWeight()) + " Kg", boldFont));
			cell2 = new PdfPCell();
			cell2.setBorder(0);
			cell2.addElement(phrase);
			table2.addCell(cell2);
			phrase = new Phrase();
			phrase.add(new Chunk("Volume\n", boldFont));
			phrase.add(new Chunk(UtilsFunctions.formatDouble(deliveryRequest.getVolume()) + " m3", boldFont));
			cell2 = new PdfPCell();
			cell2.setBorder(0);
			cell2.addElement(phrase);
			table2.addCell(cell2);
			cell1.addElement(table2);
			cell1.setBorder(Rectangle.LEFT);
			table1.addCell(cell1);
			document.add(table1);

			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return downloadPath;
	}

	public byte[] generateStampMobile(DeliveryRequest deliveryRequest) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Document document = new Document(new RectangleReadOnly(284, 171), 5, 5, 5, 5);
			PdfWriter.getInstance(document, outputStream);

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15);
			Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
			Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
			float[] pointColumnWidths = { 158F, 300F };
			PdfPTable table1 = new PdfPTable(pointColumnWidths);
			table1.setTotalWidth(290);
			table1.setLockedWidth(true);
			PdfPCell cell1, cell2;
			Phrase phrase;
			Paragraph paragraph;
			PdfPTable table2 = new PdfPTable(2);

			document.open();

			paragraph = new Paragraph(deliveryRequest.getReference() + " | Delivery Date : " + (deliveryRequest.getDate4() != null ? UtilsFunctions.getFormattedDate(deliveryRequest.getDate4()) : ""),
					titleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(10f);
			document.add(paragraph);

			BarcodeQRCode barcodeQrcode = new BarcodeQRCode(App.QR.getLink() + "/dn/" + deliveryRequest.getId() + "/" + deliveryRequest.getQrKey(), 100, 100, null);
			Image qrcodeImage = barcodeQrcode.getImage();
			qrcodeImage.scaleToFit(95, 95);

			Image logo = null;
			if ("gcom".equals(erp))
				logo = Image.getInstance("http://ilogistics.3gcominside.com/resources/pdf/gcom.png");
			else if ("orange".equals(erp))
				logo = Image.getInstance("http://ilogistics.3gcominside.com/resources/pdf/orange.png");
			logo.scaleToFit(50, 60);
			logo.setAlignment(Element.ALIGN_CENTER);

			cell1 = new PdfPCell();
			cell1.setBorder(Rectangle.NO_BORDER);
			cell1.addElement(qrcodeImage);
			cell1.addElement(logo);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell1);

			phrase = new Phrase();
			phrase.add(new Chunk("# Of Items : " + deliveryRequest.getNumberOfItems(), boldFont));
			phrase.add(new Chunk("\nOwner : ", boldFont));
			phrase.add(new Chunk(deliveryRequest.getOwnerName() != null ? UtilsFunctions.cutText(deliveryRequest.getOwnerName(), 70) : "", normalFont));
			phrase.add(new Chunk("\nProject : ", boldFont));
			phrase.add(new Chunk(UtilsFunctions.cutText(deliveryRequest.getProject().getName(), 25), normalFont));
			phrase.add(new Chunk("\nRef : ", boldFont));
			phrase.add(new Chunk(UtilsFunctions.cutText(deliveryRequest.getSmsRef(), 25), normalFont));

			cell1 = new PdfPCell();
			cell1.setPadding(3f);
			cell1.setLeading(0, 1f);
			cell1.addElement(phrase);

			phrase = new Phrase();
			phrase.add(new Chunk("Gross Weight\n", boldFont));
			phrase.add(new Chunk(UtilsFunctions.formatDouble(deliveryRequest.getGrossWeight()) + " Kg", boldFont));
			cell2 = new PdfPCell();
			cell2.setBorder(0);
			cell2.addElement(phrase);
			table2.addCell(cell2);

			phrase = new Phrase();
			phrase.add(new Chunk("Volume\n", boldFont));
			phrase.add(new Chunk(UtilsFunctions.formatDouble(deliveryRequest.getVolume()) + " m3", boldFont));
			cell2 = new PdfPCell();
			cell2.setBorder(0);
			cell2.addElement(phrase);
			table2.addCell(cell2);

			cell1.addElement(table2);
			cell1.setBorder(Rectangle.LEFT);
			table1.addCell(cell1);
			document.add(table1);

			document.close();

			return outputStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class DeliveryRequestPdfGenerator {

		static Integer rowsPerPage = 13;

		public static String generatePdf(DeliveryRequest deliveryRequest) {
			String downloadPath = "";
			try {
				Document doc = new Document();
				doc.setMargins(0, 0, 10, 0);

				downloadPath = "temp/" + deliveryRequest.getReference() + ".pdf";
				PdfWriter.getInstance(doc, new FileOutputStream(UtilsFunctions.path() + downloadPath));
				doc.open();
				Integer totalPages = 1;

				Double d = deliveryRequest.getDetailList().size() / (double) rowsPerPage;

				if (d.equals(Math.floor(d)))
					totalPages = (int) (double) d;
				else
					totalPages = (int) (double) d + 1;

				for (int i = 0; i < totalPages; i++) {
					if (i != 0) {
						doc.newPage();
					}
					PdfPTable mainTab = new PdfPTable(1);
					mainTab.setWidthPercentage(100);
					addPage(deliveryRequest, mainTab, totalPages, i);
					doc.add(mainTab);
				}

				doc.close();
				System.out.println("Done !");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return downloadPath;
		}

		public static void addPage(DeliveryRequest deliveryRequest, PdfPTable mainTab, Integer totalPages, Integer i) {
			PdfPCell cell;
			PdfPCell cell2;
			Image image;
			Phrase phrase;
			try {
				cell = mainTab.getDefaultCell();
				cell.setBorder(0);
				cell.setLeading(0, 2);

				image = Image.getInstance(UtilsFunctions.path() + "resources/pdf/company_1.jpg");
				image.scaleToFit(75, 90);

				cell = new PdfPCell(image);
				cell.setFixedHeight(60f);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_TOP);
				cell.setPaddingLeft(20);
				cell.setBorder(2);
				cell.setBorderWidth(1);
				mainTab.addCell(cell);
				phrase = new Phrase();
				phrase.add(new Chunk("REFERENCE #" + deliveryRequest.getReference(), FontFactory.getFont("Arial", 12, Font.BOLD)));
				cell = new PdfPCell(phrase);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorder(0);
				cell.setPaddingTop(5);
				cell.setPaddingBottom(5);
				mainTab.addCell(cell);

				// PdfContentByte canvas = writer.getDirectContent();
				// canvas.moveTo(120, 750);
				// canvas.lineTo(480, 750);
				// canvas.closePathStroke();

				// phrase = new Phrase(new Chunk("Rabat le " +
				// UtilsFunctions.getFormattedDate(new Date()),
				// FontFactory.getFont("Arial", 9, Font.NORMAL)));
				// cell = new PdfPCell(phrase);
				// cell.setBorder(0);
				// cell.setPaddingTop(20);
				// cell.setPaddingRight(20);
				// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				// mainTab.addCell(cell);

				PdfPTable table2 = new PdfPTable(4);
				// cell = table2.getDefaultCell();
				// cell.setBorder(0);
				// mainTab.setWidthPercentage(90);
				// table2.addCell(new
				// Phrase("AAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAA\nAAAAAAAAAAAAAAAAA",
				// FontFactory.getFont("Arial", 9, Font.NORMAL)));
				// table2.addCell("");
				// cell = new PdfPCell(new
				// Phrase("BBBBBBBBBBBBBBBBB\nBBBBBBBBBBBBBBBBB\nBBBBBBBBBBBBBBBBB",
				// FontFactory.getFont("Arial", 9, Font.NORMAL)));
				// cell.setPaddingLeft(20);
				// cell.setBorder(PdfPCell.LEFT);
				// table2.addCell(cell);
				// table2.addCell("");
				// mainTab.addCell(table2);
				phrase = new Phrase("Page " + (i + 1) + "/" + totalPages, FontFactory.getFont("Arial", 8, Font.BOLD));
				cell = new PdfPCell(phrase);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setBorder(0);
				cell.setPaddingTop(15);
				cell.setPaddingBottom(15);
				mainTab.addCell(cell);

				cell = new PdfPCell();
				cell.setPadding(10);
				// cell.setPaddingTop(30);
				// cell.setPaddingBottom(100);
				// phrase = new Phrase("Aaaaaa\n\nLorem ipsum dolor sit amet,
				// consectetuer adipiscing elit,Lorem ipsum dolor sit amet,
				// consectetuer adipiscing elit\n\n",
				// FontFactory.getFont("Arial",
				// 9, Font.NORMAL));
				//
				// cell.addElement(phrase);

				table2 = new PdfPTable(4);
				table2.setWidthPercentage(100);

				// row = new PdfPRow()
				cell2 = new PdfPCell(new Phrase("PN", FontFactory.getFont("Arial", 8, Font.NORMAL)));
				cell2.setBackgroundColor(new BaseColor(217, 217, 217));
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell2);
				cell2 = new PdfPCell(new Phrase("Description", FontFactory.getFont("Arial", 8, Font.NORMAL)));
				cell2.setBackgroundColor(new BaseColor(217, 217, 217));
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell2);
				cell2 = new PdfPCell(new Phrase("Unit/Kit", FontFactory.getFont("Arial", 8, Font.NORMAL)));
				cell2.setBackgroundColor(new BaseColor(217, 217, 217));
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell2);
				cell2 = new PdfPCell(new Phrase("Quantity", FontFactory.getFont("Arial", 8, Font.NORMAL)));
				cell2.setBackgroundColor(new BaseColor(217, 217, 217));
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell2);

				int toIndex = Math.min((i + 1) * rowsPerPage, deliveryRequest.getDetailList().size());
				List<DeliveryRequestDetail> detailList = deliveryRequest.getDetailList().subList(i * rowsPerPage, toIndex);
				for (DeliveryRequestDetail detail : detailList) {
					cell2 = new PdfPCell(new Phrase(detail.getPartNumber().getName(), FontFactory.getFont("Arial", 8, Font.BOLD)));
					cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell2.setPadding(10);
					table2.addCell(cell2);
					cell2 = new PdfPCell(new Phrase(UtilsFunctions.cutText(detail.getPartNumber().getDescription(), 100), FontFactory.getFont("Arial", 8, Font.NORMAL)));
					// cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell2.setPadding(10);
					table2.addCell(cell2);
					cell2 = new PdfPCell(new Phrase(detail.getPartNumber().getUnit() ? "Unit" : "Kit", FontFactory.getFont("Arial", 8, Font.NORMAL)));
					cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell2.setPadding(10);
					table2.addCell(cell2);
					cell2 = new PdfPCell(new Phrase(String.format("%1$,.2f", detail.getQuantity()), FontFactory.getFont("Arial", 8, Font.NORMAL)));
					cell2.setLeading(0, 2);
					cell2.setPadding(10);
					cell2.setPaddingTop(0);
					table2.addCell(cell2);
				}

				float[] columnWidths = new float[] { 15f, 40f, 8f, 10f };
				table2.setWidths(columnWidths);
				cell.addElement(table2);

				// phrase = new Phrase("\n\nLorem ipsum dolor sit amet,
				// consectetuer adipiscing elit\n\n senectus et netus et male.",
				// FontFactory.getFont("Arial", 9, Font.NORMAL));
				// cell.addElement(phrase);
				cell.setBorder(PdfPCell.NO_BORDER);
				mainTab.addCell(cell);

				Boolean lastPage = (i + 1) == totalPages;
				if (lastPage) {
					cell = new PdfPCell(new Phrase("____________________________\n\nSignature                  '", FontFactory.getFont("Arial", 9, Font.NORMAL)));
					cell.setBorder(PdfPCell.NO_BORDER);
					// cell.setPaddingTop(70);
					cell.setPaddingRight(100);
					// cell.setPaddingBottom(110);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					mainTab.addCell(cell);
				}

				// phrase = new Phrase("PDF_FOOTER",
				// FontFactory.getFont("Arial", 8, Font.BOLD, new BaseColor(31,
				// 72, 124)));
				// cell = new PdfPCell(phrase);
				// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				// cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				// cell.setBorder(0);
				// mainTab.addCell(cell);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public Long countByOriginNumber(String originNumber) {
		return deliveryRequestRepos.countByOriginNumber(originNumber);
	}

	public Double getGrossWeight(Integer deliveryRequestId) {
		Double d = deliveryRequestRepos.getGrossWeight(deliveryRequestId);
		return d != null ? d : 0.0;
	}

	public Boolean canAddTrasnport(DeliveryRequest deliveryRequest, String connectedUser) {
		return deliveryRequest.getTransportationNeeded() != null && deliveryRequest.getTransportationNeeded()
				&& (connectedUser.equals(deliveryRequest.getRequester().getUsername()) || connectedUser.equals(deliveryRequest.getProject().getManager().getUsername()))
				&& deliveryRequest.getTransportationRequest() == null && !Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED).contains(deliveryRequest.getStatus());
	}

	public List<DeliveryRequest> findOutboundFinancialByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return deliveryRequestRepos.findOutboundFinancialByCompanyOwner(username, warehouseList, assignedProjectList, companyId, ProjectTypes.STOCK.getValue(), DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	public List<DeliveryRequest> findInboundFinancialByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
//		return deliveryRequestRepos.findInboundFinancialByCompanyOwner(username, warehouseList, assignedProjectList, companyId, ProjectTypes.STOCK.getValue(),
//				DeliveryRequestType.INBOUND, InboundType.NEW, Arrays.asList(DeliveryRequestStatus.DELIVRED));
		return deliveryRequestRepos.findInboundFinancialByCompanyOwner(username, warehouseList, assignedProjectList, companyId, DeliveryRequestType.INBOUND, InboundType.NEW,
				Arrays.asList(DeliveryRequestStatus.DELIVRED));
	}

	// fillDestinationProject script
	public void fillDestinationProject() {
		List<DeliveryRequest> list = deliveryRequestRepos.findByNotHavingDestinationProject(DeliveryRequestType.OUTBOUND, ProjectTypes.STOCK.getValue());
		for (DeliveryRequest deliveryRequest : list) {
			List<Integer> associatedProject = appLinkRepos.findRevenuesIdProjectByDeliveryRequest(deliveryRequest.getId());
			if (associatedProject == null || associatedProject.size() != 1) {
				System.out.println(deliveryRequest.getReference() + "\t" + deliveryRequest.getRequester().getUsername());
				continue;
			}
			Project destinationProject = projectService.findOne(associatedProject.get(0));
			System.out.println(deliveryRequest.getReference() + "\t" + destinationProject.getName());
			deliveryRequest.setDestinationProject(destinationProject);
			save(deliveryRequest);
		}
	}

	public Long countByProject(Integer projectId) {
		return ObjectUtils.firstNonNull(deliveryRequestRepos.countByProject(projectId), 0l);
	}

	public void updatePo(Integer id, Po po) {
		deliveryRequestRepos.updatePo(id, po);
		evictCache();
	}

	public void updatePo(Integer id, Integer poId) {
		updatePo(poId, poService.findOne(poId));
		evictCache();
	}

	public void updateDetailListPurchaseCostFromBoqMapping(Integer deliveryRequestId) {
		DeliveryRequest deliveryRequest = findOne(deliveryRequestId);
		if (!deliveryRequest.getIsInbound())
			return;
		Map<Integer, List<Double[]>> map = new HashMap<Integer, List<Double[]>>(); // Map<pnId,List(qty,unitPrice)>
		for (BoqMapping bm : deliveryRequest.getBoqMappingList()) {
			Integer partNumberId = bm.getPartNumberEquivalence() == null ? bm.getBoq().getPartNumber().getId() : bm.getPartNumberEquivalence().getPartNumber().getId();
			map.putIfAbsent(partNumberId, new ArrayList<Double[]>());
			Double[] qtyUnitPrice = { bm.getQuantity(), bm.getBoq().getUnitPrice() };
			map.get(partNumberId).add(qtyUnitPrice);
		}

		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			Integer partNumberId = detail.getPartNumberId();
			List<Double[]> quantityUnitPriceTab = map.get(partNumberId);
			Double purchaseCost = quantityUnitPriceTab.stream().mapToDouble(tab -> tab[0] * tab[1]).sum() / quantityUnitPriceTab.stream().mapToDouble(tab -> tab[0]).sum();
			deliveryRequestDetailRepos.updatePurchaseCost(detail.getId(), purchaseCost);
		}
		evictCache();
	}

	public void updateDetailListPurchaseCurrency(Integer deliveryRequestId, Currency purchaseCurrency) {
		deliveryRequestDetailRepos.updatePurchaseCurrencyByDeliveryRequest(deliveryRequestId, purchaseCurrency);
	}

	public void updateDetailListPriceCurrency(Integer deliveryRequestId, Currency priceCurrency) {
		deliveryRequestDetailRepos.updatePriceCurrencyByDeliveryRequest(deliveryRequestId, priceCurrency);
	}

	public void updateDetailListPurchaseCostFromBoqMappingScript() {
		repos.findIdByTypeAndHavingBoqMapping(DeliveryRequestType.INBOUND).forEach(this::updateDetailListPurchaseCostFromBoqMapping);
	}

	public void updateDetailListUnitCost(Integer deliveryRequestId) {
		DeliveryRequest deliveryRequest = findOne(deliveryRequestId);
		if (!deliveryRequest.getIsInbound() || deliveryRequest.getPo() == null)
			return;
		Double conversionRate = deliveryRequest.getPo().getMadConversionRate();
		Double totalPurchaseCost = deliveryRequest.getDetailList().stream().mapToDouble(d -> d.getQuantity() * d.getPurchaseCost()).sum();
//		Double otherCosts = appLinkRepos.findTotalAmountByDeliveryRequestAndNotPo(deliveryRequest.getId(), deliveryRequest.getPo().getId());
//		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
//			Double purchaseCost = detail.getPurchaseCost();
//			Double unitCost = purchaseCost * conversionRate * (1 + otherCosts / totalPurchaseCost);
//			System.out.println(detail.getPartNumberName() + " : " + unitCost);
//		}
		Double totalAppLink = appLinkRepos.findTotalAmountByDeliveryRequest(deliveryRequest.getId());
		Integer transportationRequestId = transportationRequestService.findIdByDeliveryRequest(deliveryRequest.getId());
		if (transportationRequestId != null)
			totalAppLink += appLinkRepos.findTotalAmountByTransportationRequest(transportationRequestId);
		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			Double purchaseCost = detail.getPurchaseCost();
			Double unitCost = (purchaseCost / totalPurchaseCost) * totalAppLink; // equivalent ((purchaseCost * qty /
																					// totalPurchaseCost) *
																					// totalAppLink) /qty
			unitCost = Math.round(unitCost * 100) / 100.0;

			System.out.println(purchaseCost);
			System.out.println(unitCost);
			System.out.println(conversionRate);
			System.out.println(totalPurchaseCost);

			deliveryRequestDetailService.updateUnitCost(detail.getId(), unitCost);
		}
		evictCache();
	}

	public void updateDetailListUnitPriceFromBoqMapping(Integer deliveryRequestId) {
		DeliveryRequest deliveryRequest = findOne(deliveryRequestId);
		Map<PartNumber, Double> boqPriceMap = new HashMap<>();

		// Same PN
		deliveryRequest.getBoqMappingList().stream() //
				.filter(i -> i.getPartNumberEquivalence() == null) //
				.forEach(i -> boqPriceMap.put(i.getBoq().getPartNumber(), i.getBoq().getUnitPrice()));

		// Formula Equivalence list size = 1 & directEquivalence = true
		deliveryRequest.getBoqMappingList().stream()//
				.filter(i -> i.getPartNumberEquivalence() != null && i.getPartNumberEquivalence().getDetailList().size() == 1 && i.getDirectEquivalence())//
				.forEach(i -> boqPriceMap.put(i.getPartNumberEquivalence().getDetailList().get(0).getPartNumber(),
						i.getBoq().getUnitPrice() / i.getPartNumberEquivalence().getDetailList().get(0).getQuantity()));

		// Formula & directEquivalence = false
		deliveryRequest.getBoqMappingList().stream()//
				.filter(i -> i.getPartNumberEquivalence() != null && !i.getDirectEquivalence()) //
				.forEach(i -> boqPriceMap.put(i.getPartNumberEquivalence().getPartNumber(), i.getBoq().getUnitPrice() * i.getPartNumberEquivalence().getDetailList().get(0).getQuantity()));

		for (DeliveryRequestDetail drd : deliveryRequest.getDetailList()) {
			Double unitPrice = boqPriceMap.get(drd.getPartNumber());

			if (unitPrice == null)
				continue;

			deliveryRequestDetailRepos.updateUnitPrice(drd.getId(), unitPrice);

			// call service too so we update stock row unit price
			deliveryRequestDetailService.updateUnitPrice(drd.getId(), unitPrice);

			// update all return details & sock row unit price
			updateReturnInboundsUnitPrice(deliveryRequestId);

		}

		evictCache();
	}

	public Long countByOutboundDeliveryRequestTransfer(Integer outboundDeliveryRequestId) {
		return ObjectUtils.firstNonNull(deliveryRequestRepos.countByOutboundDeliveryRequestTransfer(outboundDeliveryRequestId), 0l);
	}

	public Long countByOutboundDeliveryRequestReturn(Integer outboundDeliveryRequestId) {
		return ObjectUtils.firstNonNull(deliveryRequestRepos.countByOutboundDeliveryRequestReturn(outboundDeliveryRequestId), 0l);
	}

	public void generateQrKeyScript() {
		deliveryRequestRepos.findIdListWithoutQrKey().forEach(id -> deliveryRequestRepos.updateQrKey(id, UtilsFunctions.generateQrKey()));
	}

	public void updateIsFullyReturned(Integer id, Boolean isFullyReturned) {
		deliveryRequestRepos.updateIsFullyReturned(id, isFullyReturned);
		evictCache();
	}

	public void updateIsFullyReturnedForExistingOutbound() {
		deliveryRequestRepos.findOutboundDeliveryRequestReturnIdList().forEach(i -> updateIsFullyReturned(i, deliveryRequestDetailService.isOutboundDeliveryRequestFullyReturned(findOne(i))));
		evictCache();
	}

	public String getTransferStatus(Integer outboundDeliveryRequestId) {
		DeliveryRequest dr = deliveryRequestRepos.findLightAssociatedInboundTransfer(outboundDeliveryRequestId);
		return dr != null ? " Transferred, Inbond " + dr.getReference() : "Pending";
	}

	public void updateNeededDeliveryDate(Integer id, Date neededDeliveryDate) {
		deliveryRequestRepos.updateNeededDeliveryDate(id, neededDeliveryDate);
		evictCache();
	}

	public void updateSmsRef(Integer id, String smsRef) {
		deliveryRequestRepos.updateSmsRef(id, smsRef);
		evictCache();
	}

	public void updateTransportationNeeded(Integer id, Boolean transportationNeeded) {
		deliveryRequestRepos.updateTransportationNeeded(id, transportationNeeded);
		evictCache();
	}

	// public void updateMissingSerialNumber(DeliveryRequest deliveryRequest) {
	// if (deliveryRequest.getIsInbound()) {
	// if
	// (deliveryRequestSerialNumberRepos.countByInboundDeliveryRequestAndEmpty(deliveryRequest.getId())
	// > 0)
	// deliveryRequest.setMissingSerialNumber(true);
	// else if
	// (deliveryRequestSerialNumberRepos.countByInboundDeliveryRequest(deliveryRequest.getId())
	// > 0)
	// deliveryRequest.setMissingSerialNumber(false);
	// } else if (deliveryRequest.getIsOutbound()) {
	//
	//
	//
	// }
	//
	// deliveryRequestRepos.updateMissingSerialNumber(deliveryRequest.getId(),
	// deliveryRequest.getMissingSerialNumber());
	// }

	public void updateMissingSerialNumber(Integer id, Boolean missingSerialNumber) {
		deliveryRequestRepos.updateMissingSerialNumber(id, missingSerialNumber);
		evictCache();
	}

	public void updateMissingExpiry(Integer id, Boolean missingExpiry) {
		deliveryRequestRepos.updateMissingExpiry(id, missingExpiry);
		evictCache();
	}

	public List<DeliveryRequest> findLightByMissingSerialNumber(List<Integer> warehouseList) {
		return deliveryRequestRepos.findLightByMissingSerialNumber(warehouseList);
	}

	public Long countByMissingSerialNumber(List<Integer> warehouseList) {
		return deliveryRequestRepos.countByMissingSerialNumber(warehouseList);
	}

	public List<DeliveryRequest> findLightByMissingExpiry(List<Integer> warehouseList) {
		return deliveryRequestRepos.findLightByMissingExpiry(warehouseList);
	}

	public Long countByMissingExpiry(List<Integer> warehouseList) {
		return deliveryRequestRepos.countByMissingExpiry(warehouseList);
	}

	public void updateIsForTransfer(Integer id, Boolean isForTransfer) {
		deliveryRequestRepos.updateIsForTransfer(id, isForTransfer);
		evictCache();
	}

	public void updateRequestDate(Integer id, Date requestDate) {
		deliveryRequestRepos.updateRequestDate(id, requestDate);
		evictCache();
	}

	public void updateRequestFrom(Integer id, String requestFrom) {
		deliveryRequestRepos.updateRequestFrom(id, requestFrom);
		evictCache();
	}

	public void updateExternalRequester(Integer id, User user) {
		deliveryRequestRepos.updateExternalRequester(id, user);
		evictCache();
	}

	public void updateSdm(Integer id, Boolean sdm) {
		deliveryRequestRepos.updateSdm(id, sdm);
		evictCache();
	}

	public void updateCountIssues(Integer id) {
		repos.updateCountIssues1(id, Arrays.asList(IssueStatus.RAISED, IssueStatus.CONFIRMED, IssueStatus.ASSIGNED, IssueStatus.NOT_RESOLVED));
		repos.updateCountIssues2(id, Arrays.asList(IssueStatus.RAISED, IssueStatus.CONFIRMED, IssueStatus.ASSIGNED, IssueStatus.NOT_RESOLVED));
		repos.updateCountIssues3(id, Arrays.asList(IssueStatus.RESOLVED, IssueStatus.ACKNOWLEDGED));
		evictCache();
	}

	public void updateCountIssuesScript() {
		repos.findIdList().forEach(id -> updateCountIssues(id));
	}

	public void updateIsSnRequired(Integer id, Boolean isSnRequired) {
		repos.updateIsSnRequired(id, isSnRequired);
		evictCache();
	}

	public void updateReturnInboundsUnitPrice(Integer outboundDeliveryRequestId) {
		DeliveryRequest outboundDeliveryRequest = findOne(outboundDeliveryRequestId);
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		outboundDeliveryRequest.getDetailList().forEach(d -> map.put(d.getPartNumberId(), d.getUnitPrice()));
		System.out.println(map);

		// update inbound delivery details
		map.forEach((pnId, unitPrice) -> deliveryRequestDetailRepos.updateUnitPriceByPartNumberAndOutboundDeliveryRequestReturn(unitPrice, pnId, outboundDeliveryRequestId));

		// update inbound stock row
//		map.forEach((pnId, unitPrice) -> stockRowRepos.updateUnitPriceByPartNumberAndOutboundDeliveryRequestReturn(unitPrice, pnId, outboundDeliveryRequestId));
	}

	public List<DeliveryRequest> findByMissingOutboundDeliveryNoteFile(String username, Collection<Integer> warehouseList, Collection<Integer> projectIdList) {
		return repos.findByMissingOutboundDeliveryNoteFile(username, warehouseList, projectIdList);
	}

	@Cacheable(value = "deliveryRequestService.countByMissingOutboundDeliveryNoteFile")
	public Long countByMissingOutboundDeliveryNoteFile(String username, Collection<Integer> warehouseList, Collection<Integer> projectIdList) {
		return repos.countByMissingOutboundDeliveryNoteFile(username, warehouseList, projectIdList);
	}

	public List<DeliveryRequest> findByMissingOutboundDeliveryNoteFileAndDeliverToSupplier(Integer supplierId, Collection<Integer> projectIdList) {
		return repos.findByMissingOutboundDeliveryNoteFileAndDeliverToSupplier(supplierId, projectIdList);
	}

	@Cacheable(value = "deliveryRequestService.countByMissingOutboundDeliveryNoteFileAndDeliverToSupplier")
	public Long countByMissingOutboundDeliveryNoteFileAndDeliverToSupplier(Integer supplierId, Collection<Integer> projectIdList) {
		return repos.countByMissingOutboundDeliveryNoteFileAndDeliverToSupplier(supplierId, projectIdList);
	}

	public List<DeliveryRequest> findByMissingOutboundDeliveryNoteFileAndDeliverToCustomer(Integer customerId, Collection<Integer> projectIdList) {
		return repos.findByMissingOutboundDeliveryNoteFileAndDeliverToCustomer(customerId, projectIdList);
	}

	@Cacheable(value = "deliveryRequestService.countByMissingOutboundDeliveryNoteFileAndDeliverToCustomer")
	public Long countByMissingOutboundDeliveryNoteFileAndDeliverToCustomer(Integer customerId, Collection<Integer> projectIdList) {
		return repos.countByMissingOutboundDeliveryNoteFileAndDeliverToCustomer(customerId, projectIdList);
	}

	public void clearBoqMapping(DeliveryRequest deliveryRequest) {
		Set<Integer> boqListToUpdate = boqService.getAssociatedBoqIdListWithDeliveryRequest(deliveryRequest.getId());
		deliveryRequest.clearBoqMappingList();
		save(deliveryRequest);
		boqService.updateTotalUsedQuantity(boqListToUpdate);
		deliveryRequestDetailService.clearPurchaseCostByDeliveryRequest(deliveryRequest.getId());
		if (deliveryRequest.getPo() != null) {
			poService.updateIlogisticsStatus(deliveryRequest.getPo().getId());
			poService.updateGoodsDeliveryStatus(deliveryRequest.getPo().getId());
		}

	}

	public DeliveryRequestStatus findStatusById(Integer id) {
		return repos.findStatusById(id);
	}

	public void updateHardwareSwapInboundIdAndStatus(Integer outboundId, Integer inboundId, DeliveryRequestStatus inboundStatus) {
		evictCache();
		repos.updateHardwareSwapInboundIdAndStatus(outboundId, inboundId, inboundStatus);
	}

	public Long countByHardwareSwapInboundId(Integer inboundId) {
		return repos.countByHardwareSwapInboundId(inboundId);
	}

	// mobile

	public String getOwnerName(String company, String customer, String supplier, CompanyType type) {
		if (type == null)
			return null;
		switch (type) {
		case COMPANY:
			return company;

		case CUSTOMER:
			return customer;

		case SUPPLIER:
			return supplier;
		default:
			return null;
		}
	}

	public ma.azdad.mobile.model.DeliveryRequest findOneLightMobile(Integer id) {
		ma.azdad.mobile.model.DeliveryRequest dnm = repos.findOneLightMobile(id);
		DeliveryRequest dn = repos.findById(id).get();
		if (dn.getToUserUsername() != null) {
			dn.setToUser(userService.findOne(dn.getToUserUsername()));
			dnm.setToUser(dn.getToUserFullName());
			dnm.setToUserInternal(dn.getToUser().getInternal());
		}
		if (dn.getDeliverToEntityName() != null) {
			dnm.setToCompany(dn.getDeliverToEntityName());
			dnm.setToCompanyLogo(dn.getDeliverToCompanyLogo());
		}
		dnm.setShowExpiryData(dn.showExpiryData());

		dnm.setOwnerName(getOwnerName(dn.getCompanyName(), dn.getCustomerName(), dn.getSupplierName(), dn.getOwnerType()));
		dnm.setHistoryList(repos.findHistoryListMobile(id));
		List<PackingDetail> packingList = dn.getPackingDetailSummaryList();
		for (PackingDetail packingDetail : packingList) {
			dnm.getPackingDetailList().add(new ma.azdad.mobile.model.PackingDetail(packingDetail.getId(), packingDetail.getTypeImage(), packingDetail.getLength(), packingDetail.getWidth(),
					packingDetail.getHeight(), packingDetail.getQuantity(), packingDetail.getVolume(), packingDetail.getGrossWeight()));
		}

		// dnm.setFileList(dn.getFileList());
		dnm.setDetailList(deliveryRequestDetailRepos.findByDeliveryRequestMobile(id));
		if (dn.getRequester() != null) {
			dnm.setUser1(new ma.azdad.mobile.model.User(dn.getRequester().getUsername(), dn.getRequester().getFirstName(), dn.getRequester().getLastName(), dn.getRequester().getLogin(),
					dn.getRequester().getPhoto(), dn.getRequester().getEmail()));
			dnm.setDate1(dn.getDate1());
			dnm.setUser2(new ma.azdad.mobile.model.User(dn.getRequester().getUsername(), dn.getRequester().getFirstName(), dn.getRequester().getLastName(), dn.getRequester().getLogin(),
					dn.getRequester().getPhoto(), dn.getRequester().getEmail()));
			if (dn.getDate2() != null)
				dnm.setDate2(dn.getDate2());
		}
		if (dn.getUser3() != null)
			dnm.setUser3(new ma.azdad.mobile.model.User(dn.getUser3().getUsername(), dn.getUser3().getFirstName(), dn.getUser3().getLastName(), dn.getUser3().getLogin(), dn.getUser3().getPhoto(),
					dn.getUser3().getEmail()));
		if (dn.getDate3() != null)
			dnm.setDate3(dn.getDate3());
		if (dn.getUser4() != null)
			dnm.setUser4(new ma.azdad.mobile.model.User(dn.getUser4().getUsername(), dn.getUser4().getFirstName(), dn.getUser4().getLastName(), dn.getUser4().getLogin(), dn.getUser4().getPhoto(),
					dn.getUser4().getEmail()));
		if (dn.getDate4() != null)
			dnm.setDate4(dn.getDate4());
		if (dn.getUser5() != null)
			dnm.setUser5(new ma.azdad.mobile.model.User(dn.getUser5().getUsername(), dn.getUser5().getFirstName(), dn.getUser5().getLastName(), dn.getUser5().getLogin(), dn.getUser5().getPhoto(),
					dn.getUser5().getEmail()));
		if (dn.getDate5() != null)
			dnm.setDate5(dn.getDate5());
		if (dn.getUser8() != null)
			dnm.setUser8(new ma.azdad.mobile.model.User(dn.getUser8().getUsername(), dn.getUser8().getFirstName(), dn.getUser8().getLastName(), dn.getUser8().getLogin(), dn.getUser8().getPhoto(),
					dn.getUser8().getEmail()));
		if (dn.getDate8() != null)
			dnm.setDate8(dn.getDate8());
		return dnm;
	}

	public List<ma.azdad.mobile.model.DeliveryRequest> findLightByWarehouseListMobile(List<Integer> warehouseList) {
		if (warehouseList.isEmpty())
			return new ArrayList<>();
		List<ma.azdad.mobile.model.DeliveryRequest> list = deliveryRequestRepos.findLightByWarehouseListMobile(warehouseList,
				Arrays.asList(DeliveryRequestStatus.APPROVED2, DeliveryRequestStatus.PARTIALLY_DELIVRED), DeliveryRequestType.XBOUND);
		Collections.sort(list, new Comparator<ma.azdad.mobile.model.DeliveryRequest>() {
			@Override
			public int compare(ma.azdad.mobile.model.DeliveryRequest o1, ma.azdad.mobile.model.DeliveryRequest o2) {
				return o1.getNeededDeliveryDate().compareTo(o2.getNeededDeliveryDate());
			}
		});
		return list;
	}

	public List<ma.azdad.mobile.model.DeliveryRequest> findLightNewByWarehouseListMobile(List<Integer> warehouseList) {
		if (warehouseList.isEmpty())
			return new ArrayList<>();
		return deliveryRequestRepos.findLightNewByWarehouseListMobile(warehouseList, Arrays.asList(DeliveryRequestStatus.APPROVED1, DeliveryRequestStatus.REQUESTED, DeliveryRequestStatus.EDITED),
				DeliveryRequestType.XBOUND);
	}

	public List<ma.azdad.mobile.model.DeliveryRequest> findLightDeliveredByWarehouseListMobile(List<Integer> warehouseList) {
		if (warehouseList.isEmpty())
			return new ArrayList<>();
		return deliveryRequestRepos.findLightDeliveredByWarehouseListMobile(warehouseList, Arrays.asList(DeliveryRequestStatus.DELIVRED), DeliveryRequestType.XBOUND);
	}

	public List<ma.azdad.mobile.model.DeliveryRequest> findLightByMissingSerialNumberMobile(List<Integer> warehouseList) {
		if (warehouseList.isEmpty())
			return new ArrayList<>();
		return deliveryRequestRepos.findLightByMissingSerialNumberMobile(warehouseList);
	}

	public List<ma.azdad.mobile.model.DeliveryRequest> findLightByMissingExpiryMobile(List<Integer> warehouseList) {
		if (warehouseList.isEmpty())
			return new ArrayList<>();
		return deliveryRequestRepos.findLightByMissingExpiryMobile(warehouseList);
	}

	public List<ma.azdad.mobile.model.DeliveryRequest> findByMissingOutboundDeliveryNoteFilemobile(String username, Collection<Integer> warehouseList) {
		List<Integer> projectIdList = projectService.findAssignedProjectIdListByResource(username);
		return repos.findByMissingOutboundDeliveryNoteFileMobile(username, warehouseList, projectIdList);
	}

	public Long countByMissingSerialNumberMobile(List<Integer> warehouseList) {
		return deliveryRequestRepos.countByMissingSerialNumberMobile(warehouseList);
	}

	public Long countByMissingExpiryMobile(List<Integer> warehouseList) {
		return deliveryRequestRepos.countByMissingExpiryMobile(warehouseList);
	}

	public Long countByMissingOutboundDeliveryNoteFileMobile(String username, Collection<Integer> warehouseList) {
		List<Integer> projectIdList = projectService.findAssignedProjectIdListByResource(username);
		return deliveryRequestRepos.countByMissingOutboundDeliveryNoteFileMobile(username, warehouseList, projectIdList);
	}

	public Dashboard getDashboard(String username, List<Integer> warehouseList) {

		Dashboard dashboard = new Dashboard();
		dashboard.setMissingSn(countByMissingSerialNumberMobile(warehouseList));
		dashboard.setMissingExpiry(countByMissingExpiryMobile(warehouseList));
		dashboard.setMissingBl(countByMissingOutboundDeliveryNoteFileMobile(username, warehouseList));
		return dashboard;

	}

	public void handleOut(Integer id, String username) {
		DeliveryRequest deliveryRequest = findOne(id);
		deliveryRequest.setStockRowList(stockRowService.generateStockRowFromOutboundDeliveryRequest(deliveryRequest));
		deliveryRequest.setToUser(userService.findOne(deliveryRequest.getToUserUsername()));
		if (checkDatabaseStatus(deliveryRequest.getId(), DeliveryRequestStatus.DELIVRED)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DN already Delivered !");
		}
		deliveryRequest.setStatus(DeliveryRequestStatus.DELIVRED);
		deliveryRequest.setDate4(new Date());
		User user = userService.findByUsername(username);
		deliveryRequest.setUser4(user);
		deliveryRequest.addHistory(new DeliveryRequestHistory(DeliveryRequestStatus.DELIVRED.getValue(), user));
		deliveryRequest = save(deliveryRequest);
		emailService.deliveryRequestNotification(deliveryRequest);
		smsService.sendSms(deliveryRequest);

		projectCrossService.addCrossCharge(deliveryRequest);
		stockRowService.updateOwnerId(deliveryRequest.getId());
		stockRowService.updateInboundOwnerId(deliveryRequest.getId());

//		if (deliveryRequest.getCustomer() != null)
//			customerService.updateIsStockEmpty(deliveryRequest.getCustomer().getId());

		if (deliveryRequest.getPo() != null) {
			// poService.updateIlogisticsStatus(deliveryRequest.getPo().getId());
			poService.updateGoodsDeliveryStatus(deliveryRequest.getPo().getId());
		}

		// update field missing sn
		deliveryRequest = findOne(deliveryRequest.getId());
		if (deliveryRequest.getStockRowList().stream().filter(i -> deliveryRequestSerialNumberService.countByPartNumberAndInboundDeliveryRequest(i.getId(), i.getInboundDeliveryRequest().getId()) > 0)
				.count() > 0)
			updateMissingSerialNumber(deliveryRequest.getId(), true);
		// update is missing expiry
		if (deliveryRequest.getStockRowList().stream().filter(i -> i.getPartNumber().getExpirable()).count() > 0)
			updateMissingExpiry(deliveryRequest.getId(), true);

	}

	public Boolean checkDatabaseStatus(Integer id, DeliveryRequestStatus status) {
		return status.equals(findStatusById(id));
	}

	public void handleIn(List<HardwareStatusData> list, Integer id, String category, String username) {
		DeliveryRequest deliveryRequest = findOne(id);
		if (checkDatabaseStatus(deliveryRequest.getId(), DeliveryRequestStatus.DELIVRED))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DN already Delivered !");

		for (HardwareStatusData hardwareStatusData : list) {
			DeliveryRequestDetail detail = deliveryRequestDetailService.findOne(hardwareStatusData.getDetailId());
			detail.setRemainingQuantity(detail.getRemainingQuantity() - hardwareStatusData.getQuantity());
			detail = deliveryRequestDetailService.save(detail);
			DeliveryRequestDetail inboundDeliveryRequestDetail = deliveryRequestDetailService.findByDeliveryRequestAndPartNumber(deliveryRequest.getId(), detail.getPartNumber().getId()).get(0);
			StockRow row = new StockRow(detail, hardwareStatusData.getQuantity(), hardwareStatusData.getQuantity(), detail.getPartNumber(), deliveryRequest, true, deliveryRequest.getOriginNumber(),
					deliveryRequest, inboundDeliveryRequestDetail, new Date(), detail.getPacking());
			if (category.equals("Normal")) {
				row.setState(StockRowState.NORMAL);
			} else {
				row.setState(StockRowState.FAULTY);
			}
			row.setStatus(StockRowStatus.getByValue(hardwareStatusData.getStatus()));
			row.setLocation(locationService.findOne(hardwareStatusData.getLocation().getId()));
			deliveryRequest.getStockRowList().add(row);
		}

		if (checkDatabaseStatus(deliveryRequest.getId(), DeliveryRequestStatus.PARTIALLY_DELIVRED)) {
			// Part Number / Qty maps
			Map<Integer, Double> deliveryRequestDetailMap = deliveryRequest.getDetailList().stream()
					.collect(Collectors.groupingBy(DeliveryRequestDetail::getPartNumberId, Collectors.summingDouble(DeliveryRequestDetail::getQuantity)));
			Map<Integer, Double> stockRowExistingMap = stockRowService.findQuantityPartNumberMapByDeliveryRequest(deliveryRequest.getId());
			Map<Integer, Double> stockRowNewMap = deliveryRequest.getStockRowList().stream().filter(i -> i.getId() == null)
					.collect(Collectors.groupingBy(StockRow::getPartNumberId, Collectors.summingDouble(StockRow::getQuantity)));

			for (Integer partNumberId : deliveryRequestDetailMap.keySet()) {
				Double detailQuantity = deliveryRequestDetailMap.get(partNumberId);
				Double existingQuantity = stockRowExistingMap.getOrDefault(partNumberId, 0.0);
				Double newQuantity = stockRowNewMap.getOrDefault(partNumberId, 0.0);
				if (detailQuantity < existingQuantity + newQuantity) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Remaining quantity error, probably concurrent access, please to reload this page");

				}
			}
		}

		deliveryRequest = findOne(id);
		deliveryRequest.setStatus(DeliveryRequestStatus.DELIVRED);
		for (DeliveryRequestDetail detail : deliveryRequest.getDetailList()) {
			if (detail.getRemainingQuantity() > 0) {
				deliveryRequest.setStatus(DeliveryRequestStatus.PARTIALLY_DELIVRED);
				break;
			}
		}

		deliveryRequest.setDate4(new Date());
		User user = userService.findByUsername(username);
		deliveryRequest.setUser4(user);
		deliveryRequest.addHistory(new DeliveryRequestHistory(deliveryRequest.getStatus().getValue(), user));
		save(deliveryRequest);

		stockRowService.updateOwnerId(deliveryRequest.getId());
		stockRowService.updateInboundOwnerId(deliveryRequest.getId());

		deliveryRequest = findOne(deliveryRequest.getId());

		if (deliveryRequest.getPo() != null) {
			// poService.updateIlogisticsStatus(deliveryRequest.getPo().getId());
			poService.updateGoodsDeliveryStatus(deliveryRequest.getPo().getId());
		}

		if (deliveryRequest.getIsSnRequired())
			generateSerialNumberList(deliveryRequest);

		if (DeliveryRequestStatus.DELIVRED.equals(deliveryRequest.getStatus()))
			projectCrossService.addCrossChargeForReturnFromOutbound(deliveryRequest);

		if (deliveryRequest.getIsInboundReturn()) {
			updateIsFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn().getId(),
					deliveryRequestDetailService.isOutboundDeliveryRequestFullyReturned(deliveryRequest.getOutboundDeliveryRequestReturn()));
			updateReturnInboundsUnitPrice(deliveryRequest.getOutboundDeliveryRequestReturn().getId());
			DeliveryRequest outboundDeliveryRequestReturn = findOne(deliveryRequest.getOutboundDeliveryRequestReturn().getId());
			clearBoqMapping(outboundDeliveryRequestReturn);
			jobRequestDeliveryDetailService.deleteByDeliveryRequestAndNotStartedJobRequest(deliveryRequest.getOutboundDeliveryRequestReturn(), deliveryRequest, user);
		}

		emailService.deliveryRequestNotification(deliveryRequest);
		smsService.sendSms(deliveryRequest);

		if (deliveryRequest.getStockRowList().stream().filter(i -> i.getPartNumber().getExpirable()).count() > 0)
			updateMissingExpiry(deliveryRequest.getId(), true);

		if (deliveryRequest.getIsInboundReturnFromOutboundHardwareSwap())
			updateHardwareSwapInboundIdAndStatus(deliveryRequest.getOutboundDeliveryRequestReturnId(), deliveryRequest.getId(), deliveryRequest.getStatus());

	}

	private void generateSerialNumberList(DeliveryRequest deliveryRequest) {
		Map<String, Integer> map = new HashMap<>();

		for (Integer stockRowId : deliveryRequest.getStockRowList().stream().map(i -> i.getId()).collect(Collectors.toList())) {
			StockRow inboundStockRow = stockRowService.findOne(stockRowId);
			for (PackingDetail packingDetail : inboundStockRow.getPacking().getDetailList()) {
				if (!packingDetail.getHasSerialnumber())
					continue;

				map.putIfAbsent(inboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId(), 0);

				int packingQuantity = (int) (inboundStockRow.getQuantity() / packingDetail.getParent().getQuantity());
				int n = packingDetail.getQuantity();
				for (int i = 0; i < packingQuantity; i++) {
					int packingNumero = map.get(inboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId()) + 1;
					map.put(inboundStockRow.getPartNumber().getId() + ";" + packingDetail.getId(), packingNumero);
					for (int j = 0; j < n; j++)
						deliveryRequestSerialNumberService.save(new DeliveryRequestSerialNumber(packingNumero, packingDetail, inboundStockRow));
				}
			}
		}

		if (!map.isEmpty())
			updateMissingSerialNumber(deliveryRequest.getId(), true);

	}

	public void updateOutboundInboundPo(Integer outboundId) {
		DeliveryRequest deliveryRequest = findOne(outboundId);
		if (deliveryRequest.getDetailList().stream().filter(i -> i.getInboundDeliveryRequest().getPo() == null).count() == 0 //
				&& deliveryRequest.getDetailList().stream().map(i -> i.getInboundDeliveryRequest().getPo().getId()).distinct().count() == 1)
			deliveryRequest.setInboundPo(poService.findOneLight(deliveryRequest.getDetailList().get(0).getInboundDeliveryRequest().getPo().getId()));
		repos.save(deliveryRequest);
	}

	public void updateOutboundInboundPoScript() {
		repos.findByOutboundWithoutInboundPoId().forEach(id -> updateOutboundInboundPo(id));
	}

	public void adjustQuantityScript() {
		repos.findByStatus(DeliveryRequestStatus.PARTIALLY_DELIVRED).forEach(i -> adjustQuantity(i, i.getRequester()));
	}

	public Boolean canAdjustQuantity(DeliveryRequest deliveryRequest, User connectedUser) {
		return DeliveryRequestStatus.PARTIALLY_DELIVRED.equals(deliveryRequest.getStatus()) //
				&& connectedUser.getUsername().equalsIgnoreCase(deliveryRequest.getRequester().getUsername()) //
				&& deliveryRequest.getBoqMappingList().isEmpty() //
				&& jobRequestDeliveryDetailService.countByDeliveryRequest(deliveryRequest.getId()) == 0;
	}

	public void adjustQuantity(DeliveryRequest deliveryRequest, User connectedUser) {
		if(!canAdjustQuantity(deliveryRequest, connectedUser))
			return;
		
		System.out.println(deliveryRequest.getReference());
		System.out.println(deliveryRequest.getId());
		System.out.println(jobRequestDeliveryDetailService.countByDeliveryRequest(deliveryRequest.getId()));
		System.out.println("---------------------------------");
		
		Iterator<DeliveryRequestDetail> it = deliveryRequest.getDetailList().iterator();
		while (it.hasNext()) {
			DeliveryRequestDetail detail = it.next();
			Double srQuantity = deliveryRequest.getStockRowList().stream().filter(i -> i.getDeliveryRequestDetail().getId().equals(detail.getId())).mapToDouble(i -> i.getQuantity()).sum();
			detail.setQuantity(srQuantity);
			detail.setRemainingQuantity(0.0);
			if (srQuantity.equals(0.0)) {
				detail.setDeliveryRequest(null);
				it.remove();
			}
		}
		deliveryRequest.setIsPartial(false);
		deliveryRequest.setStatus(DeliveryRequestStatus.DELIVRED);
		deliveryRequest.addHistory(new DeliveryRequestHistory("Adjust Quantity", connectedUser));
		save(deliveryRequest);
	}

	// mobile

	public void handleFileUpload(FileUploadEvent event, User user, Integer id, String fileType) throws IOException {
		DeliveryRequest deliveryRequest = findOne(id);

		File file = fileUploadService.handleFileUploadMobile(event, "deliveryRequest");
		DeliveryRequestFile modelFile = new DeliveryRequestFile(file, fileType, event.getFile().getFileName(), user);

		deliveryRequest.addFile(modelFile);
		deliveryRequest.calculateMissingOutboundDeliveryNote();
		deliveryRequest.calculateCountFiles();
		deliveryRequest = saveAndRefresh(deliveryRequest);

	}

	public void deleteFile(Integer idDn, Integer idFile) {
		DeliveryRequest deliveryRequest = findOne(idDn);
		DeliveryRequestFile deliveryRequestFile = deliveryRequestFileRepos.findById(idFile).get();

		deliveryRequest.removeFile(deliveryRequestFile);
		deliveryRequest.calculateMissingOutboundDeliveryNote();
		deliveryRequest = saveAndRefresh(deliveryRequest);
	}

	public List<ma.azdad.mobile.model.DeliveryRequestFile> findDnAttachments(Integer id) {
		DeliveryRequest deliveryRequest = findOne(id);
		List<ma.azdad.mobile.model.DeliveryRequestFile> list = new ArrayList<>();
		for (DeliveryRequestFile dnFile : deliveryRequest.getFileList()) {
			list.add(new ma.azdad.mobile.model.DeliveryRequestFile(dnFile.getId(), dnFile.getDate(), dnFile.getLink(), dnFile.getExtension(), dnFile.getType(), dnFile.getSize(), dnFile.getName()));

		}
		return list;
	}

	public List<ma.azdad.mobile.model.DeliveryRequestExpiryDate> findDnExpiry(Integer id) {
		List<DeliveryRequestExpiryDate> list = deliveryRequestExpiryDateService.findByDeliveryRequest(id);
		System.out.println("expiry1 size :" + list.size());
		DeliveryRequest dn = findOne(id);
		if (list.isEmpty() && dn.getIsInbound()) {
			dn.getStockRowList().stream().filter(i -> i.getPartNumber().getExpirable()).forEach(i -> list.add(new DeliveryRequestExpiryDate(i, true)));
			list.stream().forEach(i -> deliveryRequestExpiryDateService.save(i));
			updateMissingExpiry(dn.getId(), false);
		}
		if (dn.getIsOutbound()) {
			Map<Integer, Double> map = list.stream().collect(Collectors.groupingBy(DeliveryRequestExpiryDate::getStockRowId, Collectors.summingDouble(DeliveryRequestExpiryDate::getQuantity)));
			dn.getStockRowList().stream().filter(i -> i.getPartNumber().getExpirable() && -i.getQuantity() > map.getOrDefault(i.getId(), 0.0))
					.forEach(i -> list.add(new DeliveryRequestExpiryDate(i, true, deliveryRequestExpiryDateService.findOneExpiryDate(i))));
			list.stream().filter(i -> i.getId() == null).forEach(i -> deliveryRequestExpiryDateService.save(i));
			if (list.size() == list.stream().filter(i -> i.getExpiryDate() != null).count())
				updateMissingExpiry(dn.getId(), false);
		}
		List<DeliveryRequestExpiryDate> list2 = deliveryRequestExpiryDateService.findByDeliveryRequest(id);
		System.out.println("expiry2 size :" + list2.size());
		List<ma.azdad.mobile.model.DeliveryRequestExpiryDate> mobileList = new ArrayList<>();
		for (DeliveryRequestExpiryDate e : list2) {
			mobileList.add(new ma.azdad.mobile.model.DeliveryRequestExpiryDate(e.getId(), e.getStockRowId(), e.getStockRow().getPartNumberImage(), e.getStockRow().getPartNumberName(),
					e.getStockRow().getLocation().getName(), e.getQuantity(), e.getExpiryDate(), e.getStockRow().getStatusValue()));
		}
		return mobileList;
	}

	public List<Date> findRemainingExpiryDateList(Integer id) {
		StockRow outboundStockRow = stockRowService.findOne(id);
		return deliveryRequestExpiryDateService.findRemainingExpiryDateList(outboundStockRow);
	}

	public void updateDnExpiryById(ma.azdad.mobile.model.DeliveryRequestExpiryDate expiry, Integer id) {
		if (expiry.getExpiryDate() == null)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Expiry date could not be null");
		if (deliveryRequestExpiryDateService.countByDeliveryRequestAndDate(id, expiry.getExpiryDate()) > 0)
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Date already exist in the same stock row");
		DeliveryRequestExpiryDate exp = deliveryRequestExpiryDateService.findOne(expiry.getId());
		exp.setQuantity(expiry.getQuantity());
		exp.setExpiryDate(expiry.getExpiryDate());
		deliveryRequestExpiryDateService.save(exp);

	}

	public void createDnExpiryById(ma.azdad.mobile.model.DeliveryRequestExpiryDate expiry) {

		DeliveryRequestExpiryDate exp = new DeliveryRequestExpiryDate();
		StockRow sr = stockRowService.findOne(expiry.getStockRowId());
		exp.setQuantity(expiry.getQuantity());
		exp.setStockRow(sr);
		deliveryRequestExpiryDateService.save(exp);

	}

	public List<ma.azdad.mobile.model.DeliveryRequestSerialNumber> findSnByDnId(Integer id) {
		List<DeliveryRequestSerialNumber> list = deliveryRequestSerialNumberService.findByDeliveryRequest(id);
		List<ma.azdad.mobile.model.DeliveryRequestSerialNumber> mobileList = new ArrayList<>();
		for (DeliveryRequestSerialNumber sn : list) {
			mobileList.add(new ma.azdad.mobile.model.DeliveryRequestSerialNumber(sn.getId(), sn.getPackingDetail().getParent().getName(), sn.getPackingDetail().getParent().getPartNumber().getName(),
					sn.getSerialNumber(), sn.getPackingDetail().getParent().getPartNumber().getImage(), sn.getPackingDetail().getSnType(), sn.getPackingNumero(),
					sn.getInboundStockRow().getStatusValue(), sn.getInboundStockRow().getLocation().getName()));
		}
		return mobileList;
	}

}
