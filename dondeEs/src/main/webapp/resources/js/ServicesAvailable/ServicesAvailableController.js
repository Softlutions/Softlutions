'use strict';
angular.module('dondeEs.servicesAvailable', [ 'ngRoute', 'ngTable' ]).config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/ServicesAvailable/:id', {
				templateUrl : 'resources/ServicesAvailable/ServicesAvailable.html',
				controller : 'ServicesAvailable'
			});
		} ]).controller(
		'ServicesAvailable',
		[ '$scope', '$http', 'ngTableParams', '$filter', '$routeParams',
				function($scope, $http, ngTableParams, $filter, $routeParams) {
			
			$scope.requestObject={};
			$scope.currentCatalogId;
			$scope.init = function() {
		    	
		    	$http.get('rest/protected/serviceCatalog/getAllCatalogService')
				.success(function(response) {

					$scope.serviceCatalogList = response.serviceCatalogList;
					$scope.requestObject.serviceCatalogId = $scope.serviceCatalogList[0].serviceCatalogId;
					$scope.requestObject.name = $scope.serviceCatalogList[0].name;
					$scope.currentCatalogId = $scope.requestObject.serviceCatalogId;
					
				});
		    	
		    };
		    
		    $scope.init();
		    
		    $scope.getServicesByCatalog = function(pcurrentCatalogId){
		    	$http.get('rest/protected/service/getServiceByCatalog/' + pcurrentCatalogId)
				.success(function(response) {
													$scope.services = response.serviceLists;
					// https://github.com/esvit/ng-table/wiki/Configuring-your-table-with-ngTableParams
					var params = {
						page: 1,	// PAGINA INICIAL
					count: 10, 	// CANTIDAD DE ITEMS POR PAGINA
						sorting: {name: "asc"}
					};
					
					var settings = {
						total: $scope.services.length,	
						counts: [],	
						getData: function($defer, params){
							var fromIndex = (params.page() - 1) * params.count();
							var toIndex = params.page() * params.count();
							
							var subList = $scope.services.slice(fromIndex, toIndex);
							var sortedList = $filter('orderBy')(subList, params.orderBy());	// SOLO SI VAN A ORDENAR POR ALGUN CAMPO
							$defer.resolve(sortedList);
					}
					};
					
					$scope.servicesTable = new ngTableParams(params, settings);
	
				});
		    }
		    
		    $scope.contactProvider= function(pservice){
		    
		    	$http.get("rest/protected/serviceContact/contractService/"+pservice.serviceId+"/"+ $routeParams.id).success(function(response){
					if(response.code == 200){
						toastr.success("Servicio "+pservice.name+" contratado!");
					}else if(response.code == 400){
						toastr.warning("El servicio ya fue contratado");
					}
				}).error(function(response){
					toastr.error("Error", "No se pudo contratar el servicio");
				});
		    }
		    
		    $scope.getServiceByCatalog = function(selectedCatalog){
		    	$scope.currentCatalogId;
		    	if(selectedCatalog == 0){
		    		$scope.currentCatalogId = "";
		    	}else{
		    		$scope.currentCatalogId = selectedCatalog;
		    		$scope.getServicesByCatalog($scope.currentCatalogId);
		    		
		    	}
		    }
		
		} ]);