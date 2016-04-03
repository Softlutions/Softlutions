'use strict';

angular.module('dondeEs.auctionsEvent', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/auctionsEvent/:id', {
    templateUrl: 'resources/auction/auctionsEvent.html',
    controller: 'auctionsEventCtrl'
  });
}])

.controller('auctionsEventCtrl', ['$scope','$http','$location','$routeParams', '$window', '$timeout', 
                                  			function($scope,$http,$location,$routeParams, $window, $timeout) {	
	$scope.$parent.pageTitle = "Donde es - Subastas disponibles";
	$scope.auctionsEvent = [];
	$scope.auctionServices = [];
	
	$scope.serviceList = false;
	$scope.address = '';
	
	$scope.showServiceList = function () {
		$scope.serviceList  = true;
	}
	
	$scope.hideServiceList = function () {
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
	
	$scope.loadAuctionServices = function (index) {
		$scope.auctionServices = $scope.auctionsEvent[index].auctionServices;
		
		$timeout(function(){
			$scope.auctionServices.forEach(function(entry){
				if($scope.auctionsEvent[index].state == 0)
					$("#auctionParticipant-"+entry.auctionServicesId).attr("disabled", true);
				
				if(entry.acept = 1)
					$("#auctionParticipant-"+entry.auctionServicesId).text("Contratado");
			});
		});
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
	
}]);