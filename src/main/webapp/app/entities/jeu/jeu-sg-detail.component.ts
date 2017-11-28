import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager , JhiDataUtils } from 'ng-jhipster';

import { JeuSg } from './jeu-sg.model';
import { JeuSgService } from './jeu-sg.service';
import {Principal} from '../../shared/auth/principal.service';

@Component({
    selector: 'jhi-jeu-sg-detail',
    templateUrl: './jeu-sg-detail.component.html'
})
export class JeuSgDetailComponent implements OnInit, OnDestroy {

    currentAccount: any;
    jeu: JeuSg;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private jeuService: JeuSgService,
        private route: ActivatedRoute,
        private principal: Principal
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInJeus();

        this.principal.identity().then((account) => {
            this.currentAccount = account;
        })
    }

    load(id) {
        this.jeuService.find(id).subscribe((jeu) => {
            this.jeu = jeu;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInJeus() {
        this.eventSubscriber = this.eventManager.subscribe(
            'jeuListModification',
            (response) => this.load(this.jeu.id)
        );
    }
}
