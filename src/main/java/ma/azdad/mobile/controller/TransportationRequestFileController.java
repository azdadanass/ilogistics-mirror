package ma.azdad.mobile.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ma.azdad.mobile.model.DeliveryRequestFile;
import ma.azdad.mobile.model.Token;
import ma.azdad.mobile.model.TransportationJobFile;
import ma.azdad.mobile.model.TransportationRequestFile;
import ma.azdad.model.User;
import ma.azdad.service.DocTypeService;
import ma.azdad.service.TokenService;
import ma.azdad.service.TransportationJobService;
import ma.azdad.service.TransportationRequestService;
import ma.azdad.service.UserService;
import ma.azdad.utils.PrimeFacesUploadedFile;
import ma.azdad.utils.Public;


@RestController
@RequestMapping("/mobile/tr-file")
public class TransportationRequestFileController {
	
		@Autowired
		private TransportationRequestService transportationRequestService;
	    @Autowired
	    private TokenService tokenService;
	    @Autowired
	    UserService userService;
	    @Autowired
		DocTypeService docTypeService;

	    
	    
	    @GetMapping("/delete-file/{key}/{idTj}/{idFile}")
		public void deleteFile(@PathVariable String key,@PathVariable Integer idTj,@PathVariable Integer idFile) {
			Token token = tokenService.getBykey(key);
			System.out.println("/mobile/tj-file/delete-file/{key}");
			transportationRequestService.deleteFile(idTj, idFile);
		}

	    @PostMapping("/upload-file/{key}/{id}/{fileType}")
	    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file,@PathVariable String key,@PathVariable Integer id,@PathVariable String fileType) {
	        try {
	        	System.out.println("/mobile/tj-file/upload-file/{key}/{id}");
	    		Token token = tokenService.getBykey(key);
	    		User user = userService.findByUsername(token.getUsername());
	    		UIComponent dummyComponent = new UIOutput(); 
	    	    UploadedFile uploadedFile = new PrimeFacesUploadedFile(file);
	    	    FileUploadEvent event = new FileUploadEvent(dummyComponent, uploadedFile);
	    	    transportationRequestService.handleFileUpload(event, user, id, fileType);
	            return ResponseEntity.status(HttpStatus.OK)
	                    .contentType(MediaType.TEXT_PLAIN)  // Set the content type to text/plain
	                    .body("File uploaded successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
	        }
	    }
	    
	   

	    @GetMapping("/download/{key}")
	    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String key, @RequestParam String fileName) throws IOException {
	        System.out.println("/mobile/tj-file/download/{key}");
	        Token token = tokenService.getBykey(key);
	        String fileUrl = Public.getPublicUrl(fileName);
	        // Open connection to the remote file
	        URL url = new URL(fileUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");

	        int responseCode = connection.getResponseCode();
	        if (responseCode != HttpURLConnection.HTTP_OK) {
	            throw new FileNotFoundException("File not found at URL: " + fileUrl);
	        }
	        InputStream inputStream = connection.getInputStream();

	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
	        headers.add(HttpHeaders.CONTENT_TYPE, connection.getContentType());
	        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(connection.getContentLengthLong()));

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(new InputStreamResource(inputStream));
	    }
	    
	    @GetMapping("/find-doc-type/{key}/{id}")
	    public List<String> findDocByType(@PathVariable String key,@PathVariable Integer id) {
	    	Token token = tokenService.getBykey(key);
	    	return docTypeService.findByTypeMobile("transportationRequest", id);
	    }
	    
	    @GetMapping("/find-doc/{key}/{id}")
	    public List<TransportationRequestFile> findDnAttachments(@PathVariable String key,@PathVariable Integer id) {
	    	System.out.println("/mobile/dn/find-doc/{key}/{id}");
	    	Token token = tokenService.getBykey(key);
	    	
	    	return transportationRequestService.findTrAttachments(id);
	    }
	    

}
