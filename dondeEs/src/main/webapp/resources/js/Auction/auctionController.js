'use strict';

angular.module('dondeEs.auctionsEvent', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/auctionsEvent/:id', {
    templateUrl: 'resources/auctionsEvent.html',
    controller: 'auctionsEventCtrl'
  });
}])

.controller('auctionsEventCtrl', ['$scope','$http','$location','$routeParams', 
                                  			function($scope,$http,$location,$routeParams) {	
	$scope.auctionsEvent = [];
	$scope.auctionServices = [];
		
	$http.get('rest/protected/auction/getAllAuctionByEvent/'+$routeParams.id).success(function(response) {
		$scope.auctionsEvent = response.auctionList;
	});
	
	$scope.loadAuctionServices = function (index) {
		$scope.auctionServices = $scope.auctionsEvent[index].auctionServices;
	}
	
	$scope.goToServiceProviderProfile = function () {
	//	$location.url('/login');  colocar ruta del perfil del prestatario de servicio. 
	}
}]);