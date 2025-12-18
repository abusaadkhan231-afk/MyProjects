import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/SaveImageServlet")
@MultipartConfig
public class SaveImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String UPLOAD_DIR = "C:/civicapp_uploads/";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        File uploadFolder = new File(UPLOAD_DIR);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        String caption = request.getParameter("caption");
        String city = request.getParameter("city");

        String snapshotData = request.getParameter("snapshotData");
        String fileName = null;

        // Snapshot from webcam
        if (snapshotData != null && !snapshotData.isEmpty()) {
            try {
                String base64Image = snapshotData.split(",")[1];
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                fileName = "snapshot_" + System.currentTimeMillis() + ".png";
                File file = new File(uploadFolder, fileName);

                try (OutputStream os = new FileOutputStream(file)) {
                    os.write(imageBytes);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid snapshot data");
                return;
            }
        }

        // File upload (fallback)
        Part filePart = request.getPart("file");
        if (filePart != null && filePart.getSize() > 0) {
            fileName = "upload_" + System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
            File file = new File(uploadFolder, fileName);
            filePart.write(file.getAbsolutePath());
        }

        if (fileName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No image provided");
            return;
        }

        // Build public URL served by StaticResourceServlet
        String publicUrl = request.getContextPath() + "/uploads/" + fileName;

        // Redirect to upload.jsp with preview + details
        response.sendRedirect("upload.jsp?url=" + publicUrl + 
                              "&caption=" + caption + 
                              "&city=" + city);
    }
}
