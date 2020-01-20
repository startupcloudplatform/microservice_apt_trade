'use strict';

angular.module('app')
	.controller('mainController', function ($scope, $location) {
		var mc = this;
		$scope.loadingMain =false;
		mc.uploadProgress = false;

	/* ********************************************************** */
		/**
		 * 페이지 이동
		 * @param url
		 */
		mc.location = function (url) {
			$location.path(url);
		}

	})
;
