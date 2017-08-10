import { BaseEntity } from './../../shared';

export class EvenementSg implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public lieu?: string,
        public dateFinVote?: any,
        public nom?: string,
        public spheres?: BaseEntity[],
        public voteId?: number,
    ) {
    }
}
