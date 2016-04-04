'use strict';
angular
	.module('dondeEs.EventParticipant', ['ngRoute','ngTable'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/eventParticipants/:id', {
			templateUrl : 'resources/EventParticipants/eventParticipantsList.html',
			controller : 'EventParticipantCtrl'
		});
	} ])
	.controller('EventParticipantCtrl',['$scope','$http','$routeParams','ngTableParams',function($scope, $http, $routeParams,ngTableParams) {
		
		$http.get('rest/protected/eventParticipant/getAllEventParticipants/'+$routeParams.id).success(function(response) {
			$scope.participants = response.eventParticipantsList;
			var params = {
				page: 1,	// PAGINA INICIAL
				count: 10 	// CANTIDAD DE ITEMS POR PAGINA
				//sorting: {name: "asc"}
			};
				
			var settings = {
				total: $scope.participants.length,	
				counts: [],	
				getData: function($defer, params){
					var fromIndex = (params.page() - 1) * params.count();
					var toIndex = params.page() * params.count();						
					var subList = $scope.participants.slice(fromIndex, toIndex);
					$defer.resolve(subList);
				}
			};					
			$scope.participantsTable = new ngTableParams(params, settings);
		});
		
		
	}]);