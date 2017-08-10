import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { VoteSg } from './vote-sg.model';
import { VoteSgPopupService } from './vote-sg-popup.service';
import { VoteSgService } from './vote-sg.service';

@Component({
    selector: 'jhi-vote-sg-delete-dialog',
    templateUrl: './vote-sg-delete-dialog.component.html'
})
export class VoteSgDeleteDialogComponent {

    vote: VoteSg;

    constructor(
        private voteService: VoteSgService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.voteService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'voteListModification',
                content: 'Deleted an vote'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vote-sg-delete-popup',
    template: ''
})
export class VoteSgDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private votePopupService: VoteSgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.votePopupService
                .open(VoteSgDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
