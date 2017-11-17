import {EvenementSg} from '../../entities/evenement/evenement-sg.model';
import {DatePipe} from '@angular/common';

export class ElementCalendrier {

    titre: string;
    debut: string;
    fin: string;
    groupe: any;

    constructor() {}

    fromEvenement(evenement: EvenementSg) {

        console.log(evenement);

        this.titre = evenement.nom;
        this.debut = new DatePipe('fr').transform(new Date(evenement.date), "yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.fin = new DatePipe('fr').transform(new Date(evenement.date).getTime() + 3600000, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.groupe = evenement.sphereId;
    }
}
