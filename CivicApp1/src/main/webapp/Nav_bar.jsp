<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("Login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome User</title>
    <link rel="stylesheet" href="home.css">
</head>
<body>
    <div class="welcome-container">
        <img src="images/default-profile.png" alt="Profile" class="profile-pic">
        <h2>Welcome, <%= username %> 👋</h2>
    </div>
</body>
</html>
