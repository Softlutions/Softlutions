'use strict';

angular.module('landingPageModule.viewEvent', ['ngRoute', 'ngFileUpload'])

.config(['$routeProvider', function($routeProvider) {
	$routeProvider.when('/viewEvent', {
		templateUrl: 'resources/landingPage/viewEvent.html',
		controller: 'viewEventCtrl'
	});
}])
/*
.directive('onErrorSrc', function() {
    return {
        link: function(scope, element, attrs) {
        	console.log(element);
			element.bind('error', function() {
				if (attrs.src != attrs.onErrorSrc) {
					attrs.$set('src', attrs.onErrorSrc);
					console.log("error OK!");
				}
			});
        }
    }
})*/

.controller('viewEventCtrl', ['$scope', '$http', '$timeout', 'Upload', '$location', function($scope, $http, $timeout, Upload, $location) {
	$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
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
		console.log($scope.event);
		$http.get("rest/landing/getImagesByEventId/"+$scope.event.eventId).success(function(responseImgs){
			$scope.images = responseImgs.images;
			console.log($scope.images);
		});
		
		$http.get('rest/landing/getCommentsByEvent/'+$scope.event.eventId).success(function(response) {
			$scope.commentList = response.commentList;
		});
		
		// --------------------------- GET PARTICIPANT, ONLY IF A USER IS ALREADY LOGUED
		
		if($scope.loggedUser != null && $scope.event != null){
			$http.get("rest/landing/getEventParticipantByUserAndEvent?userId="+$scope.loggedUser.userId+"&eventId="+$scope.event.eventId).success(function(response){
				if(response.code == 200){
					$scope.eventParticipant = response.eventParticipant;
				}else{
					toastr.error('Para comentar o subir imágenes primero debe indicar que va a participar');
				}
			});
		}
	}
	
	//--------------------------- UPLOAD IMAGES
	
	$scope.$watch('files', function() {
		for(var i=0;i<$scope.files.length;i++) {
			var reader = new FileReader();
			var file = $scope.files[i];
			
	        reader.onload = function(e) {
	        	var data = {
	        		index:$scope.previewFiles.length,
	        		name:file.name,
	        		size:file.size,
	        		originalFile:file,
	        		thumbnail:e.target.result
	        	};
	        	$scope.previewFiles.push(data);
	        };
	        
	        reader.readAsDataURL(file);
		}
    });
	
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
		var m = moment.locale('es');
		var stringDate = new Date(date).toString();
		stringDate = stringDate.substring(4,24);
		m = moment(stringDate,"MMM-DD-YYYY HH:mm:ss");
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

	    // Activate WOW.js plugin for animation on scroll
	    new WOW().init();
	});
}]);