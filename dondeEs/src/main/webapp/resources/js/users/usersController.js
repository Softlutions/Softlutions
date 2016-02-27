'use strict';
console.log("loading users...");
angular.module('dondeEs.users', ['ngRoute'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/users', {
			templateUrl : 'resources/users/users.html',
			controller : 'UsersCtrl'
		});
	}])
	.controller('UsersCtrl', ['$scope', '$http', function($scope, $http){
		$scope.users = [];
		console.log("executing users");
		
		$http.get("rest/protected/users/getAll")
		.success(function(response){
			if(response.code == 200){
				$scope.users = response.listUser;
				console.log($scope.users);
			}else{
				console.log("no data");
				//$("#errorMsj").css("visibility", "visible");
			}
		})
		.error(function(response){
			//$("#errorMsj").css("visibility", "visible");
			console.log("error" + response.message);
		});
		
		//console.log($scope.users);
	}]);
console.log("users loaded!");