<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html lang="en" ng-app="myApp" class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html lang="en" ng-app="myApp" class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html lang="en" ng-app="myApp" class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html lang="en" ng-app="landingPageModule" class="no-js">
<!--<![endif]-->
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<title>Donde es</title>
		<meta name="description" content="">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta name="description" content="">
		<meta name="author" content="">
		
		<link href="resources/css/bootstrap.min.css" rel="stylesheet">
		<link href="resources/bower_components/jquery-ui/themes/smoothness/jquery-ui.min.css" rel="stylesheet">
		<link href="resources/font-awesome/css/font-awesome.css" rel="stylesheet">
		<link href="resources/css/plugins/iCheck/custom.css" rel="stylesheet">
		<link href="resources/css/animate.css" rel="stylesheet">
		<link href="resources/css/style.css" rel="stylesheet">
		<link href="resources/css/custom-style.css" rel="stylesheet">
		<link href="resources/bower_components/toastr/toastr.css" rel="stylesheet">
		<link href="resources/css/eventsPublishStyle.css" rel="stylesheet">
		<link href="resources/css/plugins/slick/slick.css" rel="stylesheet">
		<link href="resources/css/plugins/slick/slick-theme.css" rel="stylesheet">
	</head>

	<body id="page-top" class="landing-page">
	
		<div ng-view></div>
	
		<!-- Mainly scripts -->
		<script src="resources/js/jquery-2.1.1.js"></script>
		<script src="resources/js/bootstrap.min.js"></script>
		<script src="resources/js/plugins/metisMenu/jquery.metisMenu.js"></script>
		<script src="resources/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
	
		<!-- Custom and plugin javascript -->
		<script src="resources/js/inspinia.js"></script>
		<script src="resources/js/plugins/pace/pace.min.js"></script>
		<script src="resources/bower_components/toastr/toastr.js"></script>
		<script src="//cdnjs.cloudflare.com/ajax/libs/wow/0.1.12/wow.min.js"></script>
	
		<!-- JQueryUI -->
		<script src="resources/bower_components/jquery-ui/jquery-ui.js"></script>
	
		<script src="resources/bower_components/angular/angular.js"></script>
		<script	src="resources/bower_components/angular-cookies/angular-cookies.js"></script>
		<script src="resources/bower_components/angular-route/angular-route.js"></script>
		
		<script src="resources/bower_components/ng-table/dist/ng-table.js"></script>
		
		<script src="resources/bower_components/moment/min/moment.min.js"></script>
		<script src="resources/bower_components/moment/min/moment-with-locales.js"></script>
		
		<script	src="resources/non_bower_components/angular-file-upload-shim.min.js"></script>
		<script src="resources/non_bower_components/angular-file-upload.min.js"></script>
	
		<script src="resources/bower_components/ng-file-upload/ng-file-upload-shim.js"></script>
		<script src="resources/bower_components/ng-file-upload/ng-file-upload.js"></script>
		
		<script src="resources/non_bower_components/slick.min.js"></script>
		<script src="resources/non_bower_components/angular-slick.min.js"></script>
		<script src="resources/bower_components/toastr/toastr.js"></script>
		<script src="resources/bower_components/ng-table/dist/ng-table.js"></script>
		
		<!-- Controllers -->
		<script src="resources/js/LandingPage/landingPageController.js"></script>
		<script src="resources/js/LandingPage/viewEventController.js"></script>
	</body>
</html>
