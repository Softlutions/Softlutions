'use strict';
angular.module('dondeEs.ContractModule', ['ngRoute', 'ngTable'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/Contracts/:eventId', {
			templateUrl : 'resources/listContracts/listContracts.html',
			controller : 'ContractsCtrl'
		});
	}])
	.controller('ContractsCtrl',['$scope', '$http', '$routeParams', 'ngTableParams', '$filter', function($scope, $http, $routeParams, ngTableParams, $filter) {
		$scope.$parent.pageTitle = "Donde es - Contratos";
		$scope.chartValues = null;
		$scope.serviceContacts = [];
		
		var eventId = $routeParams.eventId;
		
		if(eventId >= 0){
			$http.get("rest/protected/serviceContact/getAllServiceContact/"+eventId).success(function(response){
				$scope.serviceContacts = response.listContracts;
				
				/*for(var i=0;i<10;i++){
					var temp = $scope.serviceContacts[0];
					temp.serviceContractId = i;
					$scope.serviceContacts.push(temp);
				}*/
				
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
				
				if($scope.serviceContacts.length > 0){
					$scope.refreshChart();
				}
			});
		}
		
		$scope.cancel = function(serviceContact){
			$http.post("rest/protected/serviceContact/cancelServiceContact/"+serviceContact.serviceContractId, serviceContact).success(function(response){
				if(response.code == 200){
					serviceContact.state = 2;
					
					var btn = $("#btnCancelService-"+serviceContact.serviceContractId);
					btn.text("Cancelado");
					btn.removeClass("btn-danger");
					btn.addClass("btn-warning");
					btn.prop("disabled", true);
					
					$scope.refreshChart();
				}
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
				    colors: ['#87d6c6', '#54cdb4','#1ab394'],
				});
			}
		}
		
		$scope.refreshChart();
}]);