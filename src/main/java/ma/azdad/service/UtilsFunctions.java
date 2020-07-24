package ma.azdad.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ma.azdad.model.GenericPlace;
import ma.azdad.model.Path;

@Component
public class UtilsFunctions {
	final static DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	final static DateFormat dtf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	final static DateFormat dtf2 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	private static String[] backgroundTab = { "aa-background-0", "aa-background-1", "aa-background-2", "aa-background-3", "aa-background-4", "aa-background-5", "aa-background-6", "aa-background-7", "aa-background-8", "aa-background-9", "aa-background-10", "aa-background-11", "aa-background-12", "aa-background-13", "aa-background-14", "aa-background-15", "aa-background-16" };
	private static String[] badgeTab = { "badge-success", "badge-danger", "badge-info", "badge-pink", "badge-warning", "badge-primary", "badge-purple", "badge-yellow" };
	private static String[] mapIconTab = { "https://maps.google.com/mapfiles/ms/micons/green-dot.png", "https://maps.google.com/mapfiles/ms/micons/red-dot.png", "https://maps.google.com/mapfiles/ms/micons/ltblue-dot.png", "https://maps.google.com/mapfiles/ms/micons/pink-dot.png", "https://maps.google.com/mapfiles/ms/micons/orange-dot.png", "https://maps.google.com/mapfiles/ms/micons/blue-dot.png",
			"https://maps.google.com/mapfiles/ms/micons/purple-dot.png", "https://maps.google.com/mapfiles/ms/micons/yellow-dot.png" };

	public static String formatName(String name) {
		if (name == null || name.isEmpty())
			return null;
		name = name.trim();
		name = name.toLowerCase();
		name = name.replaceAll("[^a-z ]", "");
		name = name.replaceAll("( )+", " ");
		if (name.isEmpty())
			return null;
		name = name.substring(0, 0) + Character.toUpperCase(name.charAt(0)) + name.substring(1);
		int i = 0;
		for (Character c : name.toCharArray()) {
			if (c == ' ')
				name = name.substring(0, i + 1) + Character.toUpperCase(name.charAt(i + 1)) + name.substring(i + 2);
			i++;
		}
		return name;
	}

	public static String formatTab(String[] tab) {
		String[] result = Arrays.copyOf(tab, tab.length);
		for (int i = 0; i < result.length; i++)
			result[i] = "'" + result[i] + "'";
		return Arrays.toString(result);
	}

	public static Long getDateDifference(Date endDate, Date startDate) {
		return TimeUnit.MILLISECONDS.toDays(endDate.getTime() - startDate.getTime());
	}

