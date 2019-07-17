import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class LocationTrackerProvider
{
    constructor(public http: HttpClient)
    {
        console.log('Hello LocationTrackerProvider Provider');
    }
}
