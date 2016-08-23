(function() {
    'use strict';
    angular
        .module('pedidosApp')
        .factory('UserOrder', UserOrder);

    UserOrder.$inject = ['$resource', 'DateUtils'];

    function UserOrder ($resource, DateUtils) {
        var resourceUrl =  'api/user-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
