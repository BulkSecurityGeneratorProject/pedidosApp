(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('DelicatessenDetailController', DelicatessenDetailController);

    DelicatessenDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Delicatessen', 'UserGroup'];

    function DelicatessenDetailController($scope, $rootScope, $stateParams, previousState, entity, Delicatessen, UserGroup) {
        var vm = this;

        vm.delicatessen = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pedidosApp:delicatessenUpdate', function(event, result) {
            vm.delicatessen = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
