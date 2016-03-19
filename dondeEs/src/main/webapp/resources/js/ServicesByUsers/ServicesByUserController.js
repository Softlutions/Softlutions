'use strict';
angular
		.module('dondeEs.serviceByUser', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/serviceByUser', {
				templateUrl : 'resources/ServicesByUser/ServiceByUser.html',
				controller : 'ServicesByUserCtrl'
			});
		} ])
		.controller(
				'ServicesByUserCtrl',
				[
						'$scope',
						'$http',
						function($scope, $http) {
								$scope.users = [];
								$scope.services = [];
								$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
								$scope.requestObject = {};
								$scope.objService={};
						
								
								$scope.requestObject = {"pageNumber": 0,"pageSize": 0,"direction": "","sortBy": [""],"searchColumn": "string","searchTerm": "","user": {}};
								$http.get('rest/protected/user/getAllService/' + $scope.loggedUser.userId ).success(function(response) {
									$scope.services = response.listService;
									console.log(response.listService);
								});
							
								
								
								  $scope.init = function() {
								    	
								    	$http.get('rest/protected/serviceCatalog/getAllCatalogService')
										.success(function(response) {

											$scope.serviceCatalogList = response.serviceCatalogList;
											$scope.requestObject.serviceCatalogId = $scope.serviceCatalogList[0].serviceCatalogId;
											$scope.requestObject.name = $scope.serviceCatalogList[0].name;
											
											
										});
								    	
								    };
								    var dataCreate = {
									};
								    
								    $scope.init();
								    $scope.saveService = function(event){
								    	console.log($scope.requestObject);
									$scope.onError = false;
								    	$scope.objService.state = 1
								 
								    
									dataCreate={
											serviceCatalog :$scope.requestObject,
											name : $scope.objService.name,
											description: $scope.objService.description,
											state: $scope.objService.state,
											user:$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"))
									}		
									if($scope.objService.name != null && $scope.objService.description != null){
										$("#modal-form").modal('hide');
										$http({method: 'POST',url:'rest/protected/service/createService', data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
											$scope.services = $scope.services.concat(dataCreate);
										    toastr.success('Su servicio se ha registrado en el sistema', 'Registro exitoso');

										});
									}else{
										 setTimeout(function() {					
								                toastr.options = {
								                    closeButton: true,
								                    progressBar: true,
								                    showMethod: 'slideDown',
								                    timeOut: 4000
								                };
								                toastr.error('Todos los campos son requeridos.', 'Error');

								            }, 1300);
									}
								};
						
						
						}
						
							
		])