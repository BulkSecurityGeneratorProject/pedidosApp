'use strict';

describe('Controller Tests', function() {

    describe('Garrison Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockGarrison, MockDelicatessen;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockGarrison = jasmine.createSpy('MockGarrison');
            MockDelicatessen = jasmine.createSpy('MockDelicatessen');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Garrison': MockGarrison,
                'Delicatessen': MockDelicatessen
            };
            createController = function() {
                $injector.get('$controller')("GarrisonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pedidosApp:garrisonUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
