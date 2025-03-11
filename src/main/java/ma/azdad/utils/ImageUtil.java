package ma.azdad.utils;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;

public class ImageUtil {
	 public static Image getImageFromUrl(String imageUrl) throws IOException, BadElementException {
	        // Open connection to the URL
	        URL url = new URL(imageUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("User-Agent", "Mozilla/5.0"); // Optional: to simulate browser request

	        // Set the connection to automatically follow redirects
	        connection.setInstanceFollowRedirects(true);

	        // Ensure we get a successful response (HTTP 200)
	        int responseCode = connection.getResponseCode();
	        if (responseCode != HttpURLConnection.HTTP_OK) {
	            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
	                String newLocation = connection.getHeaderField("Location");
	                // Follow the redirect and get the image from the new URL
	                return getImageFromUrl(newLocation); // Recursive call to follow the new URL
	            } else {
	                throw new IOException("Failed to retrieve image. HTTP Response Code: " + responseCode);
	            }
	        }

	        // Read the image input stream into a byte array
	        InputStream inputStream = connection.getInputStream();
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        byte[] buffer = new byte[4096];
	        int bytesRead;

	        // Read the input stream in chunks
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            byteArrayOutputStream.write(buffer, 0, bytesRead);
	        }

	        byte[] imageBytes = byteArrayOutputStream.toByteArray();

	        // Convert byte array to iText Image
	        Image image = Image.getInstance(imageBytes);

	        // Optional: Scale the image to fit within specified dimensions (e.g., 500x300)
	        

	        return image;
	    }
}

