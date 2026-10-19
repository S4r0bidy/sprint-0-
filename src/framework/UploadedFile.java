package framework;

import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/** Represents a file received through a multipart/form-data request. */
public final class UploadedFile {
    private final String originalFilename;
    private final String contentType;
    private final byte[] bytes;

    private UploadedFile(String originalFilename, String contentType, byte[] bytes) {
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.bytes = bytes;
    }

    public static UploadedFile fromPart(Part part) throws IOException {
        String filename = part.getSubmittedFileName();
        if (filename == null || filename.trim().isEmpty()) filename = "uploaded-file";
        filename = new File(filename).getName();

        try (InputStream input = part.getInputStream()) {
            byte[] data = new byte[(int) part.getSize()];
            int offset = 0;
            int read;
            while (offset < data.length && (read = input.read(data, offset, data.length - offset)) != -1) {
                offset += read;
            }
            if (offset != data.length) {
                byte[] exactData = new byte[offset];
                System.arraycopy(data, 0, exactData, 0, offset);
                data = exactData;
            }
            return new UploadedFile(filename, part.getContentType(), data);
        }
    }

    public String getOriginalFilename() { return originalFilename; }
    public String getContentType() { return contentType; }
    public long getSize() { return bytes.length; }
    public byte[] getBytes() { return bytes.clone(); }

    public void saveTo(File destination) throws IOException {
        try (FileOutputStream output = new FileOutputStream(destination)) {
            output.write(bytes);
        }
    }
}
