(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('FoodController', FoodController);

    FoodController.$inject = ['$scope', '$state', 'DataUtils', 'Food'];

    function FoodController ($scope, $state, DataUtils, Food) {
        var vm = this;
        
        vm.foods = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Food.query(function(result) {
                vm.foods = result;
            });
        }
    }
})();
