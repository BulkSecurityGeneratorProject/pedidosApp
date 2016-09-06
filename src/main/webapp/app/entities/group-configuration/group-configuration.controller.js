(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('GroupConfigurationController', GroupConfigurationController);

    GroupConfigurationController.$inject = ['$scope', '$state', 'GroupConfiguration'];

    function GroupConfigurationController ($scope, $state, GroupConfiguration) {
        var vm = this;
        
        vm.groupConfigurations = [];

        loadAll();

        function loadAll() {
            GroupConfiguration.query(function(result) {
                vm.groupConfigurations = result;
            });
        }
    }
})();
