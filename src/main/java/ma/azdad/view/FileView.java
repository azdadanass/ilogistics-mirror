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
import java.util.Arrays;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
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

	@Value("${appPath}")
	private String appPath;

	public StreamedContent getFile(String fileName) throws FileNotFoundException {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		File file = new File(appPath + fileName);
		String contentType = ec.getMimeType(fileName);
		int contentLength = (int) file.length();
		log.info("fileName : " + fileName);
		log.info("contentType : " + contentType);
		log.info("contentLength : " + contentLength);
		InputStream stream = new FileInputStream(file);
		return new DefaultStreamedContent(stream, contentType, fileName);
	}

	public StreamedContent getFile(InputStream inputStream, String destFileName) throws FileNotFoundException {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		return new DefaultStreamedContent(inputStream, ec.getMimeType(destFileName), destFileName);
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
			File file = new File(appPath + fileName);
			InputStream stream = new FileInputStream(file);
			return new DefaultStreamedContent(stream, contentType, fileName);
		}
	}

	private void createFolderIfNotExist(String path) {
		new File(path).mkdirs();
	}

	public File handleFileUpload(FileUploadEvent event, String beanName) throws IOException {
		log.info("handleFileUpload");
		String folder = appPath + "files/" + beanName;
		createFolderIfNotExist(folder);
		System.out.println("try create folder " + folder);
		File file = File.createTempFile(beanName + "-", "." + FilenameUtils.getExtension(event.getFile().getFileName()), new File(folder));
		writeFile(file, event);
		return file;
	}

	private void writeFile(File file, FileUploadEvent event) {
		try (InputStream input = event.getFile().getInputstream()) {
			Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded."));
			log.info("success : " + file.getAbsolutePath());
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error uploading", null));
			log.error(e.getMessage());
		}
	}

	// photo upload with compression
	public File handlePhotoUpload(FileUploadEvent event, String beanName, long thresholdBeforeCompression) throws IOException {
		String ext = FilenameUtils.getExtension(event.getFile().getFileName());
		System.out.println("event.getFile().getSize() --> " + event.getFile().getSize());
		return createAndCompressPhoto(event.getFile().getInputstream(), beanName, ext, event.getFile().getSize() > thresholdBeforeCompression); //
	}

	public File createAndCompressPhoto(InputStream inputStream, String beanName, String ext, Boolean enableJpegCompression) throws IOException {
		String folder = appPath + "files/" + beanName;
		File file = File.createTempFile(beanName + "-", "." + ext, new File(folder));
		try (InputStream input = inputStream) {
			if (enableJpegCompression && Arrays.asList("jpg", "jpeg").contains(ext)) {
				// ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
				ImageWriteParam param = writer.getDefaultWriteParam();
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionQuality(0.2f); // Change this, float between 0.0 and 1.0
				ImageOutputStream ios = ImageIO.createImageOutputStream(file);
				writer.setOutput(ios);
				writer.write(null, new IIOImage(ImageIO.read(inputStream), null, null), param);
				writer.dispose();
			} else
				Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Succesful uploading !", null));
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error uploading !", null));
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
