package ma.azdad.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.DeliveryRequestStatus;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestStatus;

@Component
public class SmsService {

	@Value("${utilsIpAddress}")
	private String utilsIpAddress;

	@Async
	public void sendSms(String numero, String message) {
		String url = "http://" + utilsIpAddress + "/sendSms";
		HashMap<String, String> params = new HashMap<>();
		params.put("numero", numero);
		params.put("message", message);
		UtilsFunctions.sendHttpRequest("POST", url, params);
	}

	@Async
	public void sendSms(Set<String> numeroSet, String message) {
		if (numeroSet != null)
			for (String numero : numeroSet)
				sendSms(numero, message);
	}

	@Async
	public void sendSms(DeliveryRequest deliveryRequest) {
		String message = "Message from iLogistics : ";
		message += deliveryRequest.getReference() + ", ";
		message += DeliveryRequestStatus.REQUESTED.equals(deliveryRequest.getStatus()) ? "requested by " + deliveryRequest.getRequester().getFullName() + ", " : "";
		message += "project " + deliveryRequest.getProject().getName() + ", ";
		message += "REF " + deliveryRequest.getSmsRef() + "\n";
		message += DeliveryRequestStatus.REQUESTED.equals(deliveryRequest.getStatus()) ? "submitted for Approval" : deliveryRequest.getStatus().getValue();
		if (DeliveryRequestStatus.DELIVRED.equals(deliveryRequest.getStatus()))
			message += deliveryRequest.getIsInbound() ? " to warehouse" : deliveryRequest.getDestination() != null ? " to " + deliveryRequest.getDestination().getName() : " to site";
		Set<String> numeroSet = new HashSet<>();

		if (deliveryRequest.getToNotifyList() != null) {
			//send to internal
			deliveryRequest.getToNotifyList().stream().filter(i -> i.getInternal() && i.getNotifyBySms() && UtilsFunctions.isValidPhoneNumber(i.getPhone()))
					.forEach(i -> numeroSet.add(i.getPhone()));

			//send to external
			if (Arrays.asList(DeliveryRequestStatus.APPROVED, DeliveryRequestStatus.PARTIALLY_DELIVRED, DeliveryRequestStatus.DELIVRED).contains(deliveryRequest.getStatus()))
				deliveryRequest.getToNotifyList().stream().filter(i -> !i.getInternal() && i.getNotifyBySms() && UtilsFunctions.isValidPhoneNumber(i.getPhone()))
						.forEach(i -> numeroSet.add(i.getPhone()));
		}

		numeroSet.add(deliveryRequest.getRequester().getPhone());
		numeroSet.add(deliveryRequest.getProject().getManager().getPhone());

		sendSms(numeroSet, message);

		System.out.println("------------------------------------------------------");
		System.out.println("try sendSms : " + message);
		System.out.println("to : " + numeroSet);
		System.out.println("------------------------------------------------------");
	}

	@Async
	public void sendSms(TransportationRequest transportationRequest) {
		String message = "Message from iLogistics : ";
		message += transportationRequest.getReference() + ", ";
		message += TransportationRequestStatus.REQUESTED.equals(transportationRequest.getStatus())
				? "requested by " + transportationRequest.getDeliveryRequest().getRequester().getFullName() + ", " : "";
		message += "project " + transportationRequest.getDeliveryRequest().getProject().getName() + ", ";
		message += "REF " + transportationRequest.getDeliveryRequest().getSmsRef() + "\n";
		message += TransportationRequestStatus.REQUESTED.equals(transportationRequest.getStatus()) ? "submitted for Approval" : transportationRequest.getStatus().getValue();
		Set<String> numeroSet = new HashSet<>();

		if (transportationRequest.getDeliveryRequest().getToNotifyList() != null) {
			//send to internal
			transportationRequest.getDeliveryRequest().getToNotifyList().stream().filter(i -> i.getInternal() && i.getNotifyBySms() && UtilsFunctions.isValidPhoneNumber(i.getPhone()))
					.forEach(i -> numeroSet.add(i.getPhone()));

			//send to external
			if (TransportationRequestStatus.PICKEDUP.equals(transportationRequest.getStatus()))
				transportationRequest.getDeliveryRequest().getToNotifyList().stream().filter(i -> !i.getInternal() && i.getNotifyBySms() && UtilsFunctions.isValidPhoneNumber(i.getPhone()))
						.forEach(i -> numeroSet.add(i.getPhone()));
		}

		numeroSet.add(transportationRequest.getDeliveryRequest().getRequester().getPhone());
		numeroSet.add(transportationRequest.getDeliveryRequest().getProject().getManager().getPhone());

		sendSms(numeroSet, message);

		System.out.println("------------------------------------------------------");
		System.out.println("try sendSms : " + message);
		System.out.println("to : " + numeroSet);
		System.out.println("------------------------------------------------------");
	}

}
