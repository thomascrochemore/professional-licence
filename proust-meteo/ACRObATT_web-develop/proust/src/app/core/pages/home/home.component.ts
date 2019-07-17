import {Component, OnInit} from '@angular/core';
import {ApiInfoService} from '../../_services/api-info';

import {ApiService} from '../../_services/api';
import {Subscription} from "rxjs/Subscription";

import {Router} from '@angular/router';
import {ActivatedRoute} from '@angular/router';


@Component({
    selector: 'home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})


export class HomeComponent implements OnInit {


    public locations: any; // villes sans les coordonnées

    public sub: Subscription;

    public sub_2: Subscription;

    public location: string = 'Villes';

    public cities: any; // villes avec les cordonnées

    public map: boolean = false; // affichage de la carte


    public city_map: string = 'Strasbourg';


    constructor(private _apiInfoService: ApiInfoService,
                private _apiService: ApiService,
                private router: Router,
                private route: ActivatedRoute,) {
    }

    ngOnInit() {

        // récupération de la liste des villes
        this._apiInfoService.getLocationsList().subscribe(
            data => {
                this.locations = data;
            }
        );

        // récupération des villes avec leur coordonnées
        this._apiService.getCities().subscribe(
            data => {
                this.cities = data;
                this.map = true;
            }
        );
    }

    nav(link) {
        this.router.navigateByUrl(link);
    }

    changeCity(event) {
        this.city_map = event;
    }


}
