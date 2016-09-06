(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('UserGroupDetailController', UserGroupDetailController);

    UserGroupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserGroup', 'User', 'GroupConfiguration'];

    function UserGroupDetailController($scope, $rootScope, $stateParams, previousState, entity, UserGroup, User, GroupConfiguration) {
        var vm = this;

        vm.userGroup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pedidosApp:userGroupUpdate', function(event, result) {
            vm.userGroup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
