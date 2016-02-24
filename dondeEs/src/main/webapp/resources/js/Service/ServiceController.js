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
								  $scope.init = function() {
								    	
								    	$http.get('rest/protected/serviceCatalog/getAllCatalogService')
										.success(function(response) {

											$scope.serviceCatalogList = response.serviceCatalogList;
											$scope.requestObject.serviceCatalogId = $scope.serviceCatalogList[0].serviceCatalogId;
											
										});
								    	
								    };
								    
								
								    $scope.init();
								$scope.saveService = function(event){
									$scope.onError = false;
									var dataCreate = {
										serviceCatalog :$scope.requestObject,
										name : $scope.user.name,
										description: $scope.user.description,
										//falta pasarle el usuario pero eso se hace con el usuario que esta sesion
									};
//									if($scope.service.idService !=null){
									$http({method: 'POST',url:'rest/protected/service/createService', data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
										console.log("response",response)
										
									});
//									}else{
//										alert('Mal')
//									}
								};
							}
						
							
		])