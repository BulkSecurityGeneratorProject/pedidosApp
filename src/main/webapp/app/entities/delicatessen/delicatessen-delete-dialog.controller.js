(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('DelicatessenDeleteController',DelicatessenDeleteController);

    DelicatessenDeleteController.$inject = ['$uibModalInstance', 'entity', 'Delicatessen'];

    function DelicatessenDeleteController($uibModalInstance, entity, Delicatessen) {
        var vm = this;

        vm.delicatessen = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Delicatessen.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
