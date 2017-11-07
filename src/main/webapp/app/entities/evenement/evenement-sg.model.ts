import { BaseEntity } from './../../shared';

interface EvenementInterface {
    id?: number;
    date?: any;
    lieu?: string;
    nom?: string;
    dateFinVote?: any;
    jeuxes?: BaseEntity[];
    votes?: BaseEntity[];
    sphereId?: number;
}

export class EvenementSg implements BaseEntity {

    public id?: number;
    public date?: any;
    public lieu?: string;
    public nom?: string;
    public dateFinVote?: any;
    public jeuxes?: BaseEntity[];
    public votes?: BaseEntity[];
    public sphereId?: number;

    constructor(
        public obj?: EvenementInterface
    ) {
        if (obj) {
            this.id = obj.id;
            this.date = obj.date;
            this.lieu = obj.lieu;
            this.nom = obj.lieu;
            this.dateFinVote = obj.dateFinVote;
            this.jeuxes = obj.jeuxes;
            this.votes = obj.votes;
            this.sphereId = obj.sphereId;
        }

    }
}
