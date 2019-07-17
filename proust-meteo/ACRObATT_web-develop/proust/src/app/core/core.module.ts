import {NgModule, NO_ERRORS_SCHEMA, Optional, SkipSelf} from '@angular/core';
import {ClarityModule} from "@clr/angular";
import {CommonModule} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';

// pages
import {SigninComponent} from './pages/signin/signin.component';
import {SignupComponent} from './pages/signup/signup.component';
import {AboutComponent} from './pages/about/about.component';
import {EvaluationComponent} from './pages/evaluation/evaluation.component';
import {ForecastsComponent} from './pages/data_exploration/forecasts/forecasts.component';
import {HomeComponent} from './pages/home/home.component';
import {DistanceComponent} from './pages/data_exploration/distance/distance.component';
import {ScoresComponent} from './pages/data_exploration/scores/scores.component';

let pages = [
    SigninComponent,
    SignupComponent,
    AboutComponent,
    EvaluationComponent,
    ForecastsComponent,
    HomeComponent,
];

// widgets
import {UICarouselModule} from 'ui-carousel';
import {FrugalMapsComponent} from './_components/frugal_map/frugal_maps/frugal_maps.component';

let widgets = [
    UICarouselModule,
];

// components
import {GraphicComponent} from './_components/graphic_tab/graphic/graphic.component';
import {TabComponent} from './_components/graphic_tab/tab/tab.component';
import {CarouselComponent} from './_components/carousel/carousel.component';
import {WidgetComponent} from './_components/widget/widget';
import {DetailWidgetComponent} from './_components/detail-widget/detail-widget.component';

let components = [
    GraphicComponent,
    TabComponent,
    CarouselComponent,
    WidgetComponent,
    DistanceComponent,
    ScoresComponent,
    EvaluationComponent,
    FrugalMapsComponent,
    DetailWidgetComponent
];

// services
import {GetDetailsService} from './_services/get-details.service' ;
import {ApiInfoService} from './_services/api-info';
import {ApiService} from './_services/api';
import {GetDistanceService} from './_services/get-distance.service';
import {GetScoresService} from './_services/get-scores.service';
import {GetWeatherService} from './_services/get-weather.service';

let services = [
    ApiInfoService,
    ApiService,
    GetDistanceService,
    GetScoresService,
    GetWeatherService,
    GetDetailsService
];

// material

import {
    MatTableModule,
    MatTabsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatOptionModule,
    MatCardModule,
    MatButtonModule,
    MatAutocompleteModule,
    MatCheckboxModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatSidenavModule,
} from '@angular/material';

import {ChartsModule} from 'ng2-charts';

let material = [
    MatFormFieldModule,
    MatSelectModule,
    MatInputModule,
    MatOptionModule,
    MatCardModule,
    MatButtonModule,
    MatTableModule,
    MatTabsModule,
    MatAutocompleteModule,
    MatCheckboxModule,
    MatNativeDateModule,
    MatDatepickerModule
];

@NgModule({
    imports: [
        CommonModule,
        HttpClientModule,
        ClarityModule,
        ...material,
        ...widgets,
        ChartsModule,
    ],
    declarations: [
        ...pages,
        ...components,
    ],
    providers: [
        ...services
    ],
    schemas: [NO_ERRORS_SCHEMA]
})

export class CoreModule {
    constructor(@Optional() @SkipSelf() parentModule: CoreModule) {

        if (parentModule) {
            throw new Error(
                'CoreModule is already loaded'
            );
        }
    }
}
