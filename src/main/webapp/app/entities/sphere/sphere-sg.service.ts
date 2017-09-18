import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { SphereSg } from './sphere-sg.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class SphereSgService {

    private resourceUrl = 'api/spheres';
    private resourceSearchUrl = 'api/_search/spheres';
    private resourceJoinUrl = 'api/join/sphere';
    private resourceQuitUrl = 'api/quit/sphere';

    constructor(private http: Http) { }

    create(sphere: SphereSg): Observable<SphereSg> {
        const copy = this.convert(sphere);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(sphere: SphereSg): Observable<SphereSg> {
        const copy = this.convert(sphere);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    abonnement(sphere: SphereSg, loginUtilisateur: string): Observable<SphereSg> {
        const copy = this.convert(sphere);
        return this.http.put(`${this.resourceJoinUrl}/${loginUtilisateur}`, copy).map((res: Response) => {
            return res.json();
        })
    }

    desabonnement(sphere: SphereSg, loginUtilisateur: string): Observable<SphereSg> {
        const copy = this.convert(sphere);
        return this.http.put(`${this.resourceQuitUrl}/${loginUtilisateur}`, copy).map((res: Response) => {
            return res.json();
        })
    }

    find(id: number): Observable<SphereSg> {
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

    private convert(sphere: SphereSg): SphereSg {
        const copy: SphereSg = Object.assign({}, sphere);
        return copy;
    }
}
