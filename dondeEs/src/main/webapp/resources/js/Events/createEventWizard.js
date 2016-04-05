'use strict';

'use strict';

var app = angular.module('dondeEs.eventWizard', ['ngRoute', 'ngFileUpload'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/eventWizard', {
    templateUrl: 'resources/event/createEventWizard.html',
    controller: 'eventWizardCtrl'
  });
}])

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
            if (status == google.maps.GeocoderStatus.OK) {
                var firstAddress = results[0];
                var latitude = firstAddress.geometry.location.lat();
                var longitude = firstAddress.geometry.location.lng();
                var marker = create(latitude, longitude);
                invokeSuccessCallback(successCallback, marker);
            } else {
            	toastr.error('Google Maps no pudo encontrar la dirección solicitada.');
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
        	toastr.error('Google Maps no pudo encontrar su dirección.');
        }
    }

    return {
        createByCoords: createByCoords,
        createByAddress: createByAddress,
        createByCurrentLocation: createByCurrentLocation
    };

});
app.controller('eventWizardCtrl', ['$scope','$http','$upload','MarkerCreatorService','$filter', 'WizardHandler',function($scope,$http,$upload,MarkerCreatorService,$filter, WizardHandler) {
	//#REGION ASISTENTE DE CREACION
	$scope.$parent.pageTitle = "Donde es - Mis eventos";
	$scope.eventForm = false;
	$scope.address = '';
	$scope.HOURS_BEFORE_EVENT = 12;
	$scope.eventsWizard = false;
	
	$scope.DEFAULT_IMG = "resources/img/imagen-no-disponible.gif";
	$scope.Date = function(){
		return new Date();
	};
	
	Date.prototype.addHours = function(h){
	    this.setHours(this.getHours() + h);
	    return this;
	}
	
	$scope.eventForm = false;
	$scope.tempAuction = {};
	
	$scope.listOfEmails = [];
	// Create auction
	$scope.catalogs = [];
	$scope.catalogServiceSelected = {};
	// --------------
	
	// edit event
	$scope.eventInEdition = null;
	$scope.tempEvent = {type:0, largeDesc: '', file:$scope.DEFAULT_IMG, originalFile:null};
	//-----------
	
	$scope.eventType = 0;
	$scope.globalEventId = 0;
	
	$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
	
	
	$scope.onFileSelect = function($files) {
	    var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.jpg|.png|.gif)$");
	    if (regex.test($files[0].name.toLowerCase())) {
	    	$scope.tempEvent.originalFile = $files[0];
	    	
	    	var reader = new FileReader();
	        reader.onload = function(e) {
	        	$scope.tempEvent.file = e.target.result;
	        	$scope.$apply();
	        }
	        reader.readAsDataURL($files[0]);
	    } else {
	    	$('#uploadImageEvent').val("");
	    	$scope.tempEvent.file = $scope.DEFAULT_IMG;
	    	$scope.tempEvent.originalFile = null;
	    	toastr.error('Carga de la imagen', 'El archivo no tiene un formato válido.');
	    }
	};
	
	$scope.createEvent = function() {
		if($scope.tempEvent.originalFile == null)
			$scope.tempEvent.originalFile = {};
		
		$scope.upload = $upload
			.upload(
				{
					url : 'rest/protected/event/createEvent',
					data : {
						'name':$scope.tempEvent.name,
						'description':$scope.tempEvent.desc,
						'largeDescription':$scope.tempEvent.largeDesc,
						'eventType':$scope.tempEvent.type,
						'eventPlaceName':$scope.tempEvent.placeName,
						'placeLatitude':$scope.map.center.latitude,
						'placeLongitude':$scope.map.center.longitude, 
						'publishDate':new Date($('#eventDatePicker').data("DateTimePicker").date()).toString(),
						'loggedUser':$scope.loggedUser.userId
					},
					file: $scope.tempEvent.originalFile
				})
			.progress(
				function(evt) {})
			.success(
				function(response) {
					$scope.globalEventId  = response.eventPOJO.eventId;
					if (response.code == 200) {
						$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
							if (response.code == 200) {
								if (response.eventList.length > 0) {
									$scope.events = response.eventList;
							    	
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
							    	
							    	toastr.success('Eventos del usuario', 'El evento se publicó con éxito.');
							    	$scope.hiddenEventForm();
							    	window.location.href = "/dondeEs/app#/#";
								} else {
							    	toastr.warning('Eventos del usuario', 'No se encontraron eventos.');
								}
							} else {
						    	toastr.warning('Eventos del usuario', 'No se pudieron actualizar los datos en pantalla sin embargo el evento se creó con éxito.');
						    	$scope.hiddenEventForm();
							}			
						});
						
					} else {
				    	toastr.error('Publicación del evento', 'Ocurrió un error al publicar el evento.');
					}
					
					$scope.resetCreateEvent();
				})
			.error(function(msj){
				console.log(msj);
			});
	};
	
	$scope.resetCreateEvent = function(){
		$("#eventFormTitle").text("Crear evento");
		$("#btnEventFormSubmit").text("Crear");
		$('#uploadImageEvent').val("");
		$scope.tempEvent = {type:0, largeDesc:'', file:$scope.DEFAULT_IMG, originalFile:null};
		$scope.eventInEdition = null;
		$scope.eventForm = false;
	}
	
	$scope.validationError = function(){
		toastr.warning('Algunos campos no cumplen con los requisitos');
	}
	
	$scope.saveEventChanges = function(){
		var event = {
			'eventId':$scope.eventInEdition.eventId,
			'name':$scope.tempEvent.name,
			'description':$scope.tempEvent.desc,
			'largeDescription':$scope.tempEvent.largeDesc,
			'eventType':$scope.tempEvent.type,
			'publishDate':new Date($('#eventDatePicker').data("DateTimePicker").date()).toString(),
			'placeId':$scope.eventInEdition.place.placeId,
			'placeName':$scope.tempEvent.placeName,
			'placeLatitude':$scope.map.center.latitude,
			'placeLongitude':$scope.map.center.longitude,
			'owner':$scope.loggedUser.userId
		};
		
		$scope.eventInEdition.name = event.name;
		$scope.eventInEdition.descripction = event.description;
		$scope.eventInEdition.largeDescription = event.largeDescription;
		$scope.eventInEdition.private_= event.eventType;
		$scope.eventInEdition.publishDate = event.publishDate;
		$scope.eventInEdition.place.name = event.placeName;
		
		if($scope.tempEvent.originalFile == null){
			$scope.tempEvent.originalFile = {};
		}else{
			$scope.eventInEdition.image = $scope.tempEvent.file;
		}
		
		$upload.upload({url:'rest/protected/event/editEvent', data:event, file:$scope.tempEvent.originalFile})
		.progress(function(evt) {})
		.success(function(response) {
			$scope.eventInEdition = null;
			$scope.resetCreateEvent();
			toastr.success('Eventos del usuario', 'El evento se modificó con éxito.');
		})
		.error(function(msj) {
			console.log("cathedError: ", msj);
			toastr.error('Eventos del usuario', 'Ocurrió un error al modificar el evento.');
		});
	}
	
	$scope.showEventForm = function () {
		$scope.eventForm  = true;
		$scope.eventInEdition = null;
		$scope.addCurrentLocation();
	}
	
	$('#eventDatePicker').datetimepicker({
    	minDate: new Date().addHours($scope.HOURS_BEFORE_EVENT),
    	locale: 'es',
        format: 'LLLL'
    });
	
	$('#eventDatePicker2').datetimepicker({
    	minDate: new Date().addHours($scope.HOURS_BEFORE_EVENT),
    	locale: 'es',
        format: 'LLLL'
    });
	
	$scope.hiddenEventForm = function () {
		$scope.eventForm  = false;
		$scope.eventInEdition = false;
		$scope.eventsWizard = false;
		$scope.resetCreateEvent();
	}
	
	function initMap(latitude, longitude){
		MarkerCreatorService.createByCoords(latitude, longitude, function (marker) {
	        $scope.autentiaMarker = marker;
	    });

	    $scope.map = {
	        center: {
	            latitude: $scope.autentiaMarker.latitude,
	            longitude: $scope.autentiaMarker.longitude
	        },
	        zoom: 14,
	        markers: [],
	        control: {},
	        options: {
	            scrollwheel: true
	        }
	    };
		
	    $scope.map.markers.push($scope.autentiaMarker);
	}
	
	initMap(9.6283789, -85.3756947);
	
    $scope.addCurrentLocation = function () {
        MarkerCreatorService.createByCurrentLocation(function (marker) {
            marker.options.labelContent = 'Usted está aquí.';
            refresh(marker);
            $scope.map.markers.push(marker);
        });
    };

    $scope.addAddress = function() {
        if ($scope.tempEvent.address !== '') {
            MarkerCreatorService.createByAddress($scope.tempEvent.address, function(marker) {
            	refresh(marker);
                $scope.map.markers.push(marker);
            });
        }
    };

    function refresh(marker) {
    	$scope.map.markers.length = 0;
        $scope.map.control.refresh({latitude: marker.latitude,
            							longitude: marker.longitude});
    }
    
    $scope.refreshChart = function(){
		var contractsLeft = 0;
		var contractsOk = 0;
		var contractsCanceled = 0;
		
		$('#contracts-state-chart').removeClass('hidden');
		
		angular.forEach($scope.serviceContacts, function(value){
			if(value.state == 0)
				contractsLeft++;
				
			if(value.state == 1)
				contractsOk++;
			
			if(value.state == 2)
				contractsCanceled++;
		});
		
		if($scope.chartValues != null){
			$scope.chartValues.setData([
				{ label: "Pendientes", value: contractsLeft },
				{ label: "Concretados", value: contractsOk },
				{ label: "Cancelados", value: contractsCanceled }
			]);
		}else if(contractsLeft > 0 || contractsOk > 0 || contractsCanceled > 0){
			$scope.chartValues = Morris.Donut({
			    element: 'contracts-state-chart',
			    data: [
			           { label: "Pendientes", value: contractsLeft },
			           { label: "Concretados", value: contractsOk },
			           { label: "Cancelados", value: contractsCanceled }
	            ],
			    resize: false,
			    colors: ['#87d6c6', '#54cdb4','#1ab394'],
			});
		}
    }    
	    
}]); 

