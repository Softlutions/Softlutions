'use strict';
var app = angular.module('dondeEs.myEvents', ['ngRoute', 'google-maps'])
	.config(['$routeProvider', function($routeProvider) {
	  $routeProvider.when('/index', {
	    templateUrl: 'resources/myEvents/index.html',
	    controller: 'MyEventsCtrl'
	  });
	}]);

app.factory('MarkerCreatorService', function () {
    var markerId = 0;

    function create(latitude, longitude) {
        var marker = {
            options: {
                labelAnchor: "28 -5",
                labelClass: 'markerlabel' 
            },
            latitude: latitude,
            longitude: longitude,
            id: ++markerId          
        };
        return marker;        
    }

    function invokeSuccessCallback(successCallback, marker) {
        if (typeof successCallback === 'function') {
            successCallback(marker);
        }
    }

    function createByCoords(latitude, longitude, successCallback) {
        var marker = create(latitude, longitude);
        invokeSuccessCallback(successCallback, marker);
    }

    function createByAddress(address, successCallback) {
        var geocoder = new google.maps.Geocoder();
        geocoder.geocode({'address' : address}, function (results, status) {
            if (status === google.maps.GeocoderStatus.OK) {
                var firstAddress = results[0];
                var latitude = firstAddress.geometry.location.lat();
                var longitude = firstAddress.geometry.location.lng();
                var marker = create(latitude, longitude);
                invokeSuccessCallback(successCallback, marker);
            } else {
                console.log("Google Maps no pudo encontrar la dirección.");
            }
        });
    }

    function createByCurrentLocation(successCallback) {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                var marker = create(position.coords.latitude, position.coords.longitude);
                invokeSuccessCallback(successCallback, marker);
            });
        } else {
        	console.log("Google Maps no pudo encontrar su dirección.");
        }
    }

    return {
        createByCoords: createByCoords,
        createByAddress: createByAddress,
        createByCurrentLocation: createByCurrentLocation
    };

});

app.controller('MyEventsCtrl', ['$scope','$http','$upload','MarkerCreatorService', function($scope,$http,$upload,MarkerCreatorService) { 
	$scope.listOfEmails = [];
	$scope.files = {};
	$scope.eventType = 0;
	
	$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
	$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
		$scope.events = response.eventList;
	});
	
	$scope.listParticipants = function(eventId){
		$http.get('rest/protected/eventParticipant/getAllEventParticipants/'+eventId).success(function(response) {
			$scope.participants = response.eventParticipantsList;
		});
	}
	
	$scope.auctionEventServices = function(event){
		$scope.selectedEvent = event;
	}
	
	$scope.createAuction = function(){
		
		var auction = {
				name: $('#auctionName').val(),
				description: $('#auctionDescription').val(),
				date: new Date(),
				event: $scope.selectedEvent
		}
		$http({method: 'POST',url:'rest/protected/auction/createAuction', data:auction, headers: {'Content-Type': 'application/json'}}).success(function(response) {
			$('#modalAuctionEventServices').modal('toggle');
			
		})	
	}
	
	$scope.listContracts = function(eventId){
	
		$http.get("rest/protected/serviceContact/getAllServiceContact/"+eventId).success(function(response){
				
				$scope.serviceContacts = response.listContracts;
				if($scope.serviceContacts.length == 0){
					$('#errorMessage').removeClass('hidden');
					$('#contractTable').addClass('hidden');
				}else{
					$('#contractTable').removeClass('hidden');
					$('#errorMessage').addClass('hidden');
				}
			});
	}

	 $scope.geteventById = function(eventId){
		$scope.eventId = eventId;

	};

	$scope.addEmail = function(pemail){
		$scope.listOfEmails.push(pemail.to);
		pemail.to = "";
	}
	
	/*Al que ocupe notificar al que contrata
	 * $http.get({url:'rest/protected/sendEmail/sendEmailContractNotification/idAEnviar='}).success(function(response) {
	 * 	Lo que quieran hacer xD
	 * });
	 * 
	 * */
	
	$scope.deleteEvent = function(event){
		$scope.listOfEmails.splice($scope.listOfEmails.indexOf(event), 1);
	}
	$scope.sendEmail = function(event){
		var dataCreate = {
				listSimple:$scope.listOfEmails
		};
		if($scope.listOfEmails.length != 0){
			$("#modal-formSendInvitation").modal('hide');
			$http({method: 'POST',url:'rest/protected/sendEmail/sendEmailInvitation?eventId='+ $scope.eventId, data:dataCreate, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				
			});
		}
	}
	
	$scope.publishEvent = function(eventId){  
		$scope.requestObject = {"eventId":eventId};
		$http.put('rest/protected/event/publishEvent',$scope.requestObject).success(function(response) {
				$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
					$scope.events = response.eventList;
				});
		})
		
	}
	
	$scope.cancelEvent = function(eventId){  
	 	$scope.requestObject = {"eventId":eventId};
	 	$http.put('rest/protected/event/cancelEvent',$scope.requestObject).success(function(response) {
	 		$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
				$scope.events = response.eventList;
			});
	 	})
	 }
	
	$scope.onFileSelect = function($files) {
		$scope.file = $files[0];
	};
	
	$scope.createEvent = function() {
		$scope.upload = $upload
				.upload(
						{
							url : 'rest/protected/event/createEvent',
							data : {
								'name':$scope.eventName,
								'description':$scope.eventDescription,
								'largeDescription':$scope.eventLargeDescription,
								'eventType':$scope.eventType,
								'eventPlaceName':$scope.eventPlaceName,
								'placeLatitude':$scope.map.center.latitude,
								'placeLongitude':$scope.map.center.longitude, 
								'loggedUser':$scope.loggedUser.userId,
							},
							file : $scope.file,
						})
				.progress(
						function(evt) {

						})
				.success(
						function(data, status, headers, config) {
							$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
								$scope.events = response.eventList;
							});
							$('#modalCreateEvent').modal('toggle');
						}); 
		console.log($scope.eventType);
	};
	
    MarkerCreatorService.createByCoords(9.6283789, -85.3756947, function (marker) {
        $scope.autentiaMarker = marker;
    });
    
    $scope.address = '';

    $scope.map = {
        center: {
            latitude: $scope.autentiaMarker.latitude,
            longitude: $scope.autentiaMarker.longitude
        },
        zoom: 14,
        markers: [],
        control: {},
        options: {
            scrollwheel: false
        }
    };

    $scope.map.markers.push($scope.autentiaMarker);

    $scope.addCurrentLocation = function () {
        MarkerCreatorService.createByCurrentLocation(function (marker) {
            marker.options.labelContent = 'Usted está aquí.';
            $scope.map.markers.push(marker);
            refresh(marker);
        });
    };
    
    $scope.addAddress = function() {
        var address = $scope.address;
        if (address !== '') {
            MarkerCreatorService.createByAddress(address, function(marker) {
                $scope.map.markers.push(marker);
                refresh(marker);
            });
        }
    };

    function refresh(marker) {
        $scope.map.control.refresh({latitude: marker.latitude,
            longitude: marker.longitude});
    }
}]);