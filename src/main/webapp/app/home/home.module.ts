import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SocialGameSharedModule } from '../shared';

import { HOME_ROUTE, HomeComponent } from './';

import { GridsterModule } from 'angular-gridster2'
import {PopupEventCalendrierComponent} from './home.component';

@NgModule({
    imports: [
        SocialGameSharedModule,
        RouterModule.forRoot([ HOME_ROUTE ], { useHash: true }),
        GridsterModule
    ],
    declarations: [
        HomeComponent,
        PopupEventCalendrierComponent
    ],
    entryComponents: [
        PopupEventCalendrierComponent
    ],
    providers: [
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SocialGameHomeModule {}
