import { BaseEntity } from './../../shared';

export class EvenementSg implements BaseEntity {
    constructor(
        public id?: number,
        public date?: any,
        public lieu?: string,
        public nom?: string,
        public dateFinVote?: any,
        public spheres?: BaseEntity[],
        public jeuxes?: BaseEntity[],
        public votes?: BaseEntity[],
    ) {
    }
}