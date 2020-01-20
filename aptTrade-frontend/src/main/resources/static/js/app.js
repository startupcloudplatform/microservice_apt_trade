angular.module('app',['ngRoute', 'ngAnimate', 'ngSanitize', 'errors', 'ui.bootstrap', 'bw.paging', 'addressServices', 'aptTradeServices'])
	.config(function ($locationProvider, $routeProvider) {

		$routeProvider
			.when('/', {
				controller: 'mainController',
				templateUrl: '/views/main.html',
				controllerAs: 'mc'
			})
			.when('/apt-search', {
				controller: 'aptSearchController',
				templateUrl: '/views/aptSearch/aptSearch.html',
				controllerAs: 'ct'
			})
			.when('/condition-search', {
				controller: 'conditionSearchController',
				templateUrl: '/views/conditionSearch/conditionSearch.html',
				controllerAs: 'ct'
			})
			.otherwise({
				controller: 'ErrorsController',
				templateUrl: '/views/errors.html'
		});
	})
	.filter('numberFilter', function($filter) {
		return function(input) {
			var input_to_number = Number(input);

			if (input_to_number) {
				return $filter('number')(input, 3);
			} else {
				return input;
			}
		};
	});
;