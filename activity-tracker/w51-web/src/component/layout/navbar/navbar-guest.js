import React, { Component } from 'react';
import { NavLink } from 'react-router-dom';

export default class NavbarGuest extends Component {
  state = {
    routes: {}
  };
  constructor(props){
    super(props);
  }
  render() {
    return (
      <nav className="navbar navbar-inverse navbar-fixed-top">
        <div className="navbar-header">
          <button type="button" className="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
            <span className="icon-bar"></span>
            <span className="icon-bar"></span>
            <span className="icon-bar"></span>
          </button>
          <a className="navbar-brand">ActivityTracker</a>
        </div>
        <div className="collapse navbar-collapse">
          <ul className="nav navbar-nav">
            <li><NavLink activeClassName="active" to="/">Home</NavLink></li>
            <li><NavLink activeClassName="active" to="/signin">Signin</NavLink></li>
            <li><NavLink activeClassName="active" to="/register">Register</NavLink></li>
          </ul>
        </div>
      </nav>

    )
  }
}
