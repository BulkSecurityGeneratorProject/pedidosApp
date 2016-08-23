(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('delicatessen', {
            parent: 'entity',
            url: '/delicatessen',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.delicatessen.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/delicatessen/delicatessens.html',
                    controller: 'DelicatessenController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('delicatessen');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('delicatessen-detail', {
            parent: 'entity',
            url: '/delicatessen/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.delicatessen.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/delicatessen/delicatessen-detail.html',
                    controller: 'DelicatessenDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('delicatessen');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Delicatessen', function($stateParams, Delicatessen) {
                    return Delicatessen.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'delicatessen',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('delicatessen-detail.edit', {
            parent: 'delicatessen-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delicatessen/delicatessen-dialog.html',
                    controller: 'DelicatessenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Delicatessen', function(Delicatessen) {
                            return Delicatessen.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('delicatessen.new', {
            parent: 'delicatessen',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delicatessen/delicatessen-dialog.html',
                    controller: 'DelicatessenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                address: null,
                                phone: null,
                                email: null,
                                custom: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('delicatessen', null, { reload: 'delicatessen' });
                }, function() {
                    $state.go('delicatessen');
                });
            }]
        })
        .state('delicatessen.edit', {
            parent: 'delicatessen',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delicatessen/delicatessen-dialog.html',
                    controller: 'DelicatessenDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Delicatessen', function(Delicatessen) {
                            return Delicatessen.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('delicatessen', null, { reload: 'delicatessen' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('delicatessen.delete', {
            parent: 'delicatessen',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/delicatessen/delicatessen-delete-dialog.html',
                    controller: 'DelicatessenDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Delicatessen', function(Delicatessen) {
                            return Delicatessen.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('delicatessen', null, { reload: 'delicatessen' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
