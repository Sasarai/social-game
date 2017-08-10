/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SocialGameTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SphereSgDetailComponent } from '../../../../../../main/webapp/app/entities/sphere/sphere-sg-detail.component';
import { SphereSgService } from '../../../../../../main/webapp/app/entities/sphere/sphere-sg.service';
import { SphereSg } from '../../../../../../main/webapp/app/entities/sphere/sphere-sg.model';

describe('Component Tests', () => {

    describe('SphereSg Management Detail Component', () => {
        let comp: SphereSgDetailComponent;
        let fixture: ComponentFixture<SphereSgDetailComponent>;
        let service: SphereSgService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialGameTestModule],
                declarations: [SphereSgDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SphereSgService,
                    JhiEventManager
                ]
            }).overrideTemplate(SphereSgDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SphereSgDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SphereSgService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new SphereSg(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.sphere).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
