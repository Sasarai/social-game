import { BaseEntity } from './../../shared';

const enum GenreJeu {
    'COOPERATION',
    'CHACUN_POUR_SOI',
    'EN_EQUIPE',
    'HYBRIDE'
}

export class JeuSg implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public nombreJoueurMin?: number,
        public nombreJoueurMax?: number,
        public dureeMoyenne?: number,
        public description?: string,
        public imageContentType?: string,
        public image?: any,
        public genre?: GenreJeu,
        public voteId?: number,
    ) {
    }
}
