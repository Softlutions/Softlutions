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
				
				for(var i=0;i<10;i++){
					var original = $scope.serviceContacts[i%$scope.serviceContacts.length];
					var temp = {};
					temp.service = {};
					
					temp.serviceContractId = original.serviceContractId;
					temp.service.name = original.service.name;
					temp.state = original.state;
					temp.comment = original.comment;
					
					if(temp.serviceContractId % 2 == 0){
						temp.state = 1;
						console.log(1);
					}else{
						if(Math.random() <= 0.5){
							temp.state = 0;
							console.log(0);
						}else{
							temp.state = 2;
							console.log(2);
						}
					}
					
					$scope.serviceContacts.push(temp);
				}
				
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
					btn.removeClass("btn-dangerr");
					btn.addClass("btn-default");
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
				    colors: ['#8a6d3b', '#3c763d', '#a94442'],
				});
			}
		}
		
		$scope.refreshChart();
}]);