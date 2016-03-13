'use strict';
angular.module('dondeEs.Contact', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/contact', {
	    templateUrl: 'resources/Contact/Contact.html',
	    controller: 'ContactCtrl'
	  });
	}])
	.controller('ContactCtrl', ['$scope','$http',function($scope,$http) {

		$scope.sendMessage = function () {
			var data = {
				userName: $('#txtName').val(),
				userEmail: $('#txtEmail').val(),
				message: $('#txtMessage').val()
			}
			
			console.log(data);
			
			$http({method: 'POST',url:'rest/protected/sendEmail/sendMessage', data, headers: {'Content-Type': 'application/json'}})
					.success(function(response) {
				console.log(response);
			}); 
		}
		
	}]);