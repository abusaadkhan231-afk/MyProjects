<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Recent Reports</title>
<style>
#recentReports {
  margin-top: 20px;
  padding: 10px;
}

.report-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 15px;
}

.report-card {
  border: 1px solid #ddd;
  padding: 10px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  text-align: center;
}

.report-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  border-radius: 8px;
}

.caption {
  margin: 8px 0;
  font-weight: bold;
}

.time {
  color: #666;
  font-size: 12px;
}
#Feed_bar {
    position:fixed;
    bottom: 70px;
    width: 50%;
    background-color: #b0dbda;
    display: flex;
    justify-content: space-around;
    padding: 8px ;
    border-top: 1px solid #ccc;
    z-index: 1000;
    margin-left: 100px;
    
}

#Feed_bar a {
    color: #0074cc;
    font-size: 22px;
    text-decoration: none;
}

#Feed_bar a:hover {
    color: #005fa3;
}
</style>

<link rel="stylesheet" href="css/userstyles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

</head>
<body>


<%@ page import="java.sql.*" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("Login.jsp");
        return;
    }
%>

<div id="recentReports">
    <h2>📝 Your Recent Reports</h2>
    <div class="report-list">
        <%
            try {
                Class.forName("org.postgresql.Driver");
                Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/civicapp", "postgres", "root");

                PreparedStatement ps = con.prepareStatement(
                    "SELECT image_url, caption, created_at " +
                    "FROM reports WHERE user_name=? " +
                    "ORDER BY created_at DESC LIMIT 10"
                );
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String img = rs.getString("image_url");
                    String cap = rs.getString("caption");
                    String time = rs.getString("created_at");
        %>
                    <div class="report-card">
                        <img src="<%= img %>" alt="Report Image" class="report-img">
                        <p class="caption"><%= cap %></p>
                        <small class="time"><%= time %></small>
                    </div>
                    
                     <div id="Feed_bar">
            <a href="userHome.jsp"><i class="fas fa-home"></i></a>
            <a href="feed.jsp"><i class="fas fa-plus-square"></i></a>
            <a href="Recent_reports.jsp"><i class="fa-sharp fa-solid fa-envelope"></i></a>
        </div>
        <%
                }
                rs.close();
                ps.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
        %>
            <p>Error loading reports.</p>
        <%
            }
        %>
    </div>
</div>

</body>
</html>