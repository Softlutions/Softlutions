'use strict';
console.log("loading index...");
angular.module('dondeEs.index', ['ngRoute'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/index', {
	    templateUrl: 'resources/index/index.html',
	    controller: 'IndexCtrl'
	  });
	}])
	.controller('IndexCtrl', ['$scope','$http',function($scope,$http,$upload) {
		console.log("executing index");
	}]);
console.log("index loaded!");