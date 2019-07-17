import { Component, Input } from '@angular/core';

// api
import { WidgetRestService } from '../../providers/widget-rest';

@Component({
  selector: 'miniwidget',
  templateUrl: 'miniwidget.html'
})
export class MiniwidgetComponent
{
  @Input() widget: any; // le widget
  @Input() widgets: any; // la liste de widgets
  @Input() tabCities: any;
  @Input() tabTemps: any;
  @Input() tabRains: any;
  @Input() storage: any;

  // pour la boucle d'exemple
  minis = new Array();
  fulls = new Array();
  public cities: any;

  ngOnInit()
  {
      this._widgetService.getLocationsList().subscribe(
          data => {
            this.cities = data;
          }
      );
      for(var i = 0; i < this.tabCities.length; i++) // TODO 6 = temporaire
      {
          this.minis[i] = true;
          this.fulls[i] = false;
      }

      this.storage.get('cities').then((val) =>
      {
         this.tabCities = val;
      });
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
      this.tabCities[nb] = null;

  }
}
