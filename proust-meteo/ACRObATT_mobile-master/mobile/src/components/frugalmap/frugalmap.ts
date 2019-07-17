import { Component, Input } from '@angular/core';

// api
import { WidgetRestService } from '../../providers/widget-rest';

// maps
import * as L from 'leaflet';

@Component({
    selector: 'frugal-map',
    templateUrl: './frugalmap.html'
    //, styleUrls: ['./frugalmap.scss']
})

export class FrugalmapComponent
{
    @Input() latitude: any = 48.5839;
    @Input() longitude: any = 7.74553;
    public city: any;

    constructor(public _widgetService: WidgetRestService)
    {

    }

    // Fonction d'initialisation du composant.
    ngOnInit()
    {
        // liste des villes
        this._widgetService.getLocationByGeo(this.latitude, this.longitude).subscribe(
          data => {
            this.city = data;
          }
        );

        // Déclaration de la carte avec les coordonnées du centre et le niveau de zoom.
        const myfrugalmap = L.map('frugalmap').setView([this.latitude,this.longitude], 15);

        L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png',
        {
            attribution: ''
        }).addTo(myfrugalmap);

        /*const myIcon = L.icon({
            iconUrl: '../../assets/maps/marker.png',
            iconAnchor:   [22, 94]
        });

        L.marker([this.latitude, this.longitude], null).bindPopup('Strasbourg').addTo(myfrugalmap).openPopup();

        var popup = L.popup();

        		function onMapClick(e)
            {
        		    popup
        		        .setLatLng(e.latlng)
        		        .setContent("Coordonnées : " + e.latlng.toString())
        		        .openOn(myfrugalmap);
        		}

        		myfrugalmap.on('click', onMapClick);
        
        // Déclaration de la carte avec les coordonnées du centre et le niveau de zoom.
        const myfrugalmap = L.map('frugalmap').setView([48.5839, 7.74553], 15);

        L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png').addTo(myfrugalmap);
        const myIcon = L.icon({
            iconUrl: 'http://www.clker.com/cliparts/c/9/m/4/B/d/google-maps-grey-marker-w-shadow-md.png'
        });

        L.marker([50.5839, 5.74553], {icon: myIcon}).bindPopup('').addTo(myfrugalmap).openPopup();
        L.marker([48.5839, 7.74553], {icon: myIcon});*/
    }
}
