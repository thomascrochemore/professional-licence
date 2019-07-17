import { Component, Input } from '@angular/core';

// api
import { WidgetRestService } from '../../providers/widget-rest';

@Component({
  selector: 'mini',
  templateUrl: 'mini.html'
})
export class MiniComponent
{
    _ville: string;
    mini: boolean;
    full: boolean;
    widget: any;

    constructor(private _widgetService: WidgetRestService)
    {
        this.mini = true;
        this.full = false;
    }

    @Input('ville') set ville(val)
    {
        this._ville = val;
        this.getWidgetByTeci();
    }

    get ville()
    {
        return this._ville;
    }

    mini_display()
    {
        return this.mini === true && this.full === false;
    }
    full_display()
    {
        return this.full === true && this.mini === false;
    }
    mini_click()
    {
        this.mini = false;
        this.full = true;
    }
    full_click()
    {
        this.mini = true;
        this.full = false;
    }

    getWidgetByTeci()
    {
        try
        {
            this._widgetService.getWidgetByCityWithTempAndRain(this.ville).subscribe(
              data => { this.widget = this._widgetService.wrapper(data as TwoDimDataBackEnd); });

            return this.widget;
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
