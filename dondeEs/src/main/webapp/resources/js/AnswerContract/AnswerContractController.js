'use strict';

angular.module('dondeEs.answerContract', ['ngRoute', 'ngCookies'])
.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/answerContract', {
		templateUrl : 'resources/AnswerContract/AnswerContract.html',
		controller : 'answerContractCtrl'
	});
} ])

.controller('answerContractCtrl', [ '$scope', '$http','$location', '$cookies', function($scope, $http, $location, $cookies) {
	$scope.$parent.pageTitle = "Donde es";
	
	$scope.getEventById = function(){
		$http.get('rest/protected/event/getEventByEncryptId/'+ $location.search().eventId).success(function(response) {
			$scope.event = response.eventPOJO;
		});
	}
	
	$scope.loggedUser = JSON.parse($cookies.getObject("loggedUser"));
	
	$scope.accept = function(event){
		var dataCreate = {
				eventId : $location.search().eventId,
				serviceId : $location.search().serviceId,
				state : 2
		};
		$http({method: 'POST',url:'rest/protected/serviceContact/answerContract', data:dataCreate}).success(function(response) {

		});
	}
	
	$scope.noAccept = function(event){
		var dataCreate = {
				eventId : $location.search().eventId,
				serviceId : $location.search().serviceId,
				state : 1
		};
		$http({method: 'POST',url:'rest/protected/serviceContact/answerContract', data:dataCreate}).success(function(response) {

		});
	}
	
	
} ]);