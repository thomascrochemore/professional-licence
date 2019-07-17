import {Component, OnInit} from '@angular/core';
import {GetDistanceService} from '../../../_services/get-distance.service';
import {DataFrontEnd, TwoDimDataFrontEnd} from '../../../_models/twoDimDataType.model';
import {ApiInfoService} from '../../../_services/api-info';

@Component({
    selector: 'app-distance',
    templateUrl: './distance.component.html',
    styleUrls: ['./distance.component.css']
})
export class DistanceComponent implements OnInit {

    // Params requètes :
    public hourList = ['02:00', '08:00', '14:00', '22:00'];

    public hour_start = 'Heure de début';
    public hour_end = 'Heure de fin';

    public startDate: Date = new Date();
    public startHour: number;
    public endDate: Date = new Date();
    public endHour: number;
    public delayList = [6, 12, 24, 72];
    public cityList: any;
    public apiLists: any;
    public propertyList: any;
    public delay: number;
    public city: string;
    public api: string;
    public property: string;

    // retour api
    public distances: any;
    public displayedColumns: Array<string>;

    public data_null: boolean = false;

    constructor(private distanceService: GetDistanceService,
                private apiInfoService: ApiInfoService) {
    }

    ngOnInit() {
        this.apiInfoService.getApisList().subscribe(data => {
            this.apiLists = data;
        });
        this.apiInfoService.getLocationsList().subscribe(data => {
            this.cityList = data;
        });
        this.apiInfoService.getPropertiesList().subscribe(data => {
            this.propertyList = data;
        });
    }

    updateStartDate(startDate) {
        this.startDate = startDate;
        this.updateData();
    }

    updateEndDate(endDate) {
        this.endDate = endDate;
        this.updateData();
    }

    updateData() {
        if (this.api != null && this.city != null && this.property != null && this.delay != null &&
            this.startDate != null && this.startHour != null && this.endDate != null && this.endHour)
            this.distanceService.getDistance(
                this.formatDateBackEnd(this.startDate),
                this.startHour.toString(),
                this.formatDateBackEnd(this.endDate),
                this.endHour.toString(),
                this.delay.toString(),
                this.city,
                this.api,
                this.property).subscribe(distances => {
                if (distances['data'] == 'Aucune donnée trouvée') {
                    this.data_null = true;
                } else {
                    this.data_null = false;
                    this.distances = this.distanceService.wrapper(distances);
                    this.displayedColumns = [this.distances.series_type[0], ...this.distances.column_headers];
                }

            });
    }

    formatDateBackEnd(date: Date) {
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let year = date.getFullYear();
        return day + '-' + month + '-' + year;
    }
}
