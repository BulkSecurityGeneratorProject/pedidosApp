(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('FoodDetailController', FoodDetailController);

    FoodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Food', 'Delicatessen', 'Garrison'];

    function FoodDetailController($scope, $rootScope, $stateParams, previousState, entity, Food, Delicatessen, Garrison) {
        var vm = this;

        vm.food = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pedidosApp:foodUpdate', function(event, result) {
            vm.food = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
