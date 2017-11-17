import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { EvenementSg } from './evenement-sg.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class EvenementSgService {

    private resourceUrl = 'api/evenements';
    private resourceSearchUrl = 'api/_search/evenements';
    private resourceUserUrl = 'api/_user/evenements';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(evenement: EvenementSg): Observable<EvenementSg> {
        const copy = this.convert(evenement);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(evenement: EvenementSg): Observable<EvenementSg> {
        const copy = this.convert(evenement);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<EvenementSg> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    evenementUtilisateur(loginUtilisateur: String): Observable<Response> {
        return this.http.get(`${this.resourceUserUrl}/${loginUtilisateur}`).map((res: Response) => {
            const jsonReponse = res.json();
            this.convertItemFromServer(jsonReponse);
            return jsonReponse;
        })
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.date = this.dateUtils
            .convertDateTimeFromServer(entity.date);
        entity.dateFinVote = this.dateUtils
            .convertDateTimeFromServer(entity.dateFinVote);
    }

    private convert(evenement: EvenementSg): EvenementSg {
        const copy: EvenementSg = Object.assign({}, evenement);

        copy.date = this.dateUtils.toDate(evenement.date);

        copy.dateFinVote = this.dateUtils.toDate(evenement.dateFinVote);
        return copy;
    }
}
