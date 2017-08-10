import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SphereSg } from './sphere-sg.model';
import { SphereSgPopupService } from './sphere-sg-popup.service';
import { SphereSgService } from './sphere-sg.service';

@Component({
    selector: 'jhi-sphere-sg-delete-dialog',
    templateUrl: './sphere-sg-delete-dialog.component.html'
})
export class SphereSgDeleteDialogComponent {

    sphere: SphereSg;

    constructor(
        private sphereService: SphereSgService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.sphereService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'sphereListModification',
                content: 'Deleted an sphere'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sphere-sg-delete-popup',
    template: ''
})
export class SphereSgDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private spherePopupService: SphereSgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.spherePopupService
                .open(SphereSgDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
