import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule } from 'ionic-angular';
import { HttpClientModule } from '@angular/common/http';
import { MyApp } from '../app/app.component';

// composants web angular
import { MeteoApiComponent } from './meteo-api/meteo-api';
import { MeteoCityComponent } from './meteo-city/meteo-city';

// le widget
import { WidgetComponent } from './widget/widget';
import { WidgetV2Component } from './widget-v2/widget-v2';

// charts
import { ChartsModule } from 'ng2-charts';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// material
import {
  MatFormFieldModule,
  MatSelectModule,
  MatInputModule,
  MatOptionModule,
  MatCardModule,
  MatButtonModule,
  MatTableModule,
  MatTabsModule,
} from '@angular/material';

import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';

// forms
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MiniwidgetComponent } from './miniwidget/miniwidget';
import { FrugalmapComponent } from './frugalmap/frugalmap';
import { SiteWebComponent } from './site-web/site-web';

// Pipe
import { SafePipe } from '../pipe/pipe';
import { MiniComponent } from './mini/mini';

@NgModule({
	declarations: [
    MeteoApiComponent,
    MeteoCityComponent,
    WidgetComponent,
    WidgetV2Component,
    MiniwidgetComponent,
    FrugalmapComponent,
    SiteWebComponent,
    SafePipe,
    MiniComponent,
    MiniComponent
  ],
	imports: [
		ChartsModule,
		FormsModule,
		ReactiveFormsModule,
	  BrowserModule,
	  HttpClientModule,
		IonicModule.forRoot(MyApp),
		MatFormFieldModule,
		MatSelectModule,
		MatInputModule,
		MatOptionModule,
		MatCardModule,
		MatButtonModule,
		MatIconModule,
		MatDatepickerModule,
		MatTableModule,
		MatTabsModule,
		BrowserAnimationsModule
	],
	bootstrap: [IonicApp],
	exports: [
    MeteoApiComponent,
    MeteoCityComponent,
    WidgetComponent,
    WidgetV2Component,
    MiniwidgetComponent,
    FrugalmapComponent,
    SiteWebComponent,
    MiniComponent,
    MiniComponent
  ],
})
export class ComponentsModule {}
