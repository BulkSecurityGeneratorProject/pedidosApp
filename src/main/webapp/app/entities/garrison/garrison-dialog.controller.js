(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('GarrisonDialogController', GarrisonDialogController);

    GarrisonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Garrison', 'Delicatessen'];

    function GarrisonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Garrison, Delicatessen) {
        var vm = this;

        vm.garrison = entity;
        vm.clear = clear;
        vm.save = save;
        vm.delicatessens = Delicatessen.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.garrison.id !== null) {
                Garrison.update(vm.garrison, onSaveSuccess, onSaveError);
            } else {
                Garrison.save(vm.garrison, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pedidosApp:garrisonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
