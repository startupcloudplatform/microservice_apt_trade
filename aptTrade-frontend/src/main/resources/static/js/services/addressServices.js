'use strict';
var module = angular.module('addressServices', []);
module.service('addressServices', ['$http', '$q', function ($http, $q) {
	var addressServices  = {};

	addressServices.listByKeyword = function (page, size, keyword) {
		var params ={
			page: page,
			pageSize: size,
			keyword: keyword
		};
		return $http({ method: 'GET', url: '/api/address/list', params: params });
	};

	addressServices.listSido = function () {
		return $http({ method: 'GET', url: '/api/address/sido/list'});
	};

    addressServices.listGugunBySido = function (sido) {
        var params ={
            sido: sido
        };
        return $http({ method: 'GET', url: '/api/address/gugun/list', params: params});
    };

    addressServices.listDongBySidoAndGugun = function (sido, gugun) {
        var params ={
            sido : sido,
            gugun: gugun
        };
        return $http({ method: 'GET', url: '/api/address/dong/list', params: params});
    };



	return addressServices;

}
]);