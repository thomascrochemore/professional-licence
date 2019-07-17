import { CUSTOM_ELEMENTS_SCHEMA, NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';
import { HttpClientModule } from '@angular/common/http';

// composants externes
import { ComponentsModule } from "../components/components.module"

// geolocation
import { Geolocation } from '@ionic-native/geolocation';
import { LocationTracker } from '../providers/location-tracker';
import { LocationTrackerProvider } from '../providers/location-tracker/location-tracker';
import { BackgroundGeolocation } from '@ionic-native/background-geolocation';

// templates
import { WorldPage } from '../pages/world/world';
import { HomePage } from '../pages/home/home';
import { FavsPage } from '../pages/favs/favs';
import { WebPage } from '../pages/web/web';
import { TabsPage } from '../pages/tabs/tabs';
import { TutorialPage } from '../pages/tutorial/tutorial';

// design tools
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';

// api
import { RestProvider } from '../providers/rest/rest';
import { MeteoService } from '../providers/meteo';
import { WidgetRestService } from '../providers/widget-rest';

// stockage
import { IonicStorageModule } from '@ionic/storage'

@NgModule({
  declarations: [
    MyApp,
    WorldPage,
    FavsPage,
    HomePage,
    WebPage,
    TabsPage,
    TutorialPage
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    IonicModule.forRoot(MyApp),
    IonicStorageModule.forRoot(
      {
        name: '__mydb',
        driverOrder: ['indexeddb', 'sqlite', 'websql']
      }
    ),
    ComponentsModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    WorldPage,
    FavsPage,
    HomePage,
    WebPage,
    TabsPage,
    TutorialPage
  ],
  providers: [
    StatusBar,
    LocationTracker,
    BackgroundGeolocation,
    Geolocation,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    MeteoService,
    WidgetRestService,
    RestProvider,
    LocationTrackerProvider
  ],
  schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
})
export class AppModule {}
