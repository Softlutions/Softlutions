'use strict';
angular.module('dondeEs.contact', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/contact', {
	    templateUrl: 'resources/Contact/Contact.html',
	    controller: 'ContactCtrl'
	  });
	}])
	.controller('ContactCtrl', ['$scope','$http',function($scope,$http) {

		$scope.sendMessage = function () {
			var data = {
				userName: $scope.name,
				userEmail: $scope.email,
				message: $scope.message
			}
						
			$http({method: 'POST',url:'rest/protected/sendEmail/sendMessage', data, headers: {'Content-Type': 'application/json'}})
					.success(function(response) {
				if (response.code == 200) {
			    	toastr.options = {
			    			closeButton: true,
		                    progressBar: true,
		                    showMethod: 'slideDown'
			        };
			    	toastr.success('Contacto', 'El mensaje se envió con éxito.');
				} else {
			    	toastr.options = {
			    			closeButton: true,
		                    progressBar: true,
		                    showMethod: 'slideDown'
			        };
			    	toastr.error('Contacto', 'Ocurrió un error al enviar el mensaje.');
				}
			}); 
		}
		
	}]);