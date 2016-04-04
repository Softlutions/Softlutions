'use strict';
var app = angular.module('dondeEs.myEvents', ['ngRoute', 'google-maps', 'mgo-angular-wizard', 'ngTable'])
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

app.controller('MyEventsCtrl', ['$scope', '$http', '$upload', 'MarkerCreatorService','$filter', 'WizardHandler', 'ngTableParams', function($scope,$http,$upload,MarkerCreatorService,$filter, WizardHandler, ngTableParams) { 
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
	
	if(!$scope.$parent.permissions.isAdmin){
		$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
			$scope.events = response.eventList;
			
			// https://github.com/esvit/ng-table/wiki/Configuring-your-table-with-ngTableParams
			var params = {
				page: 1,	// PAGINA INICIAL
				count: 10, 	// CANTIDAD DE ITEMS POR PAGINA
				sorting: {name: "asc"}
			};
			
			var settings = {
				total: $scope.events.length,	
				counts: [],	
				getData: function($defer, params){
					var fromIndex = (params.page() - 1) * params.count();
					var toIndex = params.page() * params.count();
					
					var subList = $scope.events.slice(fromIndex, toIndex);
					var sortedList = $filter('orderBy')(subList, params.orderBy());	// SOLO SI VAN A ORDENAR POR ALGUN CAMPO
					$defer.resolve(sortedList);
				}
			};
			
			$scope.eventsTable = new ngTableParams(params, settings);
		});
	}else{
		$http.get('rest/protected/event/getAllEventPublish').success(function(response) {
			$scope.events = response.eventList;
		});
	}
	
	$scope.listParticipants = function(eventId){
		$http.get('rest/protected/eventParticipant/getAllEventParticipants/'+eventId).success(function(response) {
			$scope.participants = response.eventParticipantsList;
		});
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
	
	$scope.loadAuctionServices = function (index) {
		$scope.auctionServices = $scope.auctionsEvent[index].auctionServices;
		setTimeout(function(){$('#modalAuctionsByEvent').modal('hide')}, 10);
		setTimeout(function(){$('#servicesOfAuction').modal('show')}, 900);
		
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
	
	
	$scope.openAuctions = function(){
		setTimeout(function(){$('#servicesOfAuction').modal('hide')}, 10)
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
	
	$scope.prepublishEvent = function(){
		setTimeout(function(){$('#modalAuctionsByEvent').modal('hide')}, 500);
		$http.get("rest/protected/chat/saveChatEventId/" + $scope.globalEventId).success(function(response){
			if (response.code == 200) {
				toastr.success('Nuevo chat administrativo', 'Visita la pagina de chats!');
				$scope.globalEventId = 0;
			}
		});
		
	}
	
	$scope.prepublishEventById = function(event){
		$http.get("rest/protected/chat/saveChatEventId/" + event.eventId).success(function(response){
			if (response.code == 200) {
				
				$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
					if (response.code == 200) {
						event.state = 2;
						$scope.events = response.eventList;
						window.location.href = "/dondeEs/app#/#";
						toastr.success('Prepublicación del evento', 'La prepublicación se hizo con éxito.');
					} else {
						toastr.warning('Prepublicación del evento');
					}
					
				});
			} else {
				toastr.error('Publicación del evento', 'Ocurrió un error al publicar el evento.');
			} 
		});

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
					$scope.refreshChart();
				}
			});
	}
	
	 $scope.geteventById = function(eventId){
		$scope.eventId = eventId;
	};
	
	$scope.selectCatalog = function(selectedCatalog){
		$scope.catalogServiceSelected = selectedCatalog;
	};
	
	$scope.addEmail = function(pemail){
		if(pemail !=null){
		$scope.listOfEmails.push(pemail.to);
		pemail.to = "";
		}else{	
		 	  setTimeout(function() {	
	                toastr.error('Tiene que ingresar la lista de correos que desea invitar', 'Error');
	            }, 1300);			 
		}
	}
	
	$scope.catalogsList = function(){
		if($scope.catalogs.length == 0){
			$http.get('rest/protected/serviceCatalog/getAllCatalogService').success(function(response) {
				$scope.catalogs = response.serviceCatalogList;
				$scope.tempAuction.selected = response.serviceCatalogList[0];
			});
		}
	};
	
	$('#modalAuctionEventServices').on('hidden.bs.modal', function () {
		$scope.tempAuction.selected = $scope.catalogs[0];
		$scope.tempAuction.name = '';
		$scope.tempAuction.description = '';
		var date = new Date();
		date.setDate(date.getDate() + 1);
		$('#datetimepicker').data('DateTimePicker').date(date);
	});
	
	$scope.openCreateAuction = function(){
		setTimeout(function(){$('#modalAuctionsByEvent').modal('hide')}, 10);
		$scope.catalogsList();
    	setTimeout(function(){$('#modalAuctionEventServices').modal('show')}, 900);
	}
	
	$scope.selectService = function(selectedService){
		var row = $("#serviceRow"+selectedService.serviceId);
		
		if(row.hasClass("selected-table-item")){
			row.removeClass("selected-table-item");
			
			var indexToRemove = -1;
			var i = 0;
			
			while(i < $scope.auctionServices.length && indexToRemove == -1){
				if($scope.auctionServices[i].service.serviceId == selectedService.serviceId){
					indexToRemove = i;
				}
				
				i++;
			}
			
			$scope.auctionServices.splice(indexToRemove, 1);
		}else{
			row.addClass("selected-table-item");
			var auctionService = {
					service: selectedService,
					description: '',
					date: new Date(),
					auction: null,
					price: 0,
					acept: 0	
			};
			
			$scope.auctionServices.push(auctionService);
		}
	}
	
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
				toastr.success('Correo enviado')
			}) .error(function(response){
				 toastr.error('Verifique la direccion de correo electronico y su coneccion a internet', 'Error');
			})
		}else{	
			 	  setTimeout(function() {	
	                toastr.error('Tiene que ingresar la lista de correos que desea invitar', 'Error');

	            }, 1300);
			 
		}
	}
		
	$scope.publishEvent = function(event){  
		$scope.requestObject = {"eventId":event.eventId};
		$http.put('rest/protected/event/publishEvent',$scope.requestObject).success(function(response) {
			if (response.code == 200) {
				window.location.href = "/dondeEs/app#/#";
			} else {
				toastr.error('Publicación del evento', 'Ocurrió un error al publicar el evento.');
			} 
		});	
	}
	
	$scope.cancel = function(serviceContact){
		$http.post("rest/protected/serviceContact/cancelServiceContact/"+serviceContact.serviceContractId, serviceContact).success(function(response){
			if(response.code == 200){
				serviceContact.state = 2;

				$("#btnCancelService-"+serviceContact.serviceContractId).text("Cancelado");
				$("#btnCancelService-"+serviceContact.serviceContractId).removeClass("btn-danger");
				$("#btnCancelService-"+serviceContact.serviceContractId).addClass("btn-warning");
				$("#btnCancelService-"+serviceContact.serviceContractId).prop("disabled", true);
				
				$scope.refreshChart();
			}
		});
	}
	
	$scope.cancelEvent = function(event){  
	 	$scope.requestObject = {"eventId":event.eventId};
	 	$http.put('rest/protected/event/cancelEvent',$scope.requestObject).success(function(response) {
	 		if (response.code == 200) {
	 			window.location.href = "/dondeEs/app#/#";
			} else if (response.errorMessage == "notification cancel event error") {
		    	toastr.options = {
	                    timeOut: 7000
		        };
		    	toastr.warning('Cancelación del evento', 'Ocurrió un error al notificar a los involucrados sin embargo el evento se canceló con éxito.');
		 		$http.get('rest/protected/event/getAllEventByUser/'+$scope.loggedUser.userId).success(function(response) {
			 		if (response.code == 200) {
			 			$scope.events = response.eventList;
			 		} else {
						toastr.warning('Cancelación del evento', 'No se pudieron actualizar los datos en pantalla.')
					}
		 		});
			} else {
		    	toastr.error('Cancelación del evento', 'Ocurrió un error al cancelar el evento.');
			} 
		 });
	 }
	
	$http.get('rest/protected/service/getServiceByProvider/'+$scope.loggedUser.userId ).success(function(response) {
		$scope.services = response.serviceLists;
	});
	
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
		
		//$scope.tempEvent.date = new Date(event.publishDate);
		$('#eventDatePicker').data("DateTimePicker").date(new Date(event.publishDate));
		
		$("#eventFormTitle").text("Modificar evento");
		$("#btnEventFormSubmit").text("Guardar cambios");
		$scope.eventForm = true;
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
    
 //#REGION ASISTENTE DE CREACION
    
    $scope.showCreateEventForm = function(estado){
    	if(estado) $scope.eventsWizard = true;
    	else $scope.eventsWizard = false;
    }
    
    
    $scope.createEventAsistente = function() {
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
						'loggedUser':$scope.loggedUser.userId,
					},
					file : $scope.tempEvent.originalFile,
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
												WizardHandler.wizard().next();
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
					
					$scope.resetCreateEvent();
				})
			.error(function(msj){
				console.log(msj);
			});
	};
    
    
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
		
	$http.get('rest/protected/auction/getAllAuctionByEvent/'+3).success(function(response) {
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
	  //#ENDREGION ASISTENTE DE CREACION
    
    
    
    
}]);