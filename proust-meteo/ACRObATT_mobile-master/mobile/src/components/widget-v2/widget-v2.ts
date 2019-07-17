import { Component, Input } from '@angular/core';

@Component({
  selector: 'widget-v2',
  templateUrl: 'widget-v2.html'
})
export class WidgetV2Component
{
    city: string;
    temp: string;
    rain: string;
    wind: string;
    pressure: string;

    constructor()
    {
        this.city = 'null'; // simple value widget.city
        this.temp = 'null'; // 10°C : value from tab from tab Widget.properties[0].Temperature[0]
        this.rain = '50%';
        this.wind = 'Very cloudy';
        this.pressure = '1 bar';
    }

    ancienWidget = {
     "city" : "Illkirch",
     "dateRequest" : "13/04/2018",
     "delay" : ["6h","12h","24h","72h"],
     "properties" : [
           { "Temperature" : ["12","15","13","9"] },
           { "Pressure" : ["53","54","52","30"] },
           { "Humidity " : ["54","57","55","3"] },]
    };

    @Input() widget: any;
    public chartClickedDynamique(e:any):void {console.log(e);}
    public chartHoveredDynamique(e:any):void {console.log(e);}

    ngOnInit()
    {
        this.city = this.widget.city;
        this.temp = this.widget.properties[0]['data'][0] + "°C";
    }
}
