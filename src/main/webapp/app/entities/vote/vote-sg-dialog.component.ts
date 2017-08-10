import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { VoteSg } from './vote-sg.model';
import { VoteSgPopupService } from './vote-sg-popup.service';
import { VoteSgService } from './vote-sg.service';

@Component({
    selector: 'jhi-vote-sg-dialog',
    templateUrl: './vote-sg-dialog.component.html'
})
export class VoteSgDialogComponent implements OnInit {

    vote: VoteSg;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private voteService: VoteSgService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
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
