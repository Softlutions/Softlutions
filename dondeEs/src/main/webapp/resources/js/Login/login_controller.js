'use strict';
angular
		.module('loginModule', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/login', {
				templateUrl : 'resources/login/login.html',
				controller : 'LoginCtrl'
			});
		} ])
		.controller(
				'LoginCtrl',
				[
						'$scope',
						'$http',
						function($scope, $http) {
							//Usuario que se llena en el login
							$scope.user = {
								email : "antoni@ramirez",
								password : "antoni"
							};
							//Request
							$scope.checkLogin = function() {
								$http.post("rest/login/checkuser/",
										$scope.user).success(function(response){
											if(response.code == 200){
												var usuario = {
													"idUser" : response.idUsuario,
													"firstName" : response.firstName,
													"lastName" : response.lastName
												};
												window.location.href = "/dondeEs/app#/view1";
											}else{
												//NO inicio sesion
											}
										});
							}
						}]);

// Aca se crea el app y el controller porque igual solo los va a usar el module
// nadie mas va a usar eso ya que el core de la app estaria en el otro module
