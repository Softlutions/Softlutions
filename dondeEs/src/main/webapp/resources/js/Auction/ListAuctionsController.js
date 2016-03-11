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
			
			$http.get('rest/protected/auction/getAllAuctions/').success(function(response) {
				$scope.auctionList = response.auctionList;
			})
			
			$scope.listParticipants = function(auction){
				$scope.selectedAuction = auction;
				$scope.auctionServices = auction.auctionServices;
				console.log(auction);
			}
			
			$scope.displayForm = function(){
				$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
				$http.get('rest/protected/user/getAllService/' + $scope.loggedUser.userId ).success(function(response) {
					$scope.services = response.listService;
				});	
				$scope.listForm = false;
				
			}
			
			$scope.joinAuction = function(){
				var newAuctionService = {
						acept : 0,
						date : new Date(),
						description : $scope.auctionService.description,
						price : $scope.auctionService.price,
						auction : $scope.selectedAuction,
						service : $scope.auctionService.service
				}
				
				$http({method: 'POST',url:'rest/protected/auctionService/createAuctionService', data:newAuctionService, headers: {'Content-Type': 'application/json'}}).success(function(response) {
					$('#modalAuctionParticipants').modal('toggle');
				})
			}
			
			
}]);