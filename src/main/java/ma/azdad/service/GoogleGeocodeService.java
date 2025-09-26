package ma.azdad.service;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import ma.azdad.model.DriverLocation;
import ma.azdad.model.GenericPlace;
import ma.azdad.model.Localizable;
import ma.azdad.model.Site;
import ma.azdad.repos.DriverLocationRepo;
import ma.azdad.repos.SiteRepos;

@Component
public class GoogleGeocodeService extends GenericService<Integer, Site, SiteRepos> {

	// KEYS MUST HAVE SERVICE --> Google Maps Geocoding API

	public final static String API_KEY_1 = "AIzaSyCpnK95Zp-919lAUqRjSu2U4_zsGIrBvOY"; // a.azdad Project1
	public final static String API_KEY_2 = "AIzaSyCsA6hQ1C8D6IIeB_r2WDEEgPelvpUWIf8"; // a.azdad Project2
	public final static String API_KEY_3 = "AIzaSyD7sKgpDd_azAnYxwcz3JuBV5-NJyLEzlk";// azdadanass Project1
	public final static String API_KEY_4 = "AIzaSyBvgp5kPhkuY2Ysu0DWmBUGPGm230iHOao";// azdadanass Project2
	public final static String API_KEY_5 = "AIzaSyDAw9H2VEsfWW2EQRUbuFf4UjehhHsQcRg";// azdadanass Project3
	public final static String API_KEY_6 = "AIzaSyAgL0vOsHHXRWSZ1ZiefHtwCYOO7gEd-5A";// azdadanass Project4

	public final static String[] API_KEYS = { API_KEY_1, API_KEY_2, API_KEY_3, API_KEY_4, API_KEY_5, API_KEY_6 };
	public static Integer currentApiKey = 0;

	@Autowired
	SiteRepos siteRepos;
	
	@Autowired
	DriverLocationRepo driverLocationRepo;

	public void updateGoogleGeocodeData(Site site) {
		System.out.println("--------------------------------------------");
		GenericPlace place = getGoogleGeocodeData(site);
		if (place.getGoogleAddress() != null)
			siteRepos.updateGoogleGeocodeData(site.getId(), place.getGoogleAddress(), place.getGoogleCity(), place.getGoogleRegion());
		System.out.println("--------------------------------------------");
	}

	@Async
	public void updateGoogleGeocodeDataAsync(Site site) {
		synchronized (GoogleGeocodeService.class) {
			updateGoogleGeocodeData(site);
		}
	}
	
	public void updateGoogleGeocodeData(DriverLocation userLocation) {
		System.out.println("--------------------------------------------");
		GenericPlace place = getGoogleGeocodeData(userLocation);
		if (place.getGoogleAddress() != null)
			driverLocationRepo.updateGoogleGeocodeData2(
				    userLocation.getId(),
				    userLocation.getLatitude(),
				    userLocation.getLongitude(),
				    place.getGoogleAddress(),
				    place.getGoogleCity(),
				    place.getGoogleRegion(),
				    new Date()
				);

		System.out.println("--------------------------------------------");
	}

	
	@Async
	public void updateGoogleGeocodeDataAsync(DriverLocation userLocation) {
		synchronized (GoogleGeocodeService.class) {
			updateGoogleGeocodeData(userLocation);
		}
	}

	// public static String getGoogleAddress(GenericPlace place) {
	// if (place == null)
	// return null;
	// return getGoogleAddress(place.getLatitude(), place.getLongitude());
	// }
	//
	// public static String getGoogleAddress(Double latitude, Double longitude) {
	// JSONObject json;
	// try {
	// String value = latitude + "," + longitude;
	// json =
	// JsonReader.readJsonFromUrl("https://maps.googleapis.com/maps/api/geocode/json?latlng="
	// + value + "&sensor=true&key=AIzaSyCsA6hQ1C8D6IIeB_r2WDEEgPelvpUWIf8");
	// return
	// json.getJSONArray("results").getJSONObject(0).get("formatted_address").toString();
	// } catch (Exception e) {
	// System.err.println(e.getMessage());
	// }
	//
	// return null;
	// }

