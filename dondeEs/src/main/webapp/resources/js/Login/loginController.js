'use strict';
angular.module('loginModule', ['ngRoute'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/login', {
			templateUrl : 'resources/login/login.html',
			controller : 'LoginCtrl'
		});
	}])
	.controller('LoginCtrl', ['$scope', '$http', function($scope, $http){
		$scope.user = {
			email : "",
			password : ""
		};
		
		$scope.checkLogin = function() {
			if($scope.user.email != '' && $scope.user.password != ''){
				$http.post("rest/login/checkuser/", $scope.user)
				.success(function(response){
					if(response.code == 200){
						var responseUser = {
							"userId" : response.idUsuario,
							"name" : response.name,
							"lastName1" : response.lastName1,
							"email" : response.email
						};
						$scope.loggedUser = responseUser;
						window.location.href = "/dondeEs/app#/index";
					}else{
						$("#errorMsj").css("visibility", "visible");
					}
				})
				.error(function(response){
					$("#errorMsj").css("visibility", "visible");
					console.log("error" + response.message);
				});
			}else{
				$("#errorMsj").css("visibility", "visible");
			}
		}
	}]);