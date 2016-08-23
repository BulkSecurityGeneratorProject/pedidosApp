(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('GarrisonController', GarrisonController);

    GarrisonController.$inject = ['$scope', '$state', 'Garrison'];

    function GarrisonController ($scope, $state, Garrison) {
        var vm = this;
        
        vm.garrisons = [];

        loadAll();

        function loadAll() {
            Garrison.query(function(result) {
                vm.garrisons = result;
            });
        }
    }
})();
