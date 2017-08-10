import { BaseEntity } from './../../shared';

export class VoteSg implements BaseEntity {
    constructor(
        public id?: number,
        public nombreVote?: number,
        public jeus?: BaseEntity[],
        public evenements?: BaseEntity[],
    ) {
    }
}
