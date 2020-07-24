package ma.azdad.view;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.ToNotify;
import ma.azdad.service.ExternalResourceService;
import ma.azdad.service.ToNotifyService;
import ma.azdad.service.UserService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class ToNotifyView extends GenericViewOld<ToNotify> {

	@Autowired
	protected ToNotifyService toNotifyService;

	@Autowired
	protected CacheView cacheView;

	@Autowired
	protected UserService userService;

	@Autowired
	protected ExternalResourceService externalResourceService;

	private ToNotify toNotify = new ToNotify();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage) {
			toNotify = toNotifyService.findOne(selectedId);
			toNotify.init();
		} else if (isViewPage)
			toNotify = toNotifyService.findOne(selectedId);
	}

	public void refreshList() {
		if (isListPage)
			list2 = list1 = toNotifyService.findByUser(sessionView.getUsername());
	}

	public void refreshToNotify() {
		toNotifyService.flush();
		toNotify = toNotifyService.findOne(toNotify.getId());
	}

	/*
	 * Redirection
	 */
	public void redirect() {
		// if (false)
		// cacheView.accessDenied();
	}

	// SAVE TONOTIFY
	public Boolean canSaveToNotify() {
		if (isListPage || isAddPage)
			return true;
		else if (isViewPage || isEditPage)
			return true;
		return false;
	}

	public String saveToNotify() {
		if (!canSaveToNotify())
			return addParameters(listPage, "faces-redirect=true");
		if (!validateToNotify())
			return null;

		toNotify.setInternalResource(userService.findOne(toNotify.getInternalResourceUsername()));

		if (isAddPage)
			toNotify.setUser(sessionView.getUser());

		toNotify = toNotifyService.save(toNotify);

		return addParameters(listPage, "faces-redirect=true");
	}

	public Boolean validateToNotify() {
		if (toNotifyService.countByUserAndInternalResource(sessionView.getUsername(), toNotify.getInternalResourceUsername()) > 0)
			return FacesContextMessages.ErrorMessages("Resource already exists in your notify list");
		return true;
	}

	// DELETE TONOTIFY
	public Boolean canDeleteToNotify() {
		return true;
	}

	public String deleteToNotify() {
		if (canDeleteToNotify())
			toNotifyService.delete(toNotify);
		return addParameters(listPage, "faces-redirect=true");
	}

	// Generic
	public List<ToNotify> findByUser() {
		return toNotifyService.findByUser(sessionView.getUsername());
	}

	// GETTERS & SETTERS
	@Override
	public SessionView getSessionView() {
		return sessionView;
	}

	@Override
	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public ToNotifyService getToNotifyService() {
		return toNotifyService;
	}

	public void setToNotifyService(ToNotifyService toNotifyService) {
		this.toNotifyService = toNotifyService;
	}

	public ToNotify getToNotify() {
		return toNotify;
	}

	public void setToNotify(ToNotify toNotify) {
		this.toNotify = toNotify;
	}

}
