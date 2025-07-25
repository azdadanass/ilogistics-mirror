package ma.azdad.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.DeliveryRequest;
import ma.azdad.model.Role;
import ma.azdad.model.TransportationRequest;
import ma.azdad.model.TransportationRequestStatus;
import ma.azdad.model.User;
import ma.azdad.utils.App;
import ma.azdad.utils.OldMail;
import ma.azdad.utils.To;

@Component
@Transactional
public class OldEmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private DeliveryRequestService deliveryRequestService;

	@Autowired
	private TransportationRequestService transportationRequestService;

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private UserService userService;

	@Value("${applicationName}")
	private String applicationName;

	@Value("#{'${spring.profiles.active}'.replaceAll('-dev','')}")
	private String erp;

	private OldMail erectMail(String to, Set<String> emailsInCc, String subject, String message) {
		InternetAddress[] cc = generateInternetAddress(emailsInCc);
		OldMail mail = new OldMail();
		mail.setSubject(subject);

		try {
			mail.setFrom(new InternetAddress(App.SYSTEM_MAIL.getLink(), applicationName + " " + getErpName()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		mail.setTo(to);
		mail.setCc(cc);
		mail.setMessage(message);
		return mail;
	}

	private InternetAddress[] generateInternetAddress(Set<String> emails) {
		if (emails == null || emails.isEmpty())
			return null;
		List<InternetAddress> list = new ArrayList<InternetAddress>();
		for (String email : emails) {
			try {
				if (email == null)
					continue;
				InternetAddress internetAddress = new InternetAddress(email);
				internetAddress.validate();
				list.add(internetAddress);
			} catch (AddressException e) {
			}
		}
		return list.toArray(new InternetAddress[list.size()]);
	}

	private void send(final OldMail mail) {
		mailSender.send(new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MessagingException {
				System.out.println("------------------------------------------------------");
				System.out.println("Send mail");
				System.out.println("------------------------------------------------------");
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
				message.setFrom(mail.getFrom());
				message.setTo(mail.getTo());
				if (mail.getCc() != null) {
					message.setCc(mail.getCc());
				}
				message.setSubject(mail.getSubject());
				message.setText(mail.getMessage(), true);
				System.out.println("\tTo \t" + mail.getTo());
				System.out.println("\tSubject \t" + mail.getSubject());
				System.out.println("------------------------------------------------------");
			}
		});
	}

	private boolean validateEmail(String email) {
		try {
			new InternetAddress(email).validate();
			;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// @Async
	// public void deliveryRequestNotification(DeliveryRequest deliveryRequest) {
	// System.out.println("send deliveryRequestNotification");
	//
	// String object = deliveryRequest.getType().getValue() + " " +
	// deliveryRequest.getReference() + ", " + deliveryRequest.getStatus();
	// if (DeliveryRequestStatus.DELIVRED.equals(deliveryRequest.getStatus()))
	// object += deliveryRequest.getIsInbound() ? " to warehouse" :
	// deliveryRequest.getDestination() != null ? " to " +
	// deliveryRequest.getDestination().getName() : " to site";
	//
	// Set<To> toList = deliveryRequest.getToList();
	// for (To to : toList) {
	// if (!to.hasValidEmail())
	// continue;
	// try {
	// send(erectMail(to.getEmail(), null, object,
	// deliveryRequestService.generateEmailNotification(deliveryRequest, true)));
	// } catch (Exception e) {
	// System.out.println("deliveryRequestNotification error : " + e.getMessage());
	// }
	// }
	// }

	@Async
	public void deliveryRequestNotification(DeliveryRequest deliveryRequest) {
		switch (deliveryRequest.getStatus()) {
		case REQUESTED:
			deliveryRequestNotification(deliveryRequest, deliveryRequest.getProject().getManager().getEmail(), new HashSet<>(Arrays.asList(deliveryRequest.getRequester().getEmail())),
					deliveryRequest.getProject().getManager().getFullName());
			break;
		case REJECTED:
			deliveryRequestNotification(deliveryRequest, deliveryRequest.getRequester().getEmail(), new HashSet<>(Arrays.asList(deliveryRequest.getProject().getManager().getEmail())),
					deliveryRequest.getRequester().getFullName());
			break;
		case APPROVED2:
		case PARTIALLY_DELIVRED:
		case DELIVRED:
			Set<String> cc = deliveryRequest.getToNotifyList().stream().filter(item -> item.getInternalResource().getInternal()).map(item -> item.getEmail()).collect(Collectors.toSet());
			cc.add(deliveryRequest.getProject().getManager().getEmail());
			if (deliveryRequest.getWarehouse() != null)
				cc.addAll(warehouseService.findManagerList(deliveryRequest.getWarehouse().getId()).stream().map(i -> i.getEmail()).collect(Collectors.toSet()));
			if (deliveryRequest.getToUser() != null)
				cc.add(deliveryRequest.getToUser().getEmail());

			try {

				// add external deliver to supplier pms
				if (deliveryRequest.getDeliverToSupplierId() != null && deliveryRequest.getDestinationProject() != null && deliveryRequest.getDestinationProject().getId() != null) {
					List<User> userList = userService.findActiveByProjectAssignmentAndUserRoleAndSupplier(deliveryRequest.getDestinationProject().getId(), Role.ROLE_ILOGISTICS_PM,
							deliveryRequest.getDeliverToSupplierId());
					cc.addAll(userList.stream().map(u -> u.getEmail()).collect(Collectors.toList()));
				}

			} catch (Exception e) {
				System.err.println("error add external deliver to supplier pms");
				e.printStackTrace();
			}

			deliveryRequestNotification(deliveryRequest, deliveryRequest.getRequester().getEmail(), cc, deliveryRequest.getRequester().getFullName());
			deliveryRequest.getToNotifyList().stream().filter(item -> !item.getInternalResource().getInternal() && (cc == null || !cc.contains(item.getEmail())))
					.map(item -> new To(item.getFullName(), item.getEmail())).collect(Collectors.toSet())
					.forEach(to -> deliveryRequestNotification(deliveryRequest, to.getEmail(), null, to.getFullName()));
		default:
			break;
		}
	}

	private void deliveryRequestNotification(DeliveryRequest deliveryRequest, String to, Set<String> cc, String dearFullName) {
		if (!validateEmail(to))
			return;
		try {
			String object = null;
			switch (deliveryRequest.getStatus()) {
			case APPROVED2:
				object = deliveryRequest.getType().getValue() + " " + deliveryRequest.getReference() + ", Approved";
				break;
			default:
				object = deliveryRequest.getType().getValue() + " " + deliveryRequest.getReference() + ", " + deliveryRequest.getStatus();
				break;
			}

			send(erectMail(to, cc, object, deliveryRequestService.generateEmailNotification(deliveryRequest, dearFullName, true)));
		} catch (Exception e) {
			System.out.println("deliveryRequestNotification error : " + e.getMessage());
		}
	}

	// @Async
	// public void deliveryRequestNotificationOld(DeliveryRequest deliveryRequest) {
	// try {
	// System.out.println("TRY deliveryRequestNotification ! ");
	//
	// String to = "";
	// Set<String> cc = new HashSet<String>();
	// if (DeliveryRequestStatus.REQUESTED.equals(deliveryRequest.getStatus())) {
	// to = deliveryRequest.getProject().getManager().getEmail();
	// cc.add(deliveryRequest.getRequester().getEmail());
	// } else {
	// to = deliveryRequest.getRequester().getEmail();
	// cc.add(deliveryRequest.getProject().getManager().getEmail());
	// for (ToNotify toNotify : deliveryRequest.getToNotifyList())
	// if (toNotify.getNotifyByEmail() &&
	// UtilsFunctions.isValidEmail(toNotify.getEmail()))
	// cc.add(toNotify.getEmail());
	// }
	// if (!validateEmail(to))
	// return;
	//
	// String object = deliveryRequest.getType().getValue() + " " +
	// deliveryRequest.getReference() + ", " + deliveryRequest.getStatus();
	// if (DeliveryRequestStatus.DELIVRED.equals(deliveryRequest.getStatus()))
	// object += deliveryRequest.getIsInbound() ? " to warehouse" :
	// deliveryRequest.getDestination() != null ? " to " +
	// deliveryRequest.getDestination().getName() : " to site";
	//
	// Mail mail = erectMail(to, cc, object,
	// deliveryRequestService.generateEmailNotification(deliveryRequest, true));
	// send(mail);
	// } catch (Exception e) {
	// System.err.println("Emailservice deliveryRequestNotification error : ");
	// System.err.println(e.getMessage());
	// e.printStackTrace();
	// }
	// }

	@Async
	public void transportationRequestNotification(TransportationRequest transportationRequest) {
		if (TransportationRequestStatus.REQUESTED.equals(transportationRequest.getStatus()))
			transportationRequestNotification(transportationRequest, transportationRequest.getDeliveryRequest().getProject().getManager().getEmail(),
					new HashSet<>(Arrays.asList(transportationRequest.getDeliveryRequest().getRequester().getEmail())),
					transportationRequest.getDeliveryRequest().getProject().getManager().getFullName());
		else if (TransportationRequestStatus.APPROVED.equals(transportationRequest.getStatus()) || TransportationRequestStatus.ASSIGNED.equals(transportationRequest.getStatus()))
			transportationRequestNotification(transportationRequest, transportationRequest.getDeliveryRequest().getRequester().getEmail(),
					new HashSet<>(Arrays.asList(transportationRequest.getDeliveryRequest().getProject().getManager().getEmail())),
					transportationRequest.getDeliveryRequest().getRequester().getFullName());
		else if (TransportationRequestStatus.DELIVERED.equals(transportationRequest.getStatus()) || TransportationRequestStatus.PICKEDUP.equals(transportationRequest.getStatus())) {
			Set<String> cc = transportationRequest.getDeliveryRequest().getToNotifyList().stream().filter(item -> item.getInternalResource().getInternal()).map(item -> item.getEmail())
					.collect(Collectors.toSet());
			cc.add(transportationRequest.getDeliveryRequest().getProject().getManager().getEmail());

			transportationRequestNotification(transportationRequest, transportationRequest.getDeliveryRequest().getRequester().getEmail(), cc,
					transportationRequest.getDeliveryRequest().getRequester().getFullName());
			if (TransportationRequestStatus.PICKEDUP.equals(transportationRequest.getStatus()))
				transportationRequest.getDeliveryRequest().getToNotifyList().stream().filter(item -> !item.getInternalResource().getInternal()).map(item -> new To(item.getFullName(), item.getEmail()))
						.forEach(item -> transportationRequestNotification(transportationRequest, item.getEmail(), null, item.getFullName()));
		}
	}

	private void transportationRequestNotification(TransportationRequest transportationRequest, String to, Set<String> cc, String dearFullName) {
		if (!validateEmail(to))
			return;
		try {
			String object = "Transportation Request " + transportationRequest.getStatus();
			send(erectMail(to, cc, object, transportationRequestService.generateEmailNotification(transportationRequest, dearFullName)));
		} catch (Exception e) {
			System.out.println("deliveryRequestNotification error : " + e.getMessage());
		}
	}

	// @Async
	// public void transportationRequestNotificationOld(TransportationRequest
	// transportationRequest) {
	// try {
	// System.out.println("TRY transportationRequestNotification ! ");
	//
	// String to = "";
	// Set<String> cc = new HashSet<String>();
	// if
	// (TransportationRequestStatus.REQUESTED.equals(transportationRequest.getStatus()))
	// {
	// to =
	// transportationRequest.getDeliveryRequest().getProject().getManager().getEmail();
	// cc.add(transportationRequest.getDeliveryRequest().getRequester().getEmail());
	// } else {
	// to = transportationRequest.getDeliveryRequest().getRequester().getEmail();
	// cc.add(transportationRequest.getDeliveryRequest().getProject().getManager().getEmail());
	// for (ToNotify toNotify :
	// transportationRequest.getDeliveryRequest().getToNotifyList())
	// if (toNotify.getNotifyByEmail() &&
	// UtilsFunctions.isValidEmail(toNotify.getEmail()))
	// cc.add(toNotify.getEmail());
	// }
	// if (!validateEmail(to))
	// return;
	// Mail mail = erectMail(to, cc, "Transportation Request " +
	// transportationRequest.getStatus(),
	// transportationRequestService.generateEmailNotification(transportationRequest));
	// send(mail);
	// } catch (Exception e) {
	// System.err.println("Emailservice transportationRequestNotification error :
	// ");
	// System.err.println(e.getMessage());
	// e.printStackTrace();
	// }
	// }

	private String getErpName() {
		switch (erp) {
		case "gcom":
			return "3Gcom";
		case "orange":
			return "Orange";
		case "mise":
			return "Mise";
		default:
			return "";
		}
	}

}
