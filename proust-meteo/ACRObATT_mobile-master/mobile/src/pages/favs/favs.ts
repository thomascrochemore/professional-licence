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

            this.store = true;
        });
    }

    store: boolean = false; // bool d'affichage
    tabCities: Array<any> = new Array(0); // tableau des villes stockées, puis get via ces noms

    widget: any = {
     "city" : "",
     "dateRequest" : "",
     "delay" : ["","","",""],
     "properties" : [
       { "label" : "", "rawdata" : [], "mesure": 0, "unit": "",
            "type_chart": "line", "logo": "", "color": ""}
     ]
    };

    // search bar
    searchValue: string = "";
    public cities: any;

    ngOnInit()
    {
        this.storage.ready().then(() =>
        {
            this.storage.get('cities').then((val) =>
            {
               this.tabCities = val;
            });
            this.storage.set('cities', this.tabCities);

            this.store = true;
        });

        this._widgetService.getLocationsList().subscribe(
          data => {
            this.cities = data;
          }
        );
        this.currentCitiesTemp = this.widget.properties[0]['mesure'] + ' ' + this.widget.properties[0]['unit'];
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

    // action au click sur un item
    addToFavs(item, city_name)
    {
        if(this.tabCities == null)
        {
            this.tabCities = Array<any>();
            this.tabCities[0] = city_name;
        }
        else
        {
            /*this.storage.get('cities').then((val) =>
            {
               this.tabCities = val;
            });*/
            this.tabCities[this.tabCities.length] = city_name;
        }
        this.storage.set('cities', this.tabCities);
    }

    clearFavs()
    {
        this.tabCities = null;
        this.storage.set('cities', []);
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

    delete_fav(city_name)
    {
        /*int i = this.tabCities.indexOf(city_name);
        delete[this.tabCities[i]];*/
        this.tabCities = this.tabCities.filter(item => item !== city_name);
        this.storage.set('cities', this.tabCities);
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
