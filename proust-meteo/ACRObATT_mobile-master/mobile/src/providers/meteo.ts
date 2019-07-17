import {Injectable} from '@angular/core';

// import {HttpClient} from '@angular/common/http';

@Injectable()
export class MeteoService
{
    constructor(/*private _http: HttpClient*/)
    {

    }

    fetchMeteo(apis, cities, characteristics, delay)
    {
        // let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/search/weatherdata';
        // let params = '?apis=' + apis + '&cities=' + cities + '&characteritics=' + characteristics + '&delays=' + delay;
        return null; //this._http.get(url + params);
    }

    capitalizeFirstLetter(string)
    {
        string = string.toLowerCase();
        return string.charAt(0).toUpperCase() + string.slice(1);
    }
}
