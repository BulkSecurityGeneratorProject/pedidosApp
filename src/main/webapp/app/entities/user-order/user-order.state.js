(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-order', {
            parent: 'entity',
            url: '/user-order',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.userOrder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-order/user-orders.html',
                    controller: 'UserOrderController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userOrder');
                    $translatePartialLoader.addPart('orderStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-order-detail', {
            parent: 'entity',
            url: '/user-order/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.userOrder.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-order/user-order-detail.html',
                    controller: 'UserOrderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userOrder');
                    $translatePartialLoader.addPart('orderStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserOrder', function($stateParams, UserOrder) {
                    return UserOrder.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-order',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-order-detail.edit', {
            parent: 'user-order-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-order/user-order-dialog.html',
                    controller: 'UserOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserOrder', function(UserOrder) {
                            return UserOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-order.new', {
            parent: 'user-order',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-order/user-order-dialog.html',
                    controller: 'UserOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-order', null, { reload: 'user-order' });
                }, function() {
                    $state.go('user-order');
                });
            }]
        })
        .state('user-order.edit', {
            parent: 'user-order',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-order/user-order-dialog.html',
                    controller: 'UserOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserOrder', function(UserOrder) {
                            return UserOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-order', null, { reload: 'user-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-order.delete', {
            parent: 'user-order',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-order/user-order-delete-dialog.html',
                    controller: 'UserOrderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserOrder', function(UserOrder) {
                            return UserOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-order', null, { reload: 'user-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
