'use strict';

angular.module('dondeEs.index', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/index', {
	    templateUrl: 'resources/index/index.html',
	    controller: 'IndexCtrl'
	  });
	}])
	.controller('IndexCtrl', ['$scope','$http',function($scope,$http,$upload) {
		$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
		//console.log($scope.loggedUser);
	}]);