(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('UserOrderController', UserOrderController);

    UserOrderController.$inject = ['$scope', '$state', 'UserOrder'];

    function UserOrderController ($scope, $state, UserOrder) {
        var vm = this;
        
        vm.userOrders = [];

        loadAll();

        function loadAll() {
            UserOrder.query(function(result) {
                vm.userOrders = result;
            });
        }
    }
})();
