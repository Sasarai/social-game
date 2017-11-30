import {Component, OnDestroy, OnInit} from '@angular/core';
import {NgbActiveModal, NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';

import { Account, LoginModalService, Principal, StateStorageService } from '../shared';
import { GridsterConfig, GridsterItem } from 'angular-gridster2';
import {EvenementSgService} from '../entities/evenement/evenement-sg.service';
import {ResponseWrapper} from '../shared/model/response-wrapper.model';
import {ElementCalendrier} from '../shared/component/element-calendrier.model';
import {isNullOrUndefined} from 'util';
import {VoteSg} from '../entities/vote/vote-sg.model';
import {VoteSgService} from '../entities/vote/vote-sg.service';
import {LoginService} from '../shared/login/login.service';
import {Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: [
        'home.scss'
    ]

})
export class HomeComponent implements OnInit, OnDestroy {
    account: Account;
    modalRef: NgbModalRef;
    options: GridsterConfig;
    dashboard: Array<GridsterItem>;
    evenementsUtilisateur: ElementCalendrier[];
    nombreEvenementAVoter: number;
    username: string;
    password: string;
    authenticationError: boolean;
    private eventSubscriberEventUtilisateur: Subscription;
    private eventSubscriberVoteUtilisateur: Subscription;
    private eventSubscriberClickCalendrier: Subscription;

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private serviceEvenement: EvenementSgService,
        private alertService: JhiAlertService,
        private modalService: NgbModal,
        private loginService: LoginService,
        private router: Router,
        private stateStorageService: StateStorageService
    ) {
    }

    ngOnInit() {

        this.nombreEvenementAVoter = 0;

        this.principal.identity().then((account) => {
            this.account = account;

            if (this.account !== null) {
                this.eventSubscriberEventUtilisateur = this.serviceEvenement.evenementUtilisateur(this.account.login).subscribe(
                    (res: ResponseWrapper) => this.recupererEvenementUtilisateur(res),
                    (res: ResponseWrapper) => this.onError(res)
                );

                this.eventSubscriberVoteUtilisateur = this.serviceEvenement.nombreEvenementUtilisateurAVoter().subscribe(
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

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriberEventUtilisateur);
        this.eventManager.destroy(this.eventSubscriberVoteUtilisateur);
        this.eventManager.destroy(this.eventSubscriberClickCalendrier);
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
                    this.eventSubscriberEventUtilisateur = this.serviceEvenement.evenementUtilisateur(this.account.login).subscribe(
                        (res: ResponseWrapper) => this.recupererEvenementUtilisateur(res),
                        (res: ResponseWrapper) => this.onError(res)
                    );

                    this.eventSubscriberVoteUtilisateur = this.serviceEvenement.nombreEvenementUtilisateurAVoter().subscribe(
                        (res: ResponseWrapper) => this.recupererNombreEvenementAVoterUtilisateur(res),
                        (res: ResponseWrapper) => this.onError(res)
                    );
                }
            });
        });
    }

    registerOnCalendarClickEvent() {
        this.eventSubscriberClickCalendrier = this.eventManager.subscribe('clickElementCalendrier', (element) => {
            const modalRef = this.modalService.open(PopupEventCalendrierComponent);
            modalRef.componentInstance.idEvenement = element.content.id;
        })
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.loginService.login({
            username: this.username,
            password: this.password
        }).then(() => {
            this.authenticationError = false;
            if (this.router.url === '/register' || (/activate/.test(this.router.url)) ||
                this.router.url === '/finishReset' || this.router.url === '/requestReset') {
                this.router.navigate(['']);
            }

            this.eventManager.broadcast({
                name: 'authenticationSuccess',
                content: 'Sending Authentication Success'
            });

            // // previousState was set in the authExpiredInterceptor before being redirected to login modal.
            // // since login is succesful, go to stored previousState and clear previousState
            const redirect = this.stateStorageService.getUrl();
            if (redirect) {
                this.router.navigate([redirect]);
            }
        }).catch(() => {
            this.authenticationError = true;
        });
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
