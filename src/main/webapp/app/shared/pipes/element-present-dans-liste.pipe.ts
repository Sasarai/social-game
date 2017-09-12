import { Pipe, PipeTransform } from '@angular/core';

@Pipe({name: 'elementPresentDansListe'})
export class ElementPresentDansListePipe implements PipeTransform {
    transform(element: any, liste: any[]): boolean {

        let retour = false;

        liste.forEach(function(item){
            if (item === element) {
                retour = true;
            }
        });

        return retour;
    }
}
