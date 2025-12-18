

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Servlet implementation class GetPlatformTagsServlet
 */
@WebServlet("/GetPlatformTagsServlet")
public class GetPlatformTagsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 String city = request.getParameter("city");
	        String tag = "";

	        response.setContentType("text/plain");

	        try (Connection conn = DriverManager.getConnection(
	                "jdbc:postgresql://localhost:5432/civicapp", "postgres", "root")) {

	            String sql = "SELECT platform_handle FROM platform_tags WHERE city = ?";
	            PreparedStatement pst = conn.prepareStatement(sql);
	            pst.setString(1, city);
	            ResultSet rs = pst.executeQuery();

	            if (rs.next()) {
	                tag = rs.getString("platform_handle");
	            }
	        } catch (Exception e) {
	        	PrintWriter out = response.getWriter();
	            out.println(e.getMessage());
	        }

	        response.getWriter().write(tag); // Send tag back to AJAX
	    }
	
	

}
