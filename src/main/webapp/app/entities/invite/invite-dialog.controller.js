(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('InviteDialogController', InviteDialogController);

    InviteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Invite', 'User'];

    function InviteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Invite, User) {
        var vm = this;

        vm.invite = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.invite.id !== null) {
                Invite.update(vm.invite, onSaveSuccess, onSaveError);
            } else {
                Invite.save(vm.invite, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pedidosApp:inviteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
