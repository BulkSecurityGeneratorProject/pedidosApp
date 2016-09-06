(function() {
    'use strict';
    angular
        .module('pedidosApp')
        .factory('GroupConfiguration', GroupConfiguration);

    GroupConfiguration.$inject = ['$resource'];

    function GroupConfiguration ($resource) {
        var resourceUrl =  'api/group-configurations/:id';

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
