import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { JeuSg } from './jeu-sg.model';
import { JeuSgPopupService } from './jeu-sg-popup.service';
import { JeuSgService } from './jeu-sg.service';
import { VoteSg, VoteSgService } from '../vote';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-jeu-sg-dialog',
    templateUrl: './jeu-sg-dialog.component.html'
})
export class JeuSgDialogComponent implements OnInit {

    jeu: JeuSg;
    isSaving: boolean;

    votes: VoteSg[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private jeuService: JeuSgService,
        private voteService: VoteSgService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.voteService.query()
            .subscribe((res: ResponseWrapper) => { this.votes = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, jeu, field, isImage) {
        if (event && event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                jeu[field] = base64Data;
                jeu[`${field}ContentType`] = file.type;
            });
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.jeu.id !== undefined) {
            this.subscribeToSaveResponse(
                this.jeuService.update(this.jeu));
        } else {
            this.subscribeToSaveResponse(
                this.jeuService.create(this.jeu));
        }
    }

    private subscribeToSaveResponse(result: Observable<JeuSg>) {
        result.subscribe((res: JeuSg) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: JeuSg) {
        this.eventManager.broadcast({ name: 'jeuListModification', content: 'OK'});
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

    trackVoteById(index: number, item: VoteSg) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-jeu-sg-popup',
    template: ''
})
export class JeuSgPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jeuPopupService: JeuSgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.jeuPopupService
                    .open(JeuSgDialogComponent as Component, params['id']);
            } else {
                this.jeuPopupService
                    .open(JeuSgDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
