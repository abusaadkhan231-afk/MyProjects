<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
    <title>Sign Up</title>
    <link rel="stylesheet" type="text/css" href="css/styless.css"> 
<!-- Bootstrap CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

<!-- ZMDI (Material Design Iconic Font) -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@zmdi/font@2.2.0/css/material-design-iconic-font.min.css">

</head>
<body>

<div class="container">
    <div class="row">
        <div class="col-md-10 mt-60 mx-md-auto">
            <div class="login-box bg-white pl-lg-5 pl-0">
                <div class="row no-gutters align-items-center">
                    <div class="col-md-6">
                        <div class="form-wrap bg-white">
                            <h4 class="btm-sep pb-3 mb-5">Sign Up</h4>
                            <form class="form" method="post" action="signup">
                                <div class="row">
                                    <div class="col-12">
                                        <div class="form-group position-relative">
                                            <span class="zmdi zmdi-account"></span>
                                            <input type="text" name="username" class="form-control" placeholder="Full Name" required>
                                        </div>
                                    </div>
                                    <div class="col-12">
                                        <div class="form-group position-relative">
                                            <span class="zmdi zmdi-email"></span>
                                            <input type="email" name="email" class="form-control" placeholder="Email Address" required>
                                        </div>
                                    </div>
                                    <div class="col-12">
                                        <div class="form-group position-relative">
                                            <span class="zmdi zmdi-lock"></span>
                                            <input type="password" name="password" class="form-control" placeholder="Password" required>
                                        </div>
                                    </div>
                                    <div class="col-12 mt-30">
                                        <button type="submit" class="btn btn-lg btn-custom btn-dark btn-block">Register</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="col-md-6 text-center content">
                        <div class="border-bottom pb-5 mb-5">
                            <h3 class="c-black">Already a User?</h3>
                            <a href="Login.jsp" class="btn btn-custom">Login</a>
                        </div>
                        <h5 class="c-black mb-4 mt-n1">Or Sign Up With</h5>
                        <div class="socials">
                            <a href="#" class="zmdi zmdi-facebook"></a>
                            <a href="#" class="zmdi zmdi-twitter"></a>
                            <a href="#" class="zmdi zmdi-google"></a>
                            <a href="#" class="zmdi zmdi-instagram"></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>


