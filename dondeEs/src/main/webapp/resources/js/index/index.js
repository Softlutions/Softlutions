'use strict';

angular.module('dondeEs.index', ['ngRoute', 'ngCookies'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/index', {
	    templateUrl: 'resources/index/index.html',
	    controller: 'IndexCtrl'
	  });
	}])
	.controller('IndexCtrl', ['$scope', '$http', '$cookies', '$rootScope', '$filter', '$interval', 
	                          			function($scope, $http, $cookies, $rootScope, $filter, $interval) {
		$scope.DEFAULT_USER_IMAGE = "resources/img/default-profile.png";
		$scope.loggedUser = JSON.parse($cookies.getObject("loggedUser"));
		
		$scope.pageTitle = "Donde es";
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
			isPrestatario : false,
			isInvitado: false
		}
		
		if($scope.loggedUser == null)
			window.location.href = "/dondeEs/#/landingPage";
		
		$scope.logout = function(){
			$http.get("rest/login/logout").success(function(response){
				$cookies.remove("loggedUser");
				window.location.href = "/dondeEs/#/landingPage";
			});
		}
		angular.element(document).ready(function(){
            if($scope.loggedUser != null && $scope.loggedUser.state == 2){
                toastr.warning('Debes cambiar tu contraseña', 'Advertencia');
                setTimeout(function(){window.location.href = "/dondeEs/#/changePassword";}, 2000);
            }
        });
		
		/*$scope.sesionInterval = $interval(function(){
			
		}, 10000);
		
		$scope.$on("$destroy", function(){
			$interval.cancel($scope.sesionInterval);
		});*/
		
		$scope.goToProfile = function(){
			window.location.href = "/dondeEs/app#/userProfile/"+$scope.loggedUser.userId;
		}
		
		$scope.returnLandingPage = function () {
			$cookies.putObject("goToEventsPublish", true);
			window.location.href = "/dondeEs/#/events";
		}
		
		if($scope.loggedUser != null){
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
				case 4:
					$scope.permissions.isInvitado = true;
					break;
			}
			
			$rootScope.$on("$routeChangeStart", function(event, next, current) {
			    if($scope.permissions.isPrestatario){
			    	if(next.originalPath == '/users' || next.originalPath == '/index'){
			    		window.location.href = "/dondeEs/app#/serviceByUser";
			    	}
			    }
			    	
			    if($scope.permissions.isPromotor){
			    	if(next.originalPath == '/users' || next.originalPath == '/serviceByUser'){
			    		window.location.href = "/dondeEs/app#/index";
			    	}
			    }	
			    
			    if($scope.permissions.isInvitado){
		    		window.location.href = "/dondeEs/#/landingPage";
			    }	
			   });
		}
	}]);