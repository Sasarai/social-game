import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ElementCalendrier} from './element-calendrier.model';
import 'fullcalendar';
import * as $ from 'jquery';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'jhi-calendrier-sg',
    templateUrl: './calendrier.component.html'
})
export class CalendrierComponent implements OnInit, AfterViewInit {

    @Input() elements: ElementCalendrier[];
    @ViewChild('calendrier') el: ElementRef;
    calendrier: any;

    constructor(private i18n: TranslateService) {
        i18n.onLangChange.subscribe((event: LangChangeEvent) => {
            this.calendrier.fullCalendar('option', 'locale', this.i18n.currentLang);
            this.calendrier.fullCalendar('option', 'aspectRatio', 2);
        })
    }

    ngOnInit() {

    }

    ngAfterViewInit() {

        this.calendrier = $(this.el.nativeElement);

        this.calendrier.fullCalendar({
            locale: this.i18n.currentLang,
            weekend: false,
            timeFormat: 'H(:mm)',
            columnFormat: 'dddd'
        });

        this.calendrier.fullCalendar('option', 'contentHeight', 'auto');
    }
}
