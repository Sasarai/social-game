import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SphereSg } from './sphere-sg.model';
import { SphereSgPopupService } from './sphere-sg-popup.service';
import { SphereSgService } from './sphere-sg.service';
import { EvenementSg, EvenementSgService } from '../evenement';
import { User, UserService } from '../../shared';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-sphere-sg-dialog',
    templateUrl: './sphere-sg-dialog.component.html'
})
export class SphereSgDialogComponent implements OnInit {

    sphere: SphereSg;
    isSaving: boolean;

    evenements: EvenementSg[];

    users: User[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private sphereService: SphereSgService,
        private evenementService: EvenementSgService,
        private userService: UserService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.evenementService.query()
            .subscribe((res: ResponseWrapper) => { this.evenements = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.sphere.id !== undefined) {
            this.subscribeToSaveResponse(
                this.sphereService.update(this.sphere));
        } else {
            this.subscribeToSaveResponse(
                this.sphereService.create(this.sphere));
        }
    }

    private subscribeToSaveResponse(result: Observable<SphereSg>) {
        result.subscribe((res: SphereSg) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: SphereSg) {
        this.eventManager.broadcast({ name: 'sphereListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-sphere-sg-popup',
    template: ''
})
export class SphereSgPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private spherePopupService: SphereSgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.spherePopupService
                    .open(SphereSgDialogComponent as Component, params['id']);
            } else {
                this.spherePopupService
                    .open(SphereSgDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