	public static String getParameter(String name) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}

	public static Integer getIntegerParameter(String name) {
		try {
			return Integer.valueOf(getParameter(name));
		} catch (Exception e) {
			return null;
		}
	}

	public static Boolean getBooleanParameter(String name) {
		return "true".equals(getParameter(name));
	}

	public static String getFormattedDuration(long seconds) {
		return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
	}

	public static String getFormattedDuration(Date startDate, Date endDate) {
		return getFormattedDuration((endDate.getTime() - startDate.getTime()) / 1000);
	}

	public static String getBackground(Integer index) {
		return backgroundTab[index % backgroundTab.length];
	}

	public static String getBadge(Integer index) {
		return badgeTab[index % badgeTab.length];
	}

	public static String getMapIcon(Integer index) {
		return mapIconTab[index % mapIconTab.length];
	}

	public static String getLetter(Integer index) {
		if (index >= 0 && index <= 25)
			return String.valueOf((char) (index + 65));
		return null;
	}

	public static Date getMinDate(List<Date> dateList) {
		if (dateList.isEmpty())
			return null;
		Collections.sort(dateList);
		return dateList.iterator().next();
	}

	public static Date getMaxDate(List<Date> dateList) {
		if (dateList.isEmpty())
			return null;
		Collections.sort(dateList);
		Collections.reverse(dateList);
		return dateList.iterator().next();
	}

	public static Date getMinDate(Date date1, Date date2) {
		return date1.compareTo(date2) == -1 ? date1 : date2;
	}

	public static Date getMaxDate(Date date1, Date date2) {
		return date1.compareTo(date2) == 1 ? date1 : date2;
	}

	public static String getFormattedSize(Long size) {
		if (size < 0.01 * 1024)
			return size + " o";
		Double newSize = size / 1024.00;
		NumberFormat formatter = new DecimalFormat("#0.00");
		if (newSize < 0.01 * 1024)
			return formatter.format(newSize) + " Ko";
		newSize = newSize / 1024.00;
		return formatter.format(newSize) + " Mo";
	}

	public static Date getDate(String source) {
		try {
			return df.parse(source);
		} catch (Exception e) {
			return null;
		}

	}

	public static Date getDateTime(String source) {
		try {
			return dtf.parse(source);
		} catch (Exception e) {
			return null;
		}

	}

	public static Date getDateTimeWithoutSeconds(String source) {
		try {
			return dtf2.parse(source);
		} catch (Exception e) {
			return null;
		}

	}

	public static String getFormattedDate(Date date) {
		if (date == null)
			return null;
		return df.format(date);
	}

	public static String getFormattedDateTime(Date date) {
		if (date == null)
			return null;
		return dtf.format(date);
	}

	public static Boolean contains(String key, String... values) {
		return Arrays.asList(values).contains(key);
	}

	public static String cleanString(String str) {
		if (str == null)
			return null;
		return str.trim().replaceAll("\\s+", " ");
	}

	public static String formatDouble(Double d) {
		NumberFormat formatter = new DecimalFormat("#0.00");
		return formatter.format(d);
	}

	public static Integer compareDates(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		if (c1.get(Calendar.YEAR) > c2.get(Calendar.YEAR))
			return 1;
		if (c1.get(Calendar.YEAR) < c2.get(Calendar.YEAR))
			return -1;
		if (c1.get(Calendar.MONTH) > c2.get(Calendar.MONTH))
			return 1;
		if (c1.get(Calendar.MONTH) < c2.get(Calendar.MONTH))
			return -1;
		if (c1.get(Calendar.DAY_OF_MONTH) > c2.get(Calendar.DAY_OF_MONTH))
			return 1;
		if (c1.get(Calendar.DAY_OF_MONTH) < c2.get(Calendar.DAY_OF_MONTH))
			return -1;
		return 0;
	}

	public static Integer compareDoubles(Double x, Double y) {
		BigDecimal a = new BigDecimal(x).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal b = new BigDecimal(y).setScale(2, BigDecimal.ROUND_HALF_UP);
		return a.compareTo(b);
	}

	public static Integer compareDoubles(Double x, Double y, int scale) {
		BigDecimal a = new BigDecimal(x).setScale(scale, BigDecimal.ROUND_HALF_UP);
		BigDecimal b = new BigDecimal(y).setScale(scale, BigDecimal.ROUND_HALF_UP);
		return a.compareTo(b);
	}

	// public static Double distFrom(Double lat1, Double lng1, Double lat2, Double
	// lng2) {
	// double earthRadius = 6371000; // meters
	// double dLat = Math.toRadians(lat2 - lat1);
	// double dLng = Math.toRadians(lng2 - lng1);
	// double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	// Math.cos(Math.toRadians(lat1)) *
	// Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
	// double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	// Double dist = (double) (earthRadius * c);
	//
	// return dist;
	// }

	public static double distFrom(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344 * 1000;
		return (dist);
	}

	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public static Double distFrom(GenericPlace p1, GenericPlace p2) {
		return distFrom(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude());
	}

	public static Boolean isValidEmail(String email) {
		if (email == null)
			return false;
		return email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

	}

	public static Boolean isValidPhoneNumber(String phone) {
		if (phone == null)
			return false;
		return phone.matches("[+]?[0-9]+");
	}

	public static String convertToPhoneNumber(String phone) {
		if (phone == null)
			return null;
		return phone.replaceAll("[^+0-9]", "");
	}

	public static String getCorrectFromList(String item, List<String> list) {
		for (String str : list)
			if (str.equalsIgnoreCase(item))
				return str;
		return null;
	}

	public static String sendHttpRequest(String method, String url, HashMap<String, String> params) {
		try {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod(method);
			con.setDoInput(true);
			con.setDoOutput(true);
			// add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			String encoding = Base64.getEncoder().encodeToString("admin:admin".getBytes());
			con.setRequestProperty("Authorization", "Basic " + encoding);
			OutputStream os = con.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(getPostDataString(params));
			writer.flush();
			writer.close();
			os.close();
			int responseCode = con.getResponseCode();
			System.out.println("\nSending '" + method + "' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// print result
			System.out.println(response.toString());
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}

	public static String path() {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		return ctx.getRealPath("/");
	}

	public static String cutText(String text, int maxSize) {
		if (text == null)
			return null;
		return text.length() <= maxSize ? text : text.substring(0, maxSize) + "...";

	}

	public static String key1 = "AIzaSyBal3DsAyYIl1UN_oEy9NuaPXg2qqTiiyQ";
	public static String key2 = "AIzaSyCAPkjARiuD8taQUr_gxd2zuWFNJxnxHO4";
	public static int i = 0;

	public static Path getNewPath(GenericPlace from, GenericPlace to) {
		return getNewPath(from, to, i++ < 2450 ? key1 : key2);
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
				Double estimatedDistance = distFrom(from, to) / 1000.0;
				return new Path(estimatedDistance, estimatedDistance + " Km *");
			} catch (Exception e2) {
				return new Path();
			}
		}
	}

	public static String stringToMD5(String password) {
		String passwordMD5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5 = md.digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md5.length; i++) {
				sb.append(Integer.toString((md5[i] & 0xFF) + 256, 16).substring(1));
			}
			passwordMD5 = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return passwordMD5;
	}

	public static String encrypt(String password) {
		String crypte = "";
		for (int i = 0; i < password.length(); i++) {
			int c = password.charAt(i) ^ 10;
			crypte = crypte + (char) c;
		}
		return crypte;
	}

	public static void debug(String text) {
		separatorTop();
		System.out.println(text);
		separatorBottom();
	}

	public static void separatorTop() {
		System.out.println();
		System.out.println("[32m--------------------------------------------------------------------------------------------");
		System.out.println();
	}

	public static void separatorBottom() {
		System.out.println();
		System.out.println("--------------------------------------------------------------------------------------------[39m");
		System.out.println();
	}

	public static String generateQrKey() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String getServerName() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getServerName();
	}

	public static String convertToString(HSSFCell cell) {
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case HSSFCell.CELL_TYPE_NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		case HSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue() ? "true" : "false";
		case HSSFCell.CELL_TYPE_BLANK:
			return "";
		default:
			return null;
		}

	}

	public static String addParameters(String path, String... tab) {
		path += "?" + tab[0];
		if (tab.length > 1)
			for (int i = 1; i < tab.length; i++)
				path += "&" + tab[i];
		return path;
	}

	public static Boolean validateEmail(String email) {
		try {
			new InternetAddress(email).validate();
			return true;
		} catch (AddressException e) {
			return false;
		}
	}

	public static String firstNotNullAndNotEmpty(String... params) {
		for (int i = 0; i < params.length; i++)
			if (params[i] != null && !params[i].isEmpty())
				return params[i];
		return null;
	}

}