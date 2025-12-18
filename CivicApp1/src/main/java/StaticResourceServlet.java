import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Servlet implementation class StaticResourceServlet
 */
@WebServlet("/uploads/*")
public class StaticResourceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Change this to your actual folder path
    private static final String UPLOAD_DIR = "C:/civicapp_uploads/";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo(); // e.g. "/snapshot.png"

        if (path == null || path.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file specified");
            return;
        }

        // remove leading slash to avoid double slashes in path
        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        File file = new File(UPLOAD_DIR, path);
        if (!file.exists() || file.isDirectory()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Set MIME type
        resp.setContentType(getServletContext().getMimeType(file.getName()));
        resp.setContentLengthLong(file.length());

        try (InputStream in = new FileInputStream(file);
             OutputStream out = resp.getOutputStream()) {
            in.transferTo(out);
        }
    }
}
