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
		$scope.test = "working test";
		console.log("executing users");
	}]);
console.log("users loaded!");