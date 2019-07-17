import {Component, OnInit} from '@angular/core';
import {GetScoresService} from '../../../_services/get-scores.service';
import {ApiInfoService} from '../../../_services/api-info';

@Component({
    selector: 'app-scores',
    templateUrl: './scores.component.html',
    styleUrls: ['./scores.component.css']
})
export class ScoresComponent implements OnInit {

    // Params requêtes :
    public hourList = ['02:00', '08:00', '14:00', '22:00'];
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
    public scores: any;
    public displayedColumns: Array<string>;

    public data_null: boolean = false;

    constructor(private scoresService: GetScoresService,
                private apiInfoService: ApiInfoService) {
    }

    ngOnInit() {
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

    updateEndtDate(endDate) {
        this.endDate = endDate;
        this.updateData();
    }

    updateData() {

        if (this.city != null && this.property != null && this.delay != null &&
            this.startDate != null && this.startHour != null && this.endDate != null && this.endHour) {

            this.scoresService.getScores(
                this.formatDateBackEnd(this.startDate),
                this.startHour.toString(),
                this.formatDateBackEnd(this.endDate),
                this.endHour.toString(),
                this.delay.toString(),
                this.city,
                this.property).subscribe(data => {

                if (data['data'] === 'Aucune donnée trouvée') {
                    this.data_null = true;
                } else {
                    this.data_null = false;
                    this.scores = this.scoresService.wrapper(data);
                    this.displayedColumns = [this.scores.series_type[0], ...this.scores.column_headers];
                }
            });
        }
    }

    formatDateBackEnd(date: Date) {
        let month = date.getMonth() + 1;
        let day = date.getDate();
        let year = date.getFullYear();
        return day + '-' + month + '-' + year;
    }
}
