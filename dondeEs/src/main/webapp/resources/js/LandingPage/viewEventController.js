'use strict';

angular.module('landingPageModule.viewEvent', ['ngRoute', 'ngFileUpload', 'ngTable', 'ngCookies'])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/viewEvent', {
		templateUrl: 'resources/landingPage/viewEvent.html',
		controller: 'viewEventCtrl'
	});
}])

.controller('viewEventCtrl', ['$scope', '$http', '$timeout', 'Upload', '$location', 'ngTableParams', '$filter', '$cookies', function($scope, $http, $timeout, Upload, $location, ngTableParams, $filter, $cookies) {
	$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
	$scope.DEFAULT_USER_IMAGE = "http://bootdey.com/img/Content/user_1.jpg";
	$scope.commentPreviewFile = null;
	$scope.eventParticipant = null;
	$scope.commentFile = null;
	$scope.commentList = [];
	
	$scope.event = null;
	$scope.images = [];
	
	$scope.files = [];
	$scope.sending = false;
	$scope.previewFiles = [];
	$scope.progressPercentaje = 0;
	$scope. showUploadField = false;
	//localStorage.setItem("loggedUser", null);
	
	$scope.logout = function(){
		$http.get("rest/login/logout").success(function(response){
			var sessionCookie = $cookies.getObject("lastSession");
			if(sessionCookie != null){
				sessionCookie["sessionClosed"] = true;
				$cookies.putObject("lastSession", sessionCookie);
			}
			
			$scope.loggedUser = null;
			localStorage.setItem("loggedUser", null);
			window.location.href = "#/landingPage";
		});
	}
	
	// --------------------------- LOAD EVENT DATA
	
	$http.get("rest/landing/getEventById/"+$location.search().view).success(function(response){
		if(response.code == 200){
			$scope.event = response.eventPOJO;
			loadData();
		}else{
			toastr.error('El evento no existe');
			window.location.href="#/landingPage";
		}
	}).error(function(){
		toastr.error('El evento no existe');
		window.location.href="#/landingPage";
	});
	
	function loadData(){
		$http.get("rest/landing/getImagesByEventId/"+$scope.event.eventId).success(function(responseImgs){
			$scope.images = responseImgs.images;
		});
		
		$http.get('rest/landing/getCommentsByEvent/'+$scope.event.eventId).success(function(response) {
			$scope.commentList = response.commentList.reverse();
			
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
		
		// --------------------------- GET PARTICIPANT, ONLY IF A USER IS ALREADY LOGUED
		
		if($scope.loggedUser != null){
			$http.get("rest/landing/getEventParticipantByUserAndEvent?userId="+$scope.loggedUser.userId+"&eventId="+$scope.event.eventId).success(function(response){
				if(response.code == 200){
					$scope.eventParticipant = response.eventParticipant;
				}else{
					if($location.search().assist != null){
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
			});
		}
	}

	//--------------------------- ASSIST
	
	$scope.assist = function(){
		if($scope.loggedUser == null){
			window.location.href="#/landingPage?p=login&event="+$scope.event.eventId+"&assist";
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
    		var regex = new RegExp("([a-zA-Z0-9\s_\\.\-:])+(.jpg|.png|.gif)$");
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
		if($scope.comment != undefined){
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
	};
	
	$scope.setDate = function(date){
		var stringDate = new Date(date).toString();
		stringDate = stringDate.substring(4, 24);
		var m = moment(stringDate,"MMM DD YYYY HH:mm:ss").locale('es');
		return m.fromNow();
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