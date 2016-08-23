(function() {
    'use strict';
    angular
        .module('pedidosApp')
        .factory('Delicatessen', Delicatessen);

    Delicatessen.$inject = ['$resource'];

    function Delicatessen ($resource) {
        var resourceUrl =  'api/delicatessens/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
