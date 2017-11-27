import {Component, OnDestroy, OnInit} from '@angular/core';
import {EvenementSg} from '../evenement/evenement-sg.model';
import {JhiAlertService, JhiEventManager} from 'ng-jhipster';
import {VoteSgService} from './vote-sg.service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {Observable} from 'rxjs/Observable';
import {VoteSg} from './vote-sg.model';
import {JeuSg} from '../jeu/jeu-sg.model';
import {ActivatedRoute} from '@angular/router';
import {VoteSgPopupService} from './vote-sg-popup.service';
import {EvenementSgService} from '../evenement/evenement-sg.service';
import {EvenementSgPopupService} from '../evenement/evenement-sg-popup.service';

@Component({
    selector: 'jhi-vote-voter-sg',
    templateUrl: './vote-voter-sg.component.html'
})
export class VoteVoterSgComponent implements OnInit, OnDestroy {
    evenement: EvenementSg;
    vote: VoteSg;

    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private voteService: VoteSgService,
        private eventManager: JhiEventManager
    ){

    }

    ngOnInit() {
        this.vote = new VoteSg;
        this.isSaving = false;
    }

    ngOnDestroy() {

    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {

        this.vote.evenementId = this.evenement.id;

        this.isSaving = true;
        this.subscribeToSaveResponse(
            this.voteService.create(this.vote)
        );
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

    trackJeuById(index: number, item: JeuSg) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-vote-voter-sg-popup',
    template: ''
})
export class VoteVoterSgPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private evenementPopupService: EvenementSgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.evenementPopupService
                    .open(VoteVoterSgComponent as Component, params['id']);
            } else {
                this.evenementPopupService
                    .open(VoteVoterSgComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
