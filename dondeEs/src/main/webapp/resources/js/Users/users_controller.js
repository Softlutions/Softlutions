'use strict';
angular
		.module('userModule', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/user', {
				templateUrl : 'resources/user/user.html',
				controller : 'UserCtrl'
			});
		} ])
		.controller(
				'UserCtrl',
				[
						'$scope',
						'$http',
						function($scope, $http) {
								$scope.users = [];
								$scope.services = [];
								$scope.requestObject = {"pageNumber": 0,"pageSize": 0,"direction": "","sortBy": [""],"searchColumn": "string","searchTerm": "","user": {}};
								$http.get('rest/protected/user/getAllService/1').success(function(response) {
									$scope.services = response.listService;
									console.log(response.listService);
								});
							}
						
							
		])