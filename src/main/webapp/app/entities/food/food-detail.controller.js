(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('FoodDetailController', FoodDetailController);

    FoodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Food', 'Delicatessen', 'Garrison'];

    function FoodDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Food, Delicatessen, Garrison) {
        var vm = this;

        vm.food = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('pedidosApp:foodUpdate', function(event, result) {
            vm.food = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
