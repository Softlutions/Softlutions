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
			
			$scope.listContracts = function() {
				$http.post("rest/login/checkuser/",$scope.user).success(function(response){
					if(response.code == 200){
						
					}else{
						
					}
				});
			}
		
			
			
		}]);