import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

@Component({
  selector: 'page-web',
  templateUrl: 'web.html'
})
export class WebPage
{
    selected: boolean = false;
    url: any = "http://sterne.iutrs.unistra.fr:42000/#/";

    constructor(public navCtrl: NavController)
    {
       //  window.location.href=" = "http://sterne.iutrs.unistra.fr:42000/#/";
    }

    goEval()
    {
        window.location.href="http://sterne.iutrs.unistra.fr:42000/#/evaluation";
        //this.selected = true;
        //this.url = "http://sterne.iutrs.unistra.fr:42000/#/evaluation";
    }
    goPrev()
    {
        window.location.href="http://sterne.iutrs.unistra.fr:42000/#/forecasts";
        //this.selected = true;
        //this.url = "http://sterne.iutrs.unistra.fr:42000/#/forecasts";
    }
    goDist()
    {
        window.location.href="http://sterne.iutrs.unistra.fr:42000/#/distance";
        //this.selected = true;
        //this.url = "http://sterne.iutrs.unistra.fr:42000/#/distance";
    }
    goScore()
    {
        window.location.href="http://sterne.iutrs.unistra.fr:42000/#/scores";
        //this.selected = true;
        //this.url = "http://sterne.iutrs.unistra.fr:42000/#/scores";
    }
    isSelected()
    {
        if(this.selected == true)
        {
            return true;
        }
        return false;
    }
}
