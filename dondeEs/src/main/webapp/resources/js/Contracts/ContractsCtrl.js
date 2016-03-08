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
		$scope.chartValues = null;
		console.log("dawdaw");
		
		$scope.listContracts = function(idEvent){
			$http.get("rest/protected/serviceContact/getAllServiceContact/"+idEvent).success(function(response){
				$scope.serviceContacts = response.listContracts;
				
				if($scope.serviceContacts.length == 0){
					$('#errorMessage').removeClass('hidden');
					$('#contractTable').addClass('hidden');
					$('#contracts-state-chart').addClass('hidden');
				}else{
					$('#contractTable').removeClass('hidden');
					$('#errorMessage').addClass('hidden');
					console.log($scope.serviceContacts);
					$scope.refreshChart();
				}
			});
		}
		$scope.listContracts(1);
		
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
		
		$scope.cancel = function(serviceContact){
			$http.post("rest/protected/serviceContact/cancelServiceContact/"+serviceContact.serviceContractId, serviceContact).success(function(response){
				serviceContact.state = 2;

				$("#btnCancelService-"+serviceContact.serviceContractId).text("Cancelado");
				$("#btnCancelService-"+serviceContact.serviceContractId).removeClass("btn-danger");
				$("#btnCancelService-"+serviceContact.serviceContractId).addClass("btn-warning");
				$("#btnCancelService-"+serviceContact.serviceContractId).prop("disabled", true);
				
				$scope.refreshChart();
			});
		}
		
		$scope.refreshChart = function(){
			console.log("ok");
			var contractsLeft = 1;
			var contractsOk = 2;
			var contractsCanceled = 0;
			
			$('#contracts-state-chart').removeClass('hidden');
			
			angular.forEach($scope.serviceContacts, function(value){
				if(value.state == 0)
					contractsLeft++;
					
				if(value.state == 1)
					contractsOk++;
				
				if(value.state == 2)
					contractsCanceled++;
			});
			
			if($scope.chartValues != null){
				$scope.chartValues.setData([
					{ label: "Pendientes", value: contractsLeft },
					{ label: "Concretados", value: contractsOk },
					{ label: "Cancelados", value: contractsCanceled }
				]);
			}else{
				$scope.chartValues = Morris.Donut({
				    element: 'contracts-state-chart',
				    data: [
				           { label: "Pendientes", value: contractsLeft },
				           { label: "Concretados", value: contractsOk },
				           { label: "Cancelados", value: contractsCanceled }
		            ],
				    resize: true,
				    colors: ['#87d6c6', '#54cdb4','#1ab394'],
				});
			}
		}
}]);