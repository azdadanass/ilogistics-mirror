package ma.azdad.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ma.azdad.service.TransportationRequestService;

@RestController
public class RestService {

	@Autowired
	TransportationRequestService transportationRequestService;

	@Value("${appPath}")
	private String path;
	
	@GetMapping("/rest/status")
	public String getStatus() {
		return "Up";
	}

	@RequestMapping("/rest/test")
	public void test(@RequestParam(value = "message") String message) {
		System.out.println("call test");
		System.out.println(message);
	}

	@RequestMapping("/rest/updatePaymentStatus")
	public void updatePaymentStatus(@RequestParam(value = "id") Integer transportationRequestId) {
		System.out.println("call updatePaymentStatus");
		transportationRequestService.updatePaymentStatus(transportationRequestId);
	}

	@GetMapping(value = "/rest/file/{fileName}/{ext}")
	public ResponseEntity<?> getFile(@PathVariable String fileName, @PathVariable String ext) throws IOException {
		System.out.println("ilogistics rest getFile : " + path + fileName + "." + ext);
		File file = new File(path + fileName + "." + ext);
		InputStream in = new FileInputStream(file);
		Resource resource = new ByteArrayResource(IOUtils.toByteArray(in));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(resource.contentLength());
		headers.setContentDispositionFormData("attachment", fileName + "." + ext);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	@GetMapping(value = "/rest/file/{folder}/{fileName}/{ext}")
	public ResponseEntity<?> getFile(@PathVariable String folder, @PathVariable String fileName, @PathVariable String ext) throws IOException {
		System.out.println("ilogistics rest getFile : " + path + folder + fileName + "." + ext);
		File file = new File(path + folder + "/" + fileName + "." + ext);
		InputStream in = new FileInputStream(file);
		Resource resource = new ByteArrayResource(IOUtils.toByteArray(in));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentLength(resource.contentLength());
		headers.setContentDispositionFormData("attachment", fileName + "." + ext);
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

}
