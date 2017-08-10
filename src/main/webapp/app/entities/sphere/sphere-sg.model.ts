import { BaseEntity } from './../../shared';

export class SphereSg implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public evenementId?: number,
    ) {
    }
}
