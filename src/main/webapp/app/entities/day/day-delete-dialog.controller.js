(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('DayDeleteController',DayDeleteController);

    DayDeleteController.$inject = ['$uibModalInstance', 'entity', 'Day'];

    function DayDeleteController($uibModalInstance, entity, Day) {
        var vm = this;

        vm.day = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Day.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
