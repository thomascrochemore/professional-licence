import {Component, Input} from '@angular/core';

@Component({
    selector: 'tab',
    templateUrl: './tab.component.html',
    styleUrls: ['./tab.component.css']
})
export class TabComponent {

    @Input() data: any;

    @Input() displayedColumns: Array<string>;

    public datasource;

    constructor() {
    }

}
