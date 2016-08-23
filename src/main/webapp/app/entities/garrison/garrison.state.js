(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('garrison', {
            parent: 'entity',
            url: '/garrison',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.garrison.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/garrison/garrisons.html',
                    controller: 'GarrisonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('garrison');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('garrison-detail', {
            parent: 'entity',
            url: '/garrison/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.garrison.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/garrison/garrison-detail.html',
                    controller: 'GarrisonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('garrison');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Garrison', function($stateParams, Garrison) {
                    return Garrison.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'garrison',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('garrison-detail.edit', {
            parent: 'garrison-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/garrison/garrison-dialog.html',
                    controller: 'GarrisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Garrison', function(Garrison) {
                            return Garrison.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('garrison.new', {
            parent: 'garrison',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/garrison/garrison-dialog.html',
                    controller: 'GarrisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('garrison', null, { reload: 'garrison' });
                }, function() {
                    $state.go('garrison');
                });
            }]
        })
        .state('garrison.edit', {
            parent: 'garrison',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/garrison/garrison-dialog.html',
                    controller: 'GarrisonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Garrison', function(Garrison) {
                            return Garrison.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('garrison', null, { reload: 'garrison' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('garrison.delete', {
            parent: 'garrison',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/garrison/garrison-delete-dialog.html',
                    controller: 'GarrisonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Garrison', function(Garrison) {
                            return Garrison.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('garrison', null, { reload: 'garrison' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
