import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { JeuSg } from './jeu-sg.model';
import { JeuSgPopupService } from './jeu-sg-popup.service';
import { JeuSgService } from './jeu-sg.service';

@Component({
    selector: 'jhi-jeu-sg-delete-dialog',
    templateUrl: './jeu-sg-delete-dialog.component.html'
})
export class JeuSgDeleteDialogComponent {

    jeu: JeuSg;

    constructor(
        private jeuService: JeuSgService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.jeuService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'jeuListModification',
                content: 'Deleted an jeu'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-jeu-sg-delete-popup',
    template: ''
})
export class JeuSgDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private jeuPopupService: JeuSgPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.jeuPopupService
                .open(JeuSgDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
