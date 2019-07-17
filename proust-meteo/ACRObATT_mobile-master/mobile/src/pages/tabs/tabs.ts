import { Component } from '@angular/core';

import { TutorialPage } from '../tutorial/tutorial';
import { WorldPage } from '../world/world';
import { FavsPage } from '../favs/favs';
import { HomePage } from '../home/home';
import { WebPage } from '../web/web';

@Component({
  templateUrl: 'tabs.html'
})
export class TabsPage
{
    tab0Root = TutorialPage;
    tab1Root = HomePage;
    tab2Root = WorldPage;
    tab3Root = FavsPage;
    tab4Root = WebPage;

    constructor()
    {

    }
}
