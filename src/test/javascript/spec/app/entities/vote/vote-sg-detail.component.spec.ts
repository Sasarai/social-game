/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SocialGameTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { VoteSgDetailComponent } from '../../../../../../main/webapp/app/entities/vote/vote-sg-detail.component';
import { VoteSgService } from '../../../../../../main/webapp/app/entities/vote/vote-sg.service';
import { VoteSg } from '../../../../../../main/webapp/app/entities/vote/vote-sg.model';

describe('Component Tests', () => {

    describe('VoteSg Management Detail Component', () => {
        let comp: VoteSgDetailComponent;
        let fixture: ComponentFixture<VoteSgDetailComponent>;
        let service: VoteSgService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialGameTestModule],
                declarations: [VoteSgDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    VoteSgService,
                    JhiEventManager
                ]
            }).overrideTemplate(VoteSgDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(VoteSgDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(VoteSgService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new VoteSg(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.vote).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
