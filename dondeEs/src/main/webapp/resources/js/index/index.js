'use strict';

angular.module('dondeEs.index', ['ngRoute', 'ngCookies'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/index', {
	    templateUrl: 'resources/index/index.html',
	    controller: 'IndexCtrl'
	  });
	}])
	.controller('IndexCtrl', ['$scope','$http','$cookies',function($scope,$http,$cookies) {
		
		$scope.logout = function(){
			$http.get("rest/login/logout").success(function(response){
				var sessionCookie = $cookies.getObject("lastSession");
				if(sessionCookie != null){
					sessionCookie.autologin = false;
					$cookies.putObject("lastSession", sessionCookie);
				}
				
				window.location.href = "/dondeEs/#/login";
			});
		}
	}]);