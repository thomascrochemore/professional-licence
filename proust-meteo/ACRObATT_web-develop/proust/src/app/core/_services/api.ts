import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {
    TwoDimDataFrontEnd,
    DataFrontEnd
} from '../_models/twoDimDataType.model';


@Injectable()
export class ApiService {

    constructor(private _http: HttpClient) {
    }


    getEval(params) {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/weatherdata/eval' + params;
        return this._http.get(url);
    }

    getForecast(params) {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/weatherdata/rawdata' + params;
        return this._http.get(url);
    }

    wrapper(dataBackEnd: any) {

        let dataFrontEnd: TwoDimDataFrontEnd = new TwoDimDataFrontEnd();

        dataFrontEnd['column_headers'] = dataBackEnd.column_headers;
        dataFrontEnd['series_type'] = dataBackEnd.series_type;
        dataFrontEnd['datas'] = new Array();

        dataBackEnd.data.forEach(lineBackEnd => {
            let lineFrontEnd: DataFrontEnd = new DataFrontEnd();
            lineFrontEnd.data = lineBackEnd.values;
            lineFrontEnd.label = lineBackEnd.label;
            dataFrontEnd.datas.push(lineFrontEnd);
        });
        return dataFrontEnd;
    }


    getCities() {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/locations';
        return this._http.get(url);
    }

}
