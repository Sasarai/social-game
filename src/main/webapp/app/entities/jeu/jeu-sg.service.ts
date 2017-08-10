import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { JeuSg } from './jeu-sg.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class JeuSgService {

    private resourceUrl = 'api/jeus';
    private resourceSearchUrl = 'api/_search/jeus';

    constructor(private http: Http) { }

    create(jeu: JeuSg): Observable<JeuSg> {
        const copy = this.convert(jeu);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(jeu: JeuSg): Observable<JeuSg> {
        const copy = this.convert(jeu);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<JeuSg> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(jeu: JeuSg): JeuSg {
        const copy: JeuSg = Object.assign({}, jeu);
        return copy;
    }
}
