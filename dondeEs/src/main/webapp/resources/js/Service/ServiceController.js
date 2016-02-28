'use strict';
angular
		.module('serviceModule', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/service', {
				templateUrl : 'resources/service/service.html',
				controller : 'ServiceCtrl'
			});
		} ])
		.controller(
				'ServiceCtrl',
				[
						'$scope',
						'$http',
						function($scope, $http) {
								
								$scope.services = [];
								$scope.requestObject = {};
								$scope.objService={};
						
								  $scope.init = function() {
								    	
								    	$http.get('rest/protected/serviceCatalog/getAllCatalogService')
										.success(function(response) {

											$scope.serviceCatalogList = response.serviceCatalogList;
											$scope.requestObject.serviceCatalogId = $scope.serviceCatalogList[0].serviceCatalogId;
											
										});
								    	
								    };
								    var dataCreate = {
									};
								    $scope.init();
								    $scope.saveService = function(event){
									$scope.onError = false;
									dataCreate={
											serviceCatalog :$scope.requestObject,
											name : $scope.objService.name,
											description: $scope.objService.description,
											state: $scope.objService.state
									}		
									if($scope.objService.name != null && $scope.objService.description != null){
										$http({method: 'POST',url:'rest/protected/service/createService', data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
											console.log("response",response);
											console.log("response service", $scope.user);
											
										});
									}else{
										alert('Mal')
									}
								};
							}	
		])