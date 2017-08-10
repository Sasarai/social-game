import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialGameSharedModule } from '../../shared';
import {
    SphereSgService,
    SphereSgPopupService,
    SphereSgComponent,
    SphereSgDetailComponent,
    SphereSgDialogComponent,
    SphereSgPopupComponent,
    SphereSgDeletePopupComponent,
    SphereSgDeleteDialogComponent,
    sphereRoute,
    spherePopupRoute,
    SphereSgResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...sphereRoute,
    ...spherePopupRoute,
];

@NgModule({
    imports: [
        SocialGameSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SphereSgComponent,
        SphereSgDetailComponent,
        SphereSgDialogComponent,
        SphereSgDeleteDialogComponent,
        SphereSgPopupComponent,
        SphereSgDeletePopupComponent,
    ],
    entryComponents: [
        SphereSgComponent,
        SphereSgDialogComponent,
        SphereSgPopupComponent,
        SphereSgDeleteDialogComponent,
        SphereSgDeletePopupComponent,
    ],
    providers: [
        SphereSgService,
        SphereSgPopupService,
        SphereSgResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialGameSphereSgModule {}
