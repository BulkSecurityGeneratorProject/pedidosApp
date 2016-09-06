(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('group-configuration', {
            parent: 'entity',
            url: '/group-configuration',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.groupConfiguration.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/group-configuration/group-configurations.html',
                    controller: 'GroupConfigurationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupConfiguration');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('group-configuration-detail', {
            parent: 'entity',
            url: '/group-configuration/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.groupConfiguration.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/group-configuration/group-configuration-detail.html',
                    controller: 'GroupConfigurationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('groupConfiguration');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'GroupConfiguration', function($stateParams, GroupConfiguration) {
                    return GroupConfiguration.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'group-configuration',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('group-configuration-detail.edit', {
            parent: 'group-configuration-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-configuration/group-configuration-dialog.html',
                    controller: 'GroupConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GroupConfiguration', function(GroupConfiguration) {
                            return GroupConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group-configuration.new', {
            parent: 'group-configuration',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-configuration/group-configuration-dialog.html',
                    controller: 'GroupConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                weekly: null,
                                orderOpeningHour: null,
                                orderClosingHour: null,
                                ccOrderEmail: null,
                                ccCancelEmail: null,
                                orderEmailBody: null,
                                cancelEmailBody: null,
                                daysForCancellation: null,
                                hoursForCancelation: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('group-configuration', null, { reload: 'group-configuration' });
                }, function() {
                    $state.go('group-configuration');
                });
            }]
        })
        .state('group-configuration.edit', {
            parent: 'group-configuration',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-configuration/group-configuration-dialog.html',
                    controller: 'GroupConfigurationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['GroupConfiguration', function(GroupConfiguration) {
                            return GroupConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group-configuration', null, { reload: 'group-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('group-configuration.delete', {
            parent: 'group-configuration',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/group-configuration/group-configuration-delete-dialog.html',
                    controller: 'GroupConfigurationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['GroupConfiguration', function(GroupConfiguration) {
                            return GroupConfiguration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('group-configuration', null, { reload: 'group-configuration' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
