import { BaseEntity, User } from './../../shared';

export class SphereSg implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public administrateurId?: number,
        public evenements?: BaseEntity[],
        public abonnes?: User[],
    ) {
    }
}
