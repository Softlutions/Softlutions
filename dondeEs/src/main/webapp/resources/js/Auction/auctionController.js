'use strict';

angular.module('dondeEs.auctionsEvent', ['ngRoute', 'ngTable'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/auctionsEvent/:id', {
    templateUrl: 'resources/auction/auctionsEvent.html',
    controller: 'auctionsEventCtrl'
  });
}])

.controller('auctionsEventCtrl', ['$scope','$http','$location','$routeParams', '$window', '$timeout', 
                                  		'ngTableParams', '$filter', '$interval', 
                                  			function($scope, $http, $location, $routeParams, $window, 
                                  						$timeout, ngTableParams, $filter, $interval) {	
	$scope.$parent.pageTitle = "Donde es - Subastas disponibles";
	$scope.auctionsEvent = [];
	$scope.auctionServices = [];
	$scope.catalogs = [];
	$scope.tempAuction = {};
	$scope.event = {};
	$scope.eventId = $routeParams.id;
	$scope.validEventDate = false;
	$scope.HOURS_BEFORE_EVENT = 12;
	
	Date.prototype.addHours = function(h){
	    this.setHours(this.getHours() + h);
	    return this;
	}
	
	$http.get('rest/landing/getWhateverEventById/'+$routeParams.id).success(function(response) {
		if (response.code == 200) {
			$scope.event = response.eventPOJO;
			
			var date = new Date();
			var validDate = new Date($scope.event.publishDate);
			validDate.setHours(validDate.getHours() - $scope.HOURS_BEFORE_EVENT);
			
			if(date < validDate)
				$scope.validEventDate = true;
		} else {
	    	toastr.error('Subastas del evento', 'Ocurrió un error al buscar la información del evento.');
		}
	});
			
	$scope.serviceList = false;
	$scope.address = '';
	
	$scope.showServiceList = function () {
		$scope.serviceList  = true;
	}
	
	$scope.hideServiceList = function () {
		$interval.cancel($scope.refreshInterval);
		$scope.serviceList  = false;
	}
	
	var params = {
		page: 1,	
		count: 10,
		sorting: {name: "asc"}
	};
		
	var settings = {
		total: $scope.auctionsEvent.length,	
		counts: [],	
		getData: function($defer, params){
			var fromIndex = (params.page() - 1) * params.count();
			var toIndex = params.page() * params.count();
			
			var subList = $scope.auctionsEvent.slice(fromIndex, toIndex);
			var sortedList = $filter('orderBy')(subList, params.orderBy());
			$defer.resolve(sortedList);
		}
	};
		
	$scope.auctionsEventTable = new ngTableParams(params, settings);
	
	$scope.getAllAuctionByEvent = function() {
		$http.get('rest/protected/auction/getAllAuctionByEvent/'+$routeParams.id).success(function(response) {
			if(response.code == 200){
				if(response.auctionList != null && response.auctionList != {}){
					$scope.auctionsEvent = response.auctionList;
					$scope.auctionsEventTable.reload();
				}
			}else{
		    	toastr.error('Subastas del evento', 'Ocurrió un error al buscar las subastas del evento.');
		    	$interval.cancel($scope.refreshAuctionInterval);
			}
		});
	}
	
	$scope.$on('$destroy', function() {
		$interval.cancel($scope.refreshAuctionInterval);
		$interval.cancel($scope.refreshInterval);
	});
	
	$scope.refreshAuctionInterval = $interval(function(){
		$scope.getAllAuctionByEvent();
	}, 3000);
	
	$scope.getAllAuctionByEvent();
	
	$scope.loadAuctionServices = function (index) {
		$interval.cancel($scope.refreshInterval);
		$scope.auctionServices = $scope.auctionsEvent[index].auctionServices;
		
		var params = {
				page: 1,	
				count: 10,
				sorting: {price: "asc"}
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
		
		$timeout(function(){
			$scope.auctionServices.forEach(function(entry){
				if($scope.auctionsEvent[index].state == 0)
					$("#auctionParticipant-"+entry.auctionServicesId).attr("disabled", true);
				
				if(entry.acept = 1)
					$("#auctionParticipant-"+entry.auctionServicesId).text("Contratado");
			});
		});
		
		$scope.refreshInterval = $interval(function(){
			$http.get('rest/protected/auctionService/getAllAuctionServicesByAuctionId/'+$scope.auctionsEvent[index].auctionId).success(function(response) {
				$scope.auctionServices = response.auctionServiceList;
				$scope.auctionServicesTable.reload();
			});
		}, 3000);
	}
	
	$scope.goToServiceProviderProfile = function () {
	//	$location.url('/login');  colocar ruta del perfil del prestatario de servicio. 
	}
	
	$scope.finishAuction= function (auction){
		$("#finishAuctionId-"+auction.auctionId).ladda().ladda("start");
		
		$http({method: 'PUT',url:'rest/protected/auction/finishAuction', data:{"auctionId":auction.auctionId}}).success(function(response) {
			if(response.code == 200){
				auction.state = 0;
			}else{
				toastr.error("No se pudo finalizar la subasta");
			}
			$("#finishAuctionId-"+auction.auctionId).ladda().ladda("stop");
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
	};
	
	$scope.catalogsList = function(){
		if($scope.catalogs.length == 0){
			$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				$scope.catalogs = response.serviceCatalogList;
				$scope.tempAuction.selected = response.serviceCatalogList[0];
			});
		}
	};
	
	$scope.auctionEventServices = function(){
		var date = new Date();
		date.setHours(date.getHours()+1);
		var maxdate = new Date($scope.event.publishDate);
		maxdate.setHours(maxdate.getHours()-6);
        $('#datetimepicker').datetimepicker({
        	locale: 'es',
            format: 'LLLL',
            minDate: date,
            maxDate: maxdate
        });
		$scope.selectedEvent = $scope.event;
	};
		
	$('#modalAuctionEventServices').on('hidden.bs.modal', function () {
		$scope.tempAuction.selected = $scope.catalogs[0];
		$scope.tempAuction.name = '';
		$scope.tempAuction.description = '';
		var date = new Date();
		date.setDate(date.getDate() + 1);
		$('#datetimepicker').data('DateTimePicker').date(date);
	});
	
	$scope.createAuction = function(){
		if($scope.tempAuction.name == null || $scope.tempAuction.description == null || $scope.tempAuction.selected == null){
			toastr.error('Debe ingresar todos los datos!');
		}else{
			var date = new Date($('#datetimepicker').data("DateTimePicker").date());
			var auction = {
				name: $scope.tempAuction.name,
				description: $scope.tempAuction.description,
				date: date,
				state: 1,
				event: $scope.selectedEvent,
				serviceCatalog: $scope.tempAuction.selected
			};
		
			$http({method: 'POST',url:'rest/protected/auction/createAuction', data:auction, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				$('#modalAuctionEventServices').modal('toggle');
				$scope.tempAuction = {};
				toastr.success('Subasta publicada!');
				setTimeout(function(){$('#modalAuctionEventServices').modal('hide')}, 10)
				
				$scope.getAllAuctionByEvent();
				
		    	setTimeout(function(){$('#modalAuctionsByEvent').modal('show')}, 900);
			});
		}
	}	
}]);