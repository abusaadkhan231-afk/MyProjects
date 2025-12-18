import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/ImageImportServlet")
@MultipartConfig
public class ImageImportServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "C:/civicapp_uploads/";
    
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        @SuppressWarnings("unused")
		HttpSession session = request.getSession();
        String caption = request.getParameter("caption");
        String city = request.getParameter("city");

        Part filePart = request.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file uploaded");
            return;
        }

        File uploadFolder = new File(UPLOAD_DIR);
        if (!uploadFolder.exists()) uploadFolder.mkdirs();

        String fileName = "import_" + new Timestamp(System.currentTimeMillis()).getTime() + "_" + filePart.getSubmittedFileName();
        File file = new File(uploadFolder, fileName);
        filePart.write(file.getAbsolutePath());

        String publicUrl = request.getContextPath() + "/uploads/" + fileName;

        response.sendRedirect("upload.jsp?url=" + publicUrl +
                              "&caption=" + caption +
                              "&city=" + city);
    }
}
