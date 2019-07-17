import {Component, Input, OnChanges} from '@angular/core';
import {GetWeatherService} from '../../_services/get-weather.service';

@Component({
    selector: 'widget',
    templateUrl: 'widget.html',
    styleUrls: ['./widget.css']
})
export class WidgetComponent implements OnChanges {


    constructor(private getWeatherService: GetWeatherService) {
    }

    public detail: boolean = false;

    @Input() city;

    public activeProperty: any;

    public delai: number;

    public date: Date = new Date();

    public widget: any;

    public chartClickedDynamique(e: any, property: any): void {

        this.activeProperty = property.label;
        this.detail = true;

        if (e.active.length > 0) {
            let dataIndex = e.active[0]._index;
            switch (dataIndex) {
                case 0 :
                    this.delai = 0;
                    break;
                case 1 :
                    this.delai = 6;
                    break;
                case 2 :
                    this.delai = 12;
                    break;
                case 3 :
                    this.delai = 24;
                    break;
                case 4 :
                    this.delai = 72;
                    break;
            }

        }
    }

    ngOnInit() {
        this.getWeatherService.getWeather(this.city).subscribe(
            data => {
                this.widget = this.getWeatherService.wrapper(data);
            }
        );
    }

    ngOnChanges() {
        this.getWeatherService.getWeather(this.city).subscribe(
            data => {
                this.widget = this.getWeatherService.wrapper(data);
            }
        );
        this.detail = false;
    }

    onClose(test) {
        this.detail = false;
    }
}
