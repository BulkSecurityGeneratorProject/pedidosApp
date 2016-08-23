(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('UserOrderDeleteController',UserOrderDeleteController);

    UserOrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserOrder'];

    function UserOrderDeleteController($uibModalInstance, entity, UserOrder) {
        var vm = this;

        vm.userOrder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
