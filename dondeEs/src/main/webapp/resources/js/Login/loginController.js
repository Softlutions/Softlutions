'use strict';
angular.module('loginModule', ['ngRoute', 'ngCookies'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/login', {
			templateUrl : 'resources/login/login.html',
			controller : 'LoginCtrl'
		});
	}])
	.controller('LoginCtrl', ['$scope', '$http', '$cookies', function($scope, $http, $cookies){
		$scope.user = {
			email : "",
			password : "",
			isCript: false
		};
		
		$scope.checkLogin = function() {
			if($scope.user.email != '' && $scope.user.password != ''){
				$http.post("rest/login/checkuser/", $scope.user)
				.success(function(response){
					if(response.code == 200){
						var responseUser = {
							"userId" : response.idUser,
							"name" : response.firstName,
							"lastName" : response.lastName,
							"email" : response.email,
							"role" : response.role
						};
						
						localStorage.setItem("loggedUser", JSON.stringify(responseUser));
						var rememberMe = $('#chkRememberMe').is(':checked');
						
						if(rememberMe){
							var session = {
								email: responseUser.email,
								pass: response.criptPass,
								autologin: true
							};
							
							var expireDate = new Date();
							expireDate.setDate(expireDate.getDate() + 7);
							$cookies.putObject("lastSession", session, {expires: expireDate});
						}
						
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
		
		var sessionCookie = $cookies.getObject("lastSession");
		if(sessionCookie != null){
			$scope.user.email = sessionCookie.email;
			$scope.user.password = sessionCookie.pass;
			$scope.user.isCript = true;
			$('#chkRememberMe').prop('checked', true);

			if(sessionCookie.autologin)
				$scope.checkLogin();
		}
		$scope.forgotPassword = function() {
			if($scope.user.email != null){
				$http.post("rest/login/updatePassword", $scope.user)
				.success(function(response){
					if(response.code == 200){
						toastr.success('La contraseña ha sido modificada correctamente');

					}else{
						toastr.error('La contraseña no ha sido modficiada');

					}
				})
				.error(function(response){
					$("#errorMsj").css("visibility", "visible");
				});
			}else{
				$("#errorMsj").css("visibility", "visible");
				toastr.error('Debe ingresar el correo.', 'Error');
			}
		}
	}]);