// angular ionic
import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

// partie map : api de geolocation
import { LocationTracker } from '../../providers/location-tracker';

// api
import { WidgetRestService } from '../../providers/widget-rest';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage
{
// partie map
    ville: any = {"id":1,"city":"Strasbourg","latitude":48.5839,"longitude":7.74553,"country":{"id":1,"name":"France","code":"fr"}};
    // TODO bug : ville non récupérée de suite, au rechargement no pb
    widget: any = {"city" : "", "dateRequest" : "", "delay" : ["","","",""],"properties" : [{ "label" : "", "data" : [], "mesure": 0, "unit": "", "type_chart": "line", "logo": "", "color": ""}]};

    public map: boolean = false; // affichage de la carte



    constructor(public navCtrl: NavController, public locationTracker: LocationTracker, private _widgetService: WidgetRestService)
    {
        // les coordonnées
        this.locationTracker.startTracking();
        // la ville actuelle
        this._widgetService.getLocationByGeo(this.locationTracker.lat, this.locationTracker.lng).subscribe(
          data => { this.ville = data; });
    }

    ngOnInit()
    {
        try
        {
            // la ville actuelle
            this._widgetService.getLocationByGeo(this.locationTracker.lat, this.locationTracker.lng).subscribe(
              data => { this.ville = data; });
            // le widget associé
            this._widgetService.getWidgetByCity(this.ville.city).subscribe(
              data => { this.widget = this._widgetService.wrapper(data as TwoDimDataBackEnd); });
            this.map = true;
        }
        catch (Error)
        {
            alert("Erreur, impossible de récupérer les données");
        }
    }

    // refresh
    refresh()
    {
        /*if(this.locationTracker.lat == 0 && this.locationTracker.lng == 0)
        {
            // rien...
        }
        else
        {
            try
            {
                // la ville actuelle
                this._widgetService.getLocationByGeo(this.locationTracker.lat, this.locationTracker.lng).subscribe(
                  data => { this.ville = data; });
                // le widget associé
                this._widgetService.getWidgetByCity(this.ville.city).subscribe(
                  data => { this.widget = this._widgetService.wrapper(data as TwoDimDataBackEnd); });
                this.map = true;
            }
            catch (Error)
            {
                alert("Erreur, impossible de récupérer les données");
            }
        }*/
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
