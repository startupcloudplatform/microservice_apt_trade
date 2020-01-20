
'use strict';
angular.module('common.services', ['LocalStorageModule'])
    .factory('common', function ($http, $location, $route ) {

        // promise 받기 success, error
        common.retrieveResource = function (promise, finallyFn) {
            promise.success = function (fn) {
                promise.then(function (response) {
                    if(angular.isUndefined(response)) {
                        response = {data:null,status:null,headers:null};
                    }
                    fn(response.data, response.status, response.headers);
                });
                return promise;
            };
            promise.error = function (fn) {
                promise.then(null, function (response) {
                    fn(response.data, response.status, response.headers);
                });
                return promise;
            };
            return promise;
        };
    });
