import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialGameSharedModule } from '../../shared';
import {
    EvenementSgService,
    EvenementSgPopupService,
    EvenementSgComponent,
    EvenementSgDetailComponent,
    EvenementSgDialogComponent,
    EvenementSgPopupComponent,
    EvenementSgDeletePopupComponent,
    EvenementSgDeleteDialogComponent,
    evenementRoute,
    evenementPopupRoute,
    EvenementSgResolvePagingParams,
} from './';
import {AngularMultiSelectModule} from 'angular2-multiselect-dropdown';

const ENTITY_STATES = [
    ...evenementRoute,
    ...evenementPopupRoute,
];

@NgModule({
    imports: [
        SocialGameSharedModule,
        AngularMultiSelectModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EvenementSgComponent,
        EvenementSgDetailComponent,
        EvenementSgDialogComponent,
        EvenementSgDeleteDialogComponent,
        EvenementSgPopupComponent,
        EvenementSgDeletePopupComponent,
    ],
    entryComponents: [
        EvenementSgComponent,
        EvenementSgDialogComponent,
        EvenementSgPopupComponent,
        EvenementSgDeleteDialogComponent,
        EvenementSgDeletePopupComponent,
    ],
    providers: [
        EvenementSgService,
        EvenementSgPopupService,
        EvenementSgResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialGameEvenementSgModule {}
