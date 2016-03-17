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
				if(response.auctionList.length == 0){
					$scope.showError = false;
				}else{
					$scope.auctionList = response.auctionList;
				}
			})
			
			$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				$scope.catalogs = response.serviceCatalogList;
				var allAuctions = {
						serviceCatalogId:0,
						name:"Todas las categorias"
				}
				$scope.catalogs.push(allAuctions);
			});
			
			$scope.getAuctionsByCatalog = function(selectedCatalog){
				if(selectedCatalog.serviceCatalogId == 0){
					$http.get('rest/protected/auction/getAllAuctions/').success(function(response) {
						if(response.auctionList.length == 0){
							$scope.showError = false;
						}else{
							$scope.auctionList = response.auctionList;
							$scope.showError = true;
						}
					});
				}else{
					$http.get('rest/protected/auction/getAllAuctionsByServiceCatalog/'+selectedCatalog.serviceCatalogId).success(function(response) {
						if(response.auctionList.length == 0){
							$scope.showError = false;
						}else{
							$scope.auctionList = response.auctionList;
							$scope.showError = true;
						}
					});
				}
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
				if($scope.auctionService.description == null || $scope.auctionService.price == null || $scope.auctionService.service == null){
					toastr.warning('Debe ingresar todos los datos!');
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
						$scope.auctionServices.push(newAuctionService);
						$scope.auctionService = {};
						$scope.listForm = true;
						toastr.success('Se ha incorporado a la subasta!')
					})
					$scope.listForm = true;
					('#modalAuctionParticipants').toggle();
				}
			}
			
			
}]);