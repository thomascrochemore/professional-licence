import { Component, Input } from '@angular/core';

// api
import { WidgetRestService } from '../../providers/widget-rest';

@Component({
  selector: 'miniwidget',
  templateUrl: 'miniwidget.html'
})
export class MiniwidgetComponent
{
    @Input() widget: any;
    @Input() tabCities: any;

    widget_retourne: any;

    // pour la boucle d'exemple
    minis = new Array();
    fulls = new Array();

    ngOnInit()
    {
        for(var i = 0; i < 10; i++) // TODO 6 = temporaire
        {
            this.minis[i] = true;
            this.fulls[i] = false;
        }
    }

    constructor(private _widgetService: WidgetRestService)
    {

    }

    mini_display(nb)
    {
        return this.minis[nb] === true && this.fulls[nb] === false;
    }
    full_display(nb)
    {
        return this.fulls[nb] === true && this.minis[nb] === false;
    }
    mini_click(nb)
    {
        this.minis[nb] = false;
        this.fulls[nb] = true;
    }
    full_click(nb)
    {
        this.minis[nb] = true;
        this.fulls[nb] = false;
    }
    delete_fav(nb)
    {

    }
    getWidgetByTeci(city_name)
    {
        try
        {
            this._widgetService.getWidgetByCity(city_name).subscribe(
              data => { this.widget_retourne = this._widgetService.wrapper(data as TwoDimDataBackEnd); });

            return this.widget_retourne;
        }
        catch (Error)
        {
            alert("Erreur, impossible de récupérer les données");
        }
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
