import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { JeuSgComponent } from './jeu-sg.component';
import { JeuSgDetailComponent } from './jeu-sg-detail.component';
import { JeuSgPopupComponent } from './jeu-sg-dialog.component';
import { JeuSgDeletePopupComponent } from './jeu-sg-delete-dialog.component';

@Injectable()
export class JeuSgResolvePagingParams implements Resolve<any> {

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

export const jeuRoute: Routes = [
    {
        path: 'jeu-sg',
        component: JeuSgComponent,
        resolve: {
            'pagingParams': JeuSgResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.jeu.home.title.main'
        },
        canActivate: [UserRouteAccessService],
    }, {
        path: 'jeu-sg/:type',
        component: JeuSgComponent,
        resolve: {
            'pagingParams': JeuSgResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.jeu.home.title.main'
        },
        canActivate: [UserRouteAccessService],
    }, {
        path: 'jeu-sg/:id/details',
        component: JeuSgDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.jeu.home.title.main'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const jeuPopupRoute: Routes = [
    {
        path: 'jeu-sg-new',
        component: JeuSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.jeu.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'jeu-sg/:id/edit',
        component: JeuSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.jeu.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'jeu-sg/:id/delete',
        component: JeuSgDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.jeu.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
