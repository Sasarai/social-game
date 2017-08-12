import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { EvenementSg } from './evenement-sg.model';
import { EvenementSgPopupService } from './evenement-sg-popup.service';
import { EvenementSgService } from './evenement-sg.service';
import { JeuSg, JeuSgService } from '../jeu';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-evenement-sg-dialog',
    templateUrl: './evenement-sg-dialog.component.html'
})
export class EvenementSgDialogComponent implements OnInit {

    evenement: EvenementSg;
    isSaving: boolean;

    jeus: JeuSg[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private evenementService: EvenementSgService,
        private jeuService: JeuSgService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.jeuService.query()
            .subscribe((res: ResponseWrapper) => { this.jeus = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.evenement.id !== undefined) {
            this.subscribeToSaveResponse(
                this.evenementService.update(this.evenement));
        } else {
            this.subscribeToSaveResponse(
                this.evenementService.create(this.evenement));
        }
    }

    private subscribeToSaveResponse(result: Observable<EvenementSg>) {
        result.subscribe((res: EvenementSg) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: EvenementSg) {
        this.eventManager.broadcast({ name: 'evenementListModification', content: 'OK'});
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

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-evenement-sg-popup',
    template: ''
})
export class EvenementSgPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private evenementPopupService: EvenementSgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.evenementPopupService
                    .open(EvenementSgDialogComponent as Component, params['id']);
            } else {
                this.evenementPopupService
                    .open(EvenementSgDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
