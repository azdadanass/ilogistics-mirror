package ma.azdad.service;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
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

import ma.azdad.model.BoqMapping;
import ma.azdad.model.DeliverToType;
import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestDetail;
import ma.azdad.model.DeliveryRequestState;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.DeliveryRequestType;
import ma.azdad.model.InboundType;
import ma.azdad.model.IssueStatus;
import ma.azdad.model.PartNumber;
import ma.azdad.model.Po;
import ma.azdad.model.Project;
import ma.azdad.model.ProjectTypes;
import ma.azdad.model.User;
import ma.azdad.repos.AppLinkRepos;
import ma.azdad.repos.DeliveryRequestDetailRepos;
import ma.azdad.repos.DeliveryRequestRepos;
import ma.azdad.repos.DeliveryRequestSerialNumberRepos;
import ma.azdad.repos.StockRowRepos;
import ma.azdad.utils.App;

@Component
public class DeliveryRequestService extends GenericService<Integer, DeliveryRequest, DeliveryRequestRepos> {

	@Autowired
	DeliveryRequestRepos deliveryRequestRepos;

	@Autowired
	AppLinkRepos appLinkRepos;

	@Autowired
	ProjectService projectService;

	@Autowired
	PoService poService;

	@Autowired
	DeliveryRequestDetailRepos deliveryRequestDetailRepos;

	@Autowired
	StockRowRepos stockRowRepos;

	@Autowired
	DeliveryRequestDetailService deliveryRequestDetailService;

	@Autowired
	DeliveryRequestSerialNumberRepos deliveryRequestSerialNumberRepos;

	@Autowired
	TransportationRequestService transportationRequestService;

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
		if (deliveryRequest.getProject() != null)
			Hibernate.initialize(deliveryRequest.getProject().getManager());
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

	public List<DeliveryRequest> findByMissingPo(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		List<DeliveryRequest> result = deliveryRequestRepos.findByMissingPo(username, warehouseList, assignedProjectList, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.CANCELED, DeliveryRequestStatus.REJECTED));

		result.forEach(i -> {
			i.setTotalCost(deliveryRequestRepos.getTotalCost(i.getId()));
			i.setTotalPrice(deliveryRequestRepos.getTotalPrice(i.getId()));
			i.setEndCustomerName(deliveryRequestRepos.getEndCustomerName(i.getId()));
		});

