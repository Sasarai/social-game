/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SocialGameTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { JeuSgDetailComponent } from '../../../../../../main/webapp/app/entities/jeu/jeu-sg-detail.component';
import { JeuSgService } from '../../../../../../main/webapp/app/entities/jeu/jeu-sg.service';
import { JeuSg } from '../../../../../../main/webapp/app/entities/jeu/jeu-sg.model';

describe('Component Tests', () => {

    describe('JeuSg Management Detail Component', () => {
        let comp: JeuSgDetailComponent;
        let fixture: ComponentFixture<JeuSgDetailComponent>;
        let service: JeuSgService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SocialGameTestModule],
                declarations: [JeuSgDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    JeuSgService,
                    JhiEventManager
                ]
            }).overrideTemplate(JeuSgDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(JeuSgDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(JeuSgService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new JeuSg(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.jeu).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
