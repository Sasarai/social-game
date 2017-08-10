import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SocialGameEvenementSgModule } from './evenement/evenement-sg.module';
import { SocialGameJeuSgModule } from './jeu/jeu-sg.module';
import { SocialGameSphereSgModule } from './sphere/sphere-sg.module';
import { SocialGameVoteSgModule } from './vote/vote-sg.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SocialGameEvenementSgModule,
        SocialGameJeuSgModule,
        SocialGameSphereSgModule,
        SocialGameVoteSgModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialGameEntityModule {}
