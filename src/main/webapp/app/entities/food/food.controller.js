(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('FoodController', FoodController);

    FoodController.$inject = ['$scope', '$state', 'Food'];

    function FoodController ($scope, $state, Food) {
        var vm = this;
        
        vm.foods = [];

        loadAll();

        function loadAll() {
            Food.query(function(result) {
                vm.foods = result;
            });
        }
    }
})();
