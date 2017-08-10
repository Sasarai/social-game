import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { EvenementSg } from './evenement-sg.model';
import { EvenementSgService } from './evenement-sg.service';

@Component({
    selector: 'jhi-evenement-sg-detail',
    templateUrl: './evenement-sg-detail.component.html'
})
export class EvenementSgDetailComponent implements OnInit, OnDestroy {

    evenement: EvenementSg;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private evenementService: EvenementSgService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEvenements();
    }

    load(id) {
        this.evenementService.find(id).subscribe((evenement) => {
            this.evenement = evenement;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEvenements() {
        this.eventSubscriber = this.eventManager.subscribe(
            'evenementListModification',
            (response) => this.load(this.evenement.id)
        );
    }
}
