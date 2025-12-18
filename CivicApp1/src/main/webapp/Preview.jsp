<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Upload - Share</title>
<link rel="stylesheet" href="css/upload.css">
</head>
<body>

<div class="container">
    <h2>Review - Share Your Report</h2>

    <%
        // If coming back after SaveImageServlet
        String url = request.getParameter("url");
        String caption = request.getParameter("caption");
        String city = request.getParameter("city");
    
        
    %>

    <% if (url != null) { %>
        <!-- After saving (real file URL) -->
        <img src="<%= url %>" width="350" alt="Uploaded Image">
        <p><b>Caption:</b> <%= caption %></p>
        <p><b>City:</b> <%= city %></p>

       <form action="ShareToFacebookServlet" method="post">
    <input type="hidden" name="url" value="<%= url %>">
    <input type="hidden" name="caption" value="<%= caption %>">
    <button type="submit">Share to Facebook</button>
       </form>
        <button onclick="alert('TODO: Share to Twitter API')">Share to Twitter</button>
        <button onclick="alert('TODO: Share to LinkedIn API')">Share to LinkedIn</button>
        <button onclick="alert('TODO: Share to WhatsApp API')">Share to WhatsApp</button>

        <button class="retakeBtn" onclick="window.location.href='userHome.jsp'">Re-take</button>

   

        <form id="uploadForm" action="ImageImportServlet" method="post" enctype="multipart/form-data">
            <label for="caption">Add a Caption:</label>
            <textarea name="caption" id="caption" rows="3" placeholder="Describe the issue..."></textarea>

            <label for="city">Select Your City:</label>
            <select name="city" id="city">
                <option value="">--Select--</option>
                <option value="Mumbai">Mumbai</option>
                <option value="Delhi">Delhi</option>
                <option value="Bangalore">Bangalore</option>
                <option value="Hyderabad">Hyderabad</option>
                <option value="Pune">Pune</option>
                <option value="Kolkata">Kolkata</option>
            </select>

          

            <button type="submit">Save - Continue</button>
            <button type="button" class="retakeBtn" onclick="window.location.href='userHome.jsp'">Re-take</button>
           
            
        </form>

    <% } else { %>
        <p style="color:red;">No image captured. Please go back and try again.</p>
        <a href="userHome.jsp"><button class="retakeBtn">Go Back</button></a>
    <% } %>
</div>
<script>
// Add city tag automatically from DB 
document.getElementById("city").addEventListener("change", function () {
	const city = this.value; 
	if (!city) return; 
	fetch("GetPlatformTagsServlet?city=" + encodeURIComponent(city)) .then(response => { 
		if (!response.ok) throw new Error("Error fetching tag"); 
		return response.text(); }) .then(tag => { 
			let captionBox = document.getElementById("caption"); 
			let currentValue = captionBox.value.trim();
// Remove any existing handle (anything starting with @) 
			currentValue = currentValue.replace(/@\S+/g, "").trim(); 
// Append the new handle if it exists 
			if (tag) { captionBox.value = currentValue + (currentValue ? " " : "") + tag; } 
			else { 
				captionBox.value = currentValue; } }) 
				.catch(err => { console.error("Tag fetch error:", err);
				});
	});
</script>


</body>
</html>
