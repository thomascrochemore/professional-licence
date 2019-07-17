import {Component, OnInit, Output, EventEmitter, OnDestroy} from '@angular/core';

import {ApiInfoService} from '../../../_services/api-info';
import {ApiService} from '../../../_services/api';

import {GetDistanceService} from '../../../_services/get-distance.service';

import {Subscription} from "rxjs/Subscription";

@Component({
    selector: 'forecasts',
    templateUrl: './forecasts.component.html',
    styleUrls: ['./forecasts.component.css']
})

export class ForecastsComponent implements OnInit {

    public apis: any;

    public sub: Subscription;

    public sub_apis_list: Subscription;

    public sub_locations_list: Subscription;

    public sub_properties_list: Subscription;

    public locations: any;

    public properties: any;

    public delays: any;

    public hours: any;

    public api: string;

    public location: string;

    public property: string;

    public delay: any;

    public hour: any;

    public filtre: string;

    public filtres: any;

    public displayDropdowns: any;

    public option_nbr: number = 0;

    public params: string = '?';

    public display_tab_with_eval: boolean = false;

    public data_table: any;

    public displayedColumns: any;

    public infos: string;

    public current_date: Date = new Date();

    public date: any;

    public param_dropdown = 'Paramètre';

    public params_dropdown = [
        'Villes',
        'Propriétés',
        'APIs'
    ];

    public data_null = false;

    public display_apis: boolean = false;

    public display_cities: boolean = false;

    public display_properties: boolean = false;

    constructor(private _apiInfoService: ApiInfoService,
                private _apiService: ApiService,
                private _apiDistanceService: GetDistanceService) {

    }

    ngOnInit() {

        this.current_date = new Date();

        // on set des valeurs par défaut aux différentes dropdowns
        this.setDefaultDropdown();

        // récupération de la liste des APIs
        this.sub_apis_list = this._apiInfoService.getApisList().subscribe(
            data => {
                this.apis = data;
            }
        );

        // récupération de la liste des villes
        this.sub_locations_list = this._apiInfoService.getLocationsList().subscribe(
            data => {
                this.locations = data;
            }
        );

        // récupération de la liste des propriétés
        this.sub_properties_list = this._apiInfoService.getPropertiesList().subscribe(
            data => {
                this.properties = data;
            }
        );

        this.delays = [
            'temps réel',
            '6 heures',
            '12 heures',
            '1 jour',
            '3 jours'
        ];

        this.hours = [
            '02:00',
            '08:00',
            '14:00',
            '20:00'
        ];


        this.filtres = [
            {name: 'Villes', text: 'cities'},
            {name: 'Propriétés', text: 'properties'},
            {name: 'APIs', text: 'apis'},
            {name: 'Délais de prévision', text: 'delays'}
        ];

        this.displayDropdowns = {
            checking: {
                cities: false,
                properties: false,
                apis: false,
                delays: false
            },
            disabling: {
                cities: false,
                properties: false,
                apis: false,
                delays: false
            }

        };

    }

    addOption(checked, filtre) {
        if (checked) {
            this.option_nbr++;
            this.displayDropdowns['checking'][filtre] = true;
        } else {
            this.displayDropdowns['checking'][filtre] = false;
            this.setDefaultTextDropdown(filtre);
            this.option_nbr--;
        }

        if (this.option_nbr === 2) {
            this.setDisablingFalse();
        }
        if (this.option_nbr === 3) {
            this.checkForDisable();
        }

    }

    checkForDisable() {
        for (let option in this.displayDropdowns['checking']) {
            if (this.displayDropdowns['checking'][option] == false) {
                this.displayDropdowns['disabling'][option] = true;
            }
        }
    }

    setDisablingFalse() {
        for (let option in this.displayDropdowns['checking']) {
            this.displayDropdowns['disabling'][option] = false;
        }
    }

    changeDropdown(element, option) {
        this[element] = option;
    }


    changeDropdownParams(element) {
        this.param_dropdown = element;

        switch (element) {
            case 'Villes':
                this.display_cities = true;
                this.display_apis = false;
                this.display_properties = false;
                this.setDefaultTextDropdown('properties');
                this.setDefaultTextDropdown('apis');
                break;
            case 'Propriétés':
                this.display_cities = false;
                this.display_apis = false;
                this.display_properties = true;
                this.setDefaultTextDropdown('apis');
                this.setDefaultTextDropdown('cities');
                break;
            case 'APIs':
                this.display_cities = false;
                this.display_apis = true;
                this.display_properties = false;
                this.setDefaultTextDropdown('properties');
                this.setDefaultTextDropdown('cities');

        }
    }

    setDefaultDropdown() {
        this.api = 'APIs';
        this.location = 'Villes';
        this.property = 'Propriétés';
        this.delay = 'Délais de prévisions';
        this.hour = 'Heures';
    }


    setDefaultTextDropdown(filtre) {
        switch (filtre) {
            case 'cities' :
                this.location = 'Villes';
                break;
            case 'apis' :
                this.api = 'APIs';
                break;
            case 'properties' :
                this.property = 'Propriétés';
                break;
            case 'delays':
                this.delay = 'Délais de prévisions';
        }

    }

    search() {
        this.params = '?';
        let delay_value;

        // préparation des paramètres pour la requête vers l'API
        if (this.location !== 'Villes') {
            this.params += 'city=' + this.location + '&';
        }
        if (this.api !== 'APIs') {
            this.params += 'api=' + this.api + '&';
        }
        if (this.property !== 'Propriétés') {
            this.params += 'property=' + this.property + '&';
        }
        if (this.delay !== 'Délais de prévisions') {
            switch (this.delay) {
                case 'temps réel':
                    delay_value = 0;
                    break;
                case '6 heures':
                    delay_value = 6;
                    break;
                case '12 heures':
                    delay_value = 12;
                    break;
                case '1 jour':
                    delay_value = 24;
                    break;
                case '3 jours':
                    delay_value = 72;

            }

            this.params += 'delay=' + delay_value + '&';
        }

        this.current_date = new Date(this.current_date);
        this.date = this.formatDate(this.current_date);

        this.params += 'datetime=' + this.date + '_' + this.hour;

        let classement_by: string;

        for (let option in this.displayDropdowns.checking) {
            if (this.displayDropdowns.checking[option] === false) {
                switch (option) {
                    case 'cities':
                        classement_by = 'villes';
                        break;
                    case 'properties':
                        classement_by = 'propriétés';
                        break;
                    case 'apis':
                        classement_by = "APIs";
                        break;
                    case 'delays':
                        classement_by = 'délais de prévision';
                }
            }
        }

        // récupération des prévisions
        this.sub = this._apiService.getForecast(this.params).subscribe(
            data => {
                if (data['data'] === 'Aucune donnée trouvée') {
                    this.data_null = true;
                } else {
                    this.data_null = false;
                    this.data_table = this._apiDistanceService.wrapper(data);
                    this.displayedColumns = [this.data_table.series_type[0], ...this.data_table.column_headers];
                    this.display_tab_with_eval = true;
                }
            }
        );
    }


    formatDate(date) {
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let year = date.getFullYear();
        return day + '-' + month + '-' + year;
    }

}
