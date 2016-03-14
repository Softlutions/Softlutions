'use strict';

angular.module('dondeEs.users', [ 'ngRoute' ]).config(
		[ '$routeProvider', function($routeProvider) {
			$routeProvider.when('/users', {
				templateUrl : 'resources/users/users.html',
				controller : 'UsersCtrl'
			});
		} ]).controller(
		'UsersCtrl',
		[
				'$scope',
				'$http',
				function($scope, $http) {
					$scope.users = [];

					// #region list Users
					
					$http.get("rest/protected/users/getAll")
					.success(function(response){ if(response.code == 200){
						$scope.users = response.listUser; }else{ console.log("no data"); } }) .error(function(response){
					console.log("error" + response.message); });
					 
					// #endregion list Users
					// #region create users
					$scope.saveUser = function(user) {
						var userRequest = {
							user : $scope.user
						}
						console.log(userRequest);
						$http.post("rest/protected/users/create", userRequest)
								.success(function(response) {
									if (response.code == 200) {
										console.log(response);
										alert("Se registro");
									} else {
										alert("No se registro");
									}
								});
					}

					// get roles
					$http.get('rest/protected/role/getAll').success(
							function(response) {
								$scope.roles = response.listRole;
								console.log(response.listRole);
							});

					// #endregion create users

				} ]);




$(document).ready(function(){
    $("#wizard").steps();
    $("#form").steps({
        bodyTag: "fieldset",
        onStepChanging: function (event, currentIndex, newIndex)
        {
            // Always allow going backward even if the current step contains invalid fields!
            if (currentIndex > newIndex)
            {
                return true;
            }

            // Forbid suppressing "Warning" step if the user is to young
            if (newIndex === 3 && Number($("#age").val()) < 18)
            {
                return false;
            }

            var form = $(this);

            // Clean up if user went backward before
            if (currentIndex < newIndex)
            {
                // To remove error styles
                $(".body:eq(" + newIndex + ") label.error", form).remove();
                $(".body:eq(" + newIndex + ") .error", form).removeClass("error");
            }

            // Disable validation on fields that are disabled or hidden.
            form.validate().settings.ignore = ":disabled,:hidden";

            // Start validation; Prevent going forward if false
            return form.valid();
        },
        onStepChanged: function (event, currentIndex, priorIndex)
        {
            // Suppress (skip) "Warning" step if the user is old enough.
            if (currentIndex === 2 && Number($("#age").val()) >= 18)
            {
                $(this).steps("next");
            }

            // Suppress (skip) "Warning" step if the user is old enough and wants to the previous step.
            if (currentIndex === 2 && priorIndex === 3)
            {
                $(this).steps("previous");
            }
        },
        onFinishing: function (event, currentIndex)
        {
            var form = $(this);

            // Disable validation on fields that are disabled.
            // At this point it's recommended to do an overall check (mean ignoring only disabled fields)
            form.validate().settings.ignore = ":disabled";

            // Start validation; Prevent form submission if false
            return form.valid();
        },
        onFinished: function (event, currentIndex)
        {
            var form = $(this);

            // Submit form input
            form.submit();
        }
    }).validate({
                errorPlacement: function (error, element)
                {
                    element.before(error);
                },
                rules: {
                    confirm: {
                        equalTo: "#password"
                    }
                }
            });
});