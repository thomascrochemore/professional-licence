import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import * as L from 'leaflet';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
    title = 'proust';

    constructor(public router: Router) {
    }

    public ngOnInit() {

    }

}
