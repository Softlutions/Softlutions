'use strict';

angular.module('landingPageModule.viewEvent', ['ngRoute', 'ngFileUpload', 'ngTable', 'ngCookies'])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/viewEvent', {
		templateUrl: 'resources/landingPage/viewEvent.html',
		controller: 'viewEventCtrl'
	});
}])

.controller('viewEventCtrl', ['$scope', '$http', '$timeout', 'Upload', '$location', 'ngTableParams', '$filter', '$cookies', "$interval", function($scope, $http, $timeout, Upload, $location, ngTableParams, $filter, $cookies, $interval) {
	$scope.loggedUser = null;
	var loginCookie = $cookies.getObject("loggedUser");
	if(loginCookie != null)
		$scope.loggedUser = JSON.parse(loginCookie);
	
	$scope.DEFAULT_USER_IMAGE = "resources/img/default-profile.png";
	$scope.DEFAULT_EVENT_IMAGE = "resources/img/defaultEventImage.png";
	$scope.commentPreviewFile = null;
	$scope.eventParticipant = null;
	$scope.commentFile = null;
	$scope.participants = [];
	$scope.commentList = [];
	$scope.modalUser = {};
	
	$scope.event = null;
	$scope.images = [];
	$scope.email = "";
	$scope.view = 0;
	
	$scope.files = [];
	$scope.sending = false;
	$scope.previewFiles = [];
	$scope.carouselImages = [];
	$scope.refeshInterval = null;
	$scope.progressPercentaje = 0;
	$scope.showUploadField = false;
	
	$scope.logout = function(){
		$http.get("rest/login/logout").success(function(response){
			$cookies.remove("loggedUser");
			window.location.href = "#/landingPage";
		});
	}
	
	$scope.changeView = function(view){
		$("#btnView"+$scope.view).removeClass("btn-info");
		$scope.view = view;
		$("#btnView"+view).addClass("btn-info");
		
		if($scope.refeshInterval != null)
			$interval.cancel($scope.refeshInterval);
		
		if(view == 2){
			$scope.refeshInterval = $interval(function() {
				$http.get("rest/landing/getImagesByEventId/"+$scope.event.eventId).success(function(responseImgs){
					var filteredList = [];
					responseImgs.images.forEach(function(entry){
						if(entry.eventParticipant.state == 1 || entry.eventParticipant.state == 2)
							filteredList.push(entry);
					});
					
					$scope.images = filteredList;
				});
		    }, 3000);
		}
		
		if(view == 1){
			$scope.refeshInterval = $interval(function() {
				$http.get('rest/landing/getAllEventParticipants/'+$scope.event.eventId).success(function(response) {
					var filteredList = [];
					response.eventParticipantsList.forEach(function(entry){
						if(entry.state != 0)
							filteredList.push(entry);
					});
					$scope.participants = filteredList;
				});
		    }, 3000);
		}
		
		if(view == 0){
			$scope.refeshInterval = $interval(function() {
				$http.get('rest/landing/getCommentsByEvent/'+$scope.event.eventId).success(function(response) {
					var filteredList = [];
					response.commentList.reverse().forEach(function(entry){
						if(entry.eventParticipant.state == 1 || entry.eventParticipant.state == 2)
							filteredList.push(entry);
					});
					
					$scope.commentList = filteredList;
					$scope.commentsTable.reload();
				});
		    }, 3000);
		}
	}
	
	$scope.$on('$destroy', function() {
		if($scope.refeshInterval != null)
			$interval.cancel($scope.refeshInterval);
	});
	
	// --------------------------- LOAD EVENT DATA
	
	$http.get("rest/landing/getEventById/"+$location.search().view).success(function(response){
		if(response.code == 200){
			$scope.event = response.eventPOJO;
			
			if($scope.loggedUser == null && $scope.event.private_ == 1){
				toastr.warning("Este evento es privado", "Favor inicia sesión");
				window.location.href="#/landingPage";
			}else
				loadData();
		}else{
			if(response.code == 200)
				toastr.warning(response.codeMessage);
			else
				toastr.error(response.codeMessage);
			
			window.location.href="#/landingPage";
		}
	}).error(function(response){
		toastr.error('El evento no a sido publicado o no existe');
		window.location.href="#/landingPage";
	});
	
	function loadData(){
		$http.get("rest/landing/getImagesByEventId/"+$scope.event.eventId).success(function(responseImgs){
			var filteredList = [];
			responseImgs.images.forEach(function(entry){
				if(entry.eventParticipant.state == 1 || entry.eventParticipant.state == 2)
					filteredList.push(entry);
			});
			
			$scope.images = filteredList;
			$scope.carouselImages = $scope.images;
		});
		
		$http.get('rest/landing/getAllEventParticipants/'+$scope.event.eventId).success(function(response) {
			var filteredList = [];
			response.eventParticipantsList.forEach(function(entry){
				if(entry.state != 0)
					filteredList.push(entry);
			});
			$scope.participants = filteredList;
		});
		
		$http.get('rest/landing/getCommentsByEvent/'+$scope.event.eventId).success(function(response) {
			var filteredList = [];
			response.commentList.reverse().forEach(function(entry){
				if(entry.eventParticipant.state == 1 || entry.eventParticipant.state == 2)
					filteredList.push(entry);
			});
			
			$scope.commentList = filteredList;
			
			var params = {
				page: 1,
				count: 6,
				sorting: {date: "desc"}
			};
			
			var settings = {
				total: $scope.commentList.length,	
				counts: [],	
				getData: function($defer, params){
					var fromIndex = (params.page() - 1) * params.count();
					var toIndex = params.page() * params.count();
					
					var subList = $scope.commentList.slice(fromIndex, toIndex);
					var sortedList = $filter('orderBy')(subList, params.orderBy());
					$defer.resolve(sortedList);
				}
			};
			
			$scope.commentsTable = new ngTableParams(params, settings);
		});
		
		$scope.changeView(0);
		// --------------------------- GET PARTICIPANT, ONLY IF A USER IS ALREADY LOGUED
		
		if($scope.loggedUser != null){
			$http.get("rest/landing/getEventParticipantByUserAndEvent?userId="+$scope.loggedUser.userId+"&eventId="+$scope.event.eventId).success(function(response){
				if(response.code == 200){
					$scope.eventParticipant = response.eventParticipant;
					
					if($scope.event.private_ == 1){
						if($scope.eventParticipant.state == 0)
							toastr.warning("Este evento es privado", "Ya rechazaste la invitación");
						if($scope.eventParticipant.state == 1)
							toastr.warning("Este evento es privado", "Primero acepta la invitación que se te envió");
						if($scope.eventParticipant.state == 3 || $scope.eventParticipant.state == 4)
							toastr.warning("Este evento es privado", "El promotor que bloqueó");
						
						if($scope.eventParticipant.state != 2)
							window.location.href="#/landingPage";
					}
				}else{
					if($scope.event.private_ == 1){
						toastr.warning("Este evento es privado", "No has sido invidado");
						window.location.href="#/landingPage";
					}else if($location.search().assist != null){
						$http.get("rest/landing/createParticipant?userId="+$scope.loggedUser.userId+"&eventId="+$scope.event.eventId).success(function(response){
							if(response.code == 200){
								$scope.eventParticipant = response.eventParticipant;
								toastr.success('Estas participando en este evento');
							}else if(response.code == 202){
								toastr.warning('Ya estas participando en este evento');
							}else{
								toastr.error('Para comentar o subir imágenes primero debe indicar que va a participar');
							}
						});
					}
				}
			}).error(function(err){
				if($scope.event.private_ == 1){
					toastr.warning("Este evento es privado", "No has sido invidado");
					window.location.href="#/landingPage";
				}
			});
		}
	}
	
	//--------------------------- INAPPOSITE
	
	$scope.blockedParticipantsFilter = function(participant){
		return participant.state == 3 || participant.state == 4;
	}
	
	$scope.inappositeImg = function(img){
		$http.get("rest/landing/deleteImage/"+img.eventImageId).success(function(response){
			if(response.code == 200){
				$scope.images.splice($scope.images.indexOf(img), 1);
				toastr.warning('La imagen fue reportada');
				
				/*$http.get("rest/landing/reportParticipant/"+img.eventParticipant.eventParticipantId).success(function(response){
					if(response.code == 200){
						toastr.warning('Usuario reportado');
					}else{
						toastr.warning('No se pudo reportar el usuario');
					}
				});*/
			}else{
				toastr.warning('No se pudo reportar la imagen');
			}
		});
	}
	
	$scope.removeImg = function(img){
		$http.get("rest/landing/deleteImage/"+img.eventImageId).success(function(response){
			if(response.code == 200){
				$scope.images.splice($scope.images.indexOf(img), 1);
				toastr.warning('La imagen fue eliminada');
			}else{
				toastr.warning('No se pudo eliminar la imagen');
			}
		});
	}
	
	$scope.removeComment = function(comment){
		$http.get("rest/landing/deleteComment/"+comment.commentId).success(function(response){
			if(response.code == 200){
				$scope.commentList.splice($scope.commentList.indexOf(comment), 1);
				$scope.commentsTable.reload();
				toastr.warning('Comentario eliminado');
			}else{
				toastr.warning('No se pudo eliminar el comentario');
			}
		});
	}
	
	$scope.participantState = function(participant){
		var newState;
		
		if(participant.state == 1) // pendiente
			newState = 3; // pendiente-bloqueado
		else if(participant.state == 2) // asistente
			newState = 4; // asistente-bloqueado
		else if(participant.state == 3) // pendiente-bloqueado
			newState = 1; // pendiente
		else
			newState = 2; // asistente
		
		$http.get("rest/landing/participantState/"+participant.eventParticipantId+"?state="+newState).success(function(response){
			if(response.code == 200){
				participant.state = newState;
			}else{
				toastr.error('No se pudo completar la solicitud');
			}
		});
	}
	
	//--------------------------- ASSIST
	//0 no asiste, 1 pendiente, 2 asiste, 3 bloqueado pendiente, 4 bloqueado asiste
	$scope.invite = function(){
		var email = $scope.email;
		$scope.email = "";
		
		$("#laddaSendInvitation").ladda().ladda("start");
		
		$http({method: 'POST',url:'rest/landing/sendEmailInvitation?eventId='+$scope.event.eventId, data:{listSimple:[email]}, headers: {'Content-Type': 'application/json'}}).success(function(response) {	
			if(response.code == 200){
				toastr.success('Invitación enviada');
			}else{
				toastr.error('No se pudo enviar la invitación');	
			}
			
			$("#laddaSendInvitation").ladda().ladda("stop");
		}).error(function(response){
			 toastr.error('No se pudo enviar la invitación');
			$("#laddaSendInvitation").ladda().ladda("stop");
		})
	}
	
	$scope.assist = function(){
		if($scope.loggedUser == null){
			window.location.href="#/landingPage?p=login&event="+$scope.event.eventId+"&assist";
		}else if($scope.eventParticipant != null && $scope.eventParticipant.state == 0){
			$http.get("rest/landing/participantState/"+$scope.eventParticipant.eventParticipantId+"?state="+2).success(function(response){
				if(response.code == 200){
					$scope.eventParticipant.state = 2;
				}else{
					toastr.error('No se pudo completar la solicitud');
				}
			});
		}else if($scope.eventParticipant == null){
			$http.get("rest/landing/createParticipant?userId="+$scope.loggedUser.userId+"&eventId="+$scope.event.eventId).success(function(response){
				if(response.code == 200){
					$scope.eventParticipant = response.eventParticipant;
				}else if(response.code == 202){
					toastr.warning('Ya estas participando en este evento');
				}
			});
		}
	}
	
	//--------------------------- UPLOAD IMAGES
	
	$scope.loadFiles =  function() {
		for(var i=0;i<$scope.files.length;i++) {
			var reader = new FileReader();
			
			reader.onload = (function(tempFile){
			    return function(e){
		        	var data = {
		        		index:$scope.previewFiles.length,
		        		name:tempFile.name,
		        		size:tempFile.size,
		        		originalFile:tempFile,
		        		thumbnail:e.target.result
		        	};
		        	$scope.previewFiles.push(data);
			    };
			})($scope.files[i]);
			
	        reader.readAsDataURL($scope.files[i]);
		}
    };
	
    $scope.upload = function(files) {
    	if(files.length > 0 && !$scope.sending && $scope.eventParticipant.eventParticipantId > 0){
    		$scope.sending = true;
        	var uploaded = 0;
        	
    		for(var i=0;i<files.length;i++) {
    			var file = files[i].originalFile;
    			
    			if(!file.$error) {
    				Upload.upload({
    					url: 'rest/landing/saveImage',
    					data: {
    						"eventParticipantId": $scope.eventParticipant.eventParticipantId,
    						"file": file
    					}
    				}).then(function(resp) {
    					if(resp.status == 200)
    						uploaded++;
    					
    					if(uploaded == files.length){
    		        		$scope.sending = false;
    		        		toastr.success('Imagenes publicadas');
    		        	}else if(i == files.length-1){
    		        		toastr.warning('Algunas imágenes no se pudieron publicar');
    		        	}
    					
    					window.location.reload();
    				}, null, function(evt) {
    					$scope.progressPercentaje = parseInt(100.0 * evt.loaded / evt.total);
    				});
    			}
            }
    	}else if($scope.eventParticipant.eventParticipantId == 0){
    		toastr.error('Este usuario no puede subir imágenes a este evento');
    	}
    }
	
	// --------------------------- COMMENTS
    
    $scope.attach = function(file) {
    	if(file != null){
    		var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.jpg|.jpeg|.png|.gif)$");
    	    if(regex.test(file.name.toLowerCase())){
    	    	$scope.commentFile = file;
    	    	
    	    	var reader = new FileReader();
    	        reader.onload = function(e){
    	        	$scope.commentPreviewFile = e.target.result;
    	        	$scope.$apply();
    	        }
    	        
    	        reader.readAsDataURL(file);
    	    }else{
    	    	$scope.commentPreviewFile = null;
    	    	$scope.commentFile = null;
    	    	toastr.error('Carga de la imagen', 'El archivo no tiene un formato válido.');
    	    }
    	}
    }
    
	$scope.commentEvent = function(){
		if(($scope.comment != undefined && $scope.comment.length > 0) || $scope.commentFile != null){
			if($scope.comment == undefined)
				$scope.comment = "";
			
			var eventComment = {
				content: $scope.comment,
				date: new Date(),
				image: $scope.commentPreviewFile,
				eventParticipant: $scope.eventParticipant
			}
			
			Upload.upload({
				url: 'rest/landing/saveComment',
				data: {
					"participantId": $scope.eventParticipant.eventParticipantId,
					"content": $scope.comment,
					"file": $scope.commentFile
				}
			}).then(function(resp) {
				if(resp.status == 200){
					$scope.commentList.push(eventComment);
					$scope.commentsTable.reload();
					$scope.comment = undefined;
					$scope.commentPreviewFile = null;
					$scope.commentFile = null;
				}else{
					toastr.error('No se pudo publicar el comentario');
				}
			}, function(err) { toastr.error('Para comentar o subir imágenes primero debe indicar que va a participar'); }, function(prog) {});	
		}
	}
	
	$scope.setDate = function(date){
		var stringDate = new Date(date).toString();
		stringDate = stringDate.substring(4, 24);
		var m = moment(stringDate,"MMM DD YYYY HH:mm:ss").locale('es');
		return m.fromNow();
	}
	
	$scope.viewProfile = function(user){
		$scope.modalUser = user;
		$("#userProfileModal").modal("toggle");
	}
	
	// --------------------------- SCROLL LOGIC
	
	angular.element(document).ready(function () {
		var cbpAnimatedHeader = (function() {
	        var docElem = document.documentElement,
	                header = document.querySelector( '.navbar-default' ),
	                didScroll = false,
	                changeHeaderOn = 200;
	        function init() {
	            window.addEventListener( 'scroll', function( event ) {
	                if( !didScroll ) {
	                    didScroll = true;
	                    setTimeout( scrollPage, 250 );
	                }
	            }, false );
	        }
	        
	        function scrollPage() {
	            var sy = scrollY();
	            if ( sy >= changeHeaderOn ) {
	                $(header).addClass('navbar-scroll')
	            }
	            else {
	                $(header).removeClass('navbar-scroll')
	            }
	            didScroll = false;
	        }
	        function scrollY() {
	            return window.pageYOffset || docElem.scrollTop;
	        }
	        init();

	    })();
		
	    new WOW().init();
	});
}]);