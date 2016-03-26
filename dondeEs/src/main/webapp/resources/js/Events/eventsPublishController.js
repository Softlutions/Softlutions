'use strict';

angular.module('dondeEs.eventsPublish', ['ngRoute', 'ngFileUpload'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/eventsPublish', {
    templateUrl: 'resources/event/eventsPublish.html',
    controller: 'eventsPublishCtrl'
  });
}])

.controller('eventsPublishCtrl', ['$scope','$http','Upload','$timeout',function($scope,$http,Upload,$timeout) {
	$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
	
	$scope.eventsPublish = [];
	$scope.requestObject = {"eventsPublish": {}};
	
	$http.get('rest/protected/event/getAllEventPublish',$scope.requestObject).success(function(response) {
		if (response.code == 200) {
			if (response.eventList != null && response.eventList.length > 0) {
				$scope.eventsPublish = response.eventList;
				
				for (var i=0; i<$scope.eventsPublish.length; i++) {
					$scope.eventsPublish[i].day = $scope.eventsPublish[i].publishDate.substring(8, 10);
					
					switch($scope.eventsPublish[i].publishDate.substring(5, 7)) {
					    case '01': $scope.eventsPublish[i].month = "JAN"; break;
					    case '02': $scope.eventsPublish[i].month = "FEB"; break;
					    case '03': $scope.eventsPublish[i].month = "MAR"; break;
					    case '04': $scope.eventsPublish[i].month = "APR"; break;
					    case '05': $scope.eventsPublish[i].month = "MAY"; break;
					    case '06': $scope.eventsPublish[i].month = "JUN"; break;
					    case '07': $scope.eventsPublish[i].month = "JUL"; break;
					    case '08': $scope.eventsPublish[i].month = "AUG"; break;
					    case '09': $scope.eventsPublish[i].month = "SEP"; break;
					    case '10': $scope.eventsPublish[i].month = "OCT"; break;
					    case '11': $scope.eventsPublish[i].month = "NOV"; break;
					    case '12': $scope.eventsPublish[i].month = "DEC"; break;
					    default: $scope.eventsPublish[i].month = "NONE";
					}
				}
			} else {
		    	toastr.warning('Eventos publicados', 'No se encontraron eventos.');
			}
		} else {
	    	toastr.error('Eventos publicados', 'Ocurrió un error al buscar los eventos.');
		}
	});
	
	$scope.goToEventPromoterProfile = function (eventPromoterId) {
		//	$location.url('/login');  colocar ruta del perfil del promotor. 
	}
	
	//----------------------------------------------------------------------
	
	// *************
	
	$scope.eventId = 6;
	
	// *************
	
	$scope.files = [];
	$scope.sending = false;
	$scope.previewFiles = [];
	$scope.progressPercentaje = 0;
	$scope.eventParticipant = 0;
	
	$http.get("rest/protected/eventParticipant/getEventParticipantByUserAndEvent/"+$scope.loggedUser.userId+"/"+$scope.eventId).success(function(response){
		if(response.code == 200){
			$scope.eventParticipant = response.eventParticipant;
		}else{
			$("#btnPublish").attr("disabled", true);
			toastr.error('Para subir imágenes a este evento primero debes indicar que vas a participar');
		}
	});
	
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
    					url: 'rest/protected/eventImage/saveImage',
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
    
    //----------------------------------------------------------------------
    
    $scope.eventImages = [];
    $scope.selectedImgs = [];
    $scope.enableImgSelect = false;
    
    $scope.enableSelect = function(){
    	$scope.enableImgSelect = !$scope.enableImgSelect;
    	
    	if($scope.enableImgSelect){
    		$("#btnImgSelect").val("Cancelar");
    	}else{
    		$("#btnImgSelect").val("Seleccionar");
    		
    		$scope.selectedImgs.forEach(function(entry){
    			$("#eventImg-"+entry.eventImageId).removeClass("selectedImg");
    		});
    		
    	    $scope.selectedImgs = [];
    	}
    }
    
    $scope.selectImg = function(img){
    	if($scope.enableImgSelect){
    		var index = $scope.selectedImgs.indexOf(img);
    		
    		if(index >= 0){
    			$scope.selectedImgs.splice(index, 1);
    			$("#eventImg-"+img.eventImageId).removeClass("selectedImg");
    		}else{
    			$scope.selectedImgs.push(img);
    			$("#eventImg-"+img.eventImageId).addClass("selectedImg");
    		}
    	}
    }
    
    $scope.loadImgs = function(event){
    	$scope.eventImages = [];
    	
    	$http.get("rest/protected/eventImage/getAllByEventId/"+event.eventId).success(function(response){
    		if(response.code == 200)
    			$scope.eventImages = response.images;
    	});
    }
    
    $scope.deleteImgs = function(){
    	var deleted = 0;
    	
    	for(var i=0;i<$scope.selectedImgs.length;i++){
    		var img = $scope.selectedImgs[i];
    		
    		$http({method: "DELETE", url:"rest/protected/eventImage/deleteEventImage/"+img.eventImageId}).then(function(response){
        		if(response.status == 200){
        			$scope.eventImages.splice($scope.eventImages.indexOf(img), 1);
        			deleted++;
        		}
            	
            	if(deleted == $scope.selectedImgs.length){
            		toastr.success('Imágenes eliminadas');
            		var temp = $scope.eventImages;
            		$scope.eventImages = [];
            		$scope.enableSelect();
            		
            		$timeout(function(){
            			$scope.eventImages = temp;
            		});
            	}else if(i == $scope.selectedImgs.length-1){
            		toastr.error('No se pudo eliminar algunas imágenes');
            	}
            	
        	}, function(msj){
        		console.log("catchedError: ", msj);
        	});
    	}
    }
}]);