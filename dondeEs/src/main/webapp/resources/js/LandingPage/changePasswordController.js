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
		$scope.loggedUser = JSON.parse($cookies.getObject("loggedUser"));
		
		if($scope.loggedUser.state != 2)
			redirect();
		
		$scope.changePasswordRequired = function(requiredPassword){
			if(requiredPassword.password != null && requiredPassword.confirmPassword != null){
				if(requiredPassword.password.length >= 8){
					if(requiredPassword.password == requiredPassword.confirmPassword){
						var request = {
							email: $scope.loggedUser.email,
							password: requiredPassword.confirmPassword
						}
						
						$http.post("rest/login/updatePasswordRequired/", request)
						.success(function(response){
							if(response.code == 200){
								$scope.loggedUser.state = 1;
								$cookies.putObject("loggedUser", JSON.stringify($scope.loggedUser));
								toastr.success(response.codeMessage, 'Exito!');
								
								setTimeout(function(){
									redirect();
								}, 500);
							}else{
								toastr.error('La contraseña no ha podido ser cambiada', 'Error');
							}
						})
					}else{
						toastr.warning('Las contraseñas no coinciden', 'Error');
					}
				}else{
					toastr.warning('La contraseña debe tener minimo 8 caracteres', 'Error');
				}
			}else{
				toastr.warning('Debes llenar todos los campos', 'Error');
			}
		}
		
		function redirect(){
			switch ($scope.loggedUser.role.roleId) {
				case 1:
					window.location.href = "/dondeEs/app#/users";
					break;
				case 2:
					window.location.href = "/dondeEs/app#/serviceByUser";
					break;
				case 3:
					window.location.href = "/dondeEs/app#/index";
					break;
				case 4:
					window.location.href = "#/landingPage";
					break;
			}
		}
	}]);