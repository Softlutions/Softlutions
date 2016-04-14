'use strict';

angular.module('dondeEs.auctionParticipants', ['ngRoute', 'ngTable'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/auctionParticipants/:id', {
    templateUrl: 'resources/auction/auctionParticipants.html',
    controller: 'auctionParticipantsCtrl'
  });
}])

.controller('auctionParticipantsCtrl', ['$scope','$http','$routeParams','ngTableParams','$filter','$interval',function($scope,$http,$routeParams,ngTableParams,$filter,$interval){
	$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
	$scope.$parent.pageTitle = "Donde es - Participantes de subasta";
	$scope.selectedAuction = {};
	$scope.loggedUserServiceCatalogs = [];
	$scope.auctionService = {};
	$scope.auctionServices = [];
	
	var params = {
		page: 1,	// PAGINA INICIAL
		count: 10, 	// CANTIDAD DE ITEMS POR PAGINA
		sorting: {date: "desc"}
	};
	
	var settings = {
		total: $scope.auctionServices.length,	
		counts: [],	
		getData: function($defer, params){
			var fromIndex = (params.page() - 1) * params.count();
			var toIndex = params.page() * params.count();						
			var subList = $scope.auctionServices.slice(fromIndex, toIndex);
			var sortedList = $filter('orderBy')(subList, params.orderBy());
			$defer.resolve(sortedList);
		}
	};
	
	$scope.auctionServicesTable = new ngTableParams(params, settings);
	
	angular.element(document).ready(function () {
		$http.get('rest/protected/auction/getAuctionById/'+$routeParams.id).success(function(response) {
			$scope.selectedAuction = response.auction;	
		});
		
		$http.get('rest/protected/service/getServiceCatalogIdByProvider/'+$scope.loggedUser.userId).success(function(response) {
			if(response.serviceLists.length != 0){
				var x;
				for(x = 0 ; x < response.serviceLists.length ; x++){
					$scope.loggedUserServiceCatalogs.push(response.serviceLists[x].serviceCatalog.serviceCatalogId);
				}
			}
			
			if($scope.selectedAuction.serviceCatalog != null && $scope.loggedUser.role.roleId == 2){
				if($scope.loggedUserServiceCatalogs.indexOf($scope.selectedAuction.serviceCatalog.serviceCatalogId)>=0)
					$("#btnParticipate").removeAttr("disabled");
				else{
					$("#btnParticipate").attr("disabled","true");
					toastr.error("El botón participar está deshabilitado porque el usuario no posee ningún servicio del requerido en la subasta");
				}
			}
		});
		
		$scope.refreshInterval = $interval(function(){
			$http.get('rest/protected/auctionService/getAllAuctionServicesByAuctionId/'+$routeParams.id).success(function(response) {
				$scope.auctionServices = response.auctionServiceList;
				$scope.auctionServicesTable.reload();
			});
		}, 3000);
		
		$http.get('rest/protected/auctionService/getAllAuctionServicesByAuctionId/'+$routeParams.id).success(function(response) {
			$scope.auctionServices = response.auctionServiceList;
			$scope.auctionServicesTable.reload();
		});
		
	});	
	
	$scope.formatPrice = function(model){
		if(model==null) model = 0;
		model = model.replace(".00", "");
		model = model.replace("₡", "");
		model = model.replace(/,/g, "");
		var formatPrice = $filter('currency')(model, '₡', 2);
		$scope.auctionService.price = formatPrice;
	}
	
	$scope.$on('$destroy', function() {
		$interval.cancel($scope.refreshInterval);
	});
	
	$scope.loadServices = function(){
		$http.get('rest/protected/service/getAllServiceByUserAndServiceCatalog/' + $scope.loggedUser.userId + '/'+ $scope.selectedAuction.serviceCatalog.serviceCatalogId ).success(function(response) {
			$scope.services = response.serviceLists;
			$scope.auctionService.service = $scope.services[0];
		});	
	};
	
	$scope.validationError = function(){
		toastr.warning('Algunos campos no cumplen con los requisitos');
	}
	
	$scope.joinAuction = function(){	
		var price = $scope.auctionService.price.replace(".00", "").replace("₡", "").replace(/,/g, "");	
		var newAuctionService = {
				acept : 1,
				date : new Date(),
				description : $scope.auctionService.description,
				price : price,
				auction : $scope.selectedAuction,
				service : $scope.auctionService.service
		}
		$http({method: 'POST',url:'rest/protected/auctionService/createAuctionService', data:newAuctionService, headers: {'Content-Type': 'application/json'}}).success(function(response) {
			$scope.auctionServices.push(newAuctionService);
			$scope.auctionServicesTable.reload();
			$("#registerModal1").modal("toggle");
			$scope.auctionService = {};
			toastr.success('Se ha incorporado a la subasta!')
		});		
	};	
	
}]);