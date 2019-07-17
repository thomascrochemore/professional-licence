import React, { Component } from 'react';
import { Route, Switch, HashRouter, StaticR } from 'react-router-dom';

import NavbarRouter from './router/navbar-router';
import AppRouter from './router/app-router';

export default class App extends Component {
  constructor(props){
    super(props);
  }
  render() {
    return (
      <HashRouter>
      <div className="row row-eq-height">
        <NavbarRouter/>
        <div className="row row-eq-height">
          <section className="main col-md-offset-2 col-md-8">
            <AppRouter/>
          </section>
        </div>
      </div>
      </HashRouter>
    );
  }
}
