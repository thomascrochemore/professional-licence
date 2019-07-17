import {Component, Input} from '@angular/core';

// services
import {MeteoService} from '../../providers/meteo';

/**
 * Generated class for the MeteoCityComponent component.
 *
 * See https://angular.io/api/core/Component for more info on Angular
 * Components.
 */
@Component({
  selector: 'meteo-city',
  templateUrl: 'meteo-city.html'
})
export class MeteoCityComponent {

  constructor(private _meteoService: MeteoService)
  {

  }

  @Input() public name: any;
  @Input() public city: any;

  public columns: any = ['charac'];
  public dataSource: Element[]; // data for meteo table
  public delays: any = []; // characteristics (one line per delay)

  ngOnInit()
  {
      // example
      this.dataSource = [];

      this.name = this._meteoService.capitalizeFirstLetter(this.name);

      for (let column in this.city) {
          this.columns.push(this.transformColumnName(this.city[column]['name']));

          for (let chara in this.city[column]['params']) {
            this.delays.push({delay: this.city[column]['name'], chara: chara, value: this.city[column]['params'][chara]});
          }
      }

      this.dataSource = this.constructColumnsTable();
      this.dataSource = this.cleanObj(this.dataSource);
  }

  transformColumnName(name)
  {
      switch (name)
      {
        case '0h':
          name = 'real';
          break;
        case '6h':
          name = 'six';
          break;
        case '12h':
          name = 'twelve';
          break;
        case '1j':
          name = 'one';
          break;
        case '3j':
          name = 'three';
      }

      return name;
  }

  constructColumnsTable()
  {
    let param;
    let col: any = [];
    let line;

    let key_table;

    for (let obj in this.delays) {
      // console.log(this.delays[obj]['delay']);
      param = this.delays[obj]['chara'];

      line = [];
      let isExist: boolean = false;


      // console.log(this.delays);

      for (let obj_param in this.delays) {
        if (this.delays[obj_param]['chara'] === param) {

          key_table = this.delays[obj_param]['delay'];

          if (!isExist) {
            // console.log(key_table);
            switch (key_table) {
              case '0h':
                key_table = 'real';
                break;
              case '6h':
                key_table = 'six';
                break;
              case '12h':
                key_table = 'twelve';
                break;
              case '1j':
                key_table = 'one';
                break;
              case '3j':
                key_table = 'three';

            }

            // console.log(key_table);

            line['charac'] = param;

            line[key_table] = this.delays[obj_param]['value'];
            col.push(line);

          }

          isExist = false;
        }
      }
    }
    return col;
  }


  cleanObj(object) {

    let new_obj = [];

    let charac = [];

    for (let obj in object) {
      if (charac.indexOf(object[obj]['charac']) === -1) {
        charac.push(object[obj]['charac']);
        new_obj.push(object[obj]);
      }
    }
    return new_obj;
  }
}


export interface Element {
  charac: string;
  real: any;
  six: any;
  twelve: any;
  one: any;
  three: any;
}
