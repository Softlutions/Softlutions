'use strict';
angular.module('dondeEs').factory(
		'request_factory',
		function($q, $http, $rootScope) {
			var dataRequest = {
				getViewRequest : function(urlAction, data) {
					var deferred = $q.defer();
					$http({
						url : urlAction,
						method : 'GET',
						params : data
					}).then(function(result) {
						deferred.resolve(result.data);
					}, function(error) {
						deferred.reject(error.data);
					});
					return deferred.promise;
				},

				postDataRequest : function(urlAction, data) {
					var deferred = $q.defer(), self = this;
					$http.post(urlAction, data).success(
							function(result) {
								deferred.resolve(response);
							}).error(function(error) {
						deferred.reject(error.data);
					});

					return deferred.promise;
				}
			}
			return dataRequest;
		});

// rest/
// Ustedes van a llamar esta fabrica desde el servicio, la cual se encarga de
// realizar los request
// Se preguntan porque estan en esta carpeta de Commons, el significado de esta
// carpeta, es colocar
// las cosas que nosotros sabemos que todos vamos a reutilizar
// De aca no toquen nada solo que sea un dedaso que se me haya ido o alguillo
// de momento dejare el GET por cuestiones de que no se vayan a embotar de info
// o algo xD

