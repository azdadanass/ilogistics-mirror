package ma.azdad.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean
@Component
@Scope("session")
public class FileView {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${filesPath}")
	private String path;

	@Value("${photosPath}")
	private String photosPath;

	public StreamedContent getFile(String fileName) throws FileNotFoundException {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		File file = new File(path + fileName);
		String contentType = ec.getMimeType(fileName);
		int contentLength = (int) file.length();
		log.info("fileName : " + fileName);
		log.info("contentType : " + contentType);
		log.info("contentLength : " + contentLength);
		// InputStream stream =
		// FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/demo/images/optimus.jpg");
		InputStream stream = new FileInputStream(file);
		return new DefaultStreamedContent(stream, contentType, fileName);
	}

	public StreamedContent getStream() throws IOException {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		if (fc.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the HTML. Return a stub StreamedContent so that it will
			// generate right URL.
			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the media. Return a real StreamedContent with the
			// media bytes.
			String fileName = fc.getExternalContext().getRequestParameterMap().get("fileName");
			String contentType = ec.getMimeType(fileName);
			File file = new File(path + fileName);
			try {
				InputStream stream = new FileInputStream(file);
				return new DefaultStreamedContent(stream, contentType, fileName);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				return null;
			}

		}
	}

	public StreamedContent getStream(String path) throws IOException {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		if (fc.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			// So, we're rendering the HTML. Return a stub StreamedContent so that it will
			// generate right URL.
			return new DefaultStreamedContent();
		} else {
			// So, browser is requesting the media. Return a real StreamedContent with the
			// media bytes.
			String fileName = fc.getExternalContext().getRequestParameterMap().get("fileName");
			String contentType = ec.getMimeType(fileName);
			File file = new File(path + fileName);
			InputStream stream = new FileInputStream(file);
			return new DefaultStreamedContent(stream, contentType, fileName);
		}
	}

	public StreamedContent getPhotoStream() throws IOException {
		return getStream(photosPath);
	}

	public File handleFileUpload(FileUploadEvent event) throws IOException {
		System.err.println("TRY UPLOAD FILE !!!! ");
		FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		String beanName = (String) event.getComponent().getAttributes().get("beanName");
		File file = File.createTempFile(beanName + "-", "." + FilenameUtils.getExtension(event.getFile().getFileName()), new File(path));
		try (InputStream input = event.getFile().getInputstream()) {
			Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			FacesContext.getCurrentInstance().addMessage(null, message);
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error uploading", null));
			log.error(e.getMessage());
		}
		return file;
	}

	// old uploading
	public String uploadFileOld(FileUploadEvent event, String fileName, String folder) {
		System.out.println("\n-------------------------------------------------------------------\n");
		System.out.println("upload : " + event.getFile().getFileName() + "\t" + event.getFile().getContentType() + "\t" + event.getFile().getSize());
		String ext = null;
		try {
			ext = FilenameUtils.getExtension(event.getFile().getFileName());
			if (ext != null)
				copyFileOld(event.getFile().getFileName(), ext, event.getFile().getInputstream(), fileName, folder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\n-------------------------------------------------------------------\n");
		return folder + "/" + fileName + "." + ext;
	}

	public String copyFileOld(String fileName, String extension, InputStream in, String destinationFileName, String destinationFolder) throws IOException {
		OutputStream output = null;
		String result = null;
		try {
			String datalien = result = destinationFolder + "/" + destinationFileName + "." + extension;
			String lien = pathOld() + datalien;
			System.out.println("lien:");
			System.out.println(lien);
			System.out.println("result:");
			System.out.println(result);
			File file = new File(lien);
			if (file.exists()) {
				file.delete();
			}
			output = new FileOutputStream(new File(lien));
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = in.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
			FacesContextMessages.InfoMessages("Done with Success");
		} finally {
			in.close();
			output.close();
		}
		return result;
	}

	public String pathOld() {
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		return ctx.getRealPath("/");
	}

}