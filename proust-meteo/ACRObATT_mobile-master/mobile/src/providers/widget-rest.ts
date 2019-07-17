import {Injectable} from '@angular/core';

import {HttpClient} from '@angular/common/http';

@Injectable()
export class WidgetRestService
{
    constructor(private _http: HttpClient)
    {

    }

    // récupérer la liste des villes
    getLocationsList()
    {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/locations/list';
        return this._http.get(url);
    }

    // récupérer ville par latitude et longitude
    getLocationByGeo(latitude, longitude)
    {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/locations/coord?latitude='+latitude+'&longitude='+longitude;
        return this._http.get(url);
    }

    // récupérer widget par ville
    getWidgetByCity(city)
    {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/weatherdata/widget?city='+city;
        return this._http.get(url);
    }

    getWidgetByCityWithTemp(city)
    {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/weatherdata/widget?city='+city+'&property=Température';
        return this._http.get(url);
    }

    getWidgetByCityWithRain(city)
    {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/weatherdata/widget?city='+city+'&property=Humidité';
        return this._http.get(url);
    }

    getWidgetByCityWithTempAndRain(city)
    {
        let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/weatherdata/widget?city='+city+'&property=Humidité&property=Température';
        return this._http.get(url);
    }

    // wrapper
    wrapper(dataBackEnd: TwoDimDataBackEnd)
    {
         console.log("Conversion :") ;
         console.log(dataBackEnd) ;

         let dataFrontEnd : TwoDimDataFrontEnd = new TwoDimDataFrontEnd();

         dataFrontEnd.city = dataBackEnd.city;
         dataFrontEnd.dateRequest = dataBackEnd.dateRequest;
         dataFrontEnd.column_headers = dataBackEnd.column_headers;

         // dataFrontEnd.column_headers = dataBackEnd.column_headers;
         let tabDelays = new Array();
         let d = new Date();
         tabDelays[0] = d.getHours() + dataBackEnd.column_headers[0] + "h";
         if (d.getHours() + dataBackEnd.column_headers[1] > 24)
         {
            tabDelays[1] = d.getHours() + dataBackEnd.column_headers[1] - 24 + "h";
         }
         else
         {
            tabDelays[1] = d.getHours() + dataBackEnd.column_headers[1] + "h";
         }
         if (d.getHours() + dataBackEnd.column_headers[2] > 24)
         {
            tabDelays[2] = d.getHours() + dataBackEnd.column_headers[2] - 24 + "h";
         }
         else
         {
            tabDelays[2] = d.getHours() + dataBackEnd.column_headers[2] + "h";
         }
         tabDelays[3] = "1 jour";
         tabDelays[4] = "3 jours ";

         dataFrontEnd.column_headers = tabDelays;

         dataFrontEnd.properties = new Array();

         dataBackEnd.properties.forEach(lineBackEnd => {
           console.log(lineBackEnd)
           let lineFrontEnd : DataFrontEnd = new DataFrontEnd() ;
           lineFrontEnd.data = lineBackEnd.rawdata ;
           lineFrontEnd.label = lineBackEnd.label;



           // type de graphique par défaut
           lineFrontEnd.type_chart = "bar";
           // gestion des cas
           if(lineFrontEnd.label == "Vitesse du vent")
           {
             lineFrontEnd.label = "Vitesse du vent";
             lineFrontEnd.color = "cloud";
             lineFrontEnd.logo = "cloudy";
             lineFrontEnd.type_chart = "line";
           }
           if(lineFrontEnd.label == "Nuages")
           {
             lineFrontEnd.label = "Couverture nuageuse";
             lineFrontEnd.color = "cloud";
             lineFrontEnd.logo = "cloudy";
           }
           if(lineFrontEnd.label == "Orage")
           {
             lineFrontEnd.label = "Orages";
             lineFrontEnd.color = "dark";
             lineFrontEnd.logo = "thunderstorm";
             lineFrontEnd.type_chart = "line";
           }
           if(lineFrontEnd.label == "Humidité")
           {
             lineFrontEnd.label = "Humidité";
             lineFrontEnd.color = "sky";
             lineFrontEnd.logo = "rainy";
             lineFrontEnd.type_chart = "line";
           }
           if(lineFrontEnd.label == "Température")
           {
             lineFrontEnd.label = "Température";
             lineFrontEnd.color = "secondary";
             lineFrontEnd.logo = "sunny";
             lineFrontEnd.type_chart = "line";
           }
           if(lineFrontEnd.label == "Pluie")
           {
             lineFrontEnd.label = "Précipitations";
             lineFrontEnd.color = "sky";
             lineFrontEnd.logo = "rainy";
             lineFrontEnd.type_chart = "line";
           }
           if(lineFrontEnd.label == "Pression")
           {
             lineFrontEnd.label = "Pression";
             lineFrontEnd.color = "grass";
             lineFrontEnd.logo = "analytics";
           }

           lineFrontEnd.unit = lineBackEnd.unit;
           lineFrontEnd.mesure = Math.ceil(lineFrontEnd.data[0]);

           console.log(lineFrontEnd)
           dataFrontEnd.properties.push(lineFrontEnd);
         })

         console.log(dataFrontEnd);
         return dataFrontEnd;
    }


    // utils
    capitalizeFirstLetter(string)
    {
        string = string.toLowerCase();
        return string.charAt(0).toUpperCase() + string.slice(1);
    }

    // ancien
    fetchCities(apis, cities, characteristics, delay)
    {
        // let url = 'http://sterne.iutrs.unistra.fr:49910/PROUST-API/api/search/weatherdata';

        // sterne.iutrs.unistra.fr:49910/PROUST-API/api/...
        // routes principales c'est dans /weatherdata
        // sinon y'a /locations, /apis, /properties et /textdata
        // let params = '?apis=' + apis + '&cities=' + cities + '&characteritics=' + characteristics + '&delays=' + delay;
        return null; //this._http.get(url + params);
    }
}

export class TwoDimDataBackEnd
{
   public city : string;
   public dateRequest : Array<string>;
   public column_headers : Array<number>;
   public series_type ?: Array<string>;
   public properties : Array<DataBackEnd>;
}
export class DataBackEnd
{
   public rawdata: Array<number> ;
   public label : string ;
   public unit : string;
}
export class TwoDimDataFrontEnd
{
   public city : string;
   public dateRequest : Array<string>;
   public column_headers : Array<number>;
   public series_type ?: Array<string>;
   public properties : Array<DataFrontEnd>;
}
export class DataFrontEnd
{
   public data : Array<number> ;
   public label : string ;
   public color : string;
   public logo : string;
   public type_chart : string;
   public unit : string;
   public mesure : number;
}
