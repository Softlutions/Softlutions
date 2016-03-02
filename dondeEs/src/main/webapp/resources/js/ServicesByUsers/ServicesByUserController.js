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
								    	$("#modal-form").modal('hide');	
									$scope.onError = false;
								    if(document.getElementById('inlineCheckbox1').checked){
								    	$scope.objService.state = 1
								    }
								    else{
								    	$scope.objService.state = 0
								    }
								    
									dataCreate={
											serviceCatalog :$scope.requestObject,
											name : $scope.objService.name,
											description: $scope.objService.description,
											state: $scope.objService.state,
											user:$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"))
									}		
									if($scope.objService.name != null && $scope.objService.description != null){
									
										$http({method: 'POST',url:'rest/protected/service/createService', data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
											$scope.services = $scope.services.concat(dataCreate);
										});
									}else{
										
									}
								};
						
						
						}
						
							
		])