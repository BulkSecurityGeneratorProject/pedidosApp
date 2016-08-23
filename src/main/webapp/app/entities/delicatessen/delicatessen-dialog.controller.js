(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('DelicatessenDialogController', DelicatessenDialogController);

    DelicatessenDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Delicatessen', 'UserGroup'];

    function DelicatessenDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Delicatessen, UserGroup) {
        var vm = this;

        vm.delicatessen = entity;
        vm.clear = clear;
        vm.save = save;
        vm.usergroups = UserGroup.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.delicatessen.id !== null) {
                Delicatessen.update(vm.delicatessen, onSaveSuccess, onSaveError);
            } else {
                Delicatessen.save(vm.delicatessen, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pedidosApp:delicatessenUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
