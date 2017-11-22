import { URLSearchParams, BaseRequestOptions } from '@angular/http';
import {type} from 'os';

export const createRequestOption = (req?: any): BaseRequestOptions => {
    const options: BaseRequestOptions = new BaseRequestOptions();
    if (req) {
        const params: URLSearchParams = new URLSearchParams();
        params.set('page', req.page);
        params.set('size', req.size);
        if (req.sort) {
            params.paramsMap.set('sort', req.sort);
        }
        params.set('query', req.query);

        if (req.filtre) {

            const keyArray: any[] = Object.keys(req.filtre);

            keyArray.forEach((key: any) => {
                params.set(key, req.filtre[key]);
            })
        }

        options.params = params;
    }
    return options;
};
