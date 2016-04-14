'use strict';

angular.module('dondeEs.servicesAuctionEvent', ['ngRoute', 'ngTable'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/servicesAuctionEvent/:eventId/:id', {
    templateUrl: 'resources/auction/servicesAuctionEvent.html',
    controller: 'servicesAuctionEventCtrl'
  });
}])

.controller('servicesAuctionEventCtrl', ['$scope','$http','$location','$routeParams', '$window', 'ngTableParams', '$filter',
                                  			function($scope, $http, $location, $routeParams, $window, ngTableParams, $filter) {	
	$scope.$parent.pageTitle = "Donde es - Subastas disponibles";

	$scope.eventId = $routeParams.eventId;
			
	
	$http.get('rest/protected/auction/getAllServicesByAuction/'+$routeParams.id).success(function(response) {
		if (response.code == 200) {
			if (response.auction.auctionServices != null && response.auction.auctionServices != {}) {
				$scope.serviceList = response.auction.auctionServices;
				var params = {
						page: 1,	
						count: 10,
						sorting: {name: "asc"}
					};
					
				var settings = {
					total: $scope.serviceList.length,	
					counts: [],	
					getData: function($defer, params){
						var fromIndex = (params.page() - 1) * params.count();
						var toIndex = params.page() * params.count();
						
						var subList = $scope.serviceList.slice(fromIndex, toIndex);
						var sortedList = $filter('orderBy')(subList, params.orderBy());
						$defer.resolve(sortedList);
					}
				};
					
				$scope.servicesTable = new ngTableParams(params, settings);
			}
		} else {
	    	toastr.error('Servicios de la subasta', 'Ocurrió un error al buscar los servicios de la subasta.');
		}
	}); 
}]);