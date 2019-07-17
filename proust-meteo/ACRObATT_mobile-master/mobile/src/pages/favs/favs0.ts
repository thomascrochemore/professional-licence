import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

// api
import { WidgetRestService } from '../../providers/widget-rest';

// storage
import { Storage } from '@ionic/storage'

@Component({
  selector: 'page-favs',
  templateUrl: 'favs.html'
})
export class FavsPage
{
    constructor(private storage: Storage, public navCtrl: NavController, private _widgetService: WidgetRestService)
    {
        this.storage.ready().then(() =>
        {
            this.storage.get('cities').then((val) =>
            {
               this.tabCities = val;
            });
            this.storage.set('cities', this.tabCities);
        });
    }

    tabCities: Array<any> = new Array(); // tableau des villes stockées, puis get via ces noms
    tabTemps: Array<any> = new Array();
    tabRains: Array<any> = new Array();

    widget: any = {
     "city" : "",
     "dateRequest" : "",
     "delay" : ["","","",""],
     "properties" : [
       { "label" : "", "rawdata" : [], "mesure": 0, "unit": "",
            "type_chart": "line", "logo": "", "color": ""}
     ]
    };
    widgets: any = new Array(); // tableau de widgets
    one_more_w: boolean = false;

    // search bar
    searchValue: string = "";
    public cities: any;

    // data in search list
    widgetCities: any = this.widget; // ce widget rapporte les informations des villes cherchées

    ngOnInit()
    {
        this._widgetService.getLocationsList().subscribe(
          data => {
            this.cities = data;
          }
        );

        this.widgetCities = this.widget;
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

    // action au click sur un item
    addToFavs(item, city_name)
    {
        // alert("ajouté aux favoris")
        if(this.tabCities == null)
        {
            this.tabCities = Array<any>();
            this.tabCities[0] = city_name;
            // widgets
            this._widgetService.getWidgetByCity(city_name).subscribe(
              data => {
                this.widgets[0] = data;
              }
            );

            this.one_more_w = true;
        }
        else
        {
            this.storage.get('cities').then((val) =>
            {
               this.tabCities = val;
            });
            this.tabCities[this.tabCities.length] = city_name;

            // widgets
            this._widgetService.getWidgetByCity(city_name).subscribe(
              data => { this.widgets[this.tabCities.length] = this._widgetService.wrapper(data as TwoDimDataBackEnd); });

            this.tabTemps[this.tabTemps.length]
            = this.widgets[this.tabCities.length].properties[3]["data"][0];
            this.tabRains[this.tabRains.length]
            = this.widgets[this.tabCities.length].properties[1];

            this.one_more_w = true;
        }
        this.storage.set('cities', this.tabCities);
    }

    clearFavs()
    {
        this.tabCities = new Array();
        this.tabTemps = new Array();
        this.tabRains = new Array();
        this.storage.set('cities', []);
        this.widgets = new Array();
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

        // affichage de petites infos sous le nom de la ville (bien sympa)
        /*for(var i = 0; i < this.tabCities.length; i++) // TODO 6 = temporaire
        {
            this._widgetService.getWidgetByCity(this.tabCities[i]).subscribe(
              data => {
                this.widgetCities = data;
              }
            );
            // température
            this.tabTemps[i] = Math.ceil(this.widgetCities.properties[3]["rawdata"][0]) + ' ' + this.widgetCities.properties[3]['unit'];
            this.tabRains[i] = Math.ceil(this.widgetCities.properties[1]) + ' ' + this.widgetCities.properties[1]['unit'];
        } */
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
