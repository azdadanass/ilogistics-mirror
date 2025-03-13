package ma.azdad.utils;


import org.primefaces.model.UploadedFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class PrimeFacesUploadedFile implements UploadedFile {
    private final MultipartFile multipartFile;

    public PrimeFacesUploadedFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    @Override
    public String getFileName() {
        return multipartFile.getOriginalFilename();
    }

    @Override
    public long getSize() {
        return multipartFile.getSize();
    }

    @Override
    public String getContentType() {
        return multipartFile.getContentType();
    }

	@Override
	public InputStream getInputstream() throws IOException {
		return  multipartFile.getInputStream();
	}

	@Override
	public byte[] getContents() {
		try {
			return multipartFile.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void write(String filePath) throws Exception {
		
	}
}
