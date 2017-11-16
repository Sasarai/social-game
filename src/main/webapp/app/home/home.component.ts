import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import { GridsterConfig, GridsterItem } from 'angular-gridster2';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    options: GridsterConfig;
    dashboard: Array<GridsterItem>;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();

        this.options = {
            itemChangeCallback: this.itemChange,
            // draggable: {
            //     enabled: true
            // },
            // resizable: {
            //     enabled: true
            // }
        };

        this.dashboard = [
            {
                cols: 2, rows: 2, y: 0, x: 0, 'type': 'calendrier'
            }
        ];
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    itemChange(item, itemComponent) {
        // console.info('change', item, itemComponent);
    }
}
