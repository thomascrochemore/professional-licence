export class TwoDimDataBackEnd {
    public column_headers: Array<string>;
    public series_type ?: Array<string>;
    public data: Array<DataBackEnd>;
}

export class DataBackEnd {
    public values: Array<number>;
    public label: string;
}

export class TwoDimDataFrontEnd {
    public column_headers: Array<string> = Array<string>();
    public series_type: Array<string> = Array<string>();
    public datas: Array<DataFrontEnd> = Array<DataFrontEnd>();
}

export class DataFrontEnd {
    public data: Array<number> = Array<number>();
    public label: string;
}

export class LineWidgetFrontEnd {
    public unit: string;
    public data: Array<number> = new Array<number>();
    public label: string;
}

export class LineWidgetBackEnd {
    public unit: string;
    public rawdata: Array<number> = new Array<number>();
    public label: string;
}

export class WidgetFrontEnd {
    public city?: string;
    public dateRequest?: any;
    public column_headers?: Array<string> = new Array<string>();
    properties?: Array<LineWidgetFrontEnd> = new Array<LineWidgetFrontEnd>();
}

export class WidgetBackend {
    public city?: string;
    public dateRequest?: any;
    public column_headers?: Array<string> = new Array<string>();
    properties?: Array<LineWidgetBackEnd> = new Array<LineWidgetBackEnd>();
}