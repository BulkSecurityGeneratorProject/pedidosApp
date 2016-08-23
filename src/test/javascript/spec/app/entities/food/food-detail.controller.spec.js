'use strict';

describe('Controller Tests', function() {

    describe('Food Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFood, MockDelicatessen, MockGarrison;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFood = jasmine.createSpy('MockFood');
            MockDelicatessen = jasmine.createSpy('MockDelicatessen');
            MockGarrison = jasmine.createSpy('MockGarrison');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Food': MockFood,
                'Delicatessen': MockDelicatessen,
                'Garrison': MockGarrison
            };
            createController = function() {
                $injector.get('$controller')("FoodDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'pedidosApp:foodUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
