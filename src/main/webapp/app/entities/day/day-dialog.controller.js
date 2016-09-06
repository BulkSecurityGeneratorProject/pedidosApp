(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('DayDialogController', DayDialogController);

    DayDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Day'];

    function DayDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Day) {
        var vm = this;

        vm.day = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.day.id !== null) {
                Day.update(vm.day, onSaveSuccess, onSaveError);
            } else {
                Day.save(vm.day, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pedidosApp:dayUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
