import {EvenementSg} from '../../entities/evenement/evenement-sg.model';
import {DatePipe} from '@angular/common';

export class ElementCalendrier {

    titre: String;
    debut: String;
    fin: String;
    groupe: any;

    datePipe: DatePipe;

    constructor() {}

    fromEvenement(evenement: EvenementSg) {
        this.titre = evenement.nom;
        this.debut = this.datePipe.transform(evenement.date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.fin = this.datePipe.transform((evenement.date.getTime() + 3600000), "yyyy-MM-dd'T'HH:mm:ss'Z'");
        this.groupe = evenement.sphereId;
    }
}
