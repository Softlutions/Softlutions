'use strict';

angular.module('dondeEs.users', ['ngRoute'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/users', {
			templateUrl : 'resources/users/users.html',
			controller : 'UsersCtrl'
		});
	}])
	.controller('UsersCtrl', ['$scope', '$http', function($scope, $http){
		$scope.users = [];
		
		$http.get("rest/protected/users/getAll")
		.success(function(response){
			if(response.code == 200){
				$scope.users = response.listUser;
			}else{
				console.log("no data");
			}
		})
		.error(function(response){
			console.log("error" + response.message);
		});
	}]);