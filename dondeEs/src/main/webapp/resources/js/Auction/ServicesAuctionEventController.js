'use strict';

angular.module('dondeEs.servicesAuctionEvent', ['ngRoute', 'ngTable'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/servicesAuctionEvent/:eventId/:id', {
    templateUrl: 'resources/auction/servicesAuctionEvent.html',
    controller: 'servicesAuctionEventCtrl'
  });
}])

.controller('servicesAuctionEventCtrl', ['$scope','$http','$location','$routeParams', '$window', 'ngTableParams', '$filter', '$interval',
                                  			function($scope, $http, $location, $routeParams, $window, ngTableParams, $filter, $interval) {	
	$scope.$parent.pageTitle = "Donde es - Subastas disponibles";
	$scope.eventId = $routeParams.eventId;
	$scope.pauseInterval = false;
	$scope.serviceList = [];
	$scope.auction = {};
	
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
	
	$scope.$on("$destroy", function(){
		$interval.cancel($scope.refreshInterval);
	});
	
	$scope.refreshInterval = $interval(function(){
		if(!$scope.pauseInterval)
			loadServices();
	}, 3000);
	
	loadServices();
	
	function loadServices(){
		$http.get('rest/protected/auction/getAllServicesByAuction/'+$routeParams.id).success(function(response) {
			if (response.code == 200) {
				if (response.auction.auctionServices != null && response.auction.auctionServices != {}) {
					$scope.auction = response.auction;
					$scope.serviceList = $scope.auction.auctionServices;
					$scope.servicesTable.reload();
					
					if($scope.auction.state == 0)
						$interval.cancel($scope.refreshInterval);
				}
			} else {
		    	toastr.error('Servicios de la subasta', 'Ocurrió un error al buscar los servicios de la subasta.');
		    	$interval.cancel($scope.refreshInterval);
			}
		}).error(function(err){
			toastr.error('Servicios de la subasta', 'Ocurrió un error al buscar los servicios de la subasta.');
			$interval.cancel($scope.refreshInterval);
		});
	}
	
	$scope.contract = function(auctionService){
		$scope.pauseInterval = true;
		$("#btnAucServ"+auctionService.auctionServicesId).ladda().ladda("start");
		
		$http.get("rest/protected/auctionService/contract/"+auctionService.auctionServicesId).success(function(response){
			if(response.code == 200){
				auctionService.acept = 1;
				$scope.auction.state = 0;
				$interval.cancel($scope.refreshInterval);
				toastr.success("Servicio "+auctionService.service.name+" contratado!");
			}else if(response.code == 400){
				auctionService.acept = 1;
				$scope.auction.state = 0;
				toastr.warning("El servicio ya fue contratado");
			}else{
				toastr.error("No se pudo contratar el servicio");
			}
			$("#btnAucServ"+auctionService.auctionServicesId).ladda().ladda("stop");
			$scope.pauseInterval = false;
		}).error(function(err){
			toastr.error("Error", "No se pudo contratar el servicio");
			$("#btnAucServ"+auctionService.auctionServicesId).ladda().ladda("stop");
			$scope.pauseInterval = false;
		});
	};
}]);