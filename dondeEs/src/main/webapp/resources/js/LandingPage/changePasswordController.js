'use strict';
angular.module('landingPageModule.changePassword', ['ngRoute', 'ngCookies'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/changePassword', {
			templateUrl : 'resources/landingPage/changePassword.html',
			controller : 'LandingChangePasswordController'
		});
	}])
	.controller('LandingChangePasswordController', ['$scope', '$http', '$cookies', '$rootScope', '$location', '$filter',
	                                		function($scope, $http, $cookies, $rootScope, $location, $filter){
		$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
		console.log($scope.loggedUser);
		
		$scope.changePasswordRequired = function(requiredPassword){
			if(requiredPassword.password != null && requiredPassword.confirmPassword != null){
				if(requiredPassword.password == requiredPassword.confirmPassword){
					var request = {
							email: $scope.loggedUser.email,
							password: requiredPassword.confirmPassword
					}
					$http.post("rest/login/updatePasswordRequired/", request)
					.success(function(response){
						if(response.code == 200){
							$scope.loggedUser.state = 1;
							localStorage.setItem("loggedUser", JSON.stringify($scope.loggedUser));
							toastr.success(response.codeMessage, 'Exito!');
							setTimeout(function(){window.location.href = "#/landingPage";}, 2000);
							//$('#modal-changePassword').modal('hide');
						}else{
							toastr.error('La contraseña no ha podido ser cambiada', 'Error');
						}
					})
				}else{
					toastr.error('Las contraseñas no coinciden', 'Error');
				}
			}else{
				toastr.error('Debes llenar todos los campos', 'Error');
			}
		}
	}]);