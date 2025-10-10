package ma.azdad.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ma.azdad.model.GenericPlace;
import ma.azdad.model.Path;
import ma.azdad.repos.PathRepos;

@Component
public class PathService extends GenericService<Integer, Path, PathRepos> {

	@Autowired
	PathRepos pathRepos;

	@Override
	public Path findOne(Integer id) {
		Path path = super.findOne(id);
		return path;
	}

	public static String key1 = "AIzaSyBal3DsAyYIl1UN_oEy9NuaPXg2qqTiiyQ";
	public static String key2 = "AIzaSyCAPkjARiuD8taQUr_gxd2zuWFNJxnxHO4";
	public static String key3 = "AIzaSyCsA6hQ1C8D6IIeB_r2WDEEgPelvpUWIf8";
	public static int i = 0;

	public static Path getNewPath(GenericPlace from, GenericPlace to) {
		return getNewPath(from, to, i++ < 2450 ? key3 : key3);
	}
	
	public static Double getDistance(Double fromLat, Double fromLng,Double toLat, Double toLng) {
		return getDistance(fromLat,fromLng, toLat,toLng, i++ < 2450 ? key3 : key3);
	}
	
	public static String getDuration(Double fromLat, Double fromLng,Double toLat, Double toLng) {
		return getDuration(fromLat,fromLng, toLat,toLng, i++ < 2450 ? key3 : key3);
	}


	public static Path getNewPath(GenericPlace from, GenericPlace to, String apiKey) {
		JSONObject json;
		try {
			json = JsonReader.readJsonFromUrl("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + from.getValue() + "&destinations=" + to.getValue() + "&key=" + apiKey);
			JSONObject firstRow = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
			System.out.println(firstRow);
			System.out.println(firstRow.getJSONObject("duration").get("text"));
			System.out.println(firstRow.getJSONObject("duration").get("value"));
			System.out.println(firstRow.getJSONObject("distance").get("text"));
			System.out.println(firstRow.getJSONObject("distance").get("value"));
			Double estimatedDuration = Double.valueOf(firstRow.getJSONObject("duration").get("value").toString());
			String estimatedDurationText = firstRow.getJSONObject("duration").get("text").toString();
			Double estimatedDistance = Double.valueOf(firstRow.getJSONObject("distance").get("value").toString()) / 1000.0;
			String estimatedDistanceText = firstRow.getJSONObject("distance").get("text").toString();
			return new Path(estimatedDuration, estimatedDurationText, estimatedDistance, estimatedDistanceText);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			try {
				Double estimatedDistance = UtilsFunctions.distFrom(from, to) / 1000.0;
				return new Path(estimatedDistance, estimatedDistance + " Km *");
			} catch (Exception e2) {
				return new Path();
			}
		}
	}
	
	public static Double getDistance(Double fromLat, Double fromLng, Double toLat, Double toLng, String apiKey) {
	    try {
	        JSONObject json = JsonReader.readJsonFromUrl(
	            "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" 
	            + fromLat + "," + fromLng + "&destinations=" + toLat + "," + toLng + "&key=" + apiKey
	        );

	        String status = json.getString("status");
	        if (!"OK".equals(status)) {
	            System.out.println("Distance Matrix API returned status=" + status);
	            return 0.0; // fallback
	        }

	        if (!json.has("rows") || json.getJSONArray("rows").length() == 0) {
	            System.out.println("No rows returned by Distance Matrix API");
	            return 0.0;
	        }

	        JSONObject row = json.getJSONArray("rows").getJSONObject(0);
	        if (!row.has("elements") || row.getJSONArray("elements").length() == 0) {
	            System.out.println("No elements returned in row");
	            return 0.0;
	        }

	        JSONObject element = row.getJSONArray("elements").getJSONObject(0);
	        String elementStatus = element.getString("status");
	        if (!"OK".equals(elementStatus)) {
	            System.out.println("Element status not OK: " + elementStatus);
	            return 0.0;
	        }

	        Double estimatedDistance = element.getJSONObject("distance").getDouble("value") / 1000.0;
	        System.out.println("Distance from (" + fromLat + "," + fromLng + ") to (" + toLat + "," + toLng + ") = " + estimatedDistance + " km");
	        return estimatedDistance;

	    } catch (Exception e) {
	        System.err.println("Error in getDistance: " + e.getMessage());
	        return 0.0;
	    }
	}

	
	public static String getDuration(Double fromLat, Double fromLng,Double toLat, Double toLng, String apiKey) {
		JSONObject json;
		try {
			json = JsonReader.readJsonFromUrl("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" 
		+ fromLat+","+fromLng + "&destinations=" + toLat+","+toLng + "&key=" + apiKey);
			JSONObject firstRow = json.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
			System.out.println(firstRow);
			System.out.println(firstRow.getJSONObject("duration").get("text"));
			System.out.println(firstRow.getJSONObject("duration").get("value"));
			System.out.println(firstRow.getJSONObject("distance").get("text"));
			System.out.println(firstRow.getJSONObject("distance").get("value"));
			String estimatedDuration = firstRow.getJSONObject("duration").get("text").toString();
			//String estimatedDurationText = firstRow.getJSONObject("duration").get("text").toString();
			//Double estimatedDistance = Double.valueOf(firstRow.getJSONObject("distance").get("value").toString()) / 1000.0;
			//String estimatedDistanceText = firstRow.getJSONObject("distance").get("text").toString();
			return estimatedDuration;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			
		}
		return null;
	}

}
