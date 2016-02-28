'use strict';

angular.module('eventsPublishModule.eventsPublish', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/eventsPublish', {
    templateUrl: 'resources/eventsPublish.html',
    controller: 'eventsPublishCtrl'
  });
}])

.controller('eventsPublishCtrl', ['$scope','$http',function($scope,$http,$upload) {	
	$scope.eventsPublish = [];
	$scope.requestObject = {"pageNumber": 0,"pageSize": 0,"direction": "","sortBy": [""],"searchColumn": "string","searchTerm": "","eventsPublish": {}};
	
	$http.get('rest/protected/event/getAllEventPublish',$scope.requestObject).success(function(response) {
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
			
		console.log("$scope.eventsPublish: ",$scope.eventsPublish);		
	});
	
	$scope.publishEvent = function(eventId){  
		$scope.requestObject = {"eventId":eventId};
		$http.put('rest/protected/event/publishEvent',$scope.requestObject).success(function(response) {
			console.log("$scope.eventsPublish: ",response);		
		})
	}
}]);