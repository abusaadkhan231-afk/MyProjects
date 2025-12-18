<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("Login.jsp");
        return;
    }
%>




<%
    
    String profileimage = (String) session.getAttribute("Profileimage");

   

    // Fallback if no image set
    if (profileimage == null || profileimage.trim().isEmpty()) {
        profileimage = "default.png";
    }
%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CivicCam</title>
    <link rel="stylesheet" href="css/userstyles.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>

<div id="header">Welcome to Civic Cam</div>

<div id="Container">
    <!-- Sidebar -->
    <div id="Profile_image" class="sidebar">
        <div class="logout-btn">
            <a href="Login.jsp" class="logout-link">Logout</a>
        </div>
     <img src="images/<%= profileimage %>" alt="Profile Picture">
        <p>Welcome, <%= username %></p>
        <div id="nav_bar">
            <a href="profile.jsp">Profile</a>
            
            <a href="about.html">About</a>
        </div>
    </div>

    <!-- Main Content -->
    <div id="Right_content" class="main">
       <video id="video" autoplay></video>
<canvas id="canvas" style="display:none;"></canvas>

<form id="captureForm" action="upload.jsp" method="post">
    <input type="hidden" name="snapshotData" id="snapshotData">
    <button type="button" id="startBtn">Start Camera</button>
    <button type="button" id="captureBtn" disabled>Capture</button>
    
</form>


    
      
   




        <div id="howToUse">
            <h2>📖 How to Use CivicCam</h2>
            <ul>
                <li><i class="fas fa-camera"></i> <strong>Capture Mode</strong> – Click <b>Capture</b> to open your camera instantly. Snap the moment, review it, and upload in seconds.</li>
                <li><i class="fas fa-image"></i> <strong>Import Mode</strong> – Use <b>Import</b> to select an image from your gallery. Perfect for events you recorded earlier.</li>
                <li><i class="fas fa-pen"></i> <strong>Add Details</strong> – After selecting or capturing, you can add notes to describe what happened.</li>
                <li><i class="fas fa-paper-plane"></i> <strong>Submit - Share</strong> – Send your report to help keep your community informed and safe.</li>
                <li><i class="fas fa-bolt"></i> <strong>Quick - Flexible</strong> – Whether live or from storage, reporting takes less than a minute.</li>
            </ul>
        </div>

        <!-- Bottom Feed Bar -->
        <div id="Feed_bar">
            <a href="userHome.jsp"><i class="fas fa-home"></i></a>
            <a href="feed.jsp"><i class="fas fa-plus-circle"></i></a>
            <a href="Recent_reports.jsp"><i class="fa-sharp fa-solid fa-envelope"></i></a>
        </div>
    </div>
</div>

<!-- JavaScript for Camera -->
<script>
    const video = document.getElementById("video");
    const canvas = document.getElementById("canvas");
    const startBtn = document.getElementById("startBtn");
    const captureBtn = document.getElementById("captureBtn");
    const snapshotData = document.getElementById("snapshotData");
    const captureForm = document.getElementById("captureForm");
    

    let streamStarted = false;

    // Start the camera
    startBtn.addEventListener("click", async () => {
        try {
            const stream = await navigator.mediaDevices.getUserMedia({ video: true });
            video.srcObject = stream;
            streamStarted = true;
            captureBtn.disabled = false; // enable capture button
        } catch (err) {
            alert("Camera access denied or unavailable.");
            console.error(err);
        }
    });

    // Capture snapshot and send to upload.jsp
    captureBtn.addEventListener("click", () => {
        if (!streamStarted) {
            alert("Please start the camera first.");
            return;
        }

        // Match canvas size to video feed
        canvas.width = video.videoWidth;
        canvas.height = video.videoHeight;

        const ctx = canvas.getContext("2d");
        ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

        // Convert to Base64 and put in hidden field
        snapshotData.value = canvas.toDataURL("image/png");

        // Submit form
        captureForm.submit();
    });
    
    
   
</script>

</body>
</html>
