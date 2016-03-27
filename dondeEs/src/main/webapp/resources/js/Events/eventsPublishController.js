'use strict';

angular.module('dondeEs.eventsPublish', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/eventsPublish', {
    templateUrl: 'resources/event/eventsPublish.html',
    controller: 'eventsPublishCtrl'
  });
}])

.controller('eventsPublishCtrl', ['$scope','$http',function($scope,$http) {	
	$scope.loggedUser = JSON.parse(localStorage.getItem("loggedUser"));
	$scope.eventsPublish = [];
	$scope.requestObject = {"eventsPublish": {}};
	$scope.step = 0;
	$scope.commentList = [];
	
	$http.get('rest/protected/event/getAllEventPublish',$scope.requestObject).success(function(response) {
		if (response.code == 200) {
			if (response.eventList != null && response.eventList.length > 0) {
				$scope.eventsPublish = response.eventList;
				for (var i=0; i<$scope.eventsPublish.length; i++) {
					
					$scope.eventsPublish[i].day = $scope.eventsPublish[i].publishDate.substring(8, 10);
					
					switch($scope.eventsPublish[i].publishDate.substring(5, 7)) {
					    case '01': $scope.eventsPublish[i].month = "ENE"; break;
					    case '02': $scope.eventsPublish[i].month = "FEB"; break;
					    case '03': $scope.eventsPublish[i].month = "MAR"; break;
					    case '04': $scope.eventsPublish[i].month = "ABR"; break;
					    case '05': $scope.eventsPublish[i].month = "MAY"; break;
					    case '06': $scope.eventsPublish[i].month = "JUN"; break;
					    case '07': $scope.eventsPublish[i].month = "JUL"; break;
					    case '08': $scope.eventsPublish[i].month = "AGO"; break;
					    case '09': $scope.eventsPublish[i].month = "SEP"; break;
					    case '10': $scope.eventsPublish[i].month = "OCT"; break;
					    case '11': $scope.eventsPublish[i].month = "NOV"; break;
					    case '12': $scope.eventsPublish[i].month = "DIC"; break;
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
	};
	
	$scope.showComments = function(selectedEvent){
		$scope.step = 1;
		$http.get('rest/protected/comment/getCommentsByEvent/'+selectedEvent.eventId).success(function(response) {
			$scope.commentList = response.commentList;
		});
		$http.get("rest/protected/eventParticipant/getEventParticipantByUserAndEvent/"+$scope.loggedUser.userId+"/"+selectedEvent.eventId).success(function(response){
			
			if(response.code == 200){
				$scope.eventParticipant = response.eventParticipant;
				console.log($scope.eventParticipant);
			}else{
				$("#commentDiv").hide();
			}
		});
	};
	
	$scope.setDate = function(date){
		var m = moment.locale('es');
		var stringDate = new Date(date).toString();
		stringDate = stringDate.substring(4,24);
		m = moment(stringDate,"MMM-DD-YYYY HH:mm:ss");
		return m.fromNow();
	}
	
	$scope.validateUser = function(){
		return true;
	};
	
	$scope.commentEvent = function(){
		if($scope.comment != undefined){
			var eventComment = {
					content: $scope.comment,
					date: new Date(),
					eventParticipant: $scope.eventParticipant
			}
			console.log(eventComment);
			$http({method: 'POST',url:'rest/protected/comment/createComment', data:eventComment, headers: {'Content-Type': 'application/json'}}).success(function(response) {
				if(response.code==200){
					$scope.commentList.push(eventComment);
					$scope.comment = undefined;
				}
			});
		}		
	};
	
}]);