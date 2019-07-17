import React, { Component } from 'react';
import {
  Route,
  Switch,
} from 'react-router-dom';

import AppRoutes from '../routes';
import NotFound from '../common/error/notfound';

export default class AppRouter extends Component {
  render() {
    return (
      <Switch>
        {AppRoutes.map((route) =>(
          <Route key={route.path} path={route.path} component={route.component} exact/>
        ))}
        <Route component={NotFound} />
      </Switch>
    );
  }
}
