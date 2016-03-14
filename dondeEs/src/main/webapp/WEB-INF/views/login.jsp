<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html lang="en" ng-app="myApp" class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html lang="en" ng-app="myApp" class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html lang="en" ng-app="myApp" class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html lang="en" ng-app="loginModule" class="no-js">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Login | Donde es</title>
<meta name="description" content="">

<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link
	href="resources/bower_components/jquery-ui/themes/smoothness/jquery-ui.min.css"
	rel="stylesheet">
<link href="resources/font-awesome/css/font-awesome.css"
	rel="stylesheet">
<link href="resources/css/plugins/iCheck/custom.css" rel="stylesheet">
<link href="resources/css/animate.css" rel="stylesheet">
<link href="resources/css/style.css" rel="stylesheet">
<link href="resources/css/custom-style.css" rel="stylesheet">
</head>
<body>
	<div id="wrapper">
		<nav class="navbar-default navbar-static-side" role="navigation">
			<div class="sidebar-collapse">
				<ul class="nav metismenu" id="side-menu">
					<li class="nav-header">
						<div class="dropdown profile-element">
							<a data-toggle="dropdown" class="dropdown-toggle" href="#"> <span
								class="clear"> <span class="block m-t-xs"> <strong
										class="font-bold">{{loggedUser.name+'
											'+loggedUser.lastName1}}</strong>
								</span> <span class="text-muted text-xs block"><b class="caret"></b></span>
							</span>
							</a>
							<ul class="dropdown-menu animated fadeInRight m-t-xs">
								<li><a href="#">Cerrar sesión</a></li>
							</ul>
						</div>
						<div class="logo-element">IN+</div>
					</li>
					<li class="active"><a href="index.html"><i
							class="fa fa-th-large"></i> <span class="nav-label">Página
								principal</span></a></li>
				</ul>
			</div>
		</nav>
		<div id="page-wrapper" class="gray-bg">
			<div class="row border-bottom">
				<nav class="navbar navbar-static-top white-bg" role="navigation"
					style="margin-bottom: 0"></nav>
			</div>
			<div class="wrapper wrapper-content animated fadeInRight">
				<div class="row">
					<div ng-view></div>
				</div>
			</div>
			<div class="footer">
				<div class="pull-right"></div>
				<div>
					<strong>Copyright</strong> Softlutions &copy; 2016
				</div>
			</div>
		</div>
	</div>
	<!-- Mainly scripts -->
	<script src="resources/js/jquery-2.1.1.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
	<script src="resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>
	<script src="resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>

	<!-- Custom and plugin javascript -->
	<script src="resources/js/inspinia.js"></script>
	<script src="resources/js/plugins/pace/pace.min.js"></script>


	<!-- JQueryUI -->
	<script src="resources/bower_components/jquery-ui/jquery-ui.js"></script>

	<script src="resources/bower_components/angular/angular.js"></script>
	<script src="resources/bower_components/angular-cookies/angular-cookies.js"></script>
	<script src="resources/bower_components/angular-route/angular-route.js"></script>
	<script src="resources/js/Login/loginController.js"></script>
</body>
</html>
