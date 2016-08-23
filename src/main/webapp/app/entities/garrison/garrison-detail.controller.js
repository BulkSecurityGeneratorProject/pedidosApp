(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('GarrisonDetailController', GarrisonDetailController);

    GarrisonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Garrison', 'Delicatessen'];

    function GarrisonDetailController($scope, $rootScope, $stateParams, previousState, entity, Garrison, Delicatessen) {
        var vm = this;

        vm.garrison = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pedidosApp:garrisonUpdate', function(event, result) {
            vm.garrison = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
