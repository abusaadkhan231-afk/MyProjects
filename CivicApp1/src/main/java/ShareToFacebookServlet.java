import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@WebServlet("/ShareToFacebookServlet")
public class ShareToFacebookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ⚠️ Replace with your actual credentials
    private static final String PAGE_ID = ""; //your id
    private static final String PAGE_ACCESS_TOKEN = ""; // your token
    private static final String UPLOAD_DIR = "C:/civicapp_uploads/";

    

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            
            HttpSession session = request.getSession();

            String imageUrl = request.getParameter("url");  // web path
            String caption = request.getParameter("caption");
            String city = request.getParameter("city");

            // example: username from session (or set default)
            String userName = (session.getAttribute("username") != null) ? 
                               session.getAttribute("username").toString() : "guest";

            // map URL -> local path
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
            File imageFile = new File(UPLOAD_DIR, fileName);

            if (!imageFile.exists()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found on server");
                return;
            }

            // 1️⃣ Send to Facebook
            try {
                String boundary = "===" + System.currentTimeMillis() + "===";
                @SuppressWarnings("deprecation")
				URL url = new URL("https://graph.facebook.com/" + PAGE_ID + "/photos?access_token=" + PAGE_ACCESS_TOKEN);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                try (OutputStream output = conn.getOutputStream();
                     PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"), true)) {

                    // caption
                    writer.append("--").append(boundary).append("\r\n");
                    writer.append("Content-Disposition: form-data; name=\"caption\"\r\n\r\n");
                    writer.append(caption + " " + city).append("\r\n");
                    writer.flush();

                    // file
                    String mimeType = getServletContext().getMimeType(imageFile.getName());
                    if (mimeType == null) mimeType = "application/octet-stream";

                    writer.append("--").append(boundary).append("\r\n");
                    writer.append("Content-Disposition: form-data; name=\"source\"; filename=\"" + imageFile.getName() + "\"\r\n");
                    writer.append("Content-Type: ").append(mimeType).append("\r\n\r\n");
                    writer.flush();

                    try (FileInputStream fis = new FileInputStream(imageFile)) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                        output.flush();
                    }
                    writer.append("\r\n").flush();

                    writer.append("--").append(boundary).append("--").append("\r\n");
                }

                int responseCode = conn.getResponseCode();
                InputStream is = (responseCode >= 400) ? conn.getErrorStream() : conn.getInputStream();
                StringBuilder fbResponse = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fbResponse.append(line);
                    }
                }
                System.out.println("Facebook API response (" + responseCode + "): " + fbResponse);

                if (responseCode == 200) {
                    // ✅ Save only if successful
                    saveReport(userName, city, caption, fileName);
                    response.sendRedirect("success.jsp");
                } else {
                    request.setAttribute("error", "Facebook post failed: " + fbResponse);
                    response.sendRedirect("error.jsp");
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Facebook API error: " + e.getMessage());
                response.sendRedirect("error.jsp");
            }
        }

        private void saveReport(String userName, String city, String caption, String fileName) {
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection con = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/civicapp", "postgres", "root")) {

                    String sql = "INSERT INTO reports (user_name, city, caption, image_url, created_at) VALUES (?, ?, ?, ?, NOW())";
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setString(1, userName);
                        ps.setString(2, city);
                        ps.setString(3, caption);
                        ps.setString(4, "uploads/" + fileName); 
                        ps.executeUpdate();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