	public static GenericPlace getGoogleGeocodeData(GenericPlace place) {
		try {
			String url;
			JSONObject json;
			Boolean retry = false;
			int k = 0;
			do {
				url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + place.getValue() + "&sensor=true&key=" + API_KEYS[currentApiKey];
				System.out.println(url);
				json = JsonReader.readJsonFromUrl(url);
				if ("OVER_QUERY_LIMIT".equals(json.getString("status"))) {
					currentApiKey = (++currentApiKey) % API_KEYS.length;
					retry = true;
				} else
					retry = false;
			} while (retry && ++k < API_KEYS.length);
			if (k >= API_KEYS.length) // none of the keys is working
				return place;
			JSONObject firstResult = json.getJSONArray("results").getJSONObject(0);
			String formattedAddress = null, city = null, region = null;
			try {
				formattedAddress = firstResult.get("formatted_address").toString();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			try {
				JSONArray addressComponentArray = firstResult.getJSONArray("address_components");
				for (int i = 0; i < addressComponentArray.length(); i++) {
					JSONObject addressComponent = addressComponentArray.getJSONObject(i);
					String types = addressComponent.getJSONArray("types").toString();
					if (city == null && types.contains("\"locality\""))
						city = addressComponent.getString("long_name");
					if (region == null && types.contains("\"administrative_area_level_1\""))
						region = addressComponent.getString("long_name");
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			System.out.println("formattedAddress\t" + formattedAddress);
			System.out.println("city\t" + city);
			System.out.println("region\t" + region);
			place.setGoogleAddress(formattedAddress);
			place.setGoogleCity(city);
			place.setGoogleRegion(region);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return place;
	}
	
	 public String getAddress(String latlng) {
	        String url;
	        JSONObject json;
	        boolean retry;
	        int k = 0;

	        do {
	            try {
	                url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" 
	                        + latlng + "&sensor=true&key=" + API_KEYS[currentApiKey];

	                System.out.println("Calling Google Maps API: " + url);

	                json = JsonReader.readJsonFromUrl(url); // <-- your util
	                String status = json.getString("status");

	                if ("OVER_QUERY_LIMIT".equals(status)) {
	                    // Switch API key and retry
	                    currentApiKey = (++currentApiKey) % API_KEYS.length;
	                    retry = true;
	                } else if ("OK".equals(status)) {
	                    JSONObject firstResult = json.getJSONArray("results").getJSONObject(0);
	                    return firstResult.getString("formatted_address");
	                } else {
	                    System.err.println("Google API error status: " + status);
	                    return latlng; // fallback to coords
	                }
	            } catch (Exception e) {
	                System.err.println("Error calling Google API: " + e.getMessage());
	                return latlng; // fallback to coords
	            }
	        } while (retry = (k++ < API_KEYS.length));

	        // If all keys exhausted
	        return latlng;
	    }
	
	
	public static GenericPlace getGoogleGeocodeData(Localizable localizable) {
		GenericPlace place = new  GenericPlace();
		try {
			String url;
			JSONObject json;
			Boolean retry = false;
			int k = 0;
			do {
				url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + localizable.getLatitude() + "," + localizable.getLongitude() + "&sensor=true&key=" + API_KEYS[currentApiKey];
				System.out.println(url);
				json = JsonReader.readJsonFromUrl(url);
				if ("OVER_QUERY_LIMIT".equals(json.getString("status"))) {
					currentApiKey = (++currentApiKey) % API_KEYS.length;
					retry = true;
				} else
					retry = false;
			} while (retry && ++k < API_KEYS.length);
			if (k >= API_KEYS.length) // none of the keys is working
				return place;
			JSONObject firstResult = json.getJSONArray("results").getJSONObject(0);
			String formattedAddress = null, city = null, region = null;
			try {
				formattedAddress = firstResult.get("formatted_address").toString();
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			try {
				JSONArray addressComponentArray = firstResult.getJSONArray("address_components");
				for (int i = 0; i < addressComponentArray.length(); i++) {
					JSONObject addressComponent = addressComponentArray.getJSONObject(i);
					String types = addressComponent.getJSONArray("types").toString();
					if (city == null && types.contains("\"locality\""))
						city = addressComponent.getString("long_name");
					if (region == null && types.contains("\"administrative_area_level_1\""))
						region = addressComponent.getString("long_name");
				}
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			System.out.println("formattedAddress\t" + formattedAddress);
			System.out.println("city\t" + city);
			System.out.println("region\t" + region);
			place.setGoogleAddress(formattedAddress);
			place.setGoogleCity(city);
			place.setGoogleRegion(region);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return place;
	}


}
