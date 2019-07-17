import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

// geolocation
import { Geolocation } from '@ionic-native/geolocation';
import { LocationTracker } from '../../providers/location-tracker';

// api
import { WidgetRestService } from '../../providers/widget-rest';

@Component({
  selector: 'page-world',
  templateUrl: 'world.html'
})
export class WorldPage
{
    // @ViewChild('map') mapElement: ElementRef;
    // map: any;
    latitude_city: any;
    longitude_city: any;
    city: any;

    // route pour get city by name : onclick widget pour cette ville
    widget: any = {
     "city" : "",
     "dateRequest" : "",
     "delay" : ["","","",""],
     "properties" : [
       { "label" : "", "data" : [], "mesure": 0, "unit": "",
            "type_chart": "line", "logo": "", "color": ""}
     ]
    };

    constructor(public navCtrl: NavController, public geolocation: Geolocation, public locationTracker: LocationTracker, private _widgetService: WidgetRestService)
    {
        this.locationTracker.startTracking();
        this.latitude_city = this.locationTracker.lat;
        this.longitude_city = this.locationTracker.lng;
    }

    // search bar
    searchValue: string = "";
    public cities: any;

    ngOnInit()
    {
        // liste des villes
        this._widgetService.getLocationsList().subscribe(
          data => {
            this.cities = data;
          }
        );
        // current temp
        // TODO this.currentCitiesTemp = this.widget.properties[0]['mesure'] + ' ' + this.widget.properties[0]['unit'];
    }

    getCities(ev: any)
    {
        // set val to the value of the searchbar
        let val = ev.target.value;

        // if the value is an empty string don't filter the items
        if (val && val.trim() != '')
        {
            this.cities = this.cities.filter((city) =>
            {
                return (city.toLowerCase().indexOf(val.toLowerCase()) > -1);
            })
        }
    }

// barre de recherche

    // les villes affichées
    currentCities: any = [];
    currentCitiesTemp: any;
    widgetVisible: any = false;

    // action au click sur un item
    widgetOfCity(item, city_name)
    {
        this.widgetVisible = true;
        // getWidget(item.name)
        this._widgetService.getWidgetByCity(city_name).subscribe(
          data => { this.widget = this._widgetService.wrapper(data as TwoDimDataBackEnd); });
        // villes affichées
        this.currentCities = null;
    }

    // action au click sur un item
    hide()
    {
        this.widgetVisible = false;
    }

    // récupérer les villes
    getItems(ev)
    {
        let val = ev.target.value;
        if (!val || !val.trim())
        {
            this.currentCities = [];
            return;
        }
        this.currentCities = this.cities.filter((city) =>
        {
            return (city.toLowerCase().indexOf(val.toLowerCase()) > -1);
        })
    }

    // refresh
    refresh(city_name)
    {

    }
}

export class TwoDimDataBackEnd
{
   public city : string;
   public dateRequest : Array<string>;
   public column_headers : Array<number>;
   public series_type ?: Array<string>;
   public properties : Array<DataBackEnd>;
}

export class DataBackEnd
{
   public rawdata: Array<number> ;
   public label : string ;
   public unit : string;
}
