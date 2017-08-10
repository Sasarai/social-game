import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { VoteSg } from './vote-sg.model';
import { VoteSgService } from './vote-sg.service';

@Component({
    selector: 'jhi-vote-sg-detail',
    templateUrl: './vote-sg-detail.component.html'
})
export class VoteSgDetailComponent implements OnInit, OnDestroy {

    vote: VoteSg;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private voteService: VoteSgService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVotes();
    }

    load(id) {
        this.voteService.find(id).subscribe((vote) => {
            this.vote = vote;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVotes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'voteListModification',
            (response) => this.load(this.vote.id)
        );
    }
}
