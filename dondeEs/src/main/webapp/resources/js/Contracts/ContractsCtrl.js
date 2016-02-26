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
			$scope.serviceContacts = [];
		
		$http.get("rest/protected/serviceContact/getAllServiceContact/2").success(function(response){
		$scope.serviceContacts = response.listContracts;
		console.log($scope.serviceContacts);

		});
}]);