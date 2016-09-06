(function() {
    'use strict';
    angular
        .module('pedidosApp')
        .factory('Invite', Invite);

    Invite.$inject = ['$resource', 'DateUtils'];

    function Invite ($resource, DateUtils) {
        var resourceUrl =  'api/invites/:id';

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
