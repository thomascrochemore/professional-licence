import {Component, Input} from '@angular/core';

@Component({
    selector: 'graphic',
    templateUrl: './graphic.component.html',
    styleUrls: ['./graphic.component.css']
})
export class GraphicComponent {

    @Input() data: any;
    @Input() lineChartLegend: boolean = true;
    @Input() lineChartType: string = 'bar';

    public lineChartOptions: any = {
        responsive: true
    };

    constructor() {
    }
}

