package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.service.TransportationJobService;
import ma.azdad.utils.Color;

@ManagedBean
@Component
@Scope("view")
public class DashboardView {

	@Autowired
	private SessionView sessionView;

	@Autowired
	private TransportationJobService transportationJobService;

	private Long countTjToAssign = 0l;
	private Long countTjToAccept = 0l;
	private Long countTjToStart = 0l;
	private Long countTrToAssign = 0l;
	private Long countTrToPickup = 0l;
	private Long countTrToDeliver = 0l;
	private Long countTrToAcknowledge = 0l;

	private Long acceptPerfomance = 0l;
	private Long startPerfomance = 0l;
	private Long completePerfomance = 0l;

	@PostConstruct
	public void init() {
		this.countTjToAssign = sessionView.getIsInternalTM() ? transportationJobService.countToAssign1(sessionView.getUsername())
				: transportationJobService.countToAssign2(sessionView.getUser().getTransporterId());
		this.countTjToAccept = transportationJobService.countToAccept(sessionView.getUsername());
		this.countTjToStart = transportationJobService.countToStart(sessionView.getUsername());

	}

	public Long getReactivity() {
		return 0l;
	}

	public Long getPerformance() {
		return 0l;
	}

	public String getColor(Long percentage) {
		if (percentage == null)
			return null;
		if (percentage >= 80)
			return Color.GREEN.getColorCode();
		if (percentage >= 50)
			return Color.L_BLUE.getColorCode();
		if (percentage >= 30)
			return Color.ORANGE.getColorCode();
		return Color.RED.getColorCode();
	}

	// getters & setters

	public SessionView getSessionView() {
		return sessionView;
	}

	public Long getAcceptPerfomance() {
		return acceptPerfomance;
	}

	public void setAcceptPerfomance(Long acceptPerfomance) {
		this.acceptPerfomance = acceptPerfomance;
	}

	public Long getStartPerfomance() {
		return startPerfomance;
	}

	public void setStartPerfomance(Long startPerfomance) {
		this.startPerfomance = startPerfomance;
	}

	public Long getCompletePerfomance() {
		return completePerfomance;
	}

	public void setCompletePerfomance(Long completePerfomance) {
		this.completePerfomance = completePerfomance;
	}

	public TransportationJobService getTransportationJobService() {
		return transportationJobService;
	}

	public void setTransportationJobService(TransportationJobService transportationJobService) {
		this.transportationJobService = transportationJobService;
	}

	public Long getCountTjToAssign() {
		return countTjToAssign;
	}

	public void setCountTjToAssign(Long countTjToAssign) {
		this.countTjToAssign = countTjToAssign;
	}

	public Long getCountTjToAccept() {
		return countTjToAccept;
	}

	public void setCountTjToAccept(Long countTjToAccept) {
		this.countTjToAccept = countTjToAccept;
	}

	public Long getCountTjToStart() {
		return countTjToStart;
	}

	public void setCountTjToStart(Long countTjToStart) {
		this.countTjToStart = countTjToStart;
	}

	public Long getCountTrToAssign() {
		return countTrToAssign;
	}

	public void setCountTrToAssign(Long countTrToAssign) {
		this.countTrToAssign = countTrToAssign;
	}

	public Long getCountTrToPickup() {
		return countTrToPickup;
	}

	public void setCountTrToPickup(Long countTrToPickup) {
		this.countTrToPickup = countTrToPickup;
	}

	public Long getCountTrToDeliver() {
		return countTrToDeliver;
	}

	public void setCountTrToDeliver(Long countTrToDeliver) {
		this.countTrToDeliver = countTrToDeliver;
	}

	public Long getCountTrToAcknowledge() {
		return countTrToAcknowledge;
	}

	public void setCountTrToAcknowledge(Long countTrToAcknowledge) {
		this.countTrToAcknowledge = countTrToAcknowledge;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

}