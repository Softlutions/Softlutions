'use strict';
angular
	.module('dondeEs.EventParticipant', ['ngRoute','ngTable'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/eventParticipants/:id', {
			templateUrl : 'resources/EventParticipants/eventParticipantsList.html',
			controller : 'EventParticipantCtrl'
		});
	} ])
	.controller('EventParticipantCtrl',['$scope','$http','$routeParams','ngTableParams','$interval',function($scope,$http,$routeParams,ngTableParams,$interval) {
		
		$http.get('rest/protected/eventParticipant/getAllEventParticipants/'+$routeParams.id).success(function(response) {
			$scope.participants = response.eventParticipantsList;
			var params = {
				page: 1,
				count: 20
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
		
		$scope.refreshInterval = $interval(function() {
			if($routeParams.id != null){
				$http.get('rest/protected/eventParticipant/getAllEventParticipants/'+$routeParams.id).success(function(response) {
					$scope.participants = response.eventParticipantsList;
					$scope.participantsTable.reload();
				});
			}
        }, 3000);
		
		$scope.$on('$destroy', function() {
			$interval.cancel($scope.refreshInterval);
		});
		
	}]);