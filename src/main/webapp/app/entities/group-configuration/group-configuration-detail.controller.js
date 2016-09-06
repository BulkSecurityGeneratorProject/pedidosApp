(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('GroupConfigurationDetailController', GroupConfigurationDetailController);

    GroupConfigurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GroupConfiguration', 'Day'];

    function GroupConfigurationDetailController($scope, $rootScope, $stateParams, previousState, entity, GroupConfiguration, Day) {
        var vm = this;

        vm.groupConfiguration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pedidosApp:groupConfigurationUpdate', function(event, result) {
            vm.groupConfiguration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
