angular.module('dondeEs.simpleEvent', ['ngRoute', 'google-maps', 'mgo-angular-wizard', 'ngTable', 'ngCookies'])
.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/event', {
		templateUrl: 'resources/event/createSimpleEvent.html',
		controller: 'SimpleEventCtrl'
	});
}]).factory('MarkerCreatorService', function () {
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
}).controller('SimpleEventCtrl', ['$scope', '$http', '$upload', 'MarkerCreatorService', '$location', '$cookies', function($scope, $http, $upload, MarkerCreatorService, $location, $cookies) {
	$scope.loggedUser = $scope.$parent.getLoggedUser();
	$scope.sectionTitle = $location.search().edit == null? "Crear evento":"Modificar evento";
	$scope.$parent.pageTitle = "Donde es - "+$scope.sectionTitle;
	$scope.DEFAULT_IMG = "resources/img/imagen-no-disponible.gif";
	$scope.HOURS_BEFORE_EVENT = 12;
	$scope.tempEvent = {type:0, largeDesc: '', file:$scope.DEFAULT_IMG, originalFile:null};
	$scope.eventInEdition = null;
	
	Date.prototype.addHours = function(h){
	    this.setHours(this.getHours() + h);
	    return this;
	}
	
	$('#eventDatePicker').datetimepicker({
    	locale: 'es',
        format: 'LLLL',
    	minDate: new Date().addHours($scope.HOURS_BEFORE_EVENT)
    });
	
	if($location.search().edit != null){
		$http.get("rest/protected/event/getEventDataById/"+$location.search().edit).success(function(response){
			if(response.code == 200){
				$scope.editEvent(response.eventPOJO);
			}else if(response.code == 404){
				toastr.error('El evento no existe');
		    	window.location.href = "app#/index";
			}else{
				toastr.error('Ocurrió un error al cargar el evento');
		    	window.location.href = "app#/index";
			}
		}).error(function(response){
			toastr.error('Ocurrió un error al cargar el evento');
	    	window.location.href = "app#/index";
		});
	}
	
	$scope.editEvent = function(event){
		$scope.eventInEdition = event;
		
		$scope.tempEvent.name = event.name;
		$scope.tempEvent.desc = event.description;
		$scope.tempEvent.largeDesc = event.largeDescription;
		$scope.tempEvent.type = event.private_;
		$scope.tempEvent.placeName = event.place.name;
		$scope.tempEvent.file = event.image == null? $scope.DEFAULT_IMG:event.image;
		$scope.tempEvent.originalFile = null;
		
		$scope.map.center.latitude = event.place.latitude;
		$scope.map.center.longitude = event.place.longitude;
		
		$('#eventDatePicker').data("DateTimePicker").date(new Date(event.publishDate));
		$("#btnEventFormSubmit").text("Guardar cambios");
	}
	
	$scope.validationError = function(){
		toastr.warning('Algunos campos no cumplen con los requisitos');
	}
	
	$scope.onFileSelect = function($files) {
	    var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.jpg|.jpeg|.png|.gif)$");
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
		$("#btnEventFormSubmit").ladda().ladda("start");
		
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
								} else {
							    	toastr.warning('Eventos del usuario', 'No se encontraron eventos.');
								}
							} else {
						    	toastr.warning('Eventos del usuario', 'No se pudieron actualizar los datos en pantalla sin embargo el evento se creó con éxito.');
							}			
						});
						
					} else {
				    	toastr.error('Publicación del evento', 'Ocurrió un error al publicar el evento.');
					}
					
					$("#btnEventFormSubmit").ladda().ladda("stop");
			    	window.location.href = "app#/index";
				})
			.error(function(msj){
				$("#btnEventFormSubmit").ladda().ladda("stop");
				toastr.error('Ocurrió un error al crear el evento.');
			});
	};
	
	$scope.saveEventChanges = function(){
		$("#btnEventFormSubmit").ladda().ladda("start");
		
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
		
		if($scope.tempEvent.originalFile == null)
			$scope.tempEvent.originalFile = {};
		
		$upload.upload({url:'rest/protected/event/editEvent', data:event, file:$scope.tempEvent.originalFile})
		.progress(function(evt) {})
		.success(function(response) {
			toastr.success('Eventos del usuario', 'El evento se modificó con éxito.');
			$("#btnEventFormSubmit").ladda().ladda("stop");
	    	window.location.href = "app#/index";
		})
		.error(function(msj) {
			toastr.error('Eventos del usuario', 'Ocurrió un error al modificar el evento.');
			$("#btnEventFormSubmit").ladda().ladda("stop");
		});
	}
	
	// MAP
	
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
        $scope.map.control.refresh({latitude: marker.latitude, longitude: marker.longitude});
    }
    
    angular.element(document).ready(function(){
    	if($scope.event == null)
    		$scope.addCurrentLocation();
    });
}]);