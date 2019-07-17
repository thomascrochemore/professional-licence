import { Component, Input, ViewChild, ElementRef } from '@angular/core';
import { NavController } from 'ionic-angular';

// web
import { DomSanitizer } from '@angular/platform-browser';
// import { SafeResourceUrl } from '@angular/platform-browser';

// geoloc
import { Geolocation } from '@ionic-native/geolocation';
import { LocationTracker } from '../../providers/location-tracker';

@Component({
  selector: 'widget',
  templateUrl: 'widget.html'
})
export class WidgetComponent
{
    city: string;
    temp: string;
    rain: string;
    wind: string;
    pressure: string;

    date: any;

    s: any;
    url: string;

    @ViewChild('map') mapElement: ElementRef;
    map: any;

    constructor(public navCtrl: NavController, public sanitizer: DomSanitizer, public locationTracker: LocationTracker,
      public geolocation: Geolocation)
    {
        this.locationTracker.startTracking();
        this.s = sanitizer;
        this.date = new Date();
    }

    photoURL()
    {
        this.url = "http://a.tile.osm.org/1/"+ this.locationTracker.lat + "/" + this.locationTracker.lng +".png";
        return this.s.bypassSecurityTrustResourceUrl(this.url);
    }

    @Input() widget: any;
    public chartClickedDynamique(e:any):void {console.log(e);}
    public chartHoveredDynamique(e:any):void {console.log(e);}
    public lineChartColors:Array<any> = [
        { // red
            backgroundColor: 'rgba(255, 51, 0, 0.2)',
            borderColor: 'rgba(255, 51, 0,1)',
            pointBackgroundColor: 'rgba(255, 51, 0, 1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(255, 51, 0, 1)'
        }
    ];
}
