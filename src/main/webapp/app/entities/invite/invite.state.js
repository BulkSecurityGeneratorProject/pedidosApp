(function() {
    'use strict';

    angular
        .module('pedidosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('invite', {
            parent: 'entity',
            url: '/invite',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.invite.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invite/invites.html',
                    controller: 'InviteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('invite');
                    $translatePartialLoader.addPart('inviteStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('invite-detail', {
            parent: 'entity',
            url: '/invite/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pedidosApp.invite.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/invite/invite-detail.html',
                    controller: 'InviteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('invite');
                    $translatePartialLoader.addPart('inviteStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Invite', function($stateParams, Invite) {
                    return Invite.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'invite',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('invite-detail.edit', {
            parent: 'invite-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invite/invite-dialog.html',
                    controller: 'InviteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Invite', function(Invite) {
                            return Invite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invite.new', {
            parent: 'invite',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invite/invite-dialog.html',
                    controller: 'InviteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                guestMail: null,
                                guestName: null,
                                date: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('invite', null, { reload: 'invite' });
                }, function() {
                    $state.go('invite');
                });
            }]
        })
        .state('invite.edit', {
            parent: 'invite',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invite/invite-dialog.html',
                    controller: 'InviteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Invite', function(Invite) {
                            return Invite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invite', null, { reload: 'invite' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('invite.delete', {
            parent: 'invite',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/invite/invite-delete-dialog.html',
                    controller: 'InviteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Invite', function(Invite) {
                            return Invite.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('invite', null, { reload: 'invite' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
