(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('UserOrderDialogController', UserOrderDialogController);

    UserOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserOrder', 'User', 'Food', 'Garrison'];

    function UserOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserOrder, User, Food, Garrison) {
        var vm = this;

        vm.userOrder = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.foods = Food.query();
        vm.garrisons = Garrison.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userOrder.id !== null) {
                UserOrder.update(vm.userOrder, onSaveSuccess, onSaveError);
            } else {
                UserOrder.save(vm.userOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pedidosApp:userOrderUpdate', result);
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
