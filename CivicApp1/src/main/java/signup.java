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

@WebServlet("/signup")
public class signup extends HttpServlet {
    private static final long serialVersionUID = 1L;

    //email validation regex
    private boolean isValidGmail(String email) {
        return email != null && email.matches("^[\\w.-]+@gmail\\.com$");
    }

    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        if (!isValidGmail(email)) {
            out.println("<h3 style='color:red;'>Invalid Email Address. Please use a valid Gmail address.</h3>");
            return;
        }

        String query = "INSERT INTO users(username, password, email) VALUES (?,?,?)";

        try {
            Class.forName("org.postgresql.Driver");
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/civicapp", "postgres", "root");

            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, email);

            int rowsAffected = pst.executeUpdate();
           

            if (rowsAffected > 0) {
                
            	res.sendRedirect("Login.jsp?signup=success");
            } else {
                
            	 res.sendRedirect("signup.jsp?error=insert_failed");
            }

            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
