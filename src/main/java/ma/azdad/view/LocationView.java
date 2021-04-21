package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ma.azdad.model.Location;
import ma.azdad.repos.LocationRepos;
import ma.azdad.service.LocationService;

@ManagedBean
@Component
@Scope("view")
public class LocationView extends GenericView<Integer, Location, LocationRepos, LocationService> {

	@Autowired
	protected LocationService locationService;

	private Location location = new Location();

	@Override
	@PostConstruct
	public void init() {
		super.init();
		if (isListPage)
			refreshList();
		else if (isEditPage)
			location = locationService.findOne(id);
		else if (isViewPage)
			location = locationService.findOne(id);

	}

	@Override
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
			try {
				locationService.delete(location);
			} catch (Exception e) {
				FacesContextMessages.ErrorMessages(e.getMessage());
				return null;
			}

		return listPage;
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
