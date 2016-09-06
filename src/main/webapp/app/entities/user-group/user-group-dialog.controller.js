(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .controller('UserGroupDialogController', UserGroupDialogController);

    UserGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'UserGroup', 'User', 'GroupConfiguration'];

    function UserGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, UserGroup, User, GroupConfiguration) {
        var vm = this;

        vm.userGroup = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.users = User.query();
        vm.configurations = GroupConfiguration.query({filter: 'usergroup-is-null'});
        $q.all([vm.userGroup.$promise, vm.configurations.$promise]).then(function() {
            if (!vm.userGroup.configuration || !vm.userGroup.configuration.id) {
                return $q.reject();
            }
            return GroupConfiguration.get({id : vm.userGroup.configuration.id}).$promise;
        }).then(function(configuration) {
            vm.configurations.push(configuration);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userGroup.id !== null) {
                UserGroup.update(vm.userGroup, onSaveSuccess, onSaveError);
            } else {
                UserGroup.save(vm.userGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pedidosApp:userGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setPicture = function ($file, userGroup) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        userGroup.picture = base64Data;
                        userGroup.pictureContentType = $file.type;
                    });
                });
            }
        };

    }
})();
