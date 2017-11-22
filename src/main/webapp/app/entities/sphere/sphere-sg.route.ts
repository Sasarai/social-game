import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SphereSgComponent } from './sphere-sg.component';
import { SphereSgDetailComponent } from './sphere-sg-detail.component';
import { SphereSgPopupComponent } from './sphere-sg-dialog.component';
import { SphereSgDeletePopupComponent } from './sphere-sg-delete-dialog.component';

@Injectable()
export class SphereSgResolvePagingParams implements Resolve<any> {

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

export const sphereRoute: Routes = [
    {
        path: 'sphere-sg',
        component: SphereSgComponent,
        resolve: {
            'pagingParams': SphereSgResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.sphere.home.title.main'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sphere-sg/:type',
        component: SphereSgComponent,
        resolve: {
            'pagingParams': SphereSgResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.sphere.home.title.main'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sphere-sg/:id/details',
        component: SphereSgDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.sphere.home.title.main'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const spherePopupRoute: Routes = [
    {
        path: 'sphere-sg-new',
        component: SphereSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.sphere.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sphere-sg/:id/edit',
        component: SphereSgPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.sphere.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sphere-sg/:id/delete',
        component: SphereSgDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'socialGameApp.sphere.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
