(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('GroupConfigurationDialogController', GroupConfigurationDialogController);

    GroupConfigurationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'GroupConfiguration', 'Day'];

    function GroupConfigurationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, GroupConfiguration, Day) {
        var vm = this;

        vm.groupConfiguration = entity;
        vm.clear = clear;
        vm.save = save;
        vm.orderopeningdays = Day.query({filter: 'groupconfiguration-is-null'});
        $q.all([vm.groupConfiguration.$promise, vm.orderopeningdays.$promise]).then(function() {
            if (!vm.groupConfiguration.orderOpeningDay || !vm.groupConfiguration.orderOpeningDay.id) {
                return $q.reject();
            }
            return Day.get({id : vm.groupConfiguration.orderOpeningDay.id}).$promise;
        }).then(function(orderOpeningDay) {
            vm.orderopeningdays.push(orderOpeningDay);
        });
        vm.orderclosingdays = Day.query({filter: 'groupconfiguration-is-null'});
        $q.all([vm.groupConfiguration.$promise, vm.orderclosingdays.$promise]).then(function() {
            if (!vm.groupConfiguration.orderClosingDay || !vm.groupConfiguration.orderClosingDay.id) {
                return $q.reject();
            }
            return Day.get({id : vm.groupConfiguration.orderClosingDay.id}).$promise;
        }).then(function(orderClosingDay) {
            vm.orderclosingdays.push(orderClosingDay);
        });
        vm.days = Day.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.groupConfiguration.id !== null) {
                GroupConfiguration.update(vm.groupConfiguration, onSaveSuccess, onSaveError);
            } else {
                GroupConfiguration.save(vm.groupConfiguration, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pedidosApp:groupConfigurationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
