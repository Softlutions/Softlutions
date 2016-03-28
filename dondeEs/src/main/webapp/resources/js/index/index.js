'use strict';

angular.module('dondeEs.index', ['ngRoute', 'ngCookies'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/index', {
	    templateUrl: 'resources/index/index.html',
	    controller: 'IndexCtrl'
	  });
	}])
	.controller('IndexCtrl', ['$scope','$http','$cookies',function($scope,$http,$cookies) {
		$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
		$scope.permissions = {
			comentarEventos: false,
			gestionarEventosPropios: false,
			gestionarRecordatorios: false,
			gestionarRoles: false,
			gestionarUsuarios: false,
			gestionarContrataciones: false,
			verEventosPublicados: false,
			gestionarServicios: false,
			gestionarSubastas: false,
			invitado: false,
			isAdmin : false,
			isPromotor : false,
			isPrestatario : false
		}
		
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
		
		for (var i = 0; i < $scope.loggedUser.role.permissions.length; i++) { 
			switch ($scope.loggedUser.role.permissions[i].permissionId){
			case 1: // Comentar eventos
				$scope.permissions.comentarEventos = true;
				break;
			case 2: // Gestionar eventos propios
				$scope.permissions.gestionarEventosPropios = true;
				break;
			case 3: // Gestionar recordatorios
				$scope.permissions.gestionarRecordatorios = true;
				break;
			case 4: // Gestionar roles
				$scope.permissions.gestionarRoles = true;
				break;
			case 5: // Gestionar usuarios
				$scope.permissions.gestionarUsuarios = true;
				break;
			case 6: // Gestionar contrataciones
				$scope.permissions.gestionarContrataciones = true;
				break;
			case 7: // Ver eventos publicados
				$scope.permissions.verEventosPublicados = true;
				break;
			case 8: // Gestionar servicios
				$scope.permissions.gestionarServicios = true;
				break;
			case 9: // Gestionar subasta
				$scope.permissions.gestionarSubastas = true;
				break;
			case 10: // Invitado
				$scope.permissions.invitado = true;
					break;
			}
		}
		
		switch ($scope.loggedUser.role.roleId) {
		case 1:
			$scope.permissions.isAdmin = true;
			break;
		case 2:
			$scope.permissions.isPrestatario = true;
			break;
		case 3:
			$scope.permissions.isPromotor = true;
			break;
		}
	}]);