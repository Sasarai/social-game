import {Component, OnDestroy, OnInit} from '@angular/core';
import {EvenementSg} from '../evenement/evenement-sg.model';
import {Subscription} from 'rxjs/Subscription';
import {VoteSgService} from './vote-sg.service';
import {EvenementSgService} from '../evenement/evenement-sg.service';
import {JhiAlertService, JhiEventManager, JhiPaginationUtil, JhiParseLinks} from 'ng-jhipster';
import {Principal} from '../../shared/auth/principal.service';
import {ActivatedRoute, Router} from '@angular/router';
import {PaginationConfig} from '../../blocks/config/uib-pagination.config';
import {ITEMS_PER_PAGE} from '../../shared/constants/pagination.constants';
import {ResponseWrapper} from '../../shared/model/response-wrapper.model';

@Component({
    selector: 'jhi-vote-attente-sg',
    templateUrl: "./vote-attente-sg.component.html"
})
export class VoteAttenteSgComponent implements OnInit, OnDestroy {
    currentAccount: any;
    evenements: EvenementSg[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    constructor(
        private voteService: VoteSgService,
        private evenementService: EvenementSgService,
        private alertService: JhiAlertService,
        private parseLinks: JhiParseLinks,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private paginationUtil: JhiPaginationUtil,
        private paginationConfig: PaginationConfig
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data['pagingParams'].page;
            this.previousPage = data['pagingParams'].page;
            this.reverse = data['pagingParams'].ascending;
            this.predicate = data['pagingParams'].predicate;
        });
    }

    loadAll() {
        this.evenementService.evenementSansVoteUtilisateur({
                page: this.page - 1,
                size: this.itemsPerPage,
                sort: this.sort()
        }).subscribe(
            (res: ResponseWrapper) => this.onSuccess(res.json, res.headers),
            (res: ResponseWrapper) => this.onError(res.json)
        )
    }

    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }

    transition() {
        this.router.navigate(['/vote-sg/attente'], {
            queryParams: {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear(){
        this.page = 0;
        this.router.navigate(['vote-sg/attente', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInVotes();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: EvenementSg) {
        return item.id;
    }

    registerChangeInVotes() {
        this.eventSubscriber = this.eventManager.subscribe('voteListModification', (response) => this.loadAll());
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        console.log(data);
        this.evenements = data;
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
