import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ClarityModule} from '@clr/angular';
import {AppComponent} from './app.component';
import {routing, appRoutingProviders} from './app.routing';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {Observable} from 'rxjs/Observable';

import {CoreModule} from './core/core.module';

import {LocationStrategy, HashLocationStrategy} from '@angular/common';

import {ChartsModule} from 'ng2-charts';

@NgModule({
    imports: [
        routing,
        CoreModule,
        BrowserModule,
        BrowserAnimationsModule,
        ClarityModule,
        FormsModule,
        ReactiveFormsModule,
        ChartsModule
    ],
    declarations: [AppComponent,

    ],
    bootstrap: [AppComponent],
    providers: [
        appRoutingProviders,
        {provide: LocationStrategy, useClass: HashLocationStrategy}
    ],
})
export class AppModule {
}
