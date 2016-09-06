(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('UserGroupController', UserGroupController);

    UserGroupController.$inject = ['$scope', '$state', 'DataUtils', 'UserGroup'];

    function UserGroupController ($scope, $state, DataUtils, UserGroup) {
        var vm = this;
        
        vm.userGroups = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            UserGroup.query(function(result) {
                vm.userGroups = result;
            });
        }
    }
})();
