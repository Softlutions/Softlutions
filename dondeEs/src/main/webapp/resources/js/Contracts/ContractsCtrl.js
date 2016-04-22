'use strict';
angular.module('dondeEs.ContractModule', ['ngRoute', 'ngTable'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/Contracts/:eventId', {
			templateUrl : 'resources/listContracts/listContracts.html',
			controller : 'ContractsCtrl'
		});
	}])
	.controller('ContractsCtrl',['$scope', '$http', '$routeParams', 'ngTableParams', '$filter', '$interval', function($scope, $http, $routeParams, ngTableParams, $filter, $interval) {
		$scope.$parent.pageTitle = "Donde es - Contratos";
		$scope.chartValues = null;
		$scope.serviceContacts = [];
		$scope.pauseInterval = false;
		
		var params = {
			page: 1,
			count: 8,
			sorting: {name: "asc"}
		};
		
		var settings = {
			total: $scope.serviceContacts.length,	
			counts: [],	
			getData: function($defer, params){
				var fromIndex = (params.page() - 1) * params.count();
				var toIndex = params.page() * params.count();
				
				var subList = $scope.serviceContacts.slice(fromIndex, toIndex);
				var sortedList = $filter('orderBy')(subList, params.orderBy());
				$defer.resolve(sortedList);
			}
		};
		
		$scope.contractsTable = new ngTableParams(params, settings);
		
		var eventId = $routeParams.eventId;
		
		if(eventId > 0){
			$scope.refreshInterval = $interval(function(){
				if(!$scope.pauseInterval)
					getContracts();
			}, 3000);
			
			getContracts();
		}else{
			window.location.href="app#/index";
		}
		
		function getContracts(){
			$http.get("rest/protected/serviceContact/getAllServiceContact/"+eventId).success(function(response){
				$scope.serviceContacts = response.listContracts;
				$scope.contractsTable.reload();
				
				if($scope.serviceContacts.length > 0){
					$scope.refreshChart();
				}
			});
		}
		
		$scope.$on("$destroy", function(){
			$interval.cancel($scope.refreshInterval);
		});
		
		$scope.cancel = function(serviceContact){
			$("#btnCancelService-"+serviceContact.serviceContractId).ladda().ladda("start");
			$scope.pauseInterval = true;
			
			$http.post("rest/protected/serviceContact/cancelServiceContact/"+serviceContact.serviceContractId, serviceContact).success(function(response){
				if(response.code == 200){
					serviceContact.state = 2;
					$scope.refreshChart();
					toastr.success("El contrato fue cancelado, se notificará al prestatario");
				}else{
					toastr.error("No se pudo cancelar el contrato");
				}
				
				$("#btnCancelService-"+serviceContact.serviceContractId).ladda().ladda("stop");
				$scope.pauseInterval = false;
			});
		}
		
		$scope.refreshChart = function(){
			var contractsLeft = 0;
			var contractsOk = 0;
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
			}else if(contractsLeft > 0 || contractsOk > 0 || contractsCanceled > 0){
				$scope.chartValues = Morris.Donut({
				    element: 'contracts-state-chart',
				    data: [
				           { label: "Pendientes", value: contractsLeft },
				           { label: "Concretados", value: contractsOk },
				           { label: "Cancelados", value: contractsCanceled }
		            ],
				    resize: false,
				    colors: ['#8a6d3b', '#3c763d', '#a94442'],
				});
			}
		}
		
		$scope.refreshChart();
}]);