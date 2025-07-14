package ma.azdad.service;

import java.util.List;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;
import org.springframework.stereotype.Component;

import ma.azdad.model.GenericPlace;
import ma.azdad.model.Stop;

@Component
public class MapService {

	public MapModel generate(GenericPlace p1, GenericPlace p2) {
		if(p1==null || p2==null)
			return new DefaultMapModel();
		
		
		MapModel mapModel = new DefaultMapModel();

		Marker marker1 = new Marker(new LatLng(p1.getLatitude(), p1.getLongitude()), p1.getName());
		marker1.setIcon("https://maps.google.com/mapfiles/ms/micons/blue-dot.png");
		Marker marker2 = new Marker(new LatLng(p2.getLatitude(), p2.getLongitude()), p2.getName());
		marker2.setIcon("https://maps.google.com/mapfiles/ms/micons/green-dot.png");

		mapModel.addOverlay(marker1);
		mapModel.addOverlay(marker2);

		return mapModel;
	}

	public MapModel generate(List<Stop> stopList, Boolean showPolylines) {
		MapModel mapModel = new DefaultMapModel();
		Polyline polyline = new Polyline();
		Integer i = 0;
		for (Stop stop : stopList) {
			Marker marker = new Marker(new LatLng(stop.getPlace().getLatitude(), stop.getPlace().getLongitude()), stop.getPlace().getName());
			marker.setIcon(UtilsFunctions.getMapIcon(i++));
			mapModel.addOverlay(marker);
			polyline.getPaths().add(stop.getPlace().getLatLng());
		}
		polyline.setStrokeWeight(10);
		polyline.setStrokeColor("#FF9900");
		polyline.setStrokeOpacity(0.7);
		if (showPolylines)
			mapModel.addOverlay(polyline);
		return mapModel;
	}

}