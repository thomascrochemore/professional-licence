import React, { Component } from 'react';
import {
  Route,
  Switch,
} from 'react-router-dom';

import NavbarGuest from '../layout/navbar/navbar-guest';
import NavbarMember from '../layout/navbar/navbar-member';
import NavbarAdmin from '../layout/navbar/navbar-admin';

export default class NavbarRouter extends Component {
  render() {
    return (
      <Switch>
        <Route path="/s/a/" component={NavbarAdmin} />
        <Route path="/s/" component={NavbarMember} />
        <Route component={NavbarGuest}/>
      </Switch>
    );
  }
}
