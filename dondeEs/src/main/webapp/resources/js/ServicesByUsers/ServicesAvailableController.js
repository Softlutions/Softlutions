'use strict';
angular.module('dondeEs.serviceByUser', [ 'ngRoute', 'ngTable' ]).config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/ServicesAvailable', {
				templateUrl : 'resources/ServicesByUser/ServicesAvailable.html',
				controller : 'ServicesAvailable'
			});
		} ]).controller(
		'ServicesAvailable',
		[ '$scope', '$http', 'ngTableParams', '$filter',
				function($scope, $http, ngTableParams, $filter) {
			
			$scope.requestObject={};
			$scope.currentCatalogId;
			$scope.init = function() {
		    	
		    	$http.get('rest/protected/serviceCatalog/getAllCatalogService')
				.success(function(response) {

					$scope.serviceCatalogList = response.serviceCatalogList;
					$scope.requestObject.serviceCatalogId = $scope.serviceCatalogList[0].serviceCatalogId;
					$scope.requestObject.name = $scope.serviceCatalogList[0].name;
					$scope.currentCatalogId = $scope.requestObject.serviceCatalogId;
					
					console.log($scope.currentCatalogId)
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
		    $scope.contactProvider= function(){
		    	alert('AQUI EL MACHO PEGA EL BRETE DE EL')
				
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