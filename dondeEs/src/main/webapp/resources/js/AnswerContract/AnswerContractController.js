'use strict';

angular.module('dondeEs.answerContract', [ 'ngRoute' ])

.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/answerContract', {
		templateUrl : 'resources/AnswerContract/AnswerContract.html',
		controller : 'answerContractCtrl'
	});
} ])

.controller('answerContractCtrl', [ '$scope', '$http','$location', function($scope, $http, $location) {
	angular.element(document).ready(function(){
		
	});
	
	$http.get('rest/protected/event/getEventById/'+ $location.search().eventId).success(function(response) {
		$scope.event = response.eventPOJO;
		$scope.nameProvaider = $scope.event.user.name + " "+ $scope.event.user.lastName1 + " "+ $scope.event.user.lastName2;
		console.log("Provaider" + $scope.nameProvaider);
	});
} ]);