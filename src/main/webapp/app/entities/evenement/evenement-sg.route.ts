import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { EvenementSgComponent } from './evenement-sg.component';
import { EvenementSgDetailComponent } from './evenement-sg-detail.component';
import { EvenementSgPopupComponent } from './evenement-sg-dialog.component';
import { EvenementSgDeletePopupComponent } from './evenement-sg-delete-dialog.component';

@Injectable()
export class EvenementSgResolvePagingParams implements Resolve<any> {

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

export const evenementRoute: Routes = [
    {
        path: 'evenement-sg',
        component: EvenementSgComponent,
        resolve: {
            'pagingParams': EvenementSgResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.evenement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'evenement-sg/:id',
        component: EvenementSgDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.evenement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const evenementPopupRoute: Routes = [
    {
        path: 'evenement-sg-new',
        component: EvenementSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.evenement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'evenement-sg-new/:idSphere',
        component: EvenementSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.evenement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'evenement-sg/:id/edit',
        component: EvenementSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.evenement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'evenement-sg/:id/delete',
        component: EvenementSgDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.evenement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
