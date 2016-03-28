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
		$scope.auctionList =[];
		$scope.pendingAuctionList =[];
		$scope.showError = true;
		$scope.showErrorPending = true;
		$scope.selectedCatalogId = "";
		
		toastr.options = {
			closeButton: true,
			showMethod: 'slideDown',
			timeOut: 4000
		};
		
		angular.element(document).ready(function(){
			getAllAuctions();
			$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				var allAuctions = {
						serviceCatalogId:null,
						name:"Todas las categorias"
				}
				$scope.catalogs.push(allAuctions);
				$scope.catalogs.push.apply($scope.catalogs,response.serviceCatalogList);
			});
		})
		
		function getAllAuctions(){				
			$http.get('rest/protected/auction/getAllAuctions').success(function(response) {
				response.auctionList.forEach(function(auction){
					if(auction.state==1){
						$scope.auctionList.push(auction);
					}if(auction.state==2){
						$scope.pendingAuctionList.push(auction);
					}
				});
				if($scope.auctionList.length == 0){
					$scope.showError = false;
				}else{
					$scope.showError = true;
				}
				if($scope.pendingAuctionList.length == 0){
					$scope.showErrorPending = false;
				}else{
					$scope.showErrorPending = true;
				}
			});		
		}
		
		$scope.getAuctionsByCatalog = function(selectedCatalog){
			if(selectedCatalog.serviceCatalogId == 0){
				$scope.selectedCatalogId = "";
				console.log($scope.auctionList);
				
			}else{
				$scope.selectedCatalogId = selectedCatalog.serviceCatalogId;
				
			}	
		}

		$scope.listParticipants = function(auction){				
			 				$http.get('rest/protected/auctionService/getAllAuctionServicesByAuctionId/'+auction.auctionId).success(function(response) {
			 					$scope.auctionServices = response.auctionServiceList;
			 					$scope.selectedAuction = auction;
			 					$scope.listForm = true;
			 				});	
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
				toastr.error('Debe ingresar todos los datos!');
			}else{
				
				var newAuctionService = {
						acept : 1,
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
			}
		}
}]);