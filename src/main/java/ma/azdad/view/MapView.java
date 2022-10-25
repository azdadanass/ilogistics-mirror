package ma.azdad.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ma.azdad.model.Site;
import ma.azdad.service.SiteService;
import ma.azdad.service.UtilsFunctions;

@ManagedBean
@Component
@Transactional
@Scope("view")
public class MapView {

	@Autowired
	private SiteService siteService;

	private MapModel mapModel;
	private Marker marker;

	private Double latitude;
	private Double longitude;
	private String markerTitle;

	private Site site;
	private Integer siteId;

	private String fileName;

	@PostConstruct
	public void init() {
		initParameters();

		if (siteId != null && siteService.exists(siteId)) {
			site = siteService.findOne(siteId);
			latitude = site.getLatitude();
			longitude = site.getLongitude();
			markerTitle = "<b>" + site.getName() + "</b><br/>" + site.getGoogleAddress();
			if (site.getIsZone())
				fileName = site.getGeographicFile();
		}

		refreshMapModel();
	}

	public String getFilePublicUrl() {
		return "https://sdm.orange.telodigital.com/file/" + fileName;
	}

	public void refreshMapModel() {
		mapModel = new DefaultMapModel();
		if (latitude != null && longitude != null) {
			Marker marker = new Marker(new LatLng(latitude, longitude), markerTitle);
			mapModel.addOverlay(marker);
		}
	}

	private void initParameters() {
		siteId = UtilsFunctions.getIntegerParameter("siteId");
		latitude = UtilsFunctions.getDoubleParameter("latitude");
		longitude = UtilsFunctions.getDoubleParameter("longitude");
		fileName = UtilsFunctions.getParameter("fileName");
	}

	public void onMarkerSelect(OverlaySelectEvent event) {
		marker = (Marker) event.getOverlay();
	}

	public String getCenterValue() {
		return latitude + "," + longitude;
	}

	public MapModel getMapModel() {
		return mapModel;
	}

	public void setMapModel(MapModel mapModel) {
		this.mapModel = mapModel;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}