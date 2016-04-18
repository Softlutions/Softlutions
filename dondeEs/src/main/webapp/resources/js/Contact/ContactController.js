'use strict';
angular.module('dondeEs.contact', ['ngRoute','ngCookies'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/contact', {
	    templateUrl: 'resources/Contact/Contact.html',
	    controller: 'ContactCtrl'
	  });
	}])
	.controller('ContactCtrl', ['$scope','$http','$cookies',function($scope,$http,$cookies) {
		$scope.$parent.pageTitle = "Donde es - Contacto";
		$scope.loggedUser = JSON.parse($cookies.getObject("loggedUser"));
		$scope.name = $scope.loggedUser.name;
		$scope.email = $scope.loggedUser.email;
		
		$scope.sendMessage = function () {
			var dataRequest = {
				userName: $scope.name,
				userEmail: $scope.email,
				message: $scope.message
			};
			$("#btnSendContact").ladda().ladda("start");
						
			$http({method: 'POST',url:'rest/protected/sendEmail/sendMessage', data: dataRequest, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				if (response.code == 200) {
					$scope.name = "";
					$scope.email = "";
					$scope.message = "";
			    	toastr.success('Contacto', 'El mensaje se envió con éxito.');
				} else {
			    	toastr.error('Contacto', 'Ocurrió un error al enviar el mensaje.');
				}
				
				$("#btnSendContact").ladda().ladda("stop");
			}).error(function(err){
				$("#btnSendContact").ladda().ladda("stop");
			});
		}
		
	}]);