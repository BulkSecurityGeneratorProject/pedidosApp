(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('UserGroupDeleteController',UserGroupDeleteController);

    UserGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserGroup'];

    function UserGroupDeleteController($uibModalInstance, entity, UserGroup) {
        var vm = this;

        vm.userGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