		return result;
	}

	public Long countByMissingPo(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		return deliveryRequestRepos.countByMissingPo(username, warehouseList, assignedProjectList, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.CANCELED, DeliveryRequestStatus.REJECTED));
	}

	public List<DeliveryRequest> findByMissingBoqMapping(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		return deliveryRequestRepos.findByMissingBoqMapping(username, warehouseList, assignedProjectList, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.CANCELED, DeliveryRequestStatus.REJECTED));
	}

	public Long countByMissingBoqMapping(String username, List<Integer> warehouseList, List<Integer> assignedProjectList) {
		return deliveryRequestRepos.countByMissingBoqMapping(username, warehouseList, assignedProjectList, DeliveryRequestType.OUTBOUND,
				Arrays.asList(DeliveryRequestStatus.CANCELED, DeliveryRequestStatus.REJECTED));
	}

	@Cacheable(value = "deliveryRequestService.findLightByRequester")
	public List<DeliveryRequest> findLightByRequester(String username, DeliveryRequestType type, DeliveryRequestStatus status) {
		return deliveryRequestRepos.findLightByRequester(type, username, status);
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
		return deliveryRequestRepos.countByIsForReturnAndNotFullyReturned(DeliveryRequestType.OUTBOUND, username,
				Arrays.asList(DeliveryRequestStatus.DELIVRED, DeliveryRequestStatus.ACKNOWLEDGED));
	}

	public List<DeliveryRequest> findLightByPendingTransportation(String username) {
		return deliveryRequestRepos.findLightByPendingTransportation(username, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	public Long countByPendingTransportation(String username) {
		return deliveryRequestRepos.countByPendingTransportation(username, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	@Cacheable(value = "deliveryRequestService.findLightToApprove")
	public List<DeliveryRequest> findLightToApprove(String username) {
		List<DeliveryRequest> result = new ArrayList<DeliveryRequest>();
		result.addAll(deliveryRequestRepos.findLightToApprovePm(username, DeliveryRequestStatus.REQUESTED));
		result.addAll(deliveryRequestRepos.findLightToApproveHm(username, DeliveryRequestStatus.APPROVED1));
		return result;
	}

	@Cacheable(value = "deliveryRequestService.countToApprove")
	public Long countToApprove(String username) {
		return deliveryRequestRepos.countToApprovePm(username, DeliveryRequestStatus.REQUESTED) + deliveryRequestRepos.countToApproveHm(username, DeliveryRequestStatus.APPROVED1);
	}

	@Cacheable(value = "deliveryRequestService.findLightByWarehouseList")
	public List<DeliveryRequest> findLightByWarehouseList(List<Integer> warehouseList, DeliveryRequestStatus status) {
		return deliveryRequestRepos.findLightByWarehouseList(warehouseList, status, DeliveryRequestType.XBOUND);
	}

	@Cacheable(value = "deliveryRequestService.countByWarehouseList")
	public Long countByWarehouseList(List<Integer> warehouseList, DeliveryRequestStatus status) {
		return deliveryRequestRepos.countByWarehouseList(warehouseList, status, DeliveryRequestType.XBOUND);
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

		String kindly = (DeliveryRequestStatus.REQUESTED.equals(deliveryRequest.getStatus())
				? "Kindly be informed that the resource " + deliveryRequest.getRequester().getFullName() + " has raised a new delivery request on Ilogistics pending your approval"
				: "Kindly be informed that the delivery request below has been <b style='color:" + deliveryRequest.getStatus().getColorCode() + "'>"
						+ deliveryRequest.getStatus().getValue())
				+ (DeliveryRequestStatus.DELIVRED.equals(deliveryRequest.getStatus()) ? deliveryRequest.getIsInbound() ? " to warehouse"
						: deliveryRequest.getDestination() != null ? " to " + deliveryRequest.getDestination().getName() : " to site" : "")
				+ "</b> <br/><br/>";

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

		Table table = (Table) new Table()
				.setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto;font-family: 'Arial'; font-size: 12px;");
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
			row.addElement(new TD(deliveryRequest.getDestinationProject() != null ? deliveryRequest.getDestinationProject().getName() : "").setWidth(tdWidth2)
					.setStyle(tdStyle2 + ("color: #c6699f")));
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
		row.addElement(new TD(deliveryRequest.getTransportationRequest() != null ? deliveryRequest.getTransportationRequest().getReference() : "").setWidth(tdWidth2)
				.setStyle(tdStyle2 + "color:#478fca"));
		row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
		row.addElement(new TD("TR Status").setWidth(tdWidth1).setStyle(tdStyle1));
		row.addElement(new TD(deliveryRequest.getTransportationRequest() != null ? deliveryRequest.getTransportationRequest().getStatus().getValue() : "").setWidth(tdWidth2)
				.setStyle(tdStyle2));
		table.addElement(row);

		body.addElement(table);

		body.addElement(new H4("Delivery Details").setStyle("color:#478fca;font-size:17px;width:90%;margin:auto;margin-top:30px;margin-bottom:10px"));

		if (deliveryRequest.getIsInbound() || deliveryRequest.getIsXbound()) {
			table = (Table) new Table()
					.setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto;font-family: 'Arial'; font-size: 12px;");
			table.setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto; font-size: 12px;");

			row = new TR();
			row.addElement(new TD("Owner").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getOrigin() != null ? deliveryRequest.getOwnerName() : "").setWidth(tdWidth2).setStyle(tdStyle2));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Needed Delivery Date").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getNeededDeliveryDate() != null ? UtilsFunctions.getFormattedDateTime(deliveryRequest.getNeededDeliveryDate()) : "")
					.setWidth(tdWidth2).setStyle(tdStyle2 + "color:#a069c3"));
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
			table = (Table) new Table()
					.setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto;font-family: 'Arial'; font-size: 12px;");
			table.setStyle("border: 1px solid #ddd; border-spacing: 0; border-collapse: collapse; width: 90%; margin: auto; font-size: 12px;");

			row = new TR();
			row.addElement(new TD("End Customer").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(
					new TD(deliveryRequest.getEndCustomer() != null ? deliveryRequest.getEndCustomer().getName() : "").setWidth(tdWidth2).setStyle(tdStyle2 + "color:#ff851d"));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Needed Delivery Date").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getNeededDeliveryDate() != null ? UtilsFunctions.getFormattedDateTime(deliveryRequest.getNeededDeliveryDate()) : "")
					.setWidth(tdWidth2).setStyle(tdStyle2 + "color:#a069c3"));

			table.addElement(row);

			row = new TR();
			row.addElement(new TD("Destination Site").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getDestination() != null ? deliveryRequest.getDestination().getName() : "").setWidth(tdWidth2).setStyle(tdStyle2));
			row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
			row.addElement(new TD("Destination Address").setWidth(tdWidth1).setStyle(tdStyle1));
			row.addElement(new TD(deliveryRequest.getDestination() != null ? deliveryRequest.getDestination().getGoogleAddress() : "").setWidth(tdWidth2).setStyle(tdStyle2));
			table.addElement(row);

			if (DeliverToType.USER.equals(deliveryRequest.getDeliverToType())) {
				row = new TR();
				row.addElement(new TD("Deliver To Company").setWidth(tdWidth1).setStyle(tdStyle1));
				row.addElement(new TD("(Internal) 3gcom").setWidth(tdWidth2).setStyle(tdStyle2));
				row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
				row.addElement(new TD("Deliver to").setWidth(tdWidth1).setStyle(tdStyle1));
				row.addElement(new TD(deliveryRequest.getToUser() != null
						? deliveryRequest.getToUser().getFullName() + ", " + deliveryRequest.getToUser().getEmail() + ", " + deliveryRequest.getToUser().getPhone()
						: "").setWidth(tdWidth2).setStyle(tdStyle2));
				table.addElement(row);
			} else if (DeliverToType.EXTERNAL.equals(deliveryRequest.getDeliverToType())) {
				row = new TR();
				row.addElement(new TD("Deliver To Company").setWidth(tdWidth1).setStyle(tdStyle1));
				row.addElement(new TD(deliveryRequest.getDeliverToEntityName()).setWidth(tdWidth2).setStyle(tdStyle2));
				row.addElement(new TD().setWidth(tdWidth3).setStyle(tdStyle3));
				row.addElement(new TD("Deliver to").setWidth(tdWidth1).setStyle(tdStyle1));
				row.addElement(new TD(deliveryRequest.getToUser() != null
						? deliveryRequest.getToUser().getFullName() + ", " + deliveryRequest.getToUser().getEmail() + ", " + deliveryRequest.getToUser().getPhone()
						: "").setWidth(tdWidth2).setStyle(tdStyle2));
				table.addElement(row);
			}

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

			paragraph = new Paragraph(deliveryRequest.getReference() + " | Delivery Date : "
					+ (deliveryRequest.getDate4() != null ? UtilsFunctions.getFormattedDate(deliveryRequest.getDate4()) : ""), titleFont);
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(10f);
			document.add(paragraph);

			// qrcode Cell
			BarcodeQRCode barcodeQrcode = new BarcodeQRCode(App.QR.getLink() + "/dn/" + deliveryRequest.getId() + "/" + deliveryRequest.getQrKey(), 100, 100, null);
			Image qrcodeImage = barcodeQrcode.getImage();
			qrcodeImage.scaleToFit(95, 95);
			// qrcodeImage.scalePercent(100);

			Image logo = Image.getInstance(UtilsFunctions.path() + "resources/pdf/orange.png");
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
		return deliveryRequest.getTransportationNeeded() != null && deliveryRequest.getTransportationNeeded() && connectedUser.equals(deliveryRequest.getRequester().getUsername())
				&& deliveryRequest.getTransportationRequest() == null
				&& !Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED).contains(deliveryRequest.getStatus());
	}

	public List<DeliveryRequest> findOutboundFinancialByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
		return deliveryRequestRepos.findOutboundFinancialByCompanyOwner(username, warehouseList, assignedProjectList, companyId, ProjectTypes.STOCK.getValue(),
				DeliveryRequestType.OUTBOUND, Arrays.asList(DeliveryRequestStatus.REJECTED, DeliveryRequestStatus.CANCELED));
	}

	public List<DeliveryRequest> findInboundFinancialByCompanyOwner(String username, List<Integer> warehouseList, List<Integer> assignedProjectList, Integer companyId) {
//		return deliveryRequestRepos.findInboundFinancialByCompanyOwner(username, warehouseList, assignedProjectList, companyId, ProjectTypes.STOCK.getValue(),
//				DeliveryRequestType.INBOUND, InboundType.NEW, Arrays.asList(DeliveryRequestStatus.DELIVRED));
		return deliveryRequestRepos.findInboundFinancialByCompanyOwner(username, warehouseList, assignedProjectList, companyId,DeliveryRequestType.INBOUND, InboundType.NEW,
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

	public void updateDetailListPurchaseCostFromBoqMappingScript() {
		repos.findIdByTypeAndHavingBoqMapping(DeliveryRequestType.INBOUND).forEach(this::updateDetailListPurchaseCostFromBoqMapping);
	}

	public void updateDetailListUnitCost(Integer deliveryRequestId) {
		DeliveryRequest deliveryRequest = findOne(deliveryRequestId);
		if (!deliveryRequest.getIsInbound() || deliveryRequest.getPo() == null)
			return;
		Double conversionRate = deliveryRequest.getPo().getMadConversionRate();
		Double totalPurchaseCost = deliveryRequest.getDetailList().stream().mapToDouble(d -> d.getQuantity() * d.getPurchaseCost()).sum();
//		Double otherCosts = appLinkRepos.findTotalAmountByDeliveryRequestAndNotPo(deliveryRequest.getId(), deliveryRequest.getPo().getIdpo());
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
			Double unitCost = (purchaseCost / totalPurchaseCost) * totalAppLink; // equivalent ((purchaseCost * qty / totalPurchaseCost) * totalAppLink) /qty
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
				.forEach(i -> boqPriceMap.put(i.getPartNumberEquivalence().getPartNumber(),
						i.getBoq().getUnitPrice() * i.getPartNumberEquivalence().getDetailList().get(0).getQuantity()));

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

	public void generateQrKeyScript() {
		deliveryRequestRepos.findIdListWithoutQrKey().forEach(id -> deliveryRequestRepos.updateQrKey(id, UtilsFunctions.generateQrKey()));
	}

	public void updateIsFullyReturned(Integer id, Boolean isFullyReturned) {
		deliveryRequestRepos.updateIsFullyReturned(id, isFullyReturned);
		evictCache();
	}

	public void updateIsFullyReturnedForExistingOutbound() {
		deliveryRequestRepos.findOutboundDeliveryRequestReturnIdList()
				.forEach(i -> updateIsFullyReturned(i, deliveryRequestDetailService.isOutboundDeliveryRequestFullyReturned(findOne(i))));
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
		map.forEach((pnId, unitPrice) -> stockRowRepos.updateUnitPriceByPartNumberAndOutboundDeliveryRequestReturn(unitPrice, pnId, outboundDeliveryRequestId));
	}

}
