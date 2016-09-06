(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('InviteDeleteController',InviteDeleteController);

    InviteDeleteController.$inject = ['$uibModalInstance', 'entity', 'Invite'];

    function InviteDeleteController($uibModalInstance, entity, Invite) {
        var vm = this;

        vm.invite = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Invite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
