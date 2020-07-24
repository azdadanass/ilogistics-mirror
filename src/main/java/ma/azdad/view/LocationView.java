package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Location;
import ma.azdad.service.LocationService;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class LocationView extends GenericViewOld<Location> {

	@Autowired
	protected LocationService locationService;

	private Location location = new Location();

	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			location = locationService.findOne(selectedId);
		else if (isViewPage)
			location = locationService.findOne(selectedId);

	}

	public void refreshList() {
		if (isListPage)
			list2 = list1 = locationService.findAll();
	}

	public void refreshLocation() {
		locationService.flush();
		location = locationService.findOne(location.getId());
	}

	// SAVE LOCATION
	public Boolean canSaveLocation() {
		return true;
	}

	public String saveLocation() {
		if (canSaveLocation()) {
			location = locationService.save(location);
			return addParameters(viewPage, "faces-redirect=true", "id=" + location.getId());
		}
		return listPage;
	}

	// DELETE LOCATION
	public Boolean canDeleteLocation() {
		return true;
	}

	public String deleteLocation() {
		if (canDeleteLocation())
			locationService.delete(location);
		return listPage;
	}

	// GETTERS & SETTERS
	public SessionView getSessionView() {
		return sessionView;
	}

	public void setSessionView(SessionView sessionView) {
		this.sessionView = sessionView;
	}

	public LocationService getLocationService() {
		return locationService;
	}

	public void setLocationService(LocationService locationService) {
		this.locationService = locationService;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

}
