<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.*,jakarta.servlet.*" %>
<%
    
    String username = (session != null) ? (String) session.getAttribute("username") : null;
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>



<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background-color: #f0f8ff;
            margin: 0;
            padding: 0;
        }
        .container {
            max-width: 700px;
            margin: 50px auto;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            padding: 30px;
        }
        h2 {
            text-align: center;
            color: #0077cc;
            margin-bottom: 30px;
        }
        form {
            margin-bottom: 30px;
            border-top: 2px solid #0077cc;
            padding-top: 20px;
        }
        h3 {
            color: #005fa3;
            margin-bottom: 10px;
        }
        label {
            display: block;
            margin-top: 10px;
            font-weight: bold;
            color: #333;
        }
        input[type="text"],
        input[type="email"],
        input[type="password"],
        input[type="file"] {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        input[type="submit"] {
            background-color: #0077cc;
            color: white;
            border: none;
            padding: 10px 20px;
            margin-top: 15px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        input[type="submit"]:hover {
            background-color: #005fa3;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Welcome, <%= username %></h2>
    
    <!-- Profile Photo Update -->
    <form action="UpdateProfileServlet" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="updatePhoto">
    <input type="file" name="profilePhoto" accept="image/*" required>
    <button type="submit">Upload</button>
</form>
    <!-- Email Update -->
    <form action="UpdateProfileServlet" method="post">
        <h3>Change Email</h3>
        <input type="hidden" name="action" value="updateEmail">
        <label for="email">New Email:</label>
        <input type="email" name="email" required>
        <input type="submit" value="Update Email">
    </form>

    <!-- Password Update -->
    <form action="UpdateProfileServlet" method="post">
        <h3>Change Password</h3>
        <input type="hidden" name="action" value="updatePassword">
        <label for="currentPassword">Current Password:</label>
        <input type="password" name="currentPassword" required>
        <label for="newPassword">New Password:</label>
        <input type="password" name="newPassword" required>
        <label for="confirmPassword">Confirm New Password:</label>
        <input type="password" name="confirmPassword" required>
        <input type="submit" value="Update Password">
    </form>
</div>
</body>
</html>
