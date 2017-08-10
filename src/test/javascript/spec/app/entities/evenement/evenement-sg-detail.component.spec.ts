/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SocialGameTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EvenementSgDetailComponent } from '../../../../../../main/webapp/app/entities/evenement/evenement-sg-detail.component';
import { EvenementSgService } from '../../../../../../main/webapp/app/entities/evenement/evenement-sg.service';
import { EvenementSg } from '../../../../../../main/webapp/app/entities/evenement/evenement-sg.model';

describe('Component Tests', () => {

    describe('EvenementSg Management Detail Component', () => {
        let comp: EvenementSgDetailComponent;
        let fixture: ComponentFixture<EvenementSgDetailComponent>;
        let service: EvenementSgService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialGameTestModule],
                declarations: [EvenementSgDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EvenementSgService,
                    JhiEventManager
                ]
            }).overrideTemplate(EvenementSgDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EvenementSgDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EvenementSgService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new EvenementSg(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.evenement).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
