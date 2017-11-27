import {AfterViewInit, Component, ElementRef, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ElementCalendrier} from './element-calendrier.model';
import 'fullcalendar';
import * as $ from 'jquery';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import {isNullOrUndefined} from 'util';
import {isDefined} from '@ng-bootstrap/ng-bootstrap/util/util';
import {JhiEventManager} from 'ng-jhipster';

enum CouleurElement {
    PRUNE = '#AF4847',
    ORANGE = '#FA9F42',
    BLEU = '#2B4162',
    VERT = '#0B6E4F',
    PLATINE = '#E0E0E2'
}

@Component({
    selector: 'jhi-calendrier-sg',
    templateUrl: './calendrier.component.html'
})
export class CalendrierComponent implements OnInit, AfterViewInit, OnDestroy {

    private _elements: ElementCalendrier[];
    @ViewChild('calendrier') el: ElementRef;
    calendrier: any;
    couleurs: CouleurElement[];

    constructor(
        private i18n: TranslateService,
        private eventManager: JhiEventManager
    ) {
        i18n.onLangChange.subscribe((event: LangChangeEvent) => {
            this.calendrier.fullCalendar('option', 'locale', this.i18n.currentLang);
            this.calendrier.fullCalendar('option', 'aspectRatio', 8);
        })
    }

    @Input()
    set elements(elems: ElementCalendrier[]){
        this._elements = elems;

        if (isDefined(elems) && elems !== null) {
            this.ajouterElementsAuCalendrier(elems);
        }
    }

    ngOnInit() {
        this.couleurs = [
            CouleurElement.PRUNE,
            CouleurElement.ORANGE,
            CouleurElement.BLEU,
            CouleurElement.VERT,
            CouleurElement.PLATINE
        ];
    }

    ngOnDestroy() {
        this.calendrier.fullCalendar('destroy');
        console.log('Destroy !');
    }

    ngAfterViewInit() {

        this.calendrier = $(this.el.nativeElement);

        this.calendrier.fullCalendar({
            locale: this.i18n.currentLang,
            weekend: false,
            timeFormat: 'H(:mm)',
            columnFormat: 'ddd',
            header: {
                left: 'title',
                center: '',
                right: ''
            },
            eventClick: (calEvent) => this.eventManager.broadcast({name: 'clickElementCalendrier', content: calEvent})
        });

        this.calendrier.fullCalendar('option', 'aspectRatio', 8);
        this.calendrier.fullCalendar('option', 'contentHeight', 'auto');

    }

    ajouterElementsAuCalendrier(elems: ElementCalendrier[]) {

        if (isNullOrUndefined(this.couleurs)) {
            this.ngOnInit();
        }

        if (isNullOrUndefined(this.calendrier)) {
            this.ngAfterViewInit();
        }

        const events = [];

        const idsSpheresPlaces = [];

        for (const element of elems) {

            let couleur = null;
            const index = idsSpheresPlaces.indexOf(element.groupe);

            if (index >= 0) {
                couleur = this.couleurs[index]
            } else {
                couleur = this.couleurs[idsSpheresPlaces.length];
                idsSpheresPlaces.push(element.groupe);
            }

            events.push({
                title: element.titre,
                start: element.debut,
                end: element.fin,
                color: couleur,
                id: element.id
            });
        }

        this.calendrier.fullCalendar('addEventSource', events);
    }
}
