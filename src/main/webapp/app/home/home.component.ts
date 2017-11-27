import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import { Account, LoginModalService, Principal } from '../shared';
import { GridsterConfig, GridsterItem } from 'angular-gridster2';
import {EvenementSgService} from '../entities/evenement/evenement-sg.service';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';
import {ElementCalendrier} from '../shared/component/element-calendrier.model';
import {isNullOrUndefined} from 'util';
import {VoteSg} from '../entities/vote/vote-sg.model';
import {VoteSgService} from '../entities/vote/vote-sg.service';

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
        private alertService: JhiAlertService,
        private modalService: NgbModal
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

        this.registerOnCalendarClickEvent();

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

    registerOnCalendarClickEvent() {
        this.eventManager.subscribe('clickElementCalendrier', (element) => {
            const modalRef = this.modalService.open(PopupEventCalendrierComponent);
            modalRef.componentInstance.idEvenement = element.content.id;
        })
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

@Component({
    selector: 'jhi-popup-event-sg',
    templateUrl: './popup-event-calendrier.component.html'
})
export class PopupEventCalendrierComponent implements OnInit, OnDestroy {
    idEvenement: number;
    votes: VoteSg[];

    votesResumes: VoteResumeSg[];

    constructor(
        private voteService: VoteSgService,
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService
    ) {

    }

    ngOnInit() {
        this.voteService.getDetailVotesEvenement(this.idEvenement).subscribe(
            (res: ResponseWrapper) => this.recupererDetailVotes(res),
            (res: ResponseWrapper) => this.onError(res)
        )
    }

    ngOnDestroy() {

    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    private recupererDetailVotes(data) {
        this.votes = data;

        this.votesResumes = this.getVotesResumes(data);

    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    private getVotesResumes(data) {
        const resume: any = [];
        let nombreTotal: any = 0;

        for (const entry of data) {
            nombreTotal++;
            if (resume[entry.jeuNom]) {
                resume[entry.jeuNom] = resume[entry.jeuNom] + 1;
            } else {
                resume[entry.jeuNom] = 1;
            }
        }

        const retour: VoteResumeSg[] = [];

        for (const nomJeu in resume) {

            let vote: VoteResumeSg = new VoteResumeSg;

            vote.nombreTotalVotant = nombreTotal;
            vote.nomJeu = nomJeu;
            vote.nombreVotant = resume[nomJeu];

            retour.push(vote);
        }

        return retour;

    }
}

export class VoteResumeSg {
    nomJeu: string;
    nombreVotant: number;
    nombreTotalVotant: number;
}
