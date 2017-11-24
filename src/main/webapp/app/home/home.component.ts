import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import { GridsterConfig, GridsterItem } from 'angular-gridster2';
import {EvenementSgService} from '../entities/evenement/evenement-sg.service';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';
import {ElementCalendrier} from '../shared/component/element-calendrier.model';
import {isNullOrUndefined} from 'util';

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
    evenementsUtilisateur: ElementCalendrier[];
    nombreEvenementAVoter: number;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private serviceEvenement: EvenementSgService,
        private alertService: JhiAlertService
    ) {
    }

    ngOnInit() {

        this.nombreEvenementAVoter = 0;

        this.principal.identity().then((account) => {
            this.account = account;

            if (this.account !== null) {
                this.serviceEvenement.evenementUtilisateur(this.account.login).subscribe(
                    (res: ResponseWrapper) => this.recupererEvenementUtilisateur(res),
                    (res: ResponseWrapper) => this.onError(res)
                );

                this.serviceEvenement.nombreEvenementUtilisateurAVoter().subscribe(
                    (res: ResponseWrapper) => this.recupererNombreEvenementAVoterUtilisateur(res),
                    (res: ResponseWrapper) => this.onError(res)
                );
            }
        });

        this.registerAuthenticationSuccess();

        this.registerLogOutSuccess();

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
                cols: 2,
                rows: 8,
                y: 0,
                x: 0,
                'type': 'calendrier'
            },
            {
                cols: 2,
                rows: 5,
                y: 0,
                x: 2,
                'type': 'nouveaute'
            },
            {
                cols: 2,
                rows: 3,
                y: 5,
                x: 2,
                'type': 'votes'
            }
        ];
    }

    registerLogOutSuccess() {
        this.eventManager.subscribe('logoutSuccessfull', (message) => {
            this.evenementsUtilisateur = undefined;
        })
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.principal.identity().then((account) => {
                this.account = account;

                if (!isNullOrUndefined(this.account)) {
                    this.serviceEvenement.evenementUtilisateur(this.account.login).subscribe(
                        (res: ResponseWrapper) => this.recupererEvenementUtilisateur(res),
                        (res: ResponseWrapper) => this.onError(res)
                    );

                    this.serviceEvenement.nombreEvenementUtilisateurAVoter().subscribe(
                        (res: ResponseWrapper) => this.recupererNombreEvenementAVoterUtilisateur(res),
                        (res: ResponseWrapper) => this.onError(res)
                    );
                }
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

    private recupererEvenementUtilisateur(data) {

        this.evenementsUtilisateur = [];

        for (const evenement of data) {
            const element = new ElementCalendrier();
            element.fromEvenement(evenement);

            this.evenementsUtilisateur.push(element);
        }

    }

    private recupererNombreEvenementAVoterUtilisateur(data) {
        this.nombreEvenementAVoter = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
