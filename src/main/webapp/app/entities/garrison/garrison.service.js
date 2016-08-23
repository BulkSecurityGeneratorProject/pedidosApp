(function() {
    'use strict';
    angular
        .module('pedidosApp')
        .factory('Garrison', Garrison);

    Garrison.$inject = ['$resource'];

    function Garrison ($resource) {
        var resourceUrl =  'api/garrisons/:id';

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
