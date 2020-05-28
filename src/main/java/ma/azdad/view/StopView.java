package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Stop;
import ma.azdad.service.StopService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class StopView extends GenericView<Stop> {

	@Autowired
	protected StopService stopService;

	@Autowired
	protected CacheView cacheView;

	

	private Stop stop = new Stop();

	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			stop = stopService.findOne(selectedId);
		else if (isViewPage)
			stop = stopService.findOne(selectedId);

	}

	public void refreshList() {
		if (isListPage)
			list2 = list1 = stopService.findAll();
	}

	public void refreshStop() {
		stopService.flush();
		stop = stopService.findOne(stop.getId());
	}

	/*
	 * Redirection
	 */
	public void redirect() {
		// if (false)
		// cacheView.accessDenied();
	}

	// SAVE STOP
	public Boolean canSaveStop() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveStop() {
		if (!canSaveStop())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateStop())
			return null;
		stop = stopService.save(stop);
		return addParameters(viewPage, "faces-redirect=true", "id=" + stop.getId());
	}

	public Boolean validateStop() {
		return true;
	}

	// DELETE STOP
	public Boolean canDeleteStop() {
		return true;
	}

	public String deleteStop() {
		if (canDeleteStop())
			stopService.delete(stop);
		return addParameters(listPage, "faces-redirect=true");
	}

	// GETTERS & SETTERS
	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public StopService getStopService() {
		return stopService;
	}

	public void setStopService(StopService stopService) {
		this.stopService = stopService;
	}

	public Stop getStop() {
		return stop;
	}

	public void setStop(Stop stop) {
		this.stop = stop;
	}

}
