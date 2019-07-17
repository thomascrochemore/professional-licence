import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import * as L from 'leaflet';
import {Util} from "leaflet";

@Component({
    selector: 'frugal-map',
    templateUrl: './frugal_maps.component.html',
    styleUrls: ['./frugal_maps.component.css']
})


export class FrugalMapsComponent implements OnInit {

    @Input() cities: any;

    @Output() city_map = new EventEmitter<string>();

    public city: string;

    ngOnInit() {

        // Déclaration de la carte avec les coordonnées du centre et le niveau de zoom.
        const myfrugalmap = L.map('frugalmap').setView([0, 0], 0);

        L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
            attribution: 'Frugal Map'
        }).addTo(myfrugalmap);


        const myIcon = L.icon({
            iconUrl: '../../../../../assets/maps/marker.png',
            iconAnchor: [11, 28]
        });

        for (let marker in this.cities) {
            L.marker([this.cities[marker]['latitude'], this.cities[marker]['longitude']], {icon: myIcon}).bindPopup(this.cities[marker]['city']).on('click', this.markerOnClick, this).addTo(myfrugalmap);
        }
    }

    markerOnClick(e) {
        let lat = e['latlng']['lat'];
        let long = e['latlng']['lng'];
        this.getCityByLatLng(lat, long, this.cities);
    }

    getCityByLatLng(lat, long, cities) {

        let new_json_cities = this.cities.filter(
            c => {
                return c.latitude == lat && c.longitude == long;
            }
        );

        this.city = new_json_cities[0]['city'];
        this.city_map.emit(this.city);
    }

}