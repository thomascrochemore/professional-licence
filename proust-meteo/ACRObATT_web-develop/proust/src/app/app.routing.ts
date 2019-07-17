import {ModuleWithProviders} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {coreRoutes} from './core/core.routing';
import {unwrapParentheses} from "tslint";

export const appRoutes: Routes = [
    ...coreRoutes
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes);


export const appRoutingProviders: any[] = [
    // ...coreProviders
];

