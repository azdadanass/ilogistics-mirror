package ma.azdad.mobile.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ma.azdad.mobile.model.Token;
import ma.azdad.model.User;
import ma.azdad.service.DeliveryRequestService;
import ma.azdad.service.TokenService;
import ma.azdad.service.UserService;
import ma.azdad.utils.PrimeFacesUploadedFile;

@RestController
public class DeliveryRequestFileController {
	
	    @Autowired
	    private DeliveryRequestService deliveryRequestService;
	    @Autowired
	    private TokenService tokenService;
	    @Autowired
	    UserService userService;

	    @PostMapping("/mobile/dn-file/upload-file/{key}/{id}/{fileType}")
	    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file,@PathVariable String key,@PathVariable Integer id,@PathVariable String fileType) {
	        try {
	        	System.out.println("/mobile/dn-file/upload-file/{key}/{id}");
	    		Token token = tokenService.getBykey(key);
	    		User user = userService.findByUsername(token.getUsername());
	    		UIComponent dummyComponent = new UIOutput(); 
	    	    UploadedFile uploadedFile = new PrimeFacesUploadedFile(file);
	    	    FileUploadEvent event = new FileUploadEvent(dummyComponent, uploadedFile);
	            deliveryRequestService.handleFileUpload(event, user, id, fileType);
	            return ResponseEntity.status(HttpStatus.OK)
	                    .contentType(MediaType.TEXT_PLAIN)  // Set the content type to text/plain
	                    .body("File uploaded successfully");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file: " + e.getMessage());
	        }
	    }
	    
	   

	    @GetMapping("/mobile/dn-file/download/{key}")
	    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String key,@RequestParam String fileName) throws FileNotFoundException {
	    	System.out.println("/mobile/dn-file/download/{key}");
    		Token token = tokenService.getBykey(key);
	        File file = new File("http://ilogistics.3gcominside.com/" + fileName);

	        if (!file.exists()) {
	            throw new FileNotFoundException("File not found: " + fileName);
	        }

	        InputStream inputStream = new FileInputStream(file);

	        String contentType = "application/octet-stream";  // Default to binary file
	        long contentLength = file.length();

	        HttpHeaders headers = new HttpHeaders();
	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName());
	        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
	        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength));

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(new InputStreamResource(inputStream));
	    }
	
	
	
	
	
	

}
