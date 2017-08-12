import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialGameSharedModule } from '../../shared';
import { SocialGameAdminModule } from '../../admin/admin.module';
import {
    JeuSgService,
    JeuSgPopupService,
    JeuSgComponent,
    JeuSgDetailComponent,
    JeuSgDialogComponent,
    JeuSgPopupComponent,
    JeuSgDeletePopupComponent,
    JeuSgDeleteDialogComponent,
    jeuRoute,
    jeuPopupRoute,
    JeuSgResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...jeuRoute,
    ...jeuPopupRoute,
];

@NgModule({
    imports: [
        SocialGameSharedModule,
        SocialGameAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        JeuSgComponent,
        JeuSgDetailComponent,
        JeuSgDialogComponent,
        JeuSgDeleteDialogComponent,
        JeuSgPopupComponent,
        JeuSgDeletePopupComponent,
    ],
    entryComponents: [
        JeuSgComponent,
        JeuSgDialogComponent,
        JeuSgPopupComponent,
        JeuSgDeleteDialogComponent,
        JeuSgDeletePopupComponent,
    ],
    providers: [
        JeuSgService,
        JeuSgPopupService,
        JeuSgResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialGameJeuSgModule {}
