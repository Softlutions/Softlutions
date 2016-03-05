'use strict';

angular.module('dondeEs.auctionsEvent', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/auctionsEvent', {
    templateUrl: 'resources/auctionsEvent.html',
    controller: 'auctionsEventCtrl'
  });
}])

.controller('auctionsEventCtrl', ['$scope','$http',function($scope,$http) {	
	$scope.auctionsEvent = [];
	$scope.auctionServices = [];

	var eventId = 1; // prueba
		
	$http.get('rest/protected/auction/getAllAuctionByEvent/'+eventId).success(function(response) {
		$scope.auctionsEvent = response.auctionList;
			
		console.log("$scope.auctionsEvent: ",$scope.auctionsEvent);		
	});
	
	$scope.loadAuctionServices = function (index) {
		$scope.auctionServices = $scope.auctionsEvent[index].auctionServices;
		
		console.log("service: ",$scope.auctionServices + " - index: " + index);	
	}
}]);