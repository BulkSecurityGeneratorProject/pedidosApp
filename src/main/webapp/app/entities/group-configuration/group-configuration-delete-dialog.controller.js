(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('GroupConfigurationDeleteController',GroupConfigurationDeleteController);

    GroupConfigurationDeleteController.$inject = ['$uibModalInstance', 'entity', 'GroupConfiguration'];

    function GroupConfigurationDeleteController($uibModalInstance, entity, GroupConfiguration) {
        var vm = this;

        vm.groupConfiguration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GroupConfiguration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
