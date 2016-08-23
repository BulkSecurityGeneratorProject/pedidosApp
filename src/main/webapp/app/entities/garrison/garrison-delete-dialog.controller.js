(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('GarrisonDeleteController',GarrisonDeleteController);

    GarrisonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Garrison'];

    function GarrisonDeleteController($uibModalInstance, entity, Garrison) {
        var vm = this;

        vm.garrison = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Garrison.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
