

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.security.MessageDigest;
import java.sql.*;
import java.util.UUID;

@WebServlet("/UpdateProfileServlet")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024)
public class UpdateProfileServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String DB_URL = "jdbc:postgresql://localhost:5432/civicapp";
    private final String DB_USER = "postgres";
    private final String DB_PASS = "root";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        
        
        
   
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("Login.jsp");
            return; // stop execution
        }
        String action = request.getParameter("action");
        String username = (session != null) ? (String) session.getAttribute("username") : null;

       
        switch (action) {
        case "updatePhoto":
            handlePhotoUpdate(request, response, username);
            return;   // stop further execution
        case "updateEmail":
            handleEmailUpdate(request, username);
            response.sendRedirect("profile.jsp");
            return;
        case "updatePassword":
            handlePasswordUpdate(request, username);
            response.sendRedirect("profile.jsp");
            return;
        default:
            response.sendRedirect("profile.jsp?error=UnknownAction");
            return;
    }
    }
    



    private void handlePhotoUpdate(HttpServletRequest request, HttpServletResponse response, String username)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("Login.jsp");
            return;
        }

        try {
            Part filePart = request.getPart("profilePhoto"); // must match form name!
            if (filePart != null && filePart.getSize() > 0) {
                // Generate unique filename
                String fileName = UUID.randomUUID().toString() + "_" + getFileName(filePart);

                // Path: Webapp/images/
                String uploadPath = getServletContext().getRealPath("") + File.separator + "images";
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                // Save file
                filePart.write(uploadPath + File.separator + fileName);

                // Update DB
                updateProfileImage(username, fileName);

                // Update session (so userhome.jsp shows immediately)
                session.setAttribute("Profileimage", fileName);

                response.sendRedirect("userHome.jsp");
            } else {
                response.sendRedirect("profile.jsp?error=NoFile");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("profile.jsp?error=UploadFailed");
        }
    }

    private void updateProfileImage(String username, String fileName) throws Exception {
        Class.forName("org.postgresql.Driver"); // Use PostgreSQL driver
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "UPDATE users SET profile_image=? WHERE username=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, fileName);
                ps.setString(2, username);
                ps.executeUpdate();
            }
        }
    }




	// Extracts filename from Part header
    private String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf("=") + 2, content.length() - 1);
            }
        }
        return "default.png";
    }

 

    private void handleEmailUpdate(HttpServletRequest request, String username) {
        String newEmail = request.getParameter("email");
        if (newEmail == null || !newEmail.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) return;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE users SET email = ? WHERE username = ?");
            stmt.setString(1, newEmail);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePasswordUpdate(HttpServletRequest request, String username) {
        String current = request.getParameter("currentPassword");
        String newPass = request.getParameter("newPassword");
        String confirm = request.getParameter("confirmPassword");

        if (newPass == null || !newPass.equals(confirm)) return;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            PreparedStatement checkStmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (!storedHash.equals(hash(current))) return;

                PreparedStatement updateStmt = conn.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
                updateStmt.setString(1, hash(newPass));
                updateStmt.setString(2, username);
                updateStmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String hash(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashed = md.digest(password.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashed) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
