(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('UserOrderDetailController', UserOrderDetailController);

    UserOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserOrder', 'User', 'Food', 'Garrison'];

    function UserOrderDetailController($scope, $rootScope, $stateParams, previousState, entity, UserOrder, User, Food, Garrison) {
        var vm = this;

        vm.userOrder = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pedidosApp:userOrderUpdate', function(event, result) {
            vm.userOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
