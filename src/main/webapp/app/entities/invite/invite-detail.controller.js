(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('InviteDetailController', InviteDetailController);

    InviteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Invite', 'User'];

    function InviteDetailController($scope, $rootScope, $stateParams, previousState, entity, Invite, User) {
        var vm = this;

        vm.invite = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pedidosApp:inviteUpdate', function(event, result) {
            vm.invite = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
