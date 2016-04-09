'use strict';

angular.module('dondeEs.auctionsEvent', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/auctionsEvent/:id', {
    templateUrl: 'resources/auction/auctionsEvent.html',
    controller: 'auctionsEventCtrl'
  });
}])

.controller('auctionsEventCtrl', ['$scope','$http','$location','$routeParams', '$window', '$timeout', '$interval', 
                                  			function($scope,$http,$location,$routeParams, $window, $timeout, $interval) {	
	$scope.$parent.pageTitle = "Donde es - Subastas disponibles";
	$scope.auctionsEvent = [];
	$scope.auctionServices = [];
	
	$scope.serviceList = false;
	$scope.address = '';
	
	$scope.showServiceList = function () {
		$scope.serviceList  = true;
	}
	
	$scope.hideServiceList = function () {
		$interval.cancel($scope.refreshInterval);
		$scope.serviceList  = false;
	}
		
	$http.get('rest/protected/auction/getAllAuctionByEvent/'+$routeParams.id).success(function(response) {
		if (response.code == 200) {
			if (response.auctionList != null && response.auctionList != {}) {
				$scope.auctionsEvent = response.auctionList;
			}
		} else {
	    	toastr.error('Subastas del evento', 'Ocurrió un error al buscar las subastas del evento.');
		}
	});
	
	$scope.$on('$destroy', function() {
		$interval.cancel($scope.refreshInterval);
	});
	
	$scope.loadAuctionServices = function (index) {
		$interval.cancel($scope.refreshInterval);
		$scope.auctionServices = [];
		
		$http.get('rest/protected/auctionService/getAllAuctionServicesByAuctionId/'+$scope.auctionsEvent[index].auctionId).success(function(response) {
			$scope.auctionServices = response.auctionServiceList;
		});
		
		$scope.refreshInterval = $interval(function(){
			$http.get('rest/protected/auctionService/getAllAuctionServicesByAuctionId/'+$scope.auctionsEvent[index].auctionId).success(function(response) {
				$scope.auctionServices = response.auctionServiceList;
				console.log($scope.auctionServices);
			});
		}, 3000);
	}
	
	$scope.goToServiceProviderProfile = function () {
	//	$location.url('/login');  colocar ruta del perfil del prestatario de servicio. 
	}
	
	$scope.finishAuction= function (id){
		var dataCreate={
			auctionId:id
		}
		$http({method: 'PUT',url:'rest/protected/auction/finishAuction', data:dataCreate}).success(function(response) {
			$("#finishAuctionId-"+id).text("Finalizada");
			$("#finishAuctionId-"+id).removeClass("btn-danger");
			$("#finishAuctionId-"+id).addClass("btn-warning");
			$("#finishAuctionId-"+id).prop("disabled", true);
		});
	}
	
	$scope.contract = function(auctionService){
		$http.get("rest/protected/auctionService/contract/"+auctionService.auctionServicesId).success(function(response){
			if(response.code == 200){
				var index = $scope.auctionsEvent.indexOf(auctionService.auction);
				$scope.auctionsEvent.splice(index, 1);
				toastr.success("Servicio "+auctionService.service.name+" contratado!");
			}else if(response.code == 400){
				toastr.warning("El servicio ya fue contratado");
			}
		}).error(function(response){
			toastr.error("Error", "No se pudo contratar el servicio");
		});
		
		$("#modal-form").modal("toggle");
	}
	
	$scope.auctionEventServices = function(event){
		var date = new Date();
		date.setDate(date.getDate() + 1);
        $('#datetimepicker').datetimepicker({
        	locale: 'es',
            format: 'LLLL',
            minDate: date,
            maxDate: event.publishDate
        });
		$scope.selectedEvent = event;
	}
	
	$scope.catalogsList = function(){
		if($scope.catalogs.length == 0){
			$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				$scope.catalogs = response.serviceCatalogList;
			});
		}
	}
	
	$scope.createAuction = function(){
		if($scope.tempAuction.name == null || $scope.tempAuction.description == null || $scope.tempAuction.selected == null){
			toastr.error('Debe ingresar todos los datos!');
		}else{			
			var date = new Date($('#datetimepicker').data("DateTimePicker").date());
			if($scope.globalEventId !=0){
				var event = {
						eventId: $scope.globalEventId
				}
				var auction = {
						name: $scope.tempAuction.name,
						description: $scope.tempAuction.description,
						date: date,
						state: 1,
						event: event,
						serviceCatalog: $scope.tempAuction.selected
				};
			}else{
				var auction = {
						name: $scope.tempAuction.name,
						description: $scope.tempAuction.description,
						date: date,
						state: 1,
						event: $scope.selectedEvent,
						serviceCatalog: $scope.tempAuction.selected
				}
			}
			
			$http({method: 'POST',url:'rest/protected/auction/createAuction', data:auction, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				$('#modalAuctionEventServices').modal('toggle');
				$scope.tempAuction = {};
				toastr.success('Subasta publicada!');
				if($scope.globalEventId!=0){
					setTimeout(function(){$('#modalAuctionEventServices').modal('hide')}, 10)
					$http.get('rest/protected/auction/getAllAuctionByEvent/'+$scope.globalEventId).success(function(response) {
							if (response.code == 200) {
								if (response.auctionList != null && response.auctionList != {}) {
									$scope.auctionsEvent = response.auctionList;
								} else {
							    	toastr.warning('Subastas del evento', 'No se encontraron subastas del evento.');
								}
							} else {
						    	toastr.error('Subastas del evento', 'Ocurrió un error al buscar las subastas del evento.');
							}
						});
			    	setTimeout(function(){$('#modalAuctionsByEvent').modal('show')}, 900);
				}
			});	
		}
	}	
}]);