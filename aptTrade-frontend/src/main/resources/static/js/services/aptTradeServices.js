'use strict';
var module = angular.module('aptTradeServices', []);
module.factory('aptTradeServices', ['$http', '$q', function ($http, $q) {
	var aptTradeServices  = {};

	aptTradeServices.listByAdrCodeAndQuarter = function (rnMgtSn, pnu, buldMnnm, buldSlno, quarter) {
		var params ={
			pnu: pnu,
			buldMnnm: buldMnnm,
			buldSlno: buldSlno,
			quarter: quarter
		};
		return $http({ method: 'GET', url: '/api/trade/list/'+rnMgtSn, params: params });
	};

	aptTradeServices.listByAddressAndQuarter = function (address, priceRange, areaRange, quarter) {
		var params ={
			address: address,
			priceRange: priceRange,
			areaRange: areaRange,
			quarter: quarter
		};
		return $http({ method: 'GET', url: '/api/trade/condition/list', params: params });
	};

	return aptTradeServices;

}
]);