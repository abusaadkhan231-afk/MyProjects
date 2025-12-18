

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public Login() {   
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		 res.setContentType("text/html");
	        PrintWriter out = res.getWriter();
	        
	        try {
	            Class.forName("org.postgresql.Driver");
	            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/civicapp", "postgres", "root");

	    		String username = req.getParameter("username"); 
	    		String password = req.getParameter("password"); 
	    	
	            PreparedStatement pst = con.prepareStatement("select username from users where username=? and password=?");
	            pst.setString(1, username);
	            pst.setString(2, password);
	            
	            ResultSet rs = pst.executeQuery(); 
	           
	            if(rs.next()) {
	            	HttpSession session = req.getSession();
	            	session.setAttribute("username", username);
	            	res.sendRedirect("userHome.jsp"); // Will now have username in session
	            }else {
	            	out.println("<font color:red size:18>Login Failed!!!<br>");
	            	out.println("<a href=Login.jsp>Try Aagain </a>");
	            	
	            }
	            

	           

	        } catch (Exception e) {
	            e.printStackTrace();
	            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
	        } 
	
	
	}

	
}
