<%@ include file="/common/taglib.jsp"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />
<link rel="icon" type="image/x-icon" href="${contextPath}/resources/img/favicon.ico" />
<title>Modiphians</title>

<link rel='stylesheet' href='${contextPath}/resources/assets/bootstrap-3.3.7-dist/css/bootstrap.min.css' type='text/css' media='all' />
<link rel='stylesheet' href='${contextPath}/resources/assets/font-awesome-4.7.0/css/font-awesome.min.css' type='text/css' media='all' />
<link rel='stylesheet' href='${contextPath}/resources/assets/slick/slick.css' type='text/css' media='all' />
<link rel='stylesheet' href='${contextPath}/resources/assets/slick/slick-theme.css' type='text/css' media='all' />
<link rel='stylesheet' href='${contextPath}/resources/assets/fancybox-3.0/dist/jquery.fancybox.min.css' type='text/css' media='all' />
<link rel='stylesheet' href='${contextPath}/resources/css/all.css?ver=0.1' type='text/css' media='all' />

<link href="https://fonts.googleapis.com/css?family=Open+Sans" rel="stylesheet">
<link href="https://fonts.googleapis.com/css?family=PT+Sans" rel="stylesheet">

</head>

<body class="modiphius-bg">
    <div class="header"></div>
    <div class="content page-login page-form">
        
        <div class="container">
            <div class="row">
                <div class="col col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <div class="breadcrumps">
                        <a href="home.html">Home</a> / Login
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col col-lg-12 col-md-12 col-sm-12 col-xs-12">
                    <div class="block-header indigo" style="text-align: center;">
                        <i class="fa fa-lock"></i> <h2>Enter the site</h2>
                    </div>
                    <div id="register" class="register">
                        <div class="row">
                            <div class="col col-lg-6 col-lg-offset-3 col-md-8 col-md-offset-2 col-sm-10 col-sm-offset-1 col-xs-12">
                                <form class="form-horizontal ${error != null ? 'has-error' : ''}" method="POST" action="${contextPath}/login">
                                    <div class="form-group">
                                        <label for="inputEmail" class="col-lg-4 col-md-4 col-sm-4 control-label">Email</label>
                                        <div class="col-lg-7 col-md-7 col-sm-7">
                                            <input name="username" class="form-control" id="inputEmail" placeholder="Email">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputPassword" class="col-lg-4 col-md-4 col-sm-4 control-label">Password</label>
                                        <div class="col-lg-7 col-md-7 col-sm-7">
                                            <input name="password" type="password" class="form-control" id="inputPassword" placeholder="Password">
                                            <span class="error">${error}</span> 
                                        </div>
                                    </div>
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    
                                    <div class="form-group">
                                        <div class="col-lg-offset-4 col-lg-7 col-md-offset-4 col-md-7 col-sm-offset-4 col-sm-7">
                                            <button type="submit" class="btn">Sign in</button>&nbsp;&nbsp;&nbsp;<a href="#">Forgot password?</a>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="footer"></div>
    
    <script type='text/javascript' src='${contextPath}/resources/js/jquery-3.2.0.min.js'></script>
    <script type='text/javascript' src='${contextPath}/resources/assets/bootstrap-3.3.7-dist/js/bootstrap.min.js'></script>
    <script type='text/javascript' src='${contextPath}/resources/assets/slick/slick.min.js'></script>
    <script type='text/javascript' src='${contextPath}/resources/assets/fancybox-3.0/dist/jquery.fancybox.min.js'></script>
    <script type='text/javascript' src='${contextPath}/resources/js/script.js?ver=0.1'></script>

</body>
</html>