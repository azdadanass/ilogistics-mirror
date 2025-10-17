package ma.azdad.service;

import java.util.Arrays;
import java.util.List;

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

	public static List<String> apiKeys = Arrays.asList(
			"AIzaSyBal3DsAyYIl1UN_oEy9NuaPXg2qqTiiyQ",
			"AIzaSyCAPkjARiuD8taQUr_gxd2zuWFNJxnxHO4",
			"AIzaSyCsA6hQ1C8D6IIeB_r2WDEEgPelvpUWIf8",
			"AIzaSyCyNTc451BOCfr7mSXaEKGtNBMFepsxT3I"
		);
		
		private static int currentKeyIndex = 0;

		public static Path getNewPath(GenericPlace from, GenericPlace to) {
			for (int attempt = 0; attempt < apiKeys.size(); attempt++) {
				String apiKey = apiKeys.get(currentKeyIndex);
				Path result = getNewPath(from, to, apiKey);
				
				// If result is valid (not null means API returned OK status), return it
				if (result != null) {
					return result;
				}
				
				// Move to next API key
				System.out.println("API key at index " + currentKeyIndex + " failed. Trying next key...");
				currentKeyIndex = (currentKeyIndex + 1) % apiKeys.size();
			}
			
			// All keys failed, return fallback
			System.err.println("All API keys failed. Using fallback calculation.");
			try {
				Double estimatedDistance = UtilsFunctions.distFrom(from, to) / 1000.0;
				return new Path(estimatedDistance, estimatedDistance + " Km *");
			} catch (Exception e) {
				return new Path();
			}
		}
		
		public static Double getDistance(Double fromLat, Double fromLng, Double toLat, Double toLng) {
			for (int attempt = 0; attempt < apiKeys.size(); attempt++) {
				String apiKey = apiKeys.get(currentKeyIndex);
				Double result = getDistance(fromLat, fromLng, toLat, toLng, apiKey);
				
				// If result is valid (non-zero), return it
				if (result != null && result > 0.0) {
					return result;
				}
				
				// Move to next API key
				System.out.println("API key at index " + currentKeyIndex + " failed. Trying next key...");
				currentKeyIndex = (currentKeyIndex + 1) % apiKeys.size();
			}
			
			// All keys failed
			System.err.println("All API keys failed for getDistance.");
			return 0.0;
		}
		
		public static String getDuration(Double fromLat, Double fromLng, Double toLat, Double toLng) {
			for (int attempt = 0; attempt < apiKeys.size(); attempt++) {
				String apiKey = apiKeys.get(currentKeyIndex);
				String result = getDuration(fromLat, fromLng, toLat, toLng, apiKey);
				
				// If result is valid, return it
				if (result != null && !result.isEmpty()) {
					return result;
				}
				
				// Move to next API key
				System.out.println("API key at index " + currentKeyIndex + " failed. Trying next key...");
				currentKeyIndex = (currentKeyIndex + 1) % apiKeys.size();
			}
			
			// All keys failed
			System.err.println("All API keys failed for getDuration.");
			return null;
		}

		public static Path getNewPath(GenericPlace from, GenericPlace to, String apiKey) {
			JSONObject json;
			try {
				json = JsonReader.readJsonFromUrl("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" 
					+ from.getValue() + "&destinations=" + to.getValue() + "&key=" + apiKey);
				
				// Check API response status
				String status = json.getString("status");
				if (!"OK".equals(status)) {
					System.out.println("Distance Matrix API returned status=" + status);
					return null;
				}
				
				if (!json.has("rows") || json.getJSONArray("rows").length() == 0) {
					System.out.println("No rows returned by Distance Matrix API");
					return null;
				}
				
				JSONObject row = json.getJSONArray("rows").getJSONObject(0);
				if (!row.has("elements") || row.getJSONArray("elements").length() == 0) {
					System.out.println("No elements returned in row");
					return null;
				}
				
				JSONObject firstRow = row.getJSONArray("elements").getJSONObject(0);
				String elementStatus = firstRow.getString("status");
				if (!"OK".equals(elementStatus)) {
					System.out.println("Element status not OK: " + elementStatus);
					return null;
				}
				
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
				System.err.println("Error with API key: " + e.getMessage());
				return null;
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
					return null;
				}

				if (!json.has("rows") || json.getJSONArray("rows").length() == 0) {
					System.out.println("No rows returned by Distance Matrix API");
					return null;
				}

				JSONObject row = json.getJSONArray("rows").getJSONObject(0);
				if (!row.has("elements") || row.getJSONArray("elements").length() == 0) {
					System.out.println("No elements returned in row");
					return null;
				}

				JSONObject element = row.getJSONArray("elements").getJSONObject(0);
				String elementStatus = element.getString("status");
				if (!"OK".equals(elementStatus)) {
					System.out.println("Element status not OK: " + elementStatus);
					return null;
				}

				Double estimatedDistance = element.getJSONObject("distance").getDouble("value") / 1000.0;
				System.out.println("Distance from (" + fromLat + "," + fromLng + ") to (" 
					+ toLat + "," + toLng + ") = " + estimatedDistance + " km");
				return estimatedDistance;

			} catch (Exception e) {
				System.err.println("Error in getDistance: " + e.getMessage());
				return null;
			}
		}
		
		public static String getDuration(Double fromLat, Double fromLng, Double toLat, Double toLng, String apiKey) {
			try {
				JSONObject json = JsonReader.readJsonFromUrl("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" 
					+ fromLat + "," + fromLng + "&destinations=" + toLat + "," + toLng + "&key=" + apiKey);
				
				String status = json.getString("status");
				if (!"OK".equals(status)) {
					System.out.println("Distance Matrix API returned status=" + status);
					return null;
				}
				
				if (!json.has("rows") || json.getJSONArray("rows").length() == 0) {
					System.out.println("No rows returned by Distance Matrix API");
					return null;
				}
				
				JSONObject row = json.getJSONArray("rows").getJSONObject(0);
				if (!row.has("elements") || row.getJSONArray("elements").length() == 0) {
					System.out.println("No elements returned in row");
					return null;
				}
				
				JSONObject firstRow = row.getJSONArray("elements").getJSONObject(0);
				String elementStatus = firstRow.getString("status");
				if (!"OK".equals(elementStatus)) {
					System.out.println("Element status not OK: " + elementStatus);
					return null;
				}
				
				System.out.println(firstRow);
				System.out.println(firstRow.getJSONObject("duration").get("text"));
				System.out.println(firstRow.getJSONObject("duration").get("value"));
				System.out.println(firstRow.getJSONObject("distance").get("text"));
				System.out.println(firstRow.getJSONObject("distance").get("value"));
				
				String estimatedDuration = firstRow.getJSONObject("duration").get("text").toString();
				return estimatedDuration;
			} catch (Exception e) {
				System.err.println("Error in getDuration: " + e.getMessage());
				return null;
			}
		}
		
		// Helper method to manually rotate to next key if needed
		public static void rotateToNextKey() {
			currentKeyIndex = (currentKeyIndex + 1) % apiKeys.size();
			System.out.println("Manually rotated to API key at index " + currentKeyIndex);
		}
		
		// Helper method to get current key being used
		public static String getCurrentKey() {
			return apiKeys.get(currentKeyIndex);
		}

}
