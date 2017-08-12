import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { VoteSg } from './vote-sg.model';
import { VoteSgPopupService } from './vote-sg-popup.service';
import { VoteSgService } from './vote-sg.service';
import { EvenementSg, EvenementSgService } from '../evenement';
import { User, UserService } from '../../shared';
import { JeuSg, JeuSgService } from '../jeu';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-vote-sg-dialog',
    templateUrl: './vote-sg-dialog.component.html'
})
export class VoteSgDialogComponent implements OnInit {

    vote: VoteSg;
    isSaving: boolean;

    evenements: EvenementSg[];

    users: User[];

    jeus: JeuSg[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private voteService: VoteSgService,
        private evenementService: EvenementSgService,
        private userService: UserService,
        private jeuService: JeuSgService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.evenementService.query()
            .subscribe((res: ResponseWrapper) => { this.evenements = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.jeuService.query()
            .subscribe((res: ResponseWrapper) => { this.jeus = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.vote.id !== undefined) {
            this.subscribeToSaveResponse(
                this.voteService.update(this.vote));
        } else {
            this.subscribeToSaveResponse(
                this.voteService.create(this.vote));
        }
    }

    private subscribeToSaveResponse(result: Observable<VoteSg>) {
        result.subscribe((res: VoteSg) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: VoteSg) {
        this.eventManager.broadcast({ name: 'voteListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackEvenementById(index: number, item: EvenementSg) {
        return item.id;
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackJeuById(index: number, item: JeuSg) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-vote-sg-popup',
    template: ''
})
export class VoteSgPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private votePopupService: VoteSgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.votePopupService
                    .open(VoteSgDialogComponent as Component, params['id']);
            } else {
                this.votePopupService
                    .open(VoteSgDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
