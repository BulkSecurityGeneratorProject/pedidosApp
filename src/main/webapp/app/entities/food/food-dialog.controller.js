(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('FoodDialogController', FoodDialogController);

    FoodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Food', 'Delicatessen', 'Garrison'];

    function FoodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Food, Delicatessen, Garrison) {
        var vm = this;

        vm.food = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.delicatessens = Delicatessen.query();
        vm.garrisons = Garrison.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.food.id !== null) {
                Food.update(vm.food, onSaveSuccess, onSaveError);
            } else {
                Food.save(vm.food, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pedidosApp:foodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        vm.setPicture = function ($file, food) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        food.picture = base64Data;
                        food.pictureContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
