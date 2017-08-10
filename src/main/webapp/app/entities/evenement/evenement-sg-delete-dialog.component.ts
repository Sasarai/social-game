import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { EvenementSg } from './evenement-sg.model';
import { EvenementSgPopupService } from './evenement-sg-popup.service';
import { EvenementSgService } from './evenement-sg.service';

@Component({
    selector: 'jhi-evenement-sg-delete-dialog',
    templateUrl: './evenement-sg-delete-dialog.component.html'
})
export class EvenementSgDeleteDialogComponent {

    evenement: EvenementSg;

    constructor(
        private evenementService: EvenementSgService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.evenementService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'evenementListModification',
                content: 'Deleted an evenement'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-evenement-sg-delete-popup',
    template: ''
})
export class EvenementSgDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private evenementPopupService: EvenementSgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.evenementPopupService
                .open(EvenementSgDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
