(function() {
    'use strict';
    angular
        .module('pedidosApp')
        .factory('Food', Food);

    Food.$inject = ['$resource', 'DateUtils'];

    function Food ($resource, DateUtils) {
        var resourceUrl =  'api/foods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
