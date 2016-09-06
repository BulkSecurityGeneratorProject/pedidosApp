(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('InviteController', InviteController);

    InviteController.$inject = ['$scope', '$state', 'Invite'];

    function InviteController ($scope, $state, Invite) {
        var vm = this;
        
        vm.invites = [];

        loadAll();

        function loadAll() {
            Invite.query(function(result) {
                vm.invites = result;
            });
        }
    }
})();
