'use strict';
angular
		.module('dondeEs.ContractModule', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/Contracts', {
				templateUrl : 'resources/listContracts/listContracts.html',
				controller : 'ContractsCtrl'
			});
		} ])
		.controller('ContractsCtrl',['$scope','$http',function($scope, $http) {
		
		
		//$scope.listContracts = function(){
			
		$http.get("rest/protected/serviceContact/getAllServiceContact/1").success(function(response){
			
				$scope.serviceContacts = response.listContracts;
				if($scope.serviceContacts.length == 0){
					$('#errorMessage').removeClass('hidden');
					$('#contractTable').addClass('hidden');
				}else{
					$('#contractTable').removeClass('hidden');
					$('#errorMessage').addClass('hidden');
				}
			});
		//}
		
		$scope.serviceInfo = function(){
			$http.get("rest/protected/service/getService/1").success(function(response){
				console.log(response);
				$scope.service = response.service;
			})
		}
		
		$scope.eventInfo = function(){
			
			$http.get("rest/protected/service/getService/2").success(function(response){
				$scope.service = response.service;
			})
		}
		
		$scope.contractService = function(){
			var service = {
					service:$scope.service,
					comment:$('#comment').val(),
					state:0,
					event:{
						eventId:"1",
						description:"despelote",
						name:"fiesta cenfotec",
						user:{
							userId:2,
							email:"def@ghi.com",
							password:"def"
						}
					}
			};
			
			$http({method: 'POST',url:'rest/protected/serviceContact/createServiceContact', data:service, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				$('#myModal').modal('toggle');
				
			})	
		}
}]);