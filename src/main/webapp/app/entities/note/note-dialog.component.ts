import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Note } from './note.model';
import { NotePopupService } from './note-popup.service';
import { NoteService } from './note.service';
import { User, UserService } from '../../shared';
import { JeuSg, JeuSgService } from '../jeu';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-note-dialog',
    templateUrl: './note-dialog.component.html'
})
export class NoteDialogComponent implements OnInit {

    note: Note;
    isSaving: boolean;

    users: User[];

    jeus: JeuSg[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private noteService: NoteService,
        private userService: UserService,
        private jeuService: JeuSgService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
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
        if (this.note.id !== undefined) {
            this.subscribeToSaveResponse(
                this.noteService.update(this.note));
        } else {
            this.subscribeToSaveResponse(
                this.noteService.create(this.note));
        }
    }

    private subscribeToSaveResponse(result: Observable<Note>) {
        result.subscribe((res: Note) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Note) {
        this.eventManager.broadcast({ name: 'noteListModification', content: 'OK'});
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

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackJeuById(index: number, item: JeuSg) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-note-popup',
    template: ''
})
export class NotePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private notePopupService: NotePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.notePopupService
                    .open(NoteDialogComponent as Component, params['id']);
            } else {
                this.notePopupService
                    .open(NoteDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
