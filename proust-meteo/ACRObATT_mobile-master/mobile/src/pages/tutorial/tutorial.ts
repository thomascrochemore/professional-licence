import { Component } from '@angular/core';
import { IonicPage, MenuController, NavController, Platform } from 'ionic-angular';

export interface Slide {
  title: string;
  description: string;
  image: string;
}

@IonicPage()
@Component({
  selector: 'page-tutorial',
  templateUrl: 'tutorial.html'
})
export class TutorialPage {
  slides: Slide[];
  showSkip = true;
  dir: string = 'ltr';

  constructor(public navCtrl: NavController, public menu: MenuController, public platform: Platform)
  {
      this.dir = platform.dir();
      this.slides =
      [
          {
            title: "Flash <br><hr> Notre widget météo ",
            description: "<br>Vous n'avez rien à faire, l'application récupère la météo à l'endroit où vous êtes.",
            image: 'assets/imgs/widget.png',
          },
          {
            title: "Maps <br><hr> Trouvez la météo pour une ville donnée",
            description: "<br>Entrez le nom d'une ville et récupérez la météo concernant cette ville.",
            image: 'assets/city/city-hall.png',
          },
          {
            title: "Favs <br><hr> Ajoutez vos villes favorites",
            description: "<br>Entrez le nom d'une ville et ajoutez la à votre liste de favoris. <br><br> La liste affiche un widget simplifié, cliquez dessus pour obtenir le widget complet.",
            image: 'assets/city/city-church.png',
          },
          {
            title: "Web <br><hr> Utilisez notre site web",
            description: "<br>Evaluation des APIs utilisées - Exploration de données<br><br> <a href=\"http://sterne.iutrs.unistra.fr:42000/#/\">https://proust.fr</a>",
            image: 'assets/city/web.png',
          }
      ];
  }

  startApp() {
    this.navCtrl.setRoot('HomePage', {}, {
      animate: true,
      direction: 'forward'
    });
  }

  onSlideChangeStart(slider) {
    this.showSkip = !slider.isEnd();
  }

  ionViewDidEnter() {
    // the root left menu should be disabled on the tutorial page
    this.menu.enable(false);
  }

  ionViewWillLeave() {
    // enable the root left menu when leaving the tutorial page
    this.menu.enable(true);
  }

}
