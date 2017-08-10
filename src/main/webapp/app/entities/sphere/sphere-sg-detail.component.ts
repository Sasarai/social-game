import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { SphereSg } from './sphere-sg.model';
import { SphereSgService } from './sphere-sg.service';

@Component({
    selector: 'jhi-sphere-sg-detail',
    templateUrl: './sphere-sg-detail.component.html'
})
export class SphereSgDetailComponent implements OnInit, OnDestroy {

    sphere: SphereSg;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private sphereService: SphereSgService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSpheres();
    }

    load(id) {
        this.sphereService.find(id).subscribe((sphere) => {
            this.sphere = sphere;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSpheres() {
        this.eventSubscriber = this.eventManager.subscribe(
            'sphereListModification',
            (response) => this.load(this.sphere.id)
        );
    }
}
