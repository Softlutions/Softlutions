'use strict';
angular
	.module('dondeEs.serviceByUser',['ngRoute', 'ngTable', 'ngCookies'])
	.config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/serviceByUser', {
			templateUrl : 'resources/ServicesByUser/ServiceByUser.html',
			controller : 'ServicesByUserCtrl'
		});
	}])
	.controller('ServicesByUserCtrl',['$scope','$http','ngTableParams', '$filter', '$cookies', function($scope, $http, ngTableParams, $filter, $cookies) {
		$scope.$parent.pageTitle = "Donde es - Mis servicios";
		$scope.users = [];
		$scope.services = [];
		$scope.loggedUser = $scope.$parent.getLoggedUser();
		$scope.requestObject = {};
		$scope.objService={};
		$scope.creating = true;
		$scope.showError = true;
		$scope.serviceModal = {};

		if($scope.loggedUser == null)
			window.location.href = "/dondeEs/#/landingPage";

		if(!$scope.$parent.permissions.isAdmin){
			$http.get('rest/protected/user/getAllService/' + $scope.loggedUser.userId ).success(function(response) {
				$scope.services = response.listService;
				// https://github.com/esvit/ng-table/wiki/Configuring-your-table-with-ngTableParams
				var params = {
					page: 1,	// PAGINA INICIAL
					count: 10 	// CANTIDAD DE ITEMS POR PAGINA
					//sorting: {name: "asc"}
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
		}else{
			$http.get('rest/protected/service/getAllService').success(function(response) {
				$scope.services = response.serviceLists;
			});
			
			if($scope.services.length==0){
				$scope.showError = false;
			}else{
				$scope.showError = true;
			}
		};
		
		$scope.init = function() {
	    	
	    	$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				$scope.serviceCatalogList = response.serviceCatalogList;
				$scope.requestObject = $scope.serviceCatalogList[0];											
			});		 
	    	$('#modal-form').on('hidden.bs.modal', function () {
	    		$scope.requestObject = $scope.serviceCatalogList[0];
				$scope.objService.name = '';
				$scope.objService.description = '';
	    	})
	    };
		    	
	    var dataCreate = {};
		    
	    $scope.setCreate = function(){
	    	$scope.creating = true;
	    };
	    
	    $scope.init();
	    
	    $scope.saveService = function(event){
	    	$scope.creating = true;
			$scope.onError = false;
		    $scope.objService.state = 1
		 									    
			dataCreate={
					serviceCatalog :$scope.requestObject,
					name : $scope.objService.name,
					description: $scope.objService.description,
					state: $scope.objService.state,
					user:$scope.loggedUser
			}		
			if($scope.objService.name != null && $scope.objService.description != null){
				$("#modal-form").modal('hide');
				$http({method: 'POST',url:'rest/protected/service/createService', data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
					$scope.services.push(dataCreate);
				    toastr.success('Su servicio se ha registrado en el sistema', 'Registro exitoso');
				    $scope.servicesTable.reload();
				});
			}else{
				 setTimeout(function() {					
					 toastr.error('Todos los campos son requeridos.', 'Error');
				 }, 1300);
			}
		};
		
		$scope.loadInfo = function(service){
			$scope.creating = false;
			$scope.requestObject = service.serviceCatalog;
			$scope.objService.serviceId = service.serviceId;
			$scope.objService.name = service.name;
			$scope.objService.description = service.description;
			$("#updateSelect").val(service.state);
		};
		
		$scope.updateService = function(event,index){
			dataCreate={
					serviceId : $scope.objService.serviceId,
					serviceCatalog :$scope.requestObject,
					name : $scope.objService.name,
					description: $scope.objService.description,
					state: $scope.objService.state,
					user:$scope.loggedUser
			}
			$http.put('rest/protected/service/updateService',dataCreate).success(function(response) {
				var serviceIndex = $scope.services.map(function (x){return x.serviceId}).indexOf(dataCreate.serviceId);
				$scope.services[serviceIndex] = dataCreate;
				toastr.success('Su servicio se ha modificado en el sistema', 'Modificación exitosa');
				$scope.servicesTable.reload();
				$("#modal-form").modal('toggle');
			});
		};
		
		//validation error
		$scope.validationError = function(){
			toastr.warning('Algunos campos no cumplen con los requisitos');
		}
	}]);