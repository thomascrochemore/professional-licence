import {Component, Input} from '@angular/core';

// services
import {MeteoService} from '../../providers/meteo';

/**
 * Generated class for the MeteoApiComponent component.
 *
 * See https://angular.io/api/core/Component for more info on Angular
 * Components.
 */
@Component({
  selector: 'meteo-api',
  templateUrl: 'meteo-api.html'
})
export class MeteoApiComponent
{
    constructor(private _meteoService: MeteoService)
    {

    }

    @Input() public name: any;
    @Input() public api: any;

    ngOnInit()
    {
        this.name = this._meteoService.capitalizeFirstLetter(this.name);
    }
}
