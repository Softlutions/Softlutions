'use strict';
angular
		.module('ContractModule', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/Contracts', {
				templateUrl : 'resources/listContracts/listContracts.html',
				controller : 'ContractsCtrl'
			});
		} ])
		.controller('ContractsCtrl',['$scope','$http',function($scope, $http) {
		
			
			
		}]);