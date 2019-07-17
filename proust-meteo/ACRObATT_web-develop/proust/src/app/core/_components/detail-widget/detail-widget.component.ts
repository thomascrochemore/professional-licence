import {GetDistanceService} from './../../_services/get-distance.service';
import {GetDetailsService} from './../../_services/get-details.service';
import {Component, OnInit, EventEmitter, Output, OnChanges, Input} from '@angular/core';
import {TwoDimDataFrontEnd} from '../../_models/twoDimDataType.model';

@Component({
    selector: 'app-detail-widget',
    templateUrl: './detail-widget.component.html',
    styleUrls: ['./detail-widget.component.css']
})
export class DetailWidgetComponent implements OnChanges, OnInit {

    constructor(private detailsService: GetDetailsService) {
    }

    public details: any;

    @Input() city: string;
    @Input() property: string;
    @Input() delay: string;

    @Output() close = new EventEmitter<boolean>();

    onClose() {
        this.close.emit(false);
    }

    public displayedColumns: Array<string>;

    ngOnInit() {
        this.detailsService.getDetails(this.city, this.property, this.delay).subscribe(data => {
            this.displayedColumns = [];
            data['column_headers'].shift();
            this.details = data;
            this.displayedColumns = [this.details.series_type[0], ...this.details.column_headers];
        });
    }

    ngOnChanges() {
        this.detailsService.getDetails(this.city, this.property, this.delay).subscribe(data => {
            this.displayedColumns = [];
            data['column_headers'].shift();
            this.details = data;
            this.displayedColumns = [this.details.series_type[0], ...this.details.column_headers];
        });
    }
}
