'use strict';
angular
		.module('dondeEs.auctions', ['ngRoute'])
		.config([ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/auctions', {
				templateUrl : 'resources/auction/auction.html',
				controller : 'AuctionsCtrl'
			});
		} ])
		.controller('AuctionsCtrl',['$scope','$http',function($scope, $http) {
			$scope.listForm = true;
			$scope.auctionService = {};
			$scope.selectedAuction = {};
			$scope.catalogs = [];
			$scope.auctionList = [];
			$scope.showError = true;
			
			$http.get('rest/protected/auction/getAllAuctions/').success(function(response) {
				$scope.auctionList = response.auctionList;
				if($scope.auctionList == []){
					$scope.showError = false;
				}
			})
			
			$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				$scope.catalogs = response.serviceCatalogList;
			});
			
			$scope.getAuctionsByCatalog = function(selectedCatalog){
				$http.get('rest/protected/auction/getAllAuctionsByServiceCatalog/'+selectedCatalog.serviceCatalogId).success(function(response) {
					$scope.auctionList = response.auctionList;
					if($scope.auctionList == []){
						$scope.showError = false;
					}
				});
			}
			
			$scope.listParticipants = function(auction){
				$scope.selectedAuction = auction;
				$scope.auctionServices = auction.auctionServices;
			}
			
			$scope.displayForm = function(){
				$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
				$http.get('rest/protected/user/getAllService/' + $scope.loggedUser.userId ).success(function(response) {
					$scope.services = response.listService;
				});	
				$scope.listForm = false;
				
			}
			
			$scope.joinAuction = function(){
				if($scope.auctionService.description == null || $scope.auctionService.price == null){
					alert('No lleno todos los campos')
				}else{
					
					var newAuctionService = {
							acept : 0,
							date : new Date(),
							description : $scope.auctionService.description,
							price : $scope.auctionService.price,
							auction : $scope.selectedAuction,
							service : $scope.auctionService.service
					}
					
					$http({method: 'POST',url:'rest/protected/auctionService/createAuctionService', data:newAuctionService, headers: {'Content-Type': 'application/json'}}).success(function(response) {
						$scope.displayForm();
						$scope.auctionService.description = "";
						$scope.auctionService.price = "",
						$scope.listForm = true;
					})
					$scope.listForm = true;
					('#modalAuctionParticipants').toggle();
				}
			}
			
			
}]);