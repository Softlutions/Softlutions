<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html lang="en" ng-app="myApp" class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html lang="en" ng-app="myApp" class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html lang="en" ng-app="myApp" class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html lang="en" ng-app="dondeEs" class="no-js">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>Donde es | Inicio</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="resources/css/bootstrap.min.css" rel="stylesheet">
<link
	href="resources/bower_components/jquery-ui/themes/smoothness/jquery-ui.min.css"
	rel="stylesheet">
<link href="resources/font-awesome/css/font-awesome.css"
	rel="stylesheet">
<link href="resources/css/plugins/iCheck/custom.css" rel="stylesheet">
<link href="resources/css/animate.css" rel="stylesheet">
<link href="resources/css/style.css" rel="stylesheet">
<link href="resources/css/eventsPublishStyle.css" rel="stylesheet">
</head>
<body>
	<div id="wrapper">
		<nav class="navbar-default navbar-static-side" role="navigation">
			<div class="sidebar-collapse">
				<ul class="nav metismenu" id="side-menu">
					<li class="nav-header">
						<div class="dropdown profile-element" ng-controller="IndexCtrl">
							<span> <img alt="image" class="img-circle"
								src="http://lorempixel.com/32/32" />
							</span> <a data-toggle="dropdown" class="dropdown-toggle" href="#">
								<span class="clear"> <span class="block m-t-xs"> <strong
										class="font-bold">{{loggedUser.name}}
											{{loggedUser.lastName}}</strong>
								</span> <span class="text-muted text-xs block">{{loggedUser.role.name}}<b
										class="caret"></b></span>
							</span>
							</a>
							<ul class="dropdown-menu animated fadeInRight m-t-xs">
								<li><a href="profile.html">Profile</a></li>
								<li><a href="contacts.html">Contacts</a></li>
								<li><a href="mailbox.html">Mailbox</a></li>
								<li class="divider"></li>
								<li><a href="login.html">Logout</a></li>
							</ul>
						</div>
						<div class="logo-element">IN+</div>
					</li>
					<li><a href="index.html"><i class="fa fa-th-large"></i> <span
							class="nav-label">Eventos</span> <span class="fa arrow"></span></a>
						<ul class="nav nav-second-level collapse">
							<li><a href="/dondeEs/app#/myEvents">Mis eventos</a></li>
						</ul></li>
					<li><a href="#"><i class="fa fa-bar-chart-o"></i> <span
							class="nav-label">Reportes</span><span class="fa arrow"></span></a>
						<ul class="nav nav-second-level collapse">
							<li><a href="graph_flot.html">Reporte x</a></li>
							<li><a href="graph_flot.html">Reporte x</a></li>
						</ul></li>
					<li><a href="mailbox.html"><i class="fa fa-envelope"></i>
							<span class="nav-label">Notificaciones </span><span
							class="label label-warning pull-right">n/x</span></a>
						<ul class="nav nav-second-level collapse">
							<li><a href="mailbox.html">Inbox</a></li>
						</ul></li>
					<li><a href="#"><i class="fa fa-shopping-cart"></i> <span
							class="nav-label">Servicios</span><span class="fa arrow"></span></a>
						<ul class="nav nav-second-level collapse">
							<li><a href="ecommerce_products_grid.html">Mis servicios</a></li>
						</ul></li>
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

	<!--div ng-view></div>
	<div class="container">
		<div>
			version <strong><span app-version></span></strong>
		</div>
	</div--!>
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
	<script src="resources/bower_components/angular-route/angular-route.js"></script>
	<script src="resources/non_bower_components/angular-file-upload-shim.min.js"></script>
	<script src="resources/non_bower_components/angular-file-upload.min.js"></script>

	<script src="resources/js/App/App.js"></script>
	<script src="resources/js/index/index.js"></script>
	<script src="resources/js/users/usersController.js"></script>
	<script src="resources/js/Contracts/ContractsCtrl.js"></script>
	<script src="resources/js/myEvents/myEventsController.js"></script>
	<script src="resources/js/ServicesByUsers/ServicesByUserController.js"></script>
	<script src="resources/js/Events/eventsPublishController.js"></script>
	<script src="resources/js/AnswerInvitation/answerInvitation.js"></script>
	<script src="resources/js/AnswerContract/AnswerContractController.js"></script>
	<script src="resources/js/Commons/Filters.js"></script>
	<script src="resources/js/Auction/auctionController.js"></script>
	<script src="resources/js/Contact/ContactController.js"></script>
	
	<!-- Morris -->
    <script src="resources/js/plugins/morris/raphael-2.1.0.min.js"></script>
    <script src="resources/js/plugins/morris/morris.js"></script>
    
    
    <!-- Google Maps -->
    <script src="http://maps.google.com/maps/api/js?key=AIzaSyDhTmPdseX2jDRUq4svYcpckfvfHGViww0"></script>

    <!-- angular-google-maps -->
    <script src="resources/non_bower_components/lodash.underscore.min.js?v=2.4.1"></script>
    <script src="resources/non_bower_components/angular-google-maps.min.js?v=1.2.2"></script>
</body>
</html>