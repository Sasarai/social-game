import {AfterViewInit, Component, ElementRef, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ElementCalendrier} from './element-calendrier.model';
import 'fullcalendar';
import * as $ from 'jquery';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import {isNullOrUndefined} from 'util';
import {isDefined} from '@ng-bootstrap/ng-bootstrap/util/util';

enum CouleurElement {
    PRUNE = '#8B4241',
    ORANGE = '#FA9F42',
    BLEU = '#2B4162',
    VERT = '#0B6E4F',
    PLATINE = '#E0E0E2'
}

@Component({
    selector: 'jhi-calendrier-sg',
    templateUrl: './calendrier.component.html'
})
export class CalendrierComponent implements OnInit, AfterViewInit {

    private _elements: ElementCalendrier[];
    @ViewChild('calendrier') el: ElementRef;
    calendrier: any;
    couleurs: CouleurElement[];

    constructor(private i18n: TranslateService) {
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
            // events: [
            //     {
            //         color: "#721817",
            //         end: "2017-11-29T13:00:00Z",
            //         start: "2017-11-29T12:00:00Z",
            //         title: "Midi",
            //     }
            // ]
        });

        this.calendrier.fullCalendar('option', 'aspectRatio', 8);
        this.calendrier.fullCalendar('option', 'contentHeight', 'auto');

    }

    ajouterElementsAuCalendrier(elems: ElementCalendrier[]) {
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
                color: couleur
            });
        }

        this.calendrier.fullCalendar('addEventSource', events);
    }
}
