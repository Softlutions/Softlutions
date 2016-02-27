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
		
		$scope.listContracts = function(){
			var eventId = $('#eventSelect').val();
			$http.get("rest/protected/serviceContact/getAllServiceContact/"+eventId).success(function(response){
			$scope.serviceContacts = response.listContracts;
			if($scope.serviceContacts.length == 0){
				$('#errorMessage').removeClass('hidden');
				$('#contractTable').addClass('hidden');
			}else{
				$('#contractTable').removeClass('hidden');
				$('#errorMessage').addClass('hidden');
			}
			
			});
		}
		
		$scope.contractService = function(){
			var eventId = $('#eventSelect').val();
			$http.get("rest/protected/service/getService/"+eventId).success(function(response){
				$scope.service = response.service;
			})
		}
}]);