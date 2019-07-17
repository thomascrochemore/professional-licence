import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {WidgetFrontEnd, LineWidgetFrontEnd} from '../_models/twoDimDataType.model';

@Injectable()
export class GetWeatherService {

    constructor(private http: HttpClient) {
    }

    getWeather(city) {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/weatherdata/widget?city=' + city;
        return this.http.get(url);
    }

    wrapper(widgetBackEnd: any) {
        let widgetFrontEnd: WidgetFrontEnd = new WidgetFrontEnd();
        widgetFrontEnd.city = widgetBackEnd.city;

        let tabDelays = new Array();
        let d = new Date();
        tabDelays[0] = d.getHours() + widgetBackEnd.column_headers[0] + "h";
        if (d.getHours() + widgetBackEnd.column_headers[1] > 24) {
            tabDelays[1] = d.getHours() + widgetBackEnd.column_headers[1] - 24 + "h";
        }
        else {
            tabDelays[1] = d.getHours() + widgetBackEnd.column_headers[1] + "h";
        }
        if (d.getHours() + widgetBackEnd.column_headers[2] > 24) {
            tabDelays[2] = d.getHours() + widgetBackEnd.column_headers[2] - 24 + "h";
        }
        else {
            tabDelays[2] = d.getHours() + widgetBackEnd.column_headers[2] + "h";
        }
        tabDelays[3] = '1 jour';
        tabDelays[4] = '3 jours';
        widgetFrontEnd.column_headers = tabDelays;

        widgetFrontEnd.dateRequest = new Date(widgetBackEnd.dateRequest);
        widgetBackEnd.properties.forEach(element => {
            let lineWidgetFrontEnd: LineWidgetFrontEnd = new LineWidgetFrontEnd();
            lineWidgetFrontEnd.data = element.rawdata;
            lineWidgetFrontEnd.label = element.label;
            lineWidgetFrontEnd.unit = element.unit;
            widgetFrontEnd.properties.push(lineWidgetFrontEnd);
        });

        widgetFrontEnd.properties.reverse();
        return widgetFrontEnd;

    }

}