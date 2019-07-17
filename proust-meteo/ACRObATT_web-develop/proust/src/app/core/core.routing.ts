import {Routes} from '@angular/router';

import {SigninComponent} from './pages/signin/signin.component';
import {SignupComponent} from './pages/signup/signup.component';
import {AboutComponent} from './pages/about/about.component';
import {EvaluationComponent} from './pages/evaluation/evaluation.component';
import {ForecastsComponent} from './pages/data_exploration/forecasts/forecasts.component';
import {HomeComponent} from './pages/home/home.component';
import {DistanceComponent} from './pages/data_exploration/distance/distance.component';
import {ScoresComponent} from './pages/data_exploration/scores/scores.component';

export const coreRoutes: Routes = [
    {path: '', component: HomeComponent},
    {path: 'signin', component: SigninComponent},
    {path: 'signup', component: SignupComponent},
    {path: 'about', component: AboutComponent},
    {path: 'evaluation', component: EvaluationComponent},
    {path: 'forecasts', component: ForecastsComponent},
    {path: 'distance', component: DistanceComponent},
    {path: 'scores', component: ScoresComponent},
];
