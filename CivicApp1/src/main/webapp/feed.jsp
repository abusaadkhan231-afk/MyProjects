<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Recent Reports</title>
<style>
body {
  margin: 0;
  padding: 0;
  font-family: Arial, sans-serif;
  background: #f0f2f5;
}
#recentReports {
  margin: 20px auto;
  padding: 10px;
  width: 80%;
  max-width: 800px;
}
.scroll-container {
  max-height: 80vh;
  overflow-y: auto;
  padding-right: 10px;
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
  color: black;
}
.time {
  color: #666;
  font-size: 12px;
}
#username{
top-margin:12px;
color: blue;
font-size: 13px;
}
#Feed_bar {
  position: fixed;
  bottom: 70px;
  width: 50%;
  background-color: #b0dbda;
  display: flex;
  justify-content: space-around;
  padding: 8px;
  border-top: 1px solid #ccc;
  z-index: 1000;
  left: 25%;
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

<div id="recentReports">
  <h2>📝 ALl Shared  Recent Reports</h2>
  <div class="scroll-container">
    <div class="report-list">
      <%
        try {
          Class.forName("org.postgresql.Driver");
          Connection con = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/civicapp", "postgres", "root");

          PreparedStatement ps = con.prepareStatement(
            "SELECT image_url, caption, created_at,user_name FROM reports ORDER BY created_at DESC LIMIT 10"
          );

          ResultSet rs = ps.executeQuery();

          while (rs.next()) {
            String img = rs.getString("image_url");
            String cap = rs.getString("caption");
            String time = rs.getString("created_at");
            String username= rs.getString("user_name");
      %>
      <div class="report-card">
        <img src="<%= img %>" alt="Report Image" class="report-img">
        <p class="username"><%= username %></p>
        <p class="caption"><%= cap %></p>
        <small class="time"><%= time %></small>
      </div>
      <%
          }
          rs.close();
          ps.close();
          con.close();
        } catch (Exception e) {
          e.printStackTrace();
      %>
      <p>Error loading reports: <%= e.getMessage() %></p>
      <%
        }
      %>
    </div>
  </div>
</div>

<div id="Feed_bar">
  <a href="userHome.jsp"><i class="fas fa-home"></i></a>
  <a href="#"><i class="fas fa-plus-square"></i></a>
  <a href="Recent_reports.jsp"><i class="fa-sharp fa-solid fa-envelope"></i></a>
</div>

</body>
</html>
