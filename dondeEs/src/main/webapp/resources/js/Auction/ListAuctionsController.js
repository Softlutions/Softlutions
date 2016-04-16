'use strict';
angular
	.module('dondeEs.auctions', ['ngRoute', 'ngTable', 'ngCookies'])
	.config([ '$routeProvider', function($routeProvider) {
		$routeProvider.when('/auctions', {
			templateUrl : 'resources/auction/auction.html',
			controller : 'AuctionsCtrl'
		});
	}])
	.controller('AuctionsCtrl',['$scope', '$http', 'ngTableParams', '$interval', '$filter', '$window', '$cookies',function($scope, $http, ngTableParams, $interval, $filter, $window, $cookies) {
		$scope.$parent.pageTitle = "Donde es - Subastas de evento";
		$scope.selectedCatalogId = "";
		$scope.loggedUser = JSON.parse($cookies.getObject("loggedUser"));
		$scope.loggedUserServiceCatalogs = [];
		$scope.auctionService = {};
		$scope.selectedAuction = {};
		$scope.auctionServices = [];
		$scope.catalogs = [];
		$scope.auctionList =[];
		$scope.selectedCatalogId = "";
		$scope.step = 0;
		$scope.tempAuctionList = [1];
		$scope.showError = true;
		
		angular.element(document).ready(function(){
			// AUCTION SERVICES PAGINATION
			var params = {
				page: 1,
				count: 10
			};
			
			var settings = {
				total: $scope.auctionServices.length,	
				counts: [],	
				getData: function($defer, params){
					var fromIndex = (params.page() - 1) * params.count();
					var toIndex = params.page() * params.count();						
					var subList = $scope.auctionServices.slice(fromIndex, toIndex);
					$defer.resolve(subList);
				}
			};
			
			$scope.auctionServicesTable = new ngTableParams(params, settings);
			
			// AUCTION PAGINATION
			var params = {
				page: 1,
				count: 10
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
			
			// ------------------------------
			
			$scope.auctionsInterval = $interval(function(){
				getAllAuctions();
			}, 3000);
			
			getAllAuctions();
			
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
			return (existe >= 0);
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
		
		$scope.formatPrice = function(model){
			if(model==null) model = 0;
			model = model.replace(".00", "");
			model = model.replace("₡", "");
			model = model.replace(/,/g, "");
			var formatPrice = $filter('currency')(model, '₡', 2);
			$scope.auctionService.price = formatPrice;
		}
		
		
		
		
		$scope.validatelistItem = function(auction,index){
			$scope.selectedAuction = auction;
			if($scope.validateService(auction.serviceCatalog.serviceCatalogId))
				$("#liParticipate-"+index).show();
			else
				$("#liParticipate-"+index).hide();
		};
		
		function getAllAuctions(){
			$scope.auctionList = [];
			
			$http.get('rest/protected/auction/getAllAuctions').success(function(response) {
				response.auctionList.forEach(function(auction){
					if(auction.state==1){
						$scope.auctionList.push(auction);
					}
				});
				
				$scope.auctionsTable.reload();
			});		
		};
		
		$scope.$on('$destroy', function() {
			$interval.cancel($scope.auctionsInterval);
			
			if($scope.participantsInterval != null)
				$interval.cancel($scope.participantsInterval);
		});
		
		$scope.listParticipants = function(auction){
			if($scope.participantsInterval != null)
				$interval.cancel($scope.participantsInterval);
			$scope.auctionServices = [];
			
			$scope.participantsInterval = $interval(function(){
				$http.get('rest/protected/auctionService/getAllAuctionServicesByAuctionId/'+auction.auctionId).success(function(response) {
					$scope.auctionServices = response.auctionServiceList;
					$scope.auctionServicesTable.reload();
				});	
			}, 3000);
			
			$http.get('rest/protected/auctionService/getAllAuctionServicesByAuctionId/'+auction.auctionId).success(function(response) {
				$scope.auctionServices = response.auctionServiceList;
				$scope.auctionServicesTable.reload();
			});	
			
			$scope.selectedAuction = auction;
			$scope.step = 1;
			
			if($scope.loggedUser.role.roleId == 2){
				if($scope.validateService(auction.serviceCatalog.serviceCatalogId)){
					$("#btnParticipate").removeAttr("disabled");
				}else{
					$("#btnParticipate").attr("disabled","true");
					toastr.warning("No posee ningún servicio para este tipo de subasta");
				}
			}
		};	
		
		$scope.loadServices = function(){
			$http.get('rest/protected/service/getAllServiceByUserAndServiceCatalog/' + $scope.loggedUser.userId + '/'+ $scope.selectedAuction.serviceCatalog.serviceCatalogId ).success(function(response) {
				$scope.services = response.serviceLists;
				$scope.auctionService.service = $scope.services[0];
			});	
		};
		
		$scope.contract = function(auctionService){
			if(auctionService.acept == 1){
				$http.get("rest/protected/auctionService/contract/"+auctionService.auctionServicesId).success(function(response){
					if(response.code == 200){
						var index = $scope.auctionList.indexOf(auctionService.auction);
						$scope.auctionList.splice(index, 1);
						$("#modalAuctionParticipants").modal("toggle");
						toastr.success("Servicio "+auctionService.service.name+" contratado!");
					}else{
						$("#modalAuctionParticipants").modal("toggle");
						toastr.error("No se pudo contratar el servicio");
					}

				});
			}
		};
		
		$scope.displayForm = function(){
			$scope.loggedUser = JSON.parse($cookies.getObject("loggedUser"));
			$http.get('rest/protected/user/getAllService/' + $scope.loggedUser.userId ).success(function(response) {
				$scope.services = response.listService;
			});	
			$scope.listForm = false;
			
		};
		
		$scope.joinAuction = function(){
		var price = $scope.auctionService.price.replace(".00", "").replace("₡", "").replace(/,/g, "");	
			if($scope.auctionService.description == null || $scope.auctionService.price == null || $scope.auctionService.service == null){
				toastr.error('Debe ingresar todos los datos!');
			}else{
				var newAuctionService = {
					acept : 1,
					date : new Date(),
					description : $scope.auctionService.description,
					price : price,
					auction : $scope.selectedAuction,
					service : $scope.auctionService.service
				}
				$http({method: 'POST',url:'rest/protected/auctionService/createAuctionService', data:newAuctionService, headers: {'Content-Type': 'application/json'}}).success(function(response) {
					$("#registerModal").modal("toggle");
					toastr.success('Se ha incorporado a la subasta!');
					setTimeout(function(){ $window.location.href = "app#/auctionParticipants/"+$scope.selectedAuction.auctionId; }, 500);				
				});
			}
		};	
}]);
