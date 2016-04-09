'use strict';
angular
	.module('dondeEs.auctions', ['ngRoute', 'ngTable'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/auctions', {
			templateUrl : 'resources/auction/auction.html',
			controller : 'AuctionsCtrl'
		});
	} ])
	.controller('AuctionsCtrl',['$scope','$http','ngTableParams','$filter','$window',function($scope, $http,ngTableParams,$filter,$window) {
		$scope.$parent.pageTitle = "Donde es - Subastas de evento";
		$scope.selectedCatalogId = "";
		$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
		$scope.loggedUserServiceCatalogs = [];
		$scope.auctionService = {};
		$scope.selectedAuction = {};
		$scope.catalogs = [];
		$scope.auctionList =[];
		$scope.selectedCatalogId = "";
		$scope.step = 0;
		$scope.tempAuctionList = [1];
		$scope.showError = true;
		
		angular.element(document).ready(function(){

			$http.get('rest/protected/auction/getAllAuctions').success(function(response) {
				response.auctionList.forEach(function(auction){
					if(auction.state==1){
						$scope.auctionList.push(auction);
					}
				});
				var params = {
						page: 1,	// PAGINA INICIAL
						count: 10 	// CANTIDAD DE ITEMS POR PAGINA
						//sorting: {name: "asc"}
				};
					
				var settings = {
					total: $scope.auctionList.length,	
					counts: [],	
					getData: function($defer, params){
						var fromIndex = (params.page() - 1) * params.count();
						var toIndex = params.page() * params.count();						
						var subList = $scope.auctionList.slice(fromIndex, toIndex);
						$defer.resolve(subList);
					}
				};					
				$scope.auctionsTable = new ngTableParams(params, settings);
			});		
			
			$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				$scope.catalogs = response.serviceCatalogList;
			});
			
			$http.get('rest/protected/service/getServiceCatalogIdByProvider/'+$scope.loggedUser.userId).success(function(response) {
				if(response.serviceLists.length != 0){	
					var x;
					for(x = 0 ; x < response.serviceLists.length ; x++){
						$scope.loggedUserServiceCatalogs.push(response.serviceLists[x].serviceCatalog.serviceCatalogId);
					}
				}
			});
		});
		
		$scope.validateService = function(serviceCatalogId){
			var existe = $scope.loggedUserServiceCatalogs.indexOf(serviceCatalogId);
			if(existe >= 0)
				return true;
			else
				return false;
		};
		
		$scope.validationError = function(){
			toastr.warning('Algunos campos no cumplen con los requisitos');
		}
		
		$scope.$watch('tempAuctionList', function(){
			if($scope.tempAuctionList.length==0)
				$scope.showError = false;
			else
				$scope.showError = true;
		});
		
		$scope.validatelistItem = function(auction,index){
			$scope.selectedAuction = auction;
			if($scope.validateService(auction.serviceCatalog.serviceCatalogId))
				$("#liParticipate-"+index).show();
			else
				$("#liParticipate-"+index).hide();
		};
	
		$scope.loadServices = function(){
			$http.get('rest/protected/service/getAllServiceByUserAndServiceCatalog/' + $scope.loggedUser.userId + '/'+ $scope.selectedAuction.serviceCatalog.serviceCatalogId ).success(function(response) {
				$scope.services = response.serviceLists;
				$scope.auctionService.service = $scope.services[0];
			});	
		};
		
		$scope.joinAuction = function(){	
			var newAuctionService = {
					acept : 1,
					date : new Date(),
					description : $scope.auctionService.description,
					price : $scope.auctionService.price,
					auction : $scope.selectedAuction,
					service : $scope.auctionService.service
			}
			$http({method: 'POST',url:'rest/protected/auctionService/createAuctionService', data:newAuctionService, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				$("#registerModal").modal("toggle");
				toastr.success('Se ha incorporado a la subasta!');
				setTimeout(function(){ $window.location.href = "app#/auctionParticipants/"+$scope.selectedAuction.auctionId; }, 500);				
			});	
		};	
}]);
