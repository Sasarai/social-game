import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { NoteComponent } from './note.component';
import { NoteDetailComponent } from './note-detail.component';
import { NotePopupComponent } from './note-dialog.component';
import { NoteDeletePopupComponent } from './note-delete-dialog.component';

@Injectable()
export class NoteResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const noteRoute: Routes = [
    {
        path: 'note',
        component: NoteComponent,
        resolve: {
            'pagingParams': NoteResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.note.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'note/:id',
        component: NoteDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.note.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const notePopupRoute: Routes = [
    {
        path: 'note-new',
        component: NotePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.note.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'note/:id/edit',
        component: NotePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.note.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'note/:id/delete',
        component: NoteDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.note.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
