import { Component, Input } from '@angular/core';

@Component({
  selector: 'site-web',
  templateUrl: 'site-web.html'
})
export class SiteWebComponent
{
    @Input() url: any;

    constructor()
    {
        // window.location.href="http://sterne.iutrs.unistra.fr:42000/#/demo";
    }
}
