'use strict';

describe('Controller Tests', function() {

    describe('Delicatessen Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDelicatessen, MockUserGroup;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDelicatessen = jasmine.createSpy('MockDelicatessen');
            MockUserGroup = jasmine.createSpy('MockUserGroup');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Delicatessen': MockDelicatessen,
                'UserGroup': MockUserGroup
            };
            createController = function() {
                $injector.get('$controller')("DelicatessenDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pedidosApp:delicatessenUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
