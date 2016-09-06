(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('DayDetailController', DayDetailController);

    DayDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Day'];

    function DayDetailController($scope, $rootScope, $stateParams, previousState, entity, Day) {
        var vm = this;

        vm.day = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pedidosApp:dayUpdate', function(event, result) {
            vm.day = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
