(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('DayController', DayController);

    DayController.$inject = ['$scope', '$state', 'Day'];

    function DayController ($scope, $state, Day) {
        var vm = this;
        
        vm.days = [];

        loadAll();

        function loadAll() {
            Day.query(function(result) {
                vm.days = result;
            });
        }
    }
})();
