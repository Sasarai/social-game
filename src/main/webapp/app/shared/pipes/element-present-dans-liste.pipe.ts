import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'elementPresentDansListe'})
export class ElementPresentDansListePipe implements PipeTransform {
    transform(element: any, liste: any[], attribut: string): boolean {

        let retour = false;

        liste.forEach(function(item){
            if (item[attribut] === element) {
                retour = true;
            }
        });

        return retour;
    }
}
