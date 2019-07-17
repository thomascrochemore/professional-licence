import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class ApiInfoService {

    constructor(private _http: HttpClient) {
    }

    getApisList() {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/apis/list';
        return this._http.get(url);
    }

    getLocationsList() {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/locations/list';
        return this._http.get(url);
    }

    getPropertiesList() {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/properties/list';
        return this._http.get(url);
    }
}