import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialGameSharedModule } from '../../shared';
import { SocialGameAdminModule } from '../../admin/admin.module';
import {
    NoteService,
    NotePopupService,
    NoteComponent,
    NoteDetailComponent,
    NoteDialogComponent,
    NotePopupComponent,
    NoteDeletePopupComponent,
    NoteDeleteDialogComponent,
    noteRoute,
    notePopupRoute,
    NoteResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...noteRoute,
    ...notePopupRoute,
];

@NgModule({
    imports: [
        SocialGameSharedModule,
        SocialGameAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        NoteComponent,
        NoteDetailComponent,
        NoteDialogComponent,
        NoteDeleteDialogComponent,
        NotePopupComponent,
        NoteDeletePopupComponent,
    ],
    entryComponents: [
        NoteComponent,
        NoteDialogComponent,
        NotePopupComponent,
        NoteDeleteDialogComponent,
        NoteDeletePopupComponent,
    ],
    providers: [
        NoteService,
        NotePopupService,
        NoteResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialGameNoteModule {}
