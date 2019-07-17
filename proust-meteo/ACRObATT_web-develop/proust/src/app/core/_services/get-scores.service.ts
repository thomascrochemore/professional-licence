import {Injectable} from '@angular/core';
import {TwoDimDataFrontEnd, DataFrontEnd} from '../_models/twoDimDataType.model';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class GetScoresService {
    constructor(private http: HttpClient) {
    }

    getScores(startDate: string, startHour: string, endDate: string, endHour: string, delay: string, city: string, property: string) {

        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/weatherdata/score';

        let params = '?datetime=' + startDate + '_' + startHour +
            '&datetime_to=' + endDate + '_' + endHour + '&delay=' + delay + '&city=' + city + '&property=' + property;

        return this.http.get(url + params);
    }

    wrapper(dataBackEnd) {
        let dataFrontEnd: TwoDimDataFrontEnd = new TwoDimDataFrontEnd();

        dataFrontEnd.column_headers = dataBackEnd.column_headers;
        dataFrontEnd.series_type = dataBackEnd.series_type;
        dataFrontEnd.datas = new Array();

        dataBackEnd.data.forEach(lineBackEnd => {
            let lineFrontEnd: DataFrontEnd = new DataFrontEnd();
            lineFrontEnd.data = lineBackEnd.values;
            lineFrontEnd.label = lineBackEnd.label;
            dataFrontEnd.datas.push(lineFrontEnd);
        });

        return dataFrontEnd;
    }
}
