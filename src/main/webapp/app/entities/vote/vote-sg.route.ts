import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { VoteSgComponent } from './vote-sg.component';
import { VoteSgDetailComponent } from './vote-sg-detail.component';
import { VoteSgPopupComponent } from './vote-sg-dialog.component';
import { VoteSgDeletePopupComponent } from './vote-sg-delete-dialog.component';
import { VoteAttenteSgComponent } from './vote-attente-sg.component';
import {VoteVoterSgComponent, VoteVoterSgPopupComponent} from './vote-voter-sg.component';

@Injectable()
export class VoteSgResolvePagingParams implements Resolve<any> {

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

export const voteRoute: Routes = [
    {
        path: 'vote-sg',
        component: VoteSgComponent,
        resolve: {
            'pagingParams': VoteSgResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vote-sg/attente',
        component: VoteAttenteSgComponent,
        resolve: {
            'pagingParams': VoteSgResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'vote-sg/:id',
        component: VoteSgDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const votePopupRoute: Routes = [
    {
        path: 'vote-sg-new',
        component: VoteSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vote-sg/:id/edit',
        component: VoteSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vote-sg/:id/delete',
        component: VoteSgDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'vote-sg/voter/:id',
        component: VoteVoterSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.vote.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
