import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialGameSharedModule } from '../../shared';
import { SocialGameAdminModule } from '../../admin/admin.module';
import {
    VoteSgService,
    VoteSgPopupService,
    VoteSgComponent,
    VoteSgDetailComponent,
    VoteSgDialogComponent,
    VoteSgPopupComponent,
    VoteSgDeletePopupComponent,
    VoteSgDeleteDialogComponent,
    voteRoute,
    votePopupRoute,
    VoteSgResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...voteRoute,
    ...votePopupRoute,
];

@NgModule({
    imports: [
        SocialGameSharedModule,
        SocialGameAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        VoteSgComponent,
        VoteSgDetailComponent,
        VoteSgDialogComponent,
        VoteSgDeleteDialogComponent,
        VoteSgPopupComponent,
        VoteSgDeletePopupComponent,
    ],
    entryComponents: [
        VoteSgComponent,
        VoteSgDialogComponent,
        VoteSgPopupComponent,
        VoteSgDeleteDialogComponent,
        VoteSgDeletePopupComponent,
    ],
    providers: [
        VoteSgService,
        VoteSgPopupService,
        VoteSgResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialGameVoteSgModule {}
