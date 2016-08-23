(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('DelicatessenController', DelicatessenController);

    DelicatessenController.$inject = ['$scope', '$state', 'Delicatessen'];

    function DelicatessenController ($scope, $state, Delicatessen) {
        var vm = this;
        
        vm.delicatessens = [];

        loadAll();

        function loadAll() {
            Delicatessen.query(function(result) {
                vm.delicatessens = result;
            });
        }
    }
})();
