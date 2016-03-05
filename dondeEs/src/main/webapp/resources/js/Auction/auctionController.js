'use strict';

angular.module('dondeEs.auctionsEvent', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/auctionsEvent', {
    templateUrl: 'resources/auctionsEvent.html',
    controller: 'auctionsEventCtrl'
  });
}])

.controller('auctionsEventCtrl', ['$scope','$http','$location', function($scope,$http,$location) {	
	$scope.auctionsEvent = [];
	$scope.auctionServices = [];

	var eventId = 1; // dato quedamado de prueba
		
	$http.get('rest/protected/auction/getAllAuctionByEvent/'+eventId).success(function(response) {
		$scope.auctionsEvent = response.auctionList;
	});
	
	$scope.loadAuctionServices = function (index) {
		$scope.auctionServices = $scope.auctionsEvent[index].auctionServices;
	}
	
	$scope.goToServiceProviderProfile = function () {
	//	$location.url('/login');  colocar ruta del perfil del prestatario de servicio. 
	}
}]);