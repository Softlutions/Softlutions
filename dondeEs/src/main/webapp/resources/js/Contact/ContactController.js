'use strict';
angular.module('dondeEs.contact', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/contact', {
	    templateUrl: 'resources/Contact/Contact.html',
	    controller: 'ContactCtrl'
	  });
	}])
	.controller('ContactCtrl', ['$scope','$http',function($scope,$http) {
		$scope.$parent.pageTitle = "Donde es - Contacto";

		$scope.sendMessage = function () {
			var dataRequest = {
				userName: $scope.name,
				userEmail: $scope.email,
				message: $scope.message
			};
						
			$http({method: 'POST',url:'rest/protected/sendEmail/sendMessage', data: dataRequest, headers: {'Content-Type': 'application/json'}})
					.success(function(response) {
				if (response.code == 200) {
					$scope.name = "";
					$scope.email = "";
					$scope.message = "";
			    	toastr.success('Contacto', 'El mensaje se envió con éxito.');
				} else {
			    	toastr.error('Contacto', 'Ocurrió un error al enviar el mensaje.');
				}
			});
		}
		
	}]);