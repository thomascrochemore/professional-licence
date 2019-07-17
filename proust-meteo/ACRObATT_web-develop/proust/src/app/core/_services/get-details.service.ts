import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class GetDetailsService {

    constructor(private http: HttpClient) {
    }

    getDetails(city, property, delay) {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/weatherdata/values-index?' +
            'city=' + city +
            '&property=' + property +
            '&delay=' + delay;

        return this.http.get(url);
    }
}