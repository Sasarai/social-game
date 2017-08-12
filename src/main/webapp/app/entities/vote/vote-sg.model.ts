import { BaseEntity } from './../../shared';

export class VoteSg implements BaseEntity {
    constructor(
        public id?: number,
        public evenementId?: number,
        public userId?: number,
        public jeuId?: number,
    ) {
    }
}
