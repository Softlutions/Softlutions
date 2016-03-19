'use strict';

angular.module('dondeEs.auctionsEvent', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/auctionsEvent/:id', {
    templateUrl: 'resources/auction/auctionsEvent.html',
    controller: 'auctionsEventCtrl'
  });
}])

.controller('auctionsEventCtrl', ['$scope','$http','$location','$routeParams', '$window',
                                  			function($scope,$http,$location,$routeParams, $window) {	
	$scope.auctionsEvent = [];
	$scope.auctionServices = [];
		
	$http.get('rest/protected/auction/getAllAuctionByEvent/'+$routeParams.id).success(function(response) {
		if (response.code == 200) {
			if (response.auctionList != null && response.auctionList != {}) {
				$scope.auctionsEvent = response.auctionList;
			} else {
		    	toastr.options = {
		    			closeButton: true,
	                    progressBar: true,
	                    showMethod: 'slideDown'
		        };
		    	toastr.warning('Subastas del evento', 'No se encontraron subastas del evento.');
			}
		} else {
	    	toastr.options = {
	    			closeButton: true,
                    progressBar: true,
                    showMethod: 'slideDown'
	        };
	    	toastr.error('Subastas del evento', 'Ocurrió un error al buscar las subastas del evento.');
		}
	});
	
	$scope.loadAuctionServices = function (index) {
		$scope.auctionServices = $scope.auctionsEvent[index].auctionServices;
	}
	
	$scope.goToServiceProviderProfile = function () {
	//	$location.url('/login');  colocar ruta del perfil del prestatario de servicio. 
	}
	
	$scope.finishAuction= function (id){
		var dataCreate={
			auctionId:id
		}
		$http({method: 'PUT',url:'rest/protected/auction/finishAuction', data:dataCreate}).success(function(response) {
			console.log(response);
			$("#finishAuctionId-"+id).text("Finalizada");
			$("#finishAuctionId-"+id).removeClass("btn-danger");
			$("#finishAuctionId-"+id).addClass("btn-warning");
			$("#finishAuctionId-"+id).prop("disabled", true);
		});
		
	}
	
}]);